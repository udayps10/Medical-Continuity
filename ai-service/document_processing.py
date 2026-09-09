import re

from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity


# --------------------------------
# MEDICAL DOCUMENT
# --------------------------------

document = """
Patient Name: Rahul Sharma

Diagnosis:

Patient had a myocardial infarction in 2019.

Medication:

Patient was prescribed aspirin therapy.

Follow-up:

Patient was advised to return after 3 months.
"""


# --------------------------------
# 1. CLEAN TEXT
# --------------------------------

def clean_text(text):
    text = re.sub(r'\s+', ' ', text)
    return text.strip()


cleaned = clean_text(document)

print("CLEANED:")
print(cleaned)


# --------------------------------
# 2. CHUNK TEXT
# --------------------------------

def chunk_text(text, chunk_size=100):
    chunks = []
    for i in range(0, len(text), chunk_size):
        chunk = text[i:i + chunk_size]
        chunks.append(chunk)
    return chunks


chunks = chunk_text(cleaned)

print("\nCHUNKS:")
for i, chunk in enumerate(chunks):
    print(i, "->", chunk)


# --------------------------------
# 3. ADD METADATA
# --------------------------------

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


# --------------------------------
# 4. CREATE EMBEDDINGS
# --------------------------------

model = SentenceTransformer("all-MiniLM-L6-v2")

texts = [chunk["text"] for chunk in chunk_data]
embeddings = model.encode(texts)

print("\nEMBEDDINGS:")
for i, embedding in enumerate(embeddings):
    print("Chunk", i, "->", len(embedding), "numbers")


# --------------------------------
# 5. SEARCH / RETRIEVE
# --------------------------------

query = "What heart problem did Rahul have?"

query_embedding = model.encode([query])

scores = cosine_similarity(
    query_embedding,
    embeddings
)[0]

results = list(zip(chunk_data, scores))
results.sort(key=lambda x: x[1], reverse=True)

print("\nRETRIEVAL RESULTS:")
for chunk, score in results:
    print(round(score, 3), "->", chunk["text"])
