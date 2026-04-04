"""
Visual Product Search — Feature Extraction (Frozen ResNet-50)
Production use case: Customer uploads a photo → find visually similar products in catalogue.

Feature extraction (NOT fine-tuning):
  - ResNet-50 backbone weights are FROZEN (never updated)
  - Final classification layer is REMOVED
  - Output: 2048-dim embedding vector per image
  - Embeddings stored in OpenSearch k-NN index for similarity search

Pipeline:
  1. Batch job: extract embeddings for entire product catalogue → OpenSearch index
  2. Real-time: extract embedding for query image → k-NN search → return top-5 products
"""

import json
import os
import io
import boto3
import numpy as np
import sagemaker
from sagemaker.pytorch import PyTorchModel
from sagemaker.session import Session

session = Session()
role    = sagemaker.get_execution_role()
bucket  = session.default_bucket()
region  = boto3.Session().region_name

ENDPOINT_NAME       = "product-feature-extractor"
OPENSEARCH_ENDPOINT = os.environ.get("OPENSEARCH_ENDPOINT", "https://<domain>.us-east-1.es.amazonaws.com")
INDEX_NAME          = "product-embeddings"
CATALOGUE_S3        = f"s3://{bucket}/product-catalogue/images/"
TOP_K               = 5


# ── inference.py is packaged with the model (see scripts/inference.py) ────────
# It loads ResNet-50, removes the FC layer, returns 2048-dim embedding

def deploy_extractor():
    """Deploy frozen ResNet-50 feature extractor as SageMaker endpoint."""
    model = PyTorchModel(
        model_data=f"s3://{bucket}/feature-extractor/model.tar.gz",
        role=role,
        framework_version="2.0",
        py_version="py310",
        entry_point="inference.py",
        source_dir="scripts",
    )
    model.deploy(
        initial_instance_count=1,
        instance_type="ml.m5.xlarge",
        endpoint_name=ENDPOINT_NAME,
    )
    print(f"Feature extractor endpoint deployed: {ENDPOINT_NAME}")


# ── 1. Batch: index entire product catalogue into OpenSearch ──────────────────

def index_catalogue():
    """
    SageMaker Processing Job reads all product images from S3,
    extracts embeddings via endpoint, bulk-indexes into OpenSearch.
    Run once at launch, then incrementally for new products.
    """
    sm = boto3.client("sagemaker")
    sm.create_processing_job(
        ProcessingJobName="catalogue-embedding-indexer",
        RoleArn=role,
        AppSpecification={
            "ImageUri": sagemaker.image_uris.retrieve("pytorch", region, "2.0.0"),
            "ContainerEntrypoint": ["python3", "/opt/ml/processing/input/code/batch_index.py"],
        },
        ProcessingInputs=[
            {
                "InputName": "code",
                "S3Input": {
                    "S3Uri": f"s3://{bucket}/feature-extractor/scripts/",
                    "LocalPath": "/opt/ml/processing/input/code",
                    "S3DataType": "S3Prefix",
                    "S3InputMode": "File",
                },
            },
            {
                "InputName": "images",
                "S3Input": {
                    "S3Uri": CATALOGUE_S3,
                    "LocalPath": "/opt/ml/processing/input/images",
                    "S3DataType": "S3Prefix",
                    "S3InputMode": "File",
                },
            },
        ],
        ProcessingResources={
            "ClusterConfig": {
                "InstanceCount": 1,
                "InstanceType": "ml.m5.2xlarge",
                "VolumeSizeInGB": 50,
            }
        },
        Environment={
            "ENDPOINT_NAME":       ENDPOINT_NAME,
            "OPENSEARCH_ENDPOINT": OPENSEARCH_ENDPOINT,
            "INDEX_NAME":          INDEX_NAME,
        },
    )
    print("Catalogue indexing job started: catalogue-embedding-indexer")


# ── 2. Real-time: query image → embedding → k-NN search ──────────────────────

def search_similar_products(image_bytes: bytes) -> list[dict]:
    """Extract embedding for query image, search OpenSearch for top-K similar products."""
    sm_rt = boto3.client("sagemaker-runtime")

    # Extract 2048-dim embedding from frozen ResNet-50
    resp      = sm_rt.invoke_endpoint(
        EndpointName=ENDPOINT_NAME,
        ContentType="application/x-image",
        Body=image_bytes,
    )
    embedding = json.loads(resp["Body"].read())["embedding"]   # list of 2048 floats

    # k-NN search in OpenSearch
    import urllib.request
    query = json.dumps({
        "size": TOP_K,
        "_source": ["product_id", "name", "price", "category"],
        "query": {
            "knn": {
                "embedding": {"vector": embedding, "k": TOP_K}
            }
        },
    })
    req    = urllib.request.Request(
        f"{OPENSEARCH_ENDPOINT}/{INDEX_NAME}/_search",
        data=query.encode(),
        headers={"Content-Type": "application/json"},
        method="GET",
    )
    result = json.loads(urllib.request.urlopen(req).read())
    return [
        {**hit["_source"], "similarity_score": hit["_score"]}
        for hit in result["hits"]["hits"]
    ]


# ── Lambda handler — API Gateway → visual search ─────────────────────────────

def lambda_handler(event, context):
    """
    Triggered by API Gateway when customer uploads a query image.
    Returns top-5 visually similar products from catalogue.
    """
    import base64
    s3_client = boto3.client("s3")

    # Image arrives as base64 body or S3 key
    if event.get("body"):
        image_bytes = base64.b64decode(event["body"])
    else:
        body        = json.loads(event["body"])
        obj         = s3_client.get_object(Bucket=body["bucket"], Key=body["key"])
        image_bytes = obj["Body"].read()

    results = search_similar_products(image_bytes)

    return {
        "statusCode": 200,
        "headers": {"Content-Type": "application/json"},
        "body": json.dumps({"results": results}),
    }


if __name__ == "__main__":
    print("Deploying feature extractor endpoint...")
    deploy_extractor()

    print("Indexing product catalogue...")
    index_catalogue()

    print("Pipeline ready. Lambda handler active for real-time search.")
