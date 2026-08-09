"""
Generates synthetic sensor records based on sensor.json schema.
Produces ~500 records with realistic time-series values and injected anomalies.
Output: sensor_records.json
"""

import json
import random
import math
from datetime import datetime, timedelta, timezone

random.seed(42)

TURBINE_ID = "WT-402B"
LOCATION = {"latitude": 12.9716, "longitude": 77.5946}
START_TIME = datetime(2026, 7, 20, 0, 0, 0, tzinfo=timezone.utc)
INTERVAL_MINUTES = 10
NUM_RECORDS = 500

# Anomaly windows: record index ranges where specific metrics go haywire
ANOMALY_WINDOWS = [
    # Overheating event (generator temp spikes)
    {"range": (60, 70),  "type": "overheating"},
    # High vibration event (bearing fault signature)
    {"range": (140, 148), "type": "high_vibration"},
    # Power drop despite good wind (possible blade pitch fault)
    {"range": (230, 238), "type": "power_drop"},
    # Yaw misalignment event
    {"range": (310, 318), "type": "yaw_misalign"},
    # Combined stress event (temp + vibration)
    {"range": (420, 432), "type": "combined_stress"},
]


def in_anomaly(idx):
    for w in ANOMALY_WINDOWS:
        if w["range"][0] <= idx < w["range"][1]:
            return w["type"]
    return None


def gaussian(mu, sigma):
    return random.gauss(mu, sigma)


def clamp(val, lo, hi):
    return max(lo, min(hi, val))


def generate_record(idx, timestamp, wind_speed):
    anomaly_type = in_anomaly(idx)
    anomaly_detected = anomaly_type is not None

    # Wind is the driver — rotor speed and power follow it
    wind_dir = (215.0 + gaussian(0, 5)) % 360
    rotor_rpm = clamp(wind_speed * 1.32 + gaussian(0, 0.4), 0, 25)
    # Power curve: roughly cubic up to rated, flat above rated wind (~12 m/s)
    rated_wind = 12.0
    rated_power = 2000.0
    if wind_speed < 3:
        power_kw = 0.0
    elif wind_speed >= rated_wind:
        power_kw = rated_power + gaussian(0, 15)
    else:
        power_kw = rated_power * (wind_speed / rated_wind) ** 3 + gaussian(0, 20)
    power_kw = clamp(power_kw, 0, 2200)

    gen_temp = clamp(50 + wind_speed * 1.1 + gaussian(0, 1.5), 30, 120)
    vib_axial = clamp(gaussian(1.2, 0.15), 0.1, 10)
    vib_radial = clamp(gaussian(0.8, 0.1), 0.05, 10)
    yaw_angle = clamp(wind_dir + gaussian(0, 3), 0, 360)
    cpu = clamp(gaussian(24, 3), 5, 100)
    signal = clamp(gaussian(-68, 4), -100, -40)

    # --- Inject anomalies ---
    if anomaly_type == "overheating":
        gen_temp = clamp(gaussian(105, 5), 95, 120)      # temp spike
        cpu = clamp(gaussian(55, 5), 45, 80)             # edge device reacts

    elif anomaly_type == "high_vibration":
        vib_axial = clamp(gaussian(6.5, 0.8), 4.5, 10)  # axial vibration surge
        vib_radial = clamp(gaussian(5.0, 0.6), 3.5, 10) # radial too

    elif anomaly_type == "power_drop":
        # Wind is fine but power collapses — blade pitch fault
        power_kw = clamp(gaussian(300, 50), 100, 500)
        rotor_rpm = clamp(gaussian(5, 1), 2, 8)

    elif anomaly_type == "yaw_misalign":
        yaw_angle = clamp(wind_dir + gaussian(35, 5), 0, 360)  # big yaw error
        power_kw *= 0.65  # misalignment cuts power

    elif anomaly_type == "combined_stress":
        gen_temp = clamp(gaussian(98, 4), 90, 115)
        vib_axial = clamp(gaussian(5.8, 0.7), 4.0, 10)
        vib_radial = clamp(gaussian(4.2, 0.5), 3.0, 10)

    return {
        "turbine_id": TURBINE_ID,
        "timestamp": timestamp.strftime("%Y-%m-%dT%H:%M:%SZ"),
        "location": LOCATION,
        "metrics": {
            "wind_speed_ms": round(wind_speed, 2),
            "wind_direction_deg": round(wind_dir, 1),
            "rotor_speed_rpm": round(rotor_rpm, 2),
            "power_output_kw": round(power_kw, 1),
            "generator_temp_c": round(gen_temp, 1),
            "vibration_axial_mms2": round(vib_axial, 3),
            "vibration_radial_mms2": round(vib_radial, 3),
            "yaw_angle_deg": round(yaw_angle, 1),
        },
        "edge_status": {
            "anomaly_detected": anomaly_detected,
            "anomaly_type": anomaly_type,          # null for normal records
            "cpu_usage_pct": round(cpu, 1),
            "signal_strength_dbm": round(signal, 1),
        },
    }


def simulate_wind(num_records):
    """Simulate a slowly-varying wind speed with diurnal pattern."""
    speeds = []
    speed = 11.0
    for i in range(num_records):
        # Diurnal variation: peaks in afternoon (~record 90 each day of 144 records/day)
        hour_of_day = (i * INTERVAL_MINUTES // 60) % 24
        diurnal = 1.5 * math.sin(math.pi * (hour_of_day - 6) / 12)
        # Random walk
        speed += gaussian(0, 0.3)
        speed = clamp(speed + diurnal * 0.05, 2.0, 18.0)
        speeds.append(speed)
    return speeds


def main():
    wind_speeds = simulate_wind(NUM_RECORDS)
    records = []
    for i in range(NUM_RECORDS):
        ts = START_TIME + timedelta(minutes=i * INTERVAL_MINUTES)
        record = generate_record(i, ts, wind_speeds[i])
        records.append(record)

    output = {
        "metadata": {
            "turbine_id": TURBINE_ID,
            "total_records": NUM_RECORDS,
            "interval_minutes": INTERVAL_MINUTES,
            "start_time": START_TIME.strftime("%Y-%m-%dT%H:%M:%SZ"),
            "end_time": (START_TIME + timedelta(minutes=(NUM_RECORDS - 1) * INTERVAL_MINUTES))
                        .strftime("%Y-%m-%dT%H:%M:%SZ"),
            "anomaly_windows": ANOMALY_WINDOWS,
            "anomaly_summary": {
                "overheating": "records 60-69: generator_temp_c spikes to ~105°C",
                "high_vibration": "records 140-147: vibration_axial/radial surge (bearing fault)",
                "power_drop": "records 230-237: power collapses despite adequate wind (blade pitch fault)",
                "yaw_misalign": "records 310-317: yaw error ~35° causing ~35% power loss",
                "combined_stress": "records 420-431: simultaneous temp + vibration anomaly",
            },
        },
        "records": records,
    }

    out_path = "sensor_records.json"
    with open(out_path, "w") as f:
        json.dump(output, f, indent=2)

    anomaly_count = sum(1 for r in records if r["edge_status"]["anomaly_detected"])
    print(f"Generated {NUM_RECORDS} records → {out_path}")
    print(f"  Normal records : {NUM_RECORDS - anomaly_count}")
    print(f"  Anomaly records: {anomaly_count}")
    for w in ANOMALY_WINDOWS:
        print(f"  [{w['type']:18s}] records {w['range'][0]}–{w['range'][1]-1}")


if __name__ == "__main__":
    main()
