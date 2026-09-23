from fastapi import APIRouter, UploadFile, File, HTTPException

from models.ocr_extractor import extract_raw_text, extract_fields

router = APIRouter(prefix="/ocr", tags=["OCR"])


@router.post("/extract")
async def extract(file: UploadFile = File(...)):
    if not file.content_type.startswith("image/"):
        raise HTTPException(status_code=400, detail="File must be an image")

    image_bytes = await file.read()

    try:
        raw_text = extract_raw_text(image_bytes)
    except ValueError as e:
        raise HTTPException(status_code=400, detail=str(e))

    fields = extract_fields(raw_text)
    return fields