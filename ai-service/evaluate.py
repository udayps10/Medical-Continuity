from matching import calculate_score, age_within_range
import random


def create_patient(patient_id):
    names = [
        "Rahul Sharma",
        "Amit Kumar",
        "Sanjay Patil",
        "Rohit Shah",
        "Priya Mehta",
        "Neha Singh",
        "Vikas Gupta",
        "Anjali Patel",
        "Rajesh Yadav",
        "Pooja Sharma"
    ]

    villages = [
        "Mira Road",
        "Dahisar",
        "Borivali",
        "Vasai",
        "Bhayandar"
    ]

    genders = ["Male", "Female"]

    return {
        "id": patient_id,
        "name": random.choice(names),
        "age": random.randint(18, 70),
        "gender": random.choice(genders),
        "village": random.choice(villages),
        "district": "Mumbai Suburban"
    }


patients = []

for i in range(1, 51):
    patients.append(create_patient(i))


correct = 0
total = 0


for patient in patients:

    unknown = patient.copy()

    # Introduce a small name error
    if len(unknown["name"]) > 5:
        unknown["name"] = unknown["name"][:-1]

    results = []

    for candidate in patients:

        if candidate["id"] == patient["id"]:
            continue

        if age_within_range(
            unknown.get("age"),
            candidate.get("age")
        ):
            score = calculate_score(
                unknown,
                candidate
            )

            results.append({
                "id": candidate["id"],
                "score": score
            })

    # Add the real patient back as the expected answer
    results.append({
        "id": patient["id"],
        "score": calculate_score(
            unknown,
            patient
        )
    })

    results.sort(
        key=lambda x: x["score"],
        reverse=True
    )

    predicted = results[0]["id"]

    if predicted == patient["id"]:
        correct += 1

    total += 1


accuracy = correct / total

print("==============================")
print("TOTAL TESTS:", total)
print("CORRECT:", correct)
print("WRONG:", total - correct)
print("ACCURACY:", round(accuracy * 100, 2), "%")
print("==============================")