import requests

BASE_URL = "http://localhost:8083/guest-game"


def create_game():
    # Description must be between 10 and 500 chars
    description = "Test Game Valid"  # 15 chars
    if not (10 <= len(description) <= 500):
        raise ValueError("`description` must be between 10 and 500 characters")

    game_info = {
        "eventId": 1,
        "description": description,
        "userMagicEventsTag": 2
    }

    r = requests.post(f"{BASE_URL}/createGame", json=game_info)
    if r.status_code == 201:
        print("Game created successfully.")
    elif r.status_code == 400:
        print(f"Validation error creating game: {r.text}")
    else:
        print(f"Failed to create game ({r.status_code}): {r.text}")


def add_guest_info():
    for i in range(1,10):
        # userMagicEventsTag must be a non-blank string
        guest_info = {
            "isMen": i % 3 == 0,
            "age": 20 + i,
            "isHostFamilyMember": i % 3 == 0,
            "isHostAssociate": i % 4 == 0,
            "haveBeard": i % 2 == 0,
            "isBald": i % 5 == 0,
            "haveGlasses": i % 4 == 0,
            "haveDarkHair": i % 2 != 0,
            "userMagicEventsTag": str(i),  # must be string and not blank
            "gameId": 1
        }

        # local validation
        if guest_info["age"] < 0 or guest_info["age"] > 130:
            print(f"Skipping Guest {i}: age {guest_info['age']} out of range")
            continue

        r = requests.post(f"{BASE_URL}/insertGuestInfo", json=guest_info)
        if r.status_code == 201:
            print(f"Guest {i} added successfully.")
        elif r.status_code == 400:
            print(f"Validation error for Guest {i}: {r.text}")
        else:
            print(f"Failed to add Guest {i} ({r.status_code}): {r.text}")


def create_decision_tree():
    event_id = 1
    user_magic_events_tag = 2

    r = requests.get(
        f"{BASE_URL}/createDecisionTree/{event_id}",
        params={"userMagicEventsTag": user_magic_events_tag}
    )
    if r.status_code == 200:
        print("Decision Tree created successfully:")
        print(r.json())
    elif r.status_code == 400:
        print(f"Validation error creating decision tree: {r.text}")
    else:
        print(f"Failed to create decision tree ({r.status_code}): {r.text}")


def delete_game(event_id):
    user_magic_events_tag = 2
    r = requests.delete(
        f"{BASE_URL}/deleteGame/{event_id}",
        params={"userMagicEventsTag": user_magic_events_tag}
    )
    if r.status_code == 200:
        success = r.json()
        print(f"Game {event_id} deleted: {success}")
    elif r.status_code == 400:
        print(f"Validation error deleting game: {r.json}")
    else:
        print(f"Failed to delete game ({r.status_code}): {r.text}")


def game_exists(event_id):
    r = requests.get(f"{BASE_URL}/gameExists/{event_id}")
    if r.status_code == 200:
        print(f"Game {event_id} exists: {r.json()}")
    elif r.status_code == 400:
        print(f"Validation error checking gameExists: {r.text}")
    else:
        print(f"Failed to check game existence ({r.status_code}): {r.text}")


if __name__ == "__main__":
    create_game()
    add_guest_info()
    create_decision_tree()
    game_exists(1)
    delete_game(1)
    # game_exists(1)











