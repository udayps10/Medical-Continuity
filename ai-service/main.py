from fastapi import FastAPI

from routers import ocr, face, match

app = FastAPI(title="Medical Continuity AI Service", version="0.1.0")

app.include_router(ocr.router)
app.include_router(face.router)
app.include_router(match.router)


@app.get("/health")
def health():
    return {"status": "ok"}