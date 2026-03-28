# Document Classifier — AWS Textract + scikit-learn

Extracts text from documents using AWS Textract, then classifies them (e.g. invoice, receipt, letter) using a TF-IDF + Naive Bayes pipeline.

## Setup

```bash
pip install -r requirements.txt
aws configure
```

## Usage

```bash
python document_classifier.py
```

To classify a real document, edit `document_classifier.py`:

```python
text = extract_text("path/to/document.jpg")
print("Predicted class:", predict(text))
```

## Project Structure

```
textract_ml/
├── document_classifier.py   # main script
├── requirements.txt
└── README.md
```

## Requirements

- AWS credentials with `textract:DetectDocumentText` permission
- Python 3.10+
