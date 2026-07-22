"""
inference.py — SageMaker endpoint entry point
Loads fine-tuned BERT + dual heads, returns category + urgency + confidence scores.
"""

import os
import json
import torch
from torch import nn
from transformers import BertTokenizer, BertModel

CATEGORY_LABELS = ["Billing", "Technical", "Shipping", "Returns"]
URGENCY_LABELS  = ["High", "Medium", "Low"]
MAX_LEN         = 128
DEVICE          = torch.device("cuda" if torch.cuda.is_available() else "cpu")


class BertTicketClassifier(nn.Module):
    def __init__(self, model_dir: str):
        super().__init__()
        self.bert     = BertModel.from_pretrained(model_dir)
        hidden        = self.bert.config.hidden_size
        self.dropout  = nn.Dropout(0.1)
        self.cat_head = nn.Linear(hidden, len(CATEGORY_LABELS))
        self.urg_head = nn.Linear(hidden, len(URGENCY_LABELS))

    def forward(self, input_ids, attention_mask):
        cls = self.bert(input_ids=input_ids, attention_mask=attention_mask).pooler_output
        cls = self.dropout(cls)
        return self.cat_head(cls), self.urg_head(cls)


def model_fn(model_dir):
    model = BertTicketClassifier(model_dir).to(DEVICE)
    heads = torch.load(os.path.join(model_dir, "heads.pt"), map_location=DEVICE)
    model.cat_head.load_state_dict(heads["cat_head"])
    model.urg_head.load_state_dict(heads["urg_head"])
    model.eval()
    tokenizer = BertTokenizer.from_pretrained(model_dir)
    return {"model": model, "tokenizer": tokenizer}


def input_fn(request_body, content_type):
    return json.loads(request_body)["text"]


def predict_fn(text, model_bundle):
    model     = model_bundle["model"]
    tokenizer = model_bundle["tokenizer"]
    enc = tokenizer(
        text, truncation=True, padding="max_length",
        max_length=MAX_LEN, return_tensors="pt",
    )
    with torch.no_grad():
        cat_logits, urg_logits = model(
            enc["input_ids"].to(DEVICE),
            enc["attention_mask"].to(DEVICE),
        )
    cat_probs = torch.softmax(cat_logits, dim=1).squeeze().tolist()
    urg_probs = torch.softmax(urg_logits, dim=1).squeeze().tolist()
    cat_idx   = int(torch.argmax(cat_logits))
    urg_idx   = int(torch.argmax(urg_logits))
    return {
        "category":             CATEGORY_LABELS[cat_idx],
        "urgency":              URGENCY_LABELS[urg_idx],
        "category_confidence":  cat_probs[cat_idx],
        "urgency_confidence":   urg_probs[urg_idx],
        "category_scores":      dict(zip(CATEGORY_LABELS, cat_probs)),
        "urgency_scores":       dict(zip(URGENCY_LABELS,  urg_probs)),
    }


def output_fn(prediction, accept):
    return json.dumps(prediction), "application/json"
