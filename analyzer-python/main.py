from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app)

@app.route("/analyze-pdf", methods=["POST"])
def analyze_pdf():
    data = request.get_json()
    text = data.get("content", "")

    # Mock simples
    result = {
        "hard_skills": ["Python", "Machine Learning", "APIs"],
        "soft_skills": ["Critical Thinking", "Autonomy", "Comunication"],
        "summary": "This Resume shows some skills in Python, Machine Learning and APIs, and some soft skills in Critical Thinking, Autonomy and Comunication."
    }
    return jsonify(result)

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
