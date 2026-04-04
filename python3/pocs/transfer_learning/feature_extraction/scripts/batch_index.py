"""
batch_index.py — SageMaker Processing Job script
Reads all product images from /opt/ml/processing/input/images/,
extracts embeddings via SageMaker endpoint, bulk-indexes into OpenSearch.
"""

import os
import json
import boto3
import urllib.request

ENDPOINT_NAME       = os.environ["ENDPOINT_NAME"]
OPENSEARCH_ENDPOINT = os.environ["OPENSEARCH_ENDPOINT"]
INDEX_NAME          = os.environ["INDEX_NAME"]
IMAGE_DIR           = "/opt/ml/processing/input/images"

sm_rt = boto3.client("sagemaker-runtime")


def create_index():
    """Create OpenSearch k-NN index if not exists."""
    mapping = json.dumps({
        "settings": {"index": {"knn": True}},
        "mappings": {
            "properties": {
                "embedding":  {"type": "knn_vector", "dimension": 2048},
                "product_id": {"type": "keyword"},
                "name":       {"type": "text"},
                "price":      {"type": "float"},
                "category":   {"type": "keyword"},
            }
        },
    })
    req = urllib.request.Request(
        f"{OPENSEARCH_ENDPOINT}/{INDEX_NAME}",
        data=mapping.encode(),
        headers={"Content-Type": "application/json"},
        method="PUT",
    )
    try:
        urllib.request.urlopen(req)
        print(f"Index created: {INDEX_NAME}")
    except Exception as e:
        print(f"Index may already exist: {e}")


def extract_embedding(image_path: str) -> list[float]:
    with open(image_path, "rb") as f:
        image_bytes = f.read()
    resp = sm_rt.invoke_endpoint(
        EndpointName=ENDPOINT_NAME,
        ContentType="application/x-image",
        Body=image_bytes,
    )
    return json.loads(resp["Body"].read())["embedding"]


def bulk_index(documents: list[dict]):
    """OpenSearch bulk API — index all embeddings in one request."""
    lines = []
    for doc in documents:
        lines.append(json.dumps({"index": {"_index": INDEX_NAME, "_id": doc["product_id"]}}))
        lines.append(json.dumps(doc))
    body = "\n".join(lines) + "\n"
    req  = urllib.request.Request(
        f"{OPENSEARCH_ENDPOINT}/_bulk",
        data=body.encode(),
        headers={"Content-Type": "application/x-ndjson"},
        method="POST",
    )
    result = json.loads(urllib.request.urlopen(req).read())
    errors = [i for i in result["items"] if "error" in i.get("index", {})]
    print(f"Indexed {len(documents) - len(errors)} / {len(documents)} documents")


if __name__ == "__main__":
    create_index()

    documents = []
    for fname in os.listdir(IMAGE_DIR):
        if not fname.lower().endswith((".jpg", ".jpeg", ".png")):
            continue

        product_id = fname.rsplit(".", 1)[0]
        image_path = os.path.join(IMAGE_DIR, fname)

        embedding = extract_embedding(image_path)

        # In production, product metadata comes from a catalogue DB / CSV
        documents.append({
            "product_id": product_id,
            "embedding":  embedding,
            "name":       f"Product {product_id}",
            "price":      0.0,
            "category":   "unknown",
        })

        if len(documents) % 100 == 0:
            bulk_index(documents)
            documents = []

    if documents:
        bulk_index(documents)

    print("Catalogue indexing complete.")
