"""
XGBoost training entry point — called by SageMaker Training Job.
Reads processed CSV from /opt/ml/input/data/, trains, saves model.
"""

import os
import pandas as pd
import xgboost as xgb

def train():
    train_path = os.path.join(os.environ["SM_CHANNEL_TRAIN"], "train.csv")
    val_path   = os.path.join(os.environ["SM_CHANNEL_VALIDATION"], "validation.csv")

    train_df = pd.read_csv(train_path, header=None)
    val_df   = pd.read_csv(val_path,   header=None)

    # First column is label
    X_train, y_train = train_df.iloc[:, 1:], train_df.iloc[:, 0]
    X_val,   y_val   = val_df.iloc[:, 1:],   val_df.iloc[:, 0]

    dtrain = xgb.DMatrix(X_train, label=y_train)
    dval   = xgb.DMatrix(X_val,   label=y_val)

    params = {
        "max_depth":  int(os.environ.get("SM_HP_MAX_DEPTH", 5)),
        "eta":        float(os.environ.get("SM_HP_ETA", 0.2)),
        "objective":  "binary:logistic",
        "eval_metric": "auc",
    }
    model = xgb.train(
        params, dtrain,
        num_boost_round=int(os.environ.get("SM_HP_NUM_ROUND", 100)),
        evals=[(dval, "validation")],
        early_stopping_rounds=10,
    )
    model.save_model(os.path.join(os.environ["SM_MODEL_DIR"], "xgboost-model"))

if __name__ == "__main__":
    train()
