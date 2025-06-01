import './App.css';

import React, { useState } from 'react';
import './App.css';

function App() {
  const [resumeText, setResumeText] = useState('');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  // 👇 Isto puxa a variável definida no .env
  const apiUrl = process.env.REACT_APP_API_URL;

  const handleAnalyze = async () => {
    if (!resumeText.trim()) return;
    setLoading(true);

    try {
      const response = await fetch('http://localhost:8080/api/analyze', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ content: resumeText }),
      });

      const data = await response.json();
      setResult(data);
    } catch (error) {
      console.error('Error analyzing resume:', error);
      setResult({ summary: 'Error communicating with the backend.' });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="App">
      <h1>Resume Analyzer</h1>

      <textarea
        rows="10"
        cols="60"
        placeholder="Paste your resume text here..."
        value={resumeText}
        onChange={(e) => setResumeText(e.target.value)}
      ></textarea>

      <br />
      <button onClick={handleAnalyze} disabled={loading}>
        {loading ? 'Analyzing...' : 'Analyze'}
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
