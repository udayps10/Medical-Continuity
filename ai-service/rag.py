from openai import OpenAI
from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity

client = OpenAI()
model = SentenceTransformer("all-MiniLM-L6-v2")


# Your already-processed medical chunks
chunk_data = [
    {
        "text": "Patient had a myocardial infarction in 2019.",
        "patient_id": 101,
        "document_id": 55
    },
    {
        "text": "Patient was prescribed aspirin therapy.",
        "patient_id": 101,
        "document_id": 55
    },
    {
        "text": "Patient was advised to return after 3 months.",
        "patient_id": 101,
        "document_id": 55
    }
]


# Create embeddings for chunks
texts = [chunk["text"] for chunk in chunk_data]
embeddings = model.encode(texts)


# User's question
query = "What heart problem did Rahul have?"


# Embed the question
query_embedding = model.encode([query])


# Find similarity
scores = cosine_similarity(
    query_embedding,
    embeddings
)[0]


# Combine chunks + scores
results = list(zip(chunk_data, scores))

# Highest similarity first
results.sort(
    key=lambda x: x[1],
    reverse=True
)


# Take the best 2 chunks
top_results = results[:2]


# Build context for the LLM
context = "\n".join(
    chunk["text"]
    for chunk, score in top_results
)


# Ask the LLM using retrieved context
prompt = f"""
You are a medical information assistant.

Answer ONLY using the provided medical records.

If the answer is not present, say:
"I don't have enough information in the provided medical records."

MEDICAL RECORDS:
{context}

QUESTION:
{query}
"""


response = client.responses.create(
    model="gpt-5.6-luna",
    input=prompt
)

print("\nANSWER:")
print(response.output_text)