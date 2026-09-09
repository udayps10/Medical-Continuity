import pytesseract
from PIL import Image

from matching import calculate_score, age_within_range


image = Image.open("medical_note.png")


text = pytesseract.image_to_string(image)

print("RAW OCR:")
print(text)


patient = {}

for line in text.splitlines():

    if ":" not in line:
        continue

    key, value = line.split(":", 1)

    key = key.strip().lower()
    value = value.strip()

    patient[key] = value


patient_data = {
    "name": patient.get("patient name"),
    "age": int(patient["age"]) if patient.get("age", "").isdigit() else None,
    "gender": patient.get("gender"),
    "village": patient.get("village"),
    "district": patient.get("district"),
    "phone": patient.get("phone")
}
print("\nVALIDATION:")

if not patient_data["name"]:
    print("⚠️ Name missing")

if patient_data["age"] is None:
    print("⚠️ Age missing or unreadable")

if not patient_data["gender"]:
    print("⚠️ Gender missing")

if not patient_data["village"]:
    print("⚠️ Village missing")

if not patient_data["district"]:
    print("⚠️ District missing")

if not patient_data["phone"]:
    print("⚠️ Phone missing")

print("\nSTRUCTURED PATIENT:")
print(patient_data)


candidates = [
    {
        "name": "Rahul Sharma",
        "age": 45,
        "gender": "Male",
        "village": "Rampur",
        "district": "Jaipur",
        "phone": None
    },
    {
        "name": "Rohit Sharma",
        "age": 44,
        "gender": "Male",
        "village": "Rampur",
        "district": "Jaipur",
        "phone": None
    },
    {
        "name": "Amit Patel",
        "age": 30,
        "gender": "Male",
        "village": "Delhi",
        "district": "Delhi",
        "phone": None
    }
]


results = []

for candidate in candidates:

    if not age_within_range(
        patient_data["age"],
        candidate["age"]
    ):
        continue

    score = calculate_score(
        patient_data,
        candidate
    )

    results.append(
        (candidate["name"], score)
    )


results.sort(
    key=lambda x: x[1],
    reverse=True
)


print("\nMATCH RESULTS:")

for name, score in results:
    print(
        round(score * 100, 2),
        "% →",
        name
    )
