from rapidfuzz.fuzz import token_sort_ratio


def name_score(name1, name2):
    if not name1 or not name2:
        return None

    return token_sort_ratio(
        name1.lower().strip(),
        name2.lower().strip()
    ) / 100


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


def phone_score(unknown, candidate):
    if not unknown.get("phone") or not candidate.get("phone"):
        return None

    phone1 = str(unknown.get("phone")).replace(" ", "").replace("-", "").strip()
    phone2 = str(candidate.get("phone")).replace(" ", "").replace("-", "").strip()

    if phone1 == phone2:
        return 1
    else:
        return 0


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
    if unknown.get("name") and candidate.get("name"):
        name = name_score(
            unknown["name"],
            candidate["name"]
        )

        if name is not None:
            score += name * 0.40
            total_weight += 0.40

    # Gender
    if unknown.get("gender") and candidate.get("gender"):
        if unknown["gender"].lower() == candidate["gender"].lower():
            score += 1 * 0.15

        total_weight += 0.15

    # Village
    if unknown.get("village") and candidate.get("village"):
        if unknown["village"].lower() == candidate["village"].lower():
            score += 1 * 0.25

        total_weight += 0.25

    # Phone
    if unknown.get("phone") and candidate.get("phone"):
        phone = phone_score(unknown, candidate)

        if phone is not None:
            score += phone * 0.03
            total_weight += 0.03

    # District
    district_score = location_score(unknown, candidate)

    if district_score is not None:
        score += district_score * 0.10
        total_weight += 0.10

    return score / total_weight if total_weight else 0