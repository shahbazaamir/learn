"""
Anomaly Detection on Wind Turbine Sensor Data
==============================================
Uses three unsupervised methods from scikit-learn:
  1. Isolation Forest  — good at isolating outliers in high-dimensional data
  2. Local Outlier Factor (LOF) — density-based, catches local anomalies
  3. One-Class SVM     — fits a tight boundary around normal data

Ground-truth labels from edge_status.anomaly_detected are used only for
evaluation (precision / recall / F1), not for training — pure unsupervised.

Output
------
  - Console: per-method metrics + detected anomaly breakdown
  - anomaly_results.json: full per-record predictions
  - anomaly_plots.png: time-series plots highlighting detections
"""

import json
import sys
import warnings
from pathlib import Path

import numpy as np
import pandas as pd
import matplotlib
matplotlib.use("Agg")           # headless — no display needed
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from sklearn.ensemble import IsolationForest
from sklearn.neighbors import LocalOutlierFactor
from sklearn.svm import OneClassSVM
from sklearn.preprocessing import StandardScaler
from sklearn.metrics import classification_report, confusion_matrix

warnings.filterwarnings("ignore")

# ── Paths ─────────────────────────────────────────────────────────────────────
HERE = Path(__file__).parent
DATA_FILE   = HERE / "sensor_records.json"
OUTPUT_JSON = HERE / "anomaly_results.json"
OUTPUT_PLOT = HERE / "anomaly_plots.png"

# ── Features used for detection ───────────────────────────────────────────────
FEATURE_COLS = [
    "wind_speed_ms",
    "rotor_speed_rpm",
    "power_output_kw",
    "generator_temp_c",
    "vibration_axial_mms2",
    "vibration_radial_mms2",
    "yaw_angle_deg",
]

# Derived feature: power coefficient (should be ~constant for healthy turbine)
# Added after scaling to catch power-drop and yaw-misalignment subtly
DERIVED_COLS = ["power_per_wind_cube"]


# ── 1. Load data ──────────────────────────────────────────────────────────────
def load_data(path: Path) -> pd.DataFrame:
    with open(path) as f:
        raw = json.load(f)

    rows = []
    for r in raw["records"]:
        row = {
            "timestamp":        r["timestamp"],
            "turbine_id":       r["turbine_id"],
            "anomaly_true":     int(r["edge_status"]["anomaly_detected"]),
            "anomaly_type_true": r["edge_status"].get("anomaly_type"),
        }
        row.update(r["metrics"])
        rows.append(row)

    df = pd.DataFrame(rows)
    df["timestamp"] = pd.to_datetime(df["timestamp"])
    df = df.sort_values("timestamp").reset_index(drop=True)

    # Derived feature: power / wind³ (power coefficient proxy)
    wind_cube = df["wind_speed_ms"].clip(lower=0.1) ** 3
    df["power_per_wind_cube"] = df["power_output_kw"] / wind_cube

    return df, raw["metadata"]


# ── 2. Build feature matrix ───────────────────────────────────────────────────
def build_features(df: pd.DataFrame) -> np.ndarray:
    cols = FEATURE_COLS + DERIVED_COLS
    X = df[cols].values
    scaler = StandardScaler()
    return scaler.fit_transform(X), scaler, cols


# ── 3. Models ─────────────────────────────────────────────────────────────────
def run_isolation_forest(X: np.ndarray, contamination: float) -> np.ndarray:
    model = IsolationForest(
        n_estimators=200,
        contamination=contamination,
        random_state=42,
        n_jobs=-1,
    )
    preds = model.fit_predict(X)           # -1 = anomaly, 1 = normal
    scores = model.decision_function(X)    # lower = more anomalous
    return (preds == -1).astype(int), scores


def run_lof(X: np.ndarray, contamination: float) -> np.ndarray:
    model = LocalOutlierFactor(
        n_neighbors=20,
        contamination=contamination,
        novelty=False,
    )
    preds = model.fit_predict(X)
    scores = -model.negative_outlier_factor_  # higher = more anomalous
    return (preds == -1).astype(int), scores


def run_one_class_svm(X: np.ndarray, contamination: float) -> np.ndarray:
    # Train on the majority (~normal) subset using IsolationForest pre-filter
    pre = IsolationForest(contamination=contamination, random_state=42)
    pre.fit(X)
    normal_mask = pre.predict(X) == 1
    X_train = X[normal_mask]

    model = OneClassSVM(kernel="rbf", gamma="auto", nu=contamination)
    model.fit(X_train)
    preds = model.predict(X)
    scores = -model.decision_function(X)   # higher = more anomalous
    return (preds == -1).astype(int), scores


# ── 4. Evaluate ───────────────────────────────────────────────────────────────
def evaluate(name: str, y_true: np.ndarray, y_pred: np.ndarray) -> dict:
    print(f"\n{'─'*60}")
    print(f"  {name}")
    print(f"{'─'*60}")
    print(classification_report(y_true, y_pred, target_names=["Normal", "Anomaly"],
                                 zero_division=0))
    cm = confusion_matrix(y_true, y_pred)
    tn, fp, fn, tp = cm.ravel()
    print(f"  Confusion matrix  TP={tp}  FP={fp}  FN={fn}  TN={tn}")

    report = classification_report(y_true, y_pred,
                                    target_names=["Normal", "Anomaly"],
                                    output_dict=True, zero_division=0)
    return {
        "precision": round(report["Anomaly"]["precision"], 3),
        "recall":    round(report["Anomaly"]["recall"], 3),
        "f1":        round(report["Anomaly"]["f1-score"], 3),
        "tp": int(tp), "fp": int(fp), "fn": int(fn), "tn": int(tn),
    }


