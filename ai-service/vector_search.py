from embeddings import model
from sklearn.metrics.pairwise import cosine_similarity


def vector_search(query, documents, patient_id, threshold=0.5):

    patient_documents = [
        document["text"]
        for document in documents
        if document["patient_id"] == patient_id
    ]

    if not patient_documents:
        return []

    query_vector = model.encode([query])
    document_vectors = model.encode(patient_documents)

    scores = cosine_similarity(
        query_vector,
        document_vectors
    )[0]

    results = list(zip(patient_documents, scores))

    results.sort(key=lambda x: x[1], reverse=True)

    results = [
        (document, score)
        for document, score in results
        if score >= threshold
    ]

    return results


if __name__ == "__main__":
    documents = [
        {"patient_id": 1, "text": "Patient had a myocardial infarction in 2019."},
        {"patient_id": 1, "text": "Patient has no known medication allergies."},
        {"patient_id": 2, "text": "Patient had a heart attack in 2020."},
    ]

    results = vector_search(
        "Has this patient ever had a heart attack?",
        documents,
        1,
    )

    for document, score in results:
        print(round(score, 3), document)