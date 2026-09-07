from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity

model = SentenceTransformer("all-MiniLM-L6-v2")

documents = [
    "Patient had a heart attack in 2024",
    "Patient has diabetes and takes insulin",
    "Patient suffered a broken leg",
    "Patient has high blood pressure"
]

query = "history of heart attack"

query_embedding = model.encode([query])
document_embeddings = model.encode(documents)

scores = cosine_similarity(
    query_embedding,
    
    document_embeddings
)[0]

for document, score in zip(documents, scores):
    print(score, document)