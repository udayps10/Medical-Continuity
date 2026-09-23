"""
Face embedding generation and comparison.

Uses face_recognition (dlib-based) to produce a 128-dimension
embedding per face. Embeddings are what get compared between an
UnknownPatientCase photo and candidate Patient photos — not the
raw images themselves.
"""

import face_recognition
import numpy as np


def generate_embedding(image_bytes: bytes) -> list:
    """
    Returns a 128-dim face embedding for the first face found in the
    image. Returns None if no face is detected (bad photo, no face
    visible, etc.) so the caller can ask for a re-capture.
    """
    image = _load_from_bytes(image_bytes)

    face_locations = face_recognition.face_locations(image)
    if not face_locations:
        return None

    encodings = face_recognition.face_encodings(image, known_face_locations=face_locations)
    if not encodings:
        return None

    # If multiple faces are in frame, use the largest (closest to camera) —
    # registration/emergency photos should ideally be single-subject, but
    # this avoids picking a random bystander's face if one slips in.
    if len(face_locations) > 1:
        largest_idx = _largest_face_index(face_locations)
        return encodings[largest_idx].tolist()

    return encodings[0].tolist()


def _load_from_bytes(image_bytes: bytes) -> np.ndarray:
    import io
    from PIL import Image
    pil_img = Image.open(io.BytesIO(image_bytes)).convert("RGB")
    return np.array(pil_img)


def _largest_face_index(face_locations: list) -> int:
    areas = []
    for (top, right, bottom, left) in face_locations:
        areas.append((bottom - top) * (right - left))
    return int(np.argmax(areas))


def compare_embeddings(embedding_a: list, embedding_b: list) -> float:
    """
    Returns a similarity score in [0, 1] — 1.0 is identical,
    0.0 is maximally different. face_recognition's face_distance
    returns a distance (lower = more similar, typically 0-1.2 range
    for real comparisons), so we invert and clamp it.
    """
    a = np.array(embedding_a)
    b = np.array(embedding_b)
    distance = np.linalg.norm(a - b)

    # Empirically, distance < 0.6 is generally considered a match
    # by face_recognition's own convention. We map distance to a
    # 0-1 similarity score using that as the reference point.
    similarity = max(0.0, 1.0 - (distance / 1.2))
    return round(float(similarity), 4)