from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity

model = SentenceTransformer("all-MiniLM-L6-v2")


def similarity_score(text1, text2):

    embedding1 = model.encode([text1])
    embedding2 = model.encode([text2])

    similarity = cosine_similarity(
        embedding1,
        embedding2
    )[0][0]

    return similarity


print(similarity_score("heart attack", "myocardial infarction"))