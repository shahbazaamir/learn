"""
train.py — SageMaker HuggingFace Training Job entry point
Fine-tunes bert-base-uncased with two classification heads:
  1. Category: Billing / Technical / Shipping / Returns  (4 classes)
  2. Urgency:  High / Medium / Low                       (3 classes)
"""

import os
import argparse
import pandas as pd
import torch
from torch import nn
from torch.utils.data import Dataset, DataLoader
from transformers import BertTokenizer, BertModel, AdamW, get_linear_schedule_with_warmup

parser = argparse.ArgumentParser()
parser.add_argument("--model_name",       default="bert-base-uncased")
parser.add_argument("--epochs",           type=int,   default=4)
parser.add_argument("--learning_rate",    type=float, default=2e-5)
parser.add_argument("--train_batch_size", type=int,   default=32)
parser.add_argument("--eval_batch_size",  type=int,   default=64)
parser.add_argument("--max_seq_length",   type=int,   default=128)
parser.add_argument("--num_categories",   type=int,   default=4)
parser.add_argument("--num_urgencies",    type=int,   default=3)
parser.add_argument("--warmup_steps",     type=int,   default=100)
parser.add_argument("--weight_decay",     type=float, default=0.01)
parser.add_argument("--train_dir",        default=os.environ.get("SM_CHANNEL_TRAIN", "data/train"))
parser.add_argument("--val_dir",          default=os.environ.get("SM_CHANNEL_VAL",   "data/val"))
parser.add_argument("--model_dir",        default=os.environ.get("SM_MODEL_DIR",     "model"))
args = parser.parse_args()

CATEGORY_MAP = {"Billing": 0, "Technical": 1, "Shipping": 2, "Returns": 3}
URGENCY_MAP  = {"High": 0, "Medium": 1, "Low": 2}
DEVICE       = torch.device("cuda" if torch.cuda.is_available() else "cpu")


# ── Dataset ───────────────────────────────────────────────────────────────────

class TicketDataset(Dataset):
    def __init__(self, csv_path: str, tokenizer, max_len: int):
        df = pd.read_csv(csv_path)   # columns: text, category, urgency
        self.encodings = tokenizer(
            df["text"].tolist(),
            truncation=True, padding="max_length",
            max_length=max_len, return_tensors="pt",
        )
        self.cat_labels = torch.tensor([CATEGORY_MAP[c] for c in df["category"]])
        self.urg_labels = torch.tensor([URGENCY_MAP[u]  for u in df["urgency"]])

    def __len__(self):
        return len(self.cat_labels)

    def __getitem__(self, idx):
        return {
            "input_ids":      self.encodings["input_ids"][idx],
            "attention_mask": self.encodings["attention_mask"][idx],
            "cat_label":      self.cat_labels[idx],
            "urg_label":      self.urg_labels[idx],
        }


# ── Dual-head BERT model ──────────────────────────────────────────────────────

class BertTicketClassifier(nn.Module):
    def __init__(self, model_name: str, num_categories: int, num_urgencies: int):
        super().__init__()
        self.bert        = BertModel.from_pretrained(model_name)
        hidden           = self.bert.config.hidden_size          # 768
        self.dropout     = nn.Dropout(0.1)
        self.cat_head    = nn.Linear(hidden, num_categories)
        self.urg_head    = nn.Linear(hidden, num_urgencies)

    def forward(self, input_ids, attention_mask):
        cls = self.bert(input_ids=input_ids, attention_mask=attention_mask).pooler_output
        cls = self.dropout(cls)
        return self.cat_head(cls), self.urg_head(cls)


# ── Training loop ─────────────────────────────────────────────────────────────

def train():
    tokenizer = BertTokenizer.from_pretrained(args.model_name)
    train_ds  = TicketDataset(os.path.join(args.train_dir, "train.csv"), tokenizer, args.max_seq_length)
    val_ds    = TicketDataset(os.path.join(args.val_dir,   "val.csv"),   tokenizer, args.max_seq_length)
    train_dl  = DataLoader(train_ds, batch_size=args.train_batch_size, shuffle=True)
    val_dl    = DataLoader(val_ds,   batch_size=args.eval_batch_size)

    model     = BertTicketClassifier(args.model_name, args.num_categories, args.num_urgencies).to(DEVICE)
    optimizer = AdamW(model.parameters(), lr=args.learning_rate, weight_decay=args.weight_decay)
    scheduler = get_linear_schedule_with_warmup(
        optimizer, num_warmup_steps=args.warmup_steps,
        num_training_steps=len(train_dl) * args.epochs,
    )
    criterion = nn.CrossEntropyLoss()

    for epoch in range(args.epochs):
        model.train()
        for batch in train_dl:
            input_ids = batch["input_ids"].to(DEVICE)
            attn_mask = batch["attention_mask"].to(DEVICE)
            cat_logits, urg_logits = model(input_ids, attn_mask)
            loss = criterion(cat_logits, batch["cat_label"].to(DEVICE)) + \
                   criterion(urg_logits, batch["urg_label"].to(DEVICE))
            loss.backward()
            torch.nn.utils.clip_grad_norm_(model.parameters(), 1.0)
            optimizer.step()
            scheduler.step()
            optimizer.zero_grad()

        # Validation accuracy
        model.eval()
        cat_correct = urg_correct = total = 0
        with torch.no_grad():
            for batch in val_dl:
                input_ids = batch["input_ids"].to(DEVICE)
                attn_mask = batch["attention_mask"].to(DEVICE)
                cat_logits, urg_logits = model(input_ids, attn_mask)
                cat_correct += (cat_logits.argmax(1).cpu() == batch["cat_label"]).sum().item()
                urg_correct += (urg_logits.argmax(1).cpu() == batch["urg_label"]).sum().item()
                total       += len(batch["cat_label"])
        print(f"Epoch {epoch+1} | cat_acc={cat_correct/total:.4f} | urg_acc={urg_correct/total:.4f}")

    os.makedirs(args.model_dir, exist_ok=True)
    model.bert.save_pretrained(args.model_dir)
    tokenizer.save_pretrained(args.model_dir)
    torch.save({
        "cat_head": model.cat_head.state_dict(),
        "urg_head": model.urg_head.state_dict(),
    }, os.path.join(args.model_dir, "heads.pt"))
    print(f"Model saved to {args.model_dir}")


if __name__ == "__main__":
    train()
