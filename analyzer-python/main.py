from flask import Flask, request, jsonify
from flask_cors import CORS
from transformers import pipeline

app = Flask(__name__)
CORS(app)

# Carrega modelo público, sem token necessário
pipe = pipeline("summarization", model="sshleifer/distilbart-cnn-12-6")

@app.route("/analyze-pdf", methods=["POST"])
def analyze_pdf():
    data = request.get_json()
    text = data.get("content", "")

    try:
        summary_result = pipe("Summarize this resume:\n" + text, max_length=130, min_length=30, do_sample=False)[0]["summary_text"]
        print("➡️ Summary:", summary_result)

        # Simples heurística só para testes iniciais
        hard_skills = [word for word in ["Java", "Spring Boot", "PostgreSQL", "Docker", "Kubernetes"] if word.lower() in text.lower()]
        soft_skills = [word for word in ["teamwork", "communication", "leadership", "problem-solving"] if word.lower() in text.lower()]

        return jsonify({
            "summary": summary_result,
            "hard_skills": hard_skills,
            "soft_skills": soft_skills
        })

    except Exception as e:
        import traceback
        traceback.print_exc()
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    print("Device set to use cpu")
    app.run(host="0.0.0.0", port=5000)
