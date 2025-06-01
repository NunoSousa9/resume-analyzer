from flask import Flask, request, jsonify
from flask_cors import CORS
from transformers import pipeline

app = Flask(__name__)
CORS(app)

# Usa o pipeline localmente
pipe = pipeline("text2text-generation", model="google/flan-t5-small")

@app.route("/analyze-pdf", methods=["POST"])
def analyze_pdf():
    data = request.get_json()
    text = data.get("content", "")

    prompt = f"""
You are an AI career assistant. Analyze the following resume and extract:

- A 2-3 sentence summary.
- A list of hard skills.
- A list of soft skills.

Resume:
{text}
"""

    try:
        result = pipe(prompt, max_new_tokens=200)[0]["generated_text"]
        print("Generated output:", result)

        summary, hard, soft = "", [], []

        for line in result.splitlines():
            if line.lower().startswith("summary:"):
                summary = line.split(":", 1)[1].strip()
            elif line.lower().startswith("hard skills:"):
                hard = [s.strip() for s in line.split(":", 1)[1].split(",")]
            elif line.lower().startswith("soft skills:"):
                soft = [s.strip() for s in line.split(":", 1)[1].split(",")]

        return jsonify({
            "summary": summary,
            "hard_skills": hard,
            "soft_skills": soft
        })

    except Exception as e:
        import traceback
        traceback.print_exc()
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
