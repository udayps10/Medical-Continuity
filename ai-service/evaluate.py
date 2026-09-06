from matching import calculate_score, age_within_range, get_confidence
import random


random.seed(42)


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


# --------------------------------
# CREATE DATASET
# --------------------------------

patients = []

for i in range(1, 51):
    patients.append(create_patient(i))


# --------------------------------
# METRICS
# --------------------------------

top1_correct = 0
top3_correct = 0

false_positives = 0
false_negatives = 0

high_confidence = 0
review_confidence = 0
unresolved_confidence = 0

total = 0


# --------------------------------
# TEST EVERY PATIENT
# --------------------------------

for patient in patients:

    unknown = patient.copy()

    # Introduce a small spelling mistake
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

    # Add the real patient
    real_score = calculate_score(
        unknown,
        patient
    )

    results.append({
        "id": patient["id"],
        "score": real_score
    })

    # Highest score first
    results.sort(
        key=lambda x: x["score"],
        reverse=True
    )

    total += 1

    # --------------------------------
    # TOP 1
    # --------------------------------

    predicted = results[0]["id"]

    if predicted == patient["id"]:
        top1_correct += 1
    else:
        false_positives += 1
        false_negatives += 1

        print("❌ WRONG MATCH")
        print("Expected:", patient["id"])
        print("Predicted:", predicted)
        print("Expected score:", round(real_score, 3))
        print("Predicted score:", round(results[0]["score"], 3))
        print()

    # --------------------------------
    # TOP 3
    # --------------------------------

    top3_ids = [
        result["id"]
        for result in results[:3]
    ]

    if patient["id"] in top3_ids:
        top3_correct += 1

    # --------------------------------
    # CONFIDENCE
    # --------------------------------

    confidence = get_confidence(results[0]["score"])

    if confidence == "HIGH":
        high_confidence += 1

    elif confidence == "REVIEW":
        review_confidence += 1

    else:
        unresolved_confidence += 1


# --------------------------------
# FINAL METRICS
# --------------------------------

top1_accuracy = top1_correct / total
top3_accuracy = top3_correct / total


print()
print("================================")
print("       DAY 3 EVALUATION")
print("================================")

print("Total tests:", total)

print(
    "Top-1 accuracy:",
    round(top1_accuracy * 100, 2),
    "%"
)

print(
    "Top-3 accuracy:",
    round(top3_accuracy * 100, 2),
    "%"
)

print(
    "False positives:",
    false_positives
)

print(
    "False negatives:",
    false_negatives
)

print()
print("Confidence distribution:")

print(
    "HIGH:",
    high_confidence
)

print(
    "REVIEW:",
    review_confidence
)

print(
    "UNRESOLVED:",
    unresolved_confidence
)

print("================================")