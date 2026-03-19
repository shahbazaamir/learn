"""
Document Classifier using AWS Textract + scikit-learn
Extracts text from documents via Textract, then classifies them.
"""

import boto3
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.naive_bayes import MultinomialNB
from sklearn.pipeline import Pipeline
import pickle, os

# --- 1. Extract text from a local image/PDF using Textract ---

def extract_text(file_path: str) -> str:
    client = boto3.client("textract", region_name="us-east-1")
    with open(file_path, "rb") as f:
        response = client.detect_document_text(Document={"Bytes": f.read()})
    return " ".join(
        block["Text"]
        for block in response["Blocks"]
        if block["BlockType"] == "LINE"
    )


# --- 2. Train a simple classifier ---

def train(texts: list[str], labels: list[str], model_path="model.pkl"):
    pipeline = Pipeline([
        ("tfidf", TfidfVectorizer(stop_words="english")),
        ("clf",   MultinomialNB()),
    ])
    pipeline.fit(texts, labels)
    with open(model_path, "wb") as f:
        pickle.dump(pipeline, f)
    print(f"Model saved to {model_path}")
    return pipeline


# --- 3. Predict document type ---

def predict(text: str, model_path="model.pkl") -> str:
    with open(model_path, "rb") as f:
        pipeline = pickle.load(f)
    return pipeline.predict([text])[0]


# --- Demo ---

if __name__ == "__main__":
    # Toy training data (replace with real extracted texts)
    training_texts = [
        "invoice total amount due payment bill",
        "receipt purchased items store thank you",
        "dear sir letter sincerely regards",
        "invoice number date due amount",
        "receipt total paid cash change",
    ]
    training_labels = ["invoice", "receipt", "letter", "invoice", "receipt"]

    model = train(training_texts, training_labels)

    # To classify a real document:
    # text = extract_text("path/to/your/document.jpg")
    # print("Predicted class:", predict(text))

    # Quick demo with a sample text
    sample = "total amount due invoice number 1234"
    print("Sample text     :", sample)
    print("Predicted class :", predict(sample))
