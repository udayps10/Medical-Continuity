from fastapi import APIRouter

from models.face_matcher import compare_embeddings
from models.similarity_scorer import compute_match_score
from schemas import MatchRequest, MatchResponse, CandidateResult

router = APIRouter(prefix="/match", tags=["Match"])


@router.post("/score", response_model=MatchResponse)
async def score(request: MatchRequest):
    """
    Given one UnknownPatientCase (with embedding + demographic fields)
    and a list of candidate Patients (same shape), returns each
    candidate ranked by combined similarity score.

    This is the endpoint the Spring Boot backend calls when a
    MatchRun is triggered — its response is what populates the
    PatientMatch rows.
    """
    results = []

    for candidate in request.candidates:
        face_sim = compare_embeddings(
            request.unknown_case.embedding, candidate.fields.embedding
        )

        match_result = compute_match_score(
            face_similarity=face_sim,
            unknown_case=request.unknown_case.dict(),
            candidate=candidate.fields.dict(),
        )

        results.append(CandidateResult(
            patient_id=candidate.patient_id,
            score=match_result["score"],
            confidence_level=match_result["confidence_level"],
            breakdown=match_result["breakdown"],
        ))

    # Highest score first — this ordering is what PatientMatch
    # queries expect (findByMatchRunIdOrderBySimilarityScoreDesc).
    results.sort(key=lambda r: r.score, reverse=True)

    return MatchResponse(unknown_case_id=request.unknown_case_id, results=results)