import asyncio
import json
import websockets
import requests
from datetime import datetime
import base64
import os

BASE_URL = "http://localhost:8085"
WS_URL = "ws://localhost:8085/gallery/websocket"
AUTH = ("admin", "admin123")
IMAGE_PATH = "/home/patric/Documents/mask_eta.png"
REACT_ORIGIN = "http://localhost:3000"
HEADERS = {"Origin": REACT_ORIGIN, "Content-Type": "application/json"}

def load_base64_image(path):
    if not os.path.exists(path):
        print(f"Image file not found: {path}")
        return ""
    with open(path, "rb") as img_file:
        return base64.b64encode(img_file.read()).decode()

class WebSocketClient:
    def __init__(self, event_id, username):
        self.event_id = event_id
        self.username = username
        self.websocket = None
        self.received_images = []

    async def connect(self):
        try:
            self.websocket = await websockets.connect(
                WS_URL, origin=REACT_ORIGIN
            )
            print(f"[{self.username}] Connected to {WS_URL}")
            return True
        except Exception as e:
            print(f"[{self.username}] Connection failed: {e}")
            return False

    async def send_stomp_connect(self):
        frame = "CONNECT\naccept-version:1.0,1.1,2.0\nhost:localhost\n\n\x00"
        await self.websocket.send(frame)
        resp = await self.websocket.recv()
        print(f"[{self.username}] STOMP CONNECT → {resp}")

    async def subscribe_to_topics(self):
        topics = [
            f"/topic/gallery/{self.event_id}",
            f"/topic/gallery/deleteImage/{self.event_id}",
            f"/topic/gallery/imageLike/{self.event_id}"
        ]
        for t in topics:
            frame = (
                f"SUBSCRIBE\n"
                f"id:sub-{t.split('/')[-1]}-{self.username}\n"
                f"destination:{t}\n\n\x00"
            )
            await self.websocket.send(frame)
        print(f"[{self.username}] Subscribed to all topics")

    async def send_image(self, title, b64):
        data = {
            "eventID": self.event_id,
            "title": title,
            "base64Image": b64,
            "uploadedBy": self.username,
            "magiceventstag": 2,
            "dateTime": datetime.now().isoformat()
        }
        frame = (
            f"SEND\n"
            f"destination:/app/gallery/sendImage/{self.event_id}\n"
            f"content-type:application/json\n\n"
            f"{json.dumps(data)}\x00"
        )
        await self.websocket.send(frame)
        print(f"[{self.username}] Sent image: {title}")

    async def delete_image(self, image_id):
        data = {
            "eventID": self.event_id,
            "imageID": image_id,
            "deletedBy": self.username,
            "magiceventstag": 2
        }
        frame = (
            f"SEND\n"
            f"destination:/app/gallery/deleteImage/{self.event_id}\n"
            f"content-type:application/json\n\n"
            f"{json.dumps(data)}\x00"
        )
        await self.websocket.send(frame)
        print(f"[{self.username}] Requested deletion of image {image_id}")

    async def like_image(self, image_id, like=True):
        data = {
            "eventID": self.event_id,
            "imageID": image_id,
            "userMagicEventsTag": 2,
            "like": like
        }
        frame = (
            f"SEND\n"
            f"destination:/app/gallery/imageLike/{self.event_id}\n"
            f"content-type:application/json\n\n"
            f"{json.dumps(data)}\x00"
        )
        await self.websocket.send(frame)
        action = "liked" if like else "unliked"
        print(f"[{self.username}] {action} image {image_id}")

    async def listen_for_messages(self):
        try:
            while True:
                msg = await self.websocket.recv()
                if msg.startswith("MESSAGE"):
                    parts = msg.split("\n")
                    dest = ""
                    body = ""
                    capture = False
                    for line in parts:
                        if line.startswith("destination:"):
                            dest = line.split(":", 1)[1]
                        if capture:
                            body += line
                        if line == "":
                            capture = True
                    if body and body != "\x00":
                        data = json.loads(body.replace("\x00", ""))
                        if "/topic/gallery/" in dest and "deleteImage" not in dest and "imageLike" not in dest:
                            print(f"[{self.username}] Received image '{data.get('title')}' by {data.get('uploadedBy')}")
                            self.received_images.append(data)
                        elif "deleteImage" in dest:
                            print(f"[{self.username}] Deletion notice: image {data.get('imageID')} by {data.get('deletedBy')}")
                        elif "imageLike" in dest:
                            act = "liked" if data.get("like") else "unliked"
                            print(f"[{self.username}] Image {data.get('imageID')} {act} by {data.get('userMagicEventsTag')} (total {data.get('likedCount')})")
                else:
                    print(f"[{self.username}] {msg}")
        except websockets.exceptions.ConnectionClosed:
            print(f"[{self.username}] WebSocket closed")

    async def disconnect(self):
        if self.websocket:
            await self.websocket.close()
            print(f"[{self.username}] Disconnected")

