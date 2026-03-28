"""
Evaluates the trained XGBoost model on the validation set.
Writes evaluation.json with AUC score — read by CheckAUC ConditionStep
in fraud_retraining_pipeline.py to decide whether to deploy.
"""

import os
import json
import tarfile
import numpy as np
import pandas as pd
import xgboost as xgb
from sklearn.metrics import roc_auc_score

MODEL_DIR = "/opt/ml/processing/model"
VAL_DIR   = "/opt/ml/processing/validation"
OUT_DIR   = "/opt/ml/processing/evaluation"


def load_model() -> xgb.Booster:
    # SageMaker stores model as model.tar.gz
    tar_path = os.path.join(MODEL_DIR, "model.tar.gz")
    with tarfile.open(tar_path) as t:
        t.extractall(MODEL_DIR)
    model = xgb.Booster()
    model.load_model(os.path.join(MODEL_DIR, "xgboost-model"))
    return model


def load_validation() -> tuple[np.ndarray, np.ndarray]:
    df = pd.read_csv(os.path.join(VAL_DIR, "validation.csv"), header=None)
    y  = df.iloc[:, 0].values
    X  = df.iloc[:, 1:].values
    return X, y


if __name__ == "__main__":
    model   = load_model()
    X, y    = load_validation()

    dmat    = xgb.DMatrix(X)
    y_prob  = model.predict(dmat)
    auc     = float(roc_auc_score(y, y_prob))

    print(f"Validation AUC: {auc:.4f}")

    os.makedirs(OUT_DIR, exist_ok=True)
    with open(os.path.join(OUT_DIR, "evaluation.json"), "w") as f:
        json.dump({"auc": auc}, f)
