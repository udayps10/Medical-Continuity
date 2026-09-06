from rapidfuzz.fuzz import ratio


def name_score(name1, name2):
    return ratio(name1.lower(), name2.lower()) / 100
def get_confidence(score):
    if score >= 0.85:
        return "HIGH"
    elif score >= 0.55:
        return "REVIEW"
    else:
        return "UNRESOLVED"

def age_within_range(age1, age2):
    if age1 is None or age2 is None:
        return True

    return abs(age1 - age2) <= 10
def location_score(unknown, candidate):
    if not unknown.get("district") or not candidate.get("district"):
        return None

    if unknown["district"].lower() == candidate["district"].lower():
        return 0.4

    return 0

def calculate_score(unknown, candidate):

    score = 0
    total_weight = 0

    # Name
    if unknown["name"] and candidate["name"]:
        score += name_score(
            unknown["name"],
            candidate["name"]
        ) * 0.40
        total_weight += 0.40

    # Gender
    if unknown["gender"] and candidate["gender"]:
        if unknown["gender"].lower() == candidate["gender"].lower():
            score += 1 * 0.15

        total_weight += 0.15

    # Village
    if unknown["village"] and candidate["village"]:
        if unknown["village"].lower() == candidate["village"].lower():
            score += 1 * 0.25

        total_weight += 0.25

    # District
    district_score = location_score(unknown, candidate)

    if district_score is not None:
        score += district_score * 0.10
        total_weight += 0.10
    return score / total_weight if total_weight else 0