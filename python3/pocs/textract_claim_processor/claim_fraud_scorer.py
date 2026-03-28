"""
Step 3 — Claim Fraud Scorer
Triggered by EventBridge after Step 2 APPROVED decision.
Scores claim using 4 ML signals, holds or releases payment via Step Functions.
"""

import json
import os
import boto3

sagemaker_rt = boto3.client("sagemaker-runtime")
bedrock      = boto3.client("bedrock-runtime")
opensearch   = boto3.client("opensearchserverless")
neptune      = boto3.client("neptunedata")
featurestore = boto3.client("sagemaker-featurestore-runtime")
sfn          = boto3.client("stepfunctions")
sqs          = boto3.client("sqs")

XGBOOST_ENDPOINT   = os.environ["XGBOOST_ENDPOINT"]
OPENSEARCH_ENDPOINT = os.environ["OPENSEARCH_ENDPOINT"]
NEPTUNE_ENDPOINT   = os.environ["NEPTUNE_ENDPOINT"]
FEATURE_GROUP      = os.environ["FEATURE_GROUP"]
SFN_PAYMENT_ARN    = os.environ["SFN_PAYMENT_ARN"]
FRAUD_QUEUE_URL    = os.environ["FRAUD_QUEUE_URL"]

FRAUD_HOLD_THRESHOLD   = 0.7
FRAUD_AUTOREJECT_THRESHOLD = 0.9


# ── 1. XGBoost real-time fraud score ─────────────────────────────────────────

def xgboost_score(features: list) -> float:
    payload = ",".join(str(f) for f in features)
    resp = sagemaker_rt.invoke_endpoint(
        EndpointName=XGBOOST_ENDPOINT,
        ContentType="text/csv",
        Body=payload,
    )
    return float(resp["Body"].read().decode())


# ── 2. Duplicate detection via Bedrock embeddings + OpenSearch k-NN ──────────

def duplicate_score(claim_text: str) -> float:
    embed_resp = bedrock.invoke_model(
        modelId="amazon.titan-embed-text-v1",
        body=json.dumps({"inputText": claim_text}),
        contentType="application/json",
        accept="application/json",
    )
    vector = json.loads(embed_resp["body"].read())["embedding"]

    search_body = json.dumps({
        "size": 1,
        "query": {"knn": {"embedding": {"vector": vector, "k": 1}}},
    })
    import urllib.request
    req = urllib.request.Request(
        f"{OPENSEARCH_ENDPOINT}/claims/_search",
        data=search_body.encode(),
        headers={"Content-Type": "application/json"},
    )
    result = json.loads(urllib.request.urlopen(req).read())
    hits = result.get("hits", {}).get("hits", [])
    return hits[0]["_score"] if hits else 0.0   # similarity 0–1


# ── 3. Neptune provider fraud ring score ─────────────────────────────────────
"""
The Neptune Provider Fraud Ring Score models provider interactions as a graph using 
    **nodes (providers)** 
    **edges (shared patients, referrals, co-billing events)**
enabling network-based anomaly detection.
It applies graph algorithms such as
    **community detection (e.g., Louvain, Label Propagation)** 
    **centrality metrics (betweenness, eigenvector centrality)** 
to identify tightly connected suspicious clusters.
Feature engineering includes 
    **temporal claim patterns, 
    billing code entropy, 
    patient overlap ratios, 
    edge-weight normalization**, 
which are fed into machine learning models.
Advanced models like
     **Graph Neural Networks (GNNs)** 
      **gradient boosting (e.g., XGBoost)** 
compute risk scores by combining node-level and subgraph-level embeddings.
The final fraud ring score is generated via 
**ensemble scoring 
threshold calibration (ROC-AUC optimization, precision-recall tuning)** 
to balance detection sensitivity and false positives.

"""
##
## It identifies **coordinated fraud patterns** such as abnormal patient sharing, excessive referrals, and synchronized billing behavior among interconnected providers.
##
def neptune_risk_score(provider_id: str) -> float:
    query = f"g.V().has('provider_id', '{provider_id}').values('fraud_cluster_score')"
    resp  = neptune.execute_gremlin_query(gremlinQuery=query)
    scores = resp.get("result", {}).get("data", [])
    return float(scores[0]) if scores else 0.0


# ── 4. Composite risk score ───────────────────────────────────────────────────

def composite_score(xgb: float, dup: float, neptune: float) -> float:
    return round(0.6 * xgb + 0.25 * dup + 0.15 * neptune, 4)


# ── 5. Store result in Feature Store for retraining feedback loop ─────────────

def store_feature_record(claim_id: str, score: float, features: dict):
    featurestore.put_record(
        FeatureGroupName=FEATURE_GROUP,
        Record=[
            {"FeatureName": "claim_id",    "ValueAsString": claim_id},
            {"FeatureName": "fraud_score", "ValueAsString": str(score)},
            {"FeatureName": "label",       "ValueAsString": "UNKNOWN"},  # updated by analyst
            *[{"FeatureName": k, "ValueAsString": str(v)} for k, v in features.items()],
        ],
    )


# ── Lambda handler ────────────────────────────────────────────────────────────

def lambda_handler(event, context):
    detail   = event["detail"]
    claim_id = detail["claim_id"]
    fields   = detail["fields"]
    payout   = detail["payout"]

    # Build feature vector for XGBoost (order must match training)
    feature_vec = [
        float(payout["net_payout"]),
        len(detail.get("tables", [[]])[0]) if detail.get("tables") else 0,
        float(fields.get("procedure_count", {}).get("value", 0) or 0),
    ]

    provider_id  = fields.get("Hospital Name", {}).get("value", "")
    claim_text   = " ".join(
        f"{k} {v['value']}" for k, v in fields.items() if v.get("value")
    )

    xgb     = xgboost_score(feature_vec)
    dup     = duplicate_score(claim_text)
    neptune_s = neptune_risk_score(provider_id)
    score   = composite_score(xgb, dup, neptune_s)

    store_feature_record(claim_id, score, {
        "xgboost_score": xgb, "duplicate_score": dup, "neptune_score": neptune_s
    })

    print(f"Claim {claim_id} | fraud_score={score} | xgb={xgb} dup={dup} neptune={neptune_s}")

    if score > FRAUD_AUTOREJECT_THRESHOLD:
        # Auto-reject + flag provider
        sqs.send_message(
            QueueUrl=FRAUD_QUEUE_URL,
            MessageBody=json.dumps({"claim_id": claim_id, "score": score, "action": "AUTO_REJECT"}),
        )
        return {"statusCode": 200, "body": json.dumps({"claim_id": claim_id, "decision": "AUTO_REJECTED"})}

    if score > FRAUD_HOLD_THRESHOLD:
        # Hold for analyst review
        sqs.send_message(
            QueueUrl=FRAUD_QUEUE_URL,
            MessageBody=json.dumps({"claim_id": claim_id, "score": score, "action": "HOLD"}),
        )
        return {"statusCode": 200, "body": json.dumps({"claim_id": claim_id, "decision": "HELD_FOR_REVIEW"})}

    # Low risk — release payment via Step Functions
    sfn.start_execution(
        stateMachineArn=SFN_PAYMENT_ARN,
        name=f"payment-{claim_id}",
        input=json.dumps({"claim_id": claim_id, "net_payout": payout["net_payout"]}),
    )
    return {"statusCode": 200, "body": json.dumps({"claim_id": claim_id, "decision": "PAYMENT_RELEASED"})}
