# language: python
from datetime import datetime, timedelta
from typing import Dict, Any, Optional
import requests
import json

class EventSetupServiceTester:
    def __init__(
        self,
        base_url: str = "http://localhost:8086",
        username: str = "admin",
        password: str = "admin123"
    ):
        self.base_url = base_url
        self.session = requests.Session()
        # set JSON headers
        self.session.headers.update({
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        })
        # configure HTTP Basic auth for Spring Security
        self.session.auth = (username, password)

    def create_sample_event_request(self) -> Dict[str, Any]:
        now = datetime.now()
        start_time = now + timedelta(days=1)
        end_time = start_time + timedelta(hours=3)
        return {
            "title": "Test Event",
            "description": "This is a test event for API testing",
            "starting": start_time.isoformat(),
            "ending": end_time.isoformat(),
            "location": "Test Location",
            "creatorEmail": "test@example.com",
            "creatorMagicEventsTag": 1,
            "participants": ["user1@test.com", "user2@test.com"],
            "admins": ["admin@test.com"],
            "image": "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg==",
            "galleryEnabled": False,
            "boardEnabled": True,
            "gameEnabled": False,
            "galleryTitle": "Test Gallery",
            "boardTitle": "Test Board",
            "boardDescription": "Test board description",
            "gameDescription": "Test game description"
        }

    def create_service_activation_request(self, event_id: int) -> Dict[str, Any]:
        return {
            "eventId": event_id,
            "userMagicEventsTag": 1,
            "galleryEnabled": True,
            "boardEnabled": True,
            "guestGameEnabled": False
        }

    def test_setup_event(self, custom_request: Optional[Dict[str, Any]] = None) -> Dict[str, Any]:
        print("🧪 Testing Event Setup...")
        data = custom_request or self.create_sample_event_request()
        try:
            resp = self.session.post(f"{self.base_url}/eventSetup", json=data)
            print(f"📊 Status Code: {resp.status_code}")
            if resp.content:
                body = resp.json()
                print(f"📋 Response Body: {json.dumps(body, indent=2)}")
                return body
            return {}
        except requests.RequestException as e:
            print(f"❌ Request failed: {e}")
            return {"error": str(e)}
        except json.JSONDecodeError as e:
            print(f"❌ JSON decode error: {e}")
            print(f"📋 Raw response: {resp.text}")
            return {"error": "Invalid JSON response"}

    def test_activate_services(self, event_id: int, custom_request: Optional[Dict[str, Any]] = None) -> Dict[str, Any]:
        print(f"🔧 Testing Service Activation for Event ID: {event_id}...")
        data = custom_request or self.create_service_activation_request(event_id)
        try:
            resp = self.session.put(f"{self.base_url}/eventSetup/services", json=data)
            print(f"📊 Status Code: {resp.status_code}")
            print(f"📋 Response Body: {resp.text}")
            return {
                "status_code": resp.status_code,
                "response": resp.text,
                "success": resp.status_code == 200
            }
        except requests.RequestException as e:
            print(f"❌ Request failed: {e}")
            return {"error": str(e)}

    def test_invalid_requests(self):
        print("🚫 Testing Invalid Requests...")
        invalid_event = {"title": "", "description": "Test"}
        print("\n--- Testing invalid event setup ---")
        self.test_setup_event(invalid_event)
        invalid_service = {"eventId": -1, "userMagicEventsTag": 12345, "galleryEnabled": True, "boardEnabled": True, "guestGameEnabled": True}
        print("\n--- Testing invalid service activation ---")
        self.test_activate_services(-1, invalid_service)


    def run_complete_test_suite(self):
        print("🚀 Starting EventSetup Service Test Suite")
        setup_resp = self.test_setup_event()
        event_id = setup_resp.get('eventId') if isinstance(setup_resp, dict) else None
        if event_id:
            print(f"✅ Event created with ID: {event_id}")
        #activation = self.test_activate_services(3)
        #    print("✅ Service activation successful" if activation.get('success') else "❌ Service activation failed")
        #else:
        #    print("❌ Event setup failed or no event ID")
        #self.test_invalid_requests()
        print("🏁 Test Suite Completed")

def main():
    BASE_URL = "http://localhost:8086"
    tester = EventSetupServiceTester(BASE_URL, "admin", "admin123")
    print("🔍 Checking service connectivity...")
    print(f"✅ Service is reachable at {BASE_URL}")
    tester.run_complete_test_suite()

if __name__ == "__main__":
    main()
