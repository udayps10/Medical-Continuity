"""
Multi-factor similarity scoring.

Combines face-embedding similarity with demographic fields to
produce a final confidence score for a candidate match between
an UnknownPatientCase and a Patient. This is what eventually
populates PatientMatch.similarityScore and confidenceLevel.
"""

from rapidfuzz import fuzz

# Tunable weights — adjust these once you have real test data to
# validate against. Face carries the most weight since it's the
# most reliable signal; demographic fields are supporting evidence.
DEFAULT_WEIGHTS = {
    "face": 0.5,
    "name": 0.25,
    "village": 0.15,
    "age": 0.10,
}

AGE_TOLERANCE_YEARS = 5


def name_similarity(name_a: str, name_b: str) -> float:
    """Fuzzy string match, 0-1. Handles OCR/transcription typos
    that exact matching would miss entirely."""
    if not name_a or not name_b:
        return 0.0
    return fuzz.ratio(name_a.lower().strip(), name_b.lower().strip()) / 100.0


def village_match_score(
    village_a: str, district_a: str,
    village_b: str, district_b: str,
) -> float:
    """1.0 for exact village match, 0.5 for same district
    (different or missing village), 0.0 otherwise."""
    if village_a and village_b and village_a.strip().lower() == village_b.strip().lower():
        return 1.0
    if district_a and district_b and district_a.strip().lower() == district_b.strip().lower():
        return 0.5
    return 0.0


def age_match_score(age_a: int, age_b: int, tolerance: int = AGE_TOLERANCE_YEARS) -> float:
    """1.0 for exact match, linearly decreasing to 0 at the
    tolerance boundary. Reported ages in disaster settings are
    often approximate, so exact matching would be too strict."""
    if age_a is None or age_b is None:
        return 0.0
    diff = abs(age_a - age_b)
    return max(0.0, 1.0 - (diff / tolerance))


def gender_hard_filter(gender_a: str, gender_b: str) -> bool:
    """Returns True if genders are compatible (match, or either is
    unknown). Mismatched genders exclude a candidate entirely rather
    than just lowering its score."""
    if not gender_a or not gender_b:
        return True
    return gender_a.strip().upper() == gender_b.strip().upper()


def compute_match_score(
    face_similarity: float,
    unknown_case: dict,
    candidate: dict,
    weights: dict = None,
) -> dict:
    """
    unknown_case / candidate: dicts with keys
      name, village, district, age, gender

    Returns dict with final score, confidence level, and per-factor
    breakdown (useful for the 'evidence' field on PatientMatch).
    """
    weights = weights or DEFAULT_WEIGHTS

    if not gender_hard_filter(unknown_case.get("gender"), candidate.get("gender")):
        return {
            "score": 0.0,
            "confidence_level": "EXCLUDED",
            "breakdown": {"reason": "gender mismatch"},
        }

    name_sim = name_similarity(unknown_case.get("name"), candidate.get("name"))
    village_sim = village_match_score(
        unknown_case.get("village"), unknown_case.get("district"),
        candidate.get("village"), candidate.get("district"),
    )
    age_sim = age_match_score(unknown_case.get("age"), candidate.get("age"))

    score = (
        weights["face"] * face_similarity +
        weights["name"] * name_sim +
        weights["village"] * village_sim +
        weights["age"] * age_sim
    )
    score = round(score, 4)

    return {
        "score": score,
        "confidence_level": confidence_level_for(score),
        "breakdown": {
            "face_similarity": round(face_similarity, 4),
            "name_similarity": round(name_sim, 4),
            "village_similarity": round(village_sim, 4),
            "age_similarity": round(age_sim, 4),
        },
    }


def confidence_level_for(score: float) -> str:
    """
    Thresholds are starting points — tune against real test data.
    Maps to PatientMatch.confidenceLevel enum (LOW/MEDIUM/HIGH/VERY_HIGH).
    """
    if score >= 0.85:
        return "VERY_HIGH"
    if score >= 0.65:
        return "HIGH"
    if score >= 0.40:
        return "MEDIUM"
    return "LOW"