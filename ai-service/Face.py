from fastapi import APIRouter, UploadFile, File, HTTPException

from models.face_matcher import generate_embedding

router = APIRouter(prefix="/face", tags=["Face"])


@router.post("/embed")
async def embed(file: UploadFile = File(...)):
    """
    Accepts a photo (registration or emergency capture), returns a
    128-dim face embedding. Store this alongside photoRef so match
    scoring doesn't need to re-run detection every comparison.
    """
    if not file.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="File must be an image")

    image_bytes = await file.read()
    embedding = generate_embedding(image_bytes)

    if embedding is None:
        raise HTTPException(status_code=422, detail="No face detected in image")

    return {"embedding": embedding}