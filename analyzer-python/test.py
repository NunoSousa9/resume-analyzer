import requests
import os

API_URL = "http://localhost:5000/analyze-pdf"
payload = {
    "content": "John Doe is a software engineer with skills in Python, Machine Learning, and API development. Strong communication and leadership abilities."
}

response = requests.post(API_URL, json=payload)
print("Status Code:", response.status_code)
print("Response JSON:", response.json())

