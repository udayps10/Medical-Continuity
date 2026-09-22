from typing import List, Optional
from pydantic import BaseModel


class PersonFields(BaseModel):
    name: Optional[str] = None
    village: Optional[str] = None
    district: Optional[str] = None
    age: Optional[int] = None
    gender: Optional[str] = None
    embedding: List[float]


class Candidate(BaseModel):
    patient_id: int
    fields: PersonFields


class MatchRequest(BaseModel):
    unknown_case_id: int
    unknown_case: PersonFields
    candidates: List[Candidate]


class CandidateResult(BaseModel):
    patient_id: int
    score: float
    confidence_level: str
    breakdown: dict


class MatchResponse(BaseModel):
    unknown_case_id: int
    results: List[CandidateResult]