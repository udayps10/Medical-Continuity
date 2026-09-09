```python
import re
from sentence_transformers import SentenceTransformer


document = """
Patient Name: Rahul Sharma

Diagnosis:

Patient had a myocardial infarction in 2019.

Medication:

Patient was prescribed aspirin therapy.

Follow-up:

Patient was advised to return after 3 months.
"""


# 1. CLEAN THE DOCUMENT
def clean_text(text):
    # Remove extra whitespace and blank lines
    text = re.sub(r'\s+', ' ', text)
    return text.strip()


cleaned = clean_text(document)

print("CLEANED:")
print(cleaned)


# 2. SPLIT DOCUMENT INTO CHUNKS
def chunk_text(text, chunk_size=100):
    chunks = []

    for i in range(0, len(text), chunk_size):
        chunk = text[i:i + chunk_size]
        chunks.append(chunk)

    return chunks


chunks = chunk_text(cleaned)

print("\nCHUNKS:")

for i, chunk in enumerate(chunks):
    print(i, "→", chunk)


# 3. ADD METADATA TO EACH CHUNK
chunk_data = []

for i, chunk in enumerate(chunks):

    data = {
        "text": chunk,
        "patient_id": 101,
        "document_id": 55,
        "chunk_id": i,
        "document_type": "medical_history"
    }

    chunk_data.append(data)


print("\nCHUNKS WITH METADATA:")

for chunk in chunk_data:
    print(chunk)


# 4. CREATE EMBEDDINGS
model = SentenceTransformer("all-MiniLM-L6-v2")

texts = [chunk["text"] for chunk in chunk_data]

embeddings = model.encode(texts)

print("\nEMBEDDINGS:")

for i, embedding in enumerate(embeddings):
    print("Chunk", i, "→", len(embedding), "numbers")
```