# ── 5. Plot ───────────────────────────────────────────────────────────────────
def plot_results(df: pd.DataFrame, results: dict, out_path: Path):
    metrics_to_plot = [
        ("generator_temp_c",       "Generator Temp (°C)"),
        ("vibration_axial_mms2",   "Vibration Axial (mm/s²)"),
        ("power_output_kw",        "Power Output (kW)"),
        ("power_per_wind_cube",    "Power / Wind³ (proxy Cp)"),
    ]
    methods = list(results.keys())
    colors  = {"Isolation Forest": "#e74c3c",
               "LOF":              "#e67e22",
               "One-Class SVM":    "#9b59b6"}

    n_metrics = len(metrics_to_plot)
    fig, axes = plt.subplots(n_metrics, 1, figsize=(16, 4 * n_metrics), sharex=True)
    fig.suptitle("Wind Turbine Anomaly Detection — WT-402B", fontsize=14, fontweight="bold")

    t = df["timestamp"]

    for ax, (col, label) in zip(axes, metrics_to_plot):
        ax.plot(t, df[col], color="#2c3e50", linewidth=0.8, alpha=0.85, label="Sensor reading")

        # Ground truth shading
        gt_mask = df["anomaly_true"] == 1
        ax.fill_between(t, ax.get_ylim()[0], ax.get_ylim()[1],
                        where=gt_mask, alpha=0.08, color="green", label="Ground truth anomaly")

        # Per-method markers
        for method in methods:
            pred_mask = results[method]["pred"] == 1
            ax.scatter(t[pred_mask], df.loc[pred_mask, col],
                       color=colors[method], s=18, zorder=5,
                       alpha=0.7, label=f"{method} detection")

        ax.set_ylabel(label, fontsize=9)
        ax.grid(True, linestyle="--", alpha=0.4)

    # Legend on last axis
    patches = [mpatches.Patch(color="#2ecc71", alpha=0.4, label="Ground truth")]
    patches += [mpatches.Patch(color=colors[m], label=m) for m in methods]
    axes[-1].legend(handles=patches, loc="upper right", fontsize=8)
    axes[-1].set_xlabel("Timestamp", fontsize=9)

    plt.tight_layout()
    plt.savefig(out_path, dpi=130, bbox_inches="tight")
    print(f"\n  Plot saved → {out_path.name}")


# ── 6. Save results ───────────────────────────────────────────────────────────
def save_results(df: pd.DataFrame, results: dict, metadata: dict, out_path: Path):
    records_out = []
    for i, row in df.iterrows():
        records_out.append({
            "index":            i,
            "timestamp":        row["timestamp"].isoformat(),
            "anomaly_true":     bool(row["anomaly_true"]),
            "anomaly_type_true": row["anomaly_type_true"],
            "predictions": {
                method: {
                    "anomaly_predicted": bool(results[method]["pred"][i]),
                    "anomaly_score":     round(float(results[method]["score"][i]), 5),
                }
                for method in results
            },
        })

    out = {
        "metadata": metadata,
        "model_metrics": {m: results[m]["eval"] for m in results},
        "records": records_out,
    }
    with open(out_path, "w") as f:
        json.dump(out, f, indent=2, default=str)
    print(f"  Results saved → {out_path.name}")


# ── Main ──────────────────────────────────────────────────────────────────────
def main():
    print("Loading data...")
    df, metadata = load_data(DATA_FILE)
    print(f"  {len(df)} records | {df['anomaly_true'].sum()} ground-truth anomalies")

    X, scaler, feat_cols = build_features(df)
    y_true = df["anomaly_true"].values

    # Contamination ≈ fraction of anomalies in dataset
    contamination = round(df["anomaly_true"].mean(), 3)
    print(f"  Contamination rate: {contamination:.1%}  |  Features: {feat_cols}")

    print("\nRunning models...")
    methods = {
        "Isolation Forest": run_isolation_forest,
        "LOF":              run_lof,
        "One-Class SVM":    run_one_class_svm,
    }

    results = {}
    for name, fn in methods.items():
        pred, score = fn(X, contamination)
        metrics = evaluate(name, y_true, pred)
        results[name] = {"pred": pred, "score": score, "eval": metrics}

    # Summary table
    print(f"\n{'═'*60}")
    print(f"  {'Method':<20} {'Precision':>10} {'Recall':>8} {'F1':>8}")
    print(f"{'─'*60}")
    for name, res in results.items():
        e = res["eval"]
        print(f"  {name:<20} {e['precision']:>10.3f} {e['recall']:>8.3f} {e['f1']:>8.3f}")
    print(f"{'═'*60}")

    # Which anomaly types were caught?
    print("\nAnomalies caught per type (Isolation Forest):")
    if_pred = results["Isolation Forest"]["pred"]
    for atype in df["anomaly_type_true"].dropna().unique():
        mask = df["anomaly_type_true"] == atype
        caught = (if_pred[mask] == 1).sum()
        total  = mask.sum()
        print(f"  {atype:<20} {caught}/{total} detected")

    save_results(df, results, metadata, OUTPUT_JSON)
    plot_results(df, results, OUTPUT_PLOT)
    print("\nDone.")


if __name__ == "__main__":
    main()
