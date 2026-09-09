from flask import Flask, request, jsonify
from flask_cors import CORS

from matching import calculate_score, age_within_range, get_confidence

app = Flask(__name__)
CORS(app)


@app.route("/")
def home():
    return jsonify({"status": "running", "service": "ai-matching"})


@app.route("/health")
def health():
    return jsonify({"status": "ok"})


@app.route("/match", methods=["POST"])
def match_patient():
    unknown_patient = request.json.get("unknown_patient")
    candidates = request.json.get("candidates")
    results = []

    if not unknown_patient or not candidates:
        return jsonify({"error": "Invalid input"}), 400

    for candidate in candidates:
        if age_within_range(
            unknown_patient.get("age"),
            candidate.get("age")
        ):
            score = calculate_score(unknown_patient, candidate)

            if score >= 0.50:
                results.append({
                    "candidate": candidate,
                    "score": round(score, 4),
                    "confidence": get_confidence(score)
                })

    results.sort(key=lambda x: x["score"], reverse=True)

    return jsonify({
        "matches": results,
        "total": len(results)
    })


if __name__ == "__main__":
    app.run(port=5000)