def test_create_gallery(event_id):
    url = f"{BASE_URL}/gallery/createGallery"
    payload = {
        "eventID": event_id,
        "title": "Test Gallery",
        "userMagicEventsTag": 2
    }
    resp = requests.post(url, json=payload, auth=AUTH, headers=HEADERS)
    print(f"CreateGallery status: {resp.status_code}")
    return resp.status_code == 200

def check_gallery_exists(event_id):
    url = f"{BASE_URL}/gallery/isGalleryExists/{event_id}"
    resp = requests.get(url, auth=AUTH, headers={"Origin": REACT_ORIGIN})
    if resp.status_code == 200:
        exists = resp.json()
        print(f"Gallery {event_id} exists: {exists}")
        return exists
    return False

def get_gallery_images(event_id):
    url = f"{BASE_URL}/gallery/getGallery/{event_id}/0?userMagicEventsTag={2}"
    resp = requests.get(url, auth=AUTH, headers={"Origin": REACT_ORIGIN})
    if resp.status_code == 200:
        imgs = resp.json().get("images", [])
        print(f"Found {len(imgs)} images")
        return imgs
    return []

async def user_simulation(event_id, username, delay=0):
    await asyncio.sleep(delay)
    client = WebSocketClient(event_id, username)
    if not await client.connect():
        return
    try:
        await client.send_stomp_connect()
        await asyncio.sleep(1)
        await client.subscribe_to_topics()
        await asyncio.sleep(1)

        listen_task = asyncio.create_task(client.listen_for_messages())
        print(f"[{username}] Starting simulation")

        img_b64 = load_base64_image(IMAGE_PATH)
        for i in range(1, 3):
            await client.send_image(f"{username} Image {i}", img_b64)
            await asyncio.sleep(10)

        await asyncio.sleep(1)
        existing = get_gallery_images(event_id)

        if existing:
            for img in existing[:2]:
                await client.like_image(img["id"], True)
                await asyncio.sleep(5)

        await asyncio.sleep(2)
        if existing:
            await client.like_image(existing[0]["id"], False)
            await asyncio.sleep(1)

        own = [img for img in existing if img.get("uploadedBy") == username]
        if own:
            await client.delete_image(own[0]["id"])
            await asyncio.sleep(5)

        await asyncio.sleep(5)
        listen_task.cancel()
        try:
            await listen_task
        except asyncio.CancelledError:
            pass

    finally:
        await client.disconnect()

async def run_multi_user_test():
    event_id = 1
    print("Starting multi-user test")
    if not check_gallery_exists(event_id):
        if not test_create_gallery(event_id):
            print("Cannot create gallery; exiting")
            return
    users = [("Alice", 2), ("Bob", 2), ("Charlie", 2)]
    tasks = [asyncio.create_task(user_simulation(event_id, u, d)) for u, d in users]
    await asyncio.gather(*tasks)
    print("Test completed")

if __name__ == "__main__":
    asyncio.run(run_multi_user_test())
