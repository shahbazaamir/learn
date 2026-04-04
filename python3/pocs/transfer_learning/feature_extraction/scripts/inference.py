"""
inference.py — SageMaker endpoint entry point
Loads frozen ResNet-50, removes FC layer, returns 2048-dim embedding.
Packaged inside model.tar.gz alongside model weights.

This is FEATURE EXTRACTION:
  - All convolutional layers: FROZEN (weights never change)
  - Final FC layer (1000-class classifier): REMOVED
  - Output: 2048-dim vector from avg-pool layer
"""

import io
import json
import torch
import torchvision.models as models
import torchvision.transforms as transforms
from PIL import Image


# ── Load model once at container startup ─────────────────────────────────────

def model_fn(model_dir):
    model = models.resnet50(weights=models.ResNet50_Weights.IMAGENET1K_V1)

    # Remove final FC layer — output is now 2048-dim avg-pool features
    model = torch.nn.Sequential(*list(model.children())[:-1])

    # Freeze all parameters — this is what makes it feature extraction, not fine-tuning
    for param in model.parameters():
        param.requires_grad = False

    model.eval()
    return model


# ── Pre-process image ─────────────────────────────────────────────────────────

_transform = transforms.Compose([
    transforms.Resize(256),
    transforms.CenterCrop(224),
    transforms.ToTensor(),
    transforms.Normalize(mean=[0.485, 0.456, 0.406],
                         std=[0.229, 0.224, 0.225]),
])


def input_fn(request_body, content_type):
    image = Image.open(io.BytesIO(request_body)).convert("RGB")
    return _transform(image).unsqueeze(0)   # (1, 3, 224, 224)


# ── Extract embedding ─────────────────────────────────────────────────────────

def predict_fn(tensor, model):
    with torch.no_grad():
        embedding = model(tensor)           # (1, 2048, 1, 1)
    return embedding.squeeze().numpy()      # (2048,)


# ── Serialize output ──────────────────────────────────────────────────────────

def output_fn(embedding, accept):
    return json.dumps({"embedding": embedding.tolist()}), "application/json"
