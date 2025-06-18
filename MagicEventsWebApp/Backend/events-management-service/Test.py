import requests
from requests.auth import HTTPBasicAuth

BASE_URL = "http://localhost:8080/gestion"
AUTH = HTTPBasicAuth("admin", "admin123")
HEADERS = {"Content-Type": "application/json"}

def test_create_event():
    payload = {
        "title": "Aiuto",
        "description": "Una bella sonnata.",
        "starting": "2025-07-15T09:30:00",
        "ending": "2025-07-15T17:30:00",
        "location": "Casa Magriz",
        "creator": 1,
        "partecipants": [2,5,7],
        "admins": [5,6]
    }
    response = requests.post(f"{BASE_URL}/create", json=payload, headers=HEADERS, auth=AUTH)
    if response.ok:
        event_id = int(response.text)
        print(f"Event created with ID: {event_id}")
        return event_id
    else:
        print(f"Failed to create event: {response.status_code} - {response.text}")
        return None

def test_update_admins(event_id, admin_ids, creator_id):
    params = {
        "admins": admin_ids,
        "eventId": event_id,
        "creatorId": creator_id
    }
    response = requests.get(f"{BASE_URL}/updateadmins", params=params, auth=AUTH)
    if response.ok:
        print(f"Update admins response: {response.text}")
    else:
        print(f"Failed to update admins: {response.status_code} - {response.text}")

def test_update_partecipants(event_id, participant_ids, creator_id):
    params = {
        "partecipants": participant_ids,
        "eventId": event_id,
        "creatorId": creator_id
    }
    response = requests.get(f"{BASE_URL}/addpartecipants", params=params, auth=AUTH)
    if response.ok:
        print(f"Update participants response: {response.text}")
    else:
        print(f"Failed to update participants: {response.status_code} - {response.text}")

if __name__ == "__main__":
    eid = test_create_event()
    if eid:
        test_update_admins(eid, [9], 1)
        test_update_partecipants(eid, [4, 5], 1)
