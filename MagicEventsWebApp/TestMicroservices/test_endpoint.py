import requests
from requests.auth import HTTPBasicAuth
from datetime import datetime, timedelta
BASE_URL = "http://localhost:8081/board"
AUTH = HTTPBasicAuth("admin", "admin123")  # Spring Security credentials

def test_create_board():
    url = f"{BASE_URL}/createBoard"
    payload = {
        "eventID": 2,  # Ensure this matches the expected event ID
        "title": "Test Board2",
        "description": "This is a test board2"
    }
    response = requests.post(url, json=payload, auth=AUTH)
    print(f"Create Board: {response.status_code}, {response.text}")

def test_delete_board():
    url = f"{BASE_URL}/deleteBoard/1"
    response = requests.delete(url, auth=AUTH)
    print(f"Delete Board: {response.status_code}, {response.text}")

def test_get_board():
    url = f"{BASE_URL}/getBoard/2/0"
    response = requests.get(url, auth=AUTH)
    print(f"Get Board: {response.status_code}, {response.json() if response.status_code == 200 else response.text}")

def test_is_board_exists():
    url = f"{BASE_URL}/isBoardExists/2"
    response = requests.get(url, auth=AUTH)
    print(f"Is Board Exists: {response.status_code}, {response.json()}")


if __name__ == "__main__":
    test_create_board()
    test_is_board_exists()
    #test_write_message()
    test_get_board()
    test_delete_board()
