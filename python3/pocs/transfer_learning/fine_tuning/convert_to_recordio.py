"""
Convert labelled product images to RecordIO format for SageMaker Image Classification.
Run locally before uploading dataset.

Folder structure expected:
  data/images/
    OK/           img_001.jpg, img_002.jpg ...
    SCRATCH/      img_001.jpg ...
    DENT/         img_001.jpg ...
    DISCOLORATION/img_001.jpg ...
"""

import os
import random
import struct
import numpy as np
from PIL import Image

CLASSES     = ["OK", "SCRATCH", "DENT", "DISCOLORATION"]
IMAGE_DIR   = "data/images"
OUTPUT_DIR  = "data"
IMAGE_SIZE  = (224, 224)
TRAIN_SPLIT = 0.8


def write_recordio(records: list[tuple[int, bytes]], path: str):
    """Write list of (label, image_bytes) to .rec file."""
    with open(path, "wb") as f:
        for idx, (label, img_bytes) in enumerate(records):
            header = struct.pack("If", label, float(idx))
            record = header + img_bytes
            # RecordIO magic + length prefix
            magic  = struct.pack("I", 0xced7230a)
            length = struct.pack("I", len(record))
            f.write(magic + length + record)


def load_images() -> list[tuple[int, bytes]]:
    records = []
    for label_idx, cls in enumerate(CLASSES):
        cls_dir = os.path.join(IMAGE_DIR, cls)
        if not os.path.isdir(cls_dir):
            continue
        for fname in os.listdir(cls_dir):
            if not fname.lower().endswith((".jpg", ".jpeg", ".png")):
                continue
            img = Image.open(os.path.join(cls_dir, fname)).convert("RGB").resize(IMAGE_SIZE)
            import io
            buf = io.BytesIO()
            img.save(buf, format="JPEG")
            records.append((label_idx, buf.getvalue()))
    return records


if __name__ == "__main__":
    records = load_images()
    random.shuffle(records)

    split     = int(len(records) * TRAIN_SPLIT)
    train_rec = records[:split]
    val_rec   = records[split:]

    os.makedirs(OUTPUT_DIR, exist_ok=True)
    write_recordio(train_rec, os.path.join(OUTPUT_DIR, "train.rec"))
    write_recordio(val_rec,   os.path.join(OUTPUT_DIR, "val.rec"))

    print(f"Train: {len(train_rec)} | Validation: {len(val_rec)}")
    print(f"Output: {OUTPUT_DIR}/train.rec, {OUTPUT_DIR}/val.rec")
