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


# Create 50 patients
patients = []

for i in range(1, 51):
    patients.append(create_patient(i))


correct = 0
total = 0


# Test every patient
for patient in patients:

    # Create the unknown patient
    unknown = patient.copy()

    # Introduce a small spelling mistake
    if len(unknown["name"]) > 5:
        unknown["name"] = unknown["name"][:-1]

    results = []

    # Compare unknown patient with every candidate
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

    # Add the real patient
    results.append({
        "id": patient["id"],
        "score": calculate_score(
            unknown,
            patient
        )
    })

    # Highest score first
    results.sort(
        key=lambda x: x["score"],
        reverse=True
    )

    predicted = results[0]["id"]

    if predicted == patient["id"]:
        correct += 1

    else:
        expected_score = next(
            x["score"]
            for x in results
            if x["id"] == patient["id"]
        )

        print("❌ WRONG MATCH")
        print("Expected:", patient["id"])
        print("Predicted:", predicted)
        print("Expected score:", round(expected_score, 3))
        print("Predicted score:", round(results[0]["score"], 3))
        print()

    total += 1


# Final results
accuracy = correct / total

print("==============================")
print("TOTAL TESTS:", total)
print("CORRECT:", correct)
print("WRONG:", total - correct)
print("ACCURACY:", round(accuracy * 100, 2), "%")
print("==============================")