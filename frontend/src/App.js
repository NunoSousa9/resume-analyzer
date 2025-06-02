import React, { useState } from 'react';
import './App.css';

function App() {
  const [file, setFile] = useState(null);
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
    setResult(null);
  };

  const handleAnalyze = async () => {
    if (!file) return;
    setLoading(true);

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch("http://localhost:5000/analyze-pdf", {
        method: "POST",
        body: formData,
      });

      const text = await response.text();
      const data = text ? JSON.parse(text) : { summary: "No response content" };

      setResult(data);
    } catch (error) {
      console.error("Error uploading PDF:", error);
      setResult({ summary: "Error communicating with the backend." });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="App">
      <h1>Resume Analyzer (PDF Upload)</h1>

      <input type="file" accept="application/pdf" onChange={handleFileChange} />
      <br />
      <button onClick={handleAnalyze} disabled={loading || !file}>
        {loading ? 'Analyzing...' : 'Analyze PDF'}
      </button>

      {result && (
        <div className="result">
          <h3>Resume Analysis</h3>

          <h4>Hard Skills</h4>
          <ul>
            {result.hard_skills?.map((skill, i) => (
              <li key={i}>{skill}</li>
            ))}
          </ul>

          <h4>Soft Skills</h4>
          <ul>
            {result.soft_skills?.map((skill, i) => (
              <li key={i}>{skill}</li>
            ))}
          </ul>

          <h3>Summary</h3>
          <p>{result.summary}</p>
        </div>
      )}
    </div>
  );
}

export default App;
