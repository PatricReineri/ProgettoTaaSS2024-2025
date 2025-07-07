import asyncio
import json
import websockets
import requests
from datetime import datetime
import threading
import time

BASE_URL = "http://localhost:8081"
WS_URL = "ws://localhost:8081/chat"
AUTH = ("admin", "admin123")

class WebSocketSender:
    def __init__(self, event_id, username, user_magic_events_tag):
        self.event_id = event_id
        self.username = username
        self.user_magic_events_tag = user_magic_events_tag
        self.websocket = None
        self.running = False

    async def connect(self):
        """Connect to WebSocket endpoint for sending"""
        try:
            # Fixed WebSocket URL - removed /chat prefix since it's handled by Spring
            ws_url = f"ws://localhost:8081/chat/websocket"
            self.websocket = await websockets.connect(ws_url)
            print(f"📤 [SENDER] Connected to WebSocket: {ws_url}")
            return True
        except Exception as e:
            print(f"❌ [SENDER] Failed to connect to WebSocket: {e}")
            return False

    async def send_stomp_connect(self):
        """Send STOMP CONNECT frame"""
        connect_frame = "CONNECT\naccept-version:1.0,1.1,2.0\nhost:localhost\n\n\x00"
        await self.websocket.send(connect_frame)
        response = await self.websocket.recv()
        print(f"📤 [SENDER] STOMP Connect Response: {response}")

    async def send_message(self, content):
        """Send a message to the chat"""
        message_data = {
            "eventID": self.event_id,
            "content": content,
            "username": self.username,
            "dateTime": datetime.now().isoformat(),
            "userMagicEventsTag": self.user_magic_events_tag
        }

        # Fixed destination path to match @MessageMapping in ChatController
        message_frame = f"SEND\ndestination:/app/chat/sendMessage/{self.event_id}\ncontent-type:application/json\n\n{json.dumps(message_data)}\x00"
        await self.websocket.send(message_frame)
        print(f"📤 [SENDER] Sent message from {self.username}: {content}")

    async def delete_message(self, message_id, deleted_by):
        """Delete a message from the chat"""
        delete_data = {
            "eventID": self.event_id,
            "messageID": message_id,
            "deletedBy": deleted_by,
            "userMagicEventsTag": self.user_magic_events_tag
        }

        # Fixed destination path to match @MessageMapping in ChatController
        delete_frame = f"SEND\ndestination:/app/chat/deleteMessage/{self.event_id}\ncontent-type:application/json\n\n{json.dumps(delete_data)}\x00"
        await self.websocket.send(delete_frame)
        print(f"🗑️ [SENDER] Sent delete request for message ID {message_id} by {deleted_by}")

    async def disconnect(self):
        """Disconnect from WebSocket"""
        self.running = False
        if self.websocket:
            await self.websocket.close()
            print("📤 [SENDER] Disconnected from WebSocket")

class WebSocketReceiver:
    def __init__(self, event_id):
        self.event_id = event_id
        self.websocket = None
        self.running = False
        self.received_messages = []

    async def connect(self):
        """Connect to WebSocket endpoint for receiving"""
        try:
            # Fixed WebSocket URL - removed /chat prefix
            ws_url = f"ws://localhost:8081/chat/websocket"
            self.websocket = await websockets.connect(ws_url)
            print(f"📥 [RECEIVER] Connected to WebSocket: {ws_url}")
            return True
        except Exception as e:
            print(f"❌ [RECEIVER] Failed to connect to WebSocket: {e}")
            return False

    async def send_stomp_connect(self):
        """Send STOMP CONNECT frame"""
        connect_frame = "CONNECT\naccept-version:1.0,1.1,2.0\nhost:localhost\n\n\x00"
        await self.websocket.send(connect_frame)
        response = await self.websocket.recv()
        print(f"📥 [RECEIVER] STOMP Connect Response: {response}")

    async def subscribe_to_topics(self):
        """Subscribe to both chat and delete message topics"""
        # Subscribe to regular chat messages - matches @SendTo in ChatController
        subscribe_frame = f"SUBSCRIBE\nid:sub-receiver-{self.event_id}\ndestination:/topic/chat/{self.event_id}\n\n\x00"
        await self.websocket.send(subscribe_frame)
        print(f"📥 [RECEIVER] Subscribed to /topic/chat/{self.event_id}")

        # Subscribe to delete message notifications - matches @SendTo in ChatController
        delete_subscribe_frame = f"SUBSCRIBE\nid:sub-delete-{self.event_id}\ndestination:/topic/chat/deleteMessage/{self.event_id}\n\n\x00"
        await self.websocket.send(delete_subscribe_frame)
        print(f"📥 [RECEIVER] Subscribed to /topic/chat/deleteMessage/{self.event_id}")

    async def listen_for_messages(self):
        """Listen for incoming messages and delete notifications"""
        self.running = True
        try:
            while self.running:
                message = await self.websocket.recv()
                if message.startswith("MESSAGE"):
                    # Parse STOMP MESSAGE frame
                    lines = message.split('\n')
                    body_start = False
                    body = ""
                    destination = ""

                    for line in lines:
                        if line.startswith("destination:"):
                            destination = line.split(":", 1)[1]
                        elif body_start:
                            body += line
                        elif line == "":
                            body_start = True

                    if body and body != "\x00":
                        try:
                            msg_data = json.loads(body.replace('\x00', ''))
                            timestamp = datetime.now().strftime("%H:%M:%S")

                            if "/deleteMessage/" in destination:
                                print(f"🗑️ [RECEIVER][{timestamp}] MESSAGE DELETED - ID: {msg_data.get('messageID', 'Unknown')} by {msg_data.get('deletedBy', 'Unknown')}")
                            else:
                                message_content = f"📥 [RECEIVER][{timestamp}] {msg_data.get('username', 'Unknown')}: {msg_data.get('content', '')}"
                                print(message_content)
                                # Store message for potential deletion testing
                                self.received_messages.append({
                                    'content': msg_data.get('content', ''),
                                    'username': msg_data.get('username', ''),
                                    'timestamp': timestamp
                                })
                        except json.JSONDecodeError:
                            print(f"📥 [RECEIVER] Raw message: {body}")
                else:
                    print(f"📥 [RECEIVER] Server message: {message}")
        except websockets.exceptions.ConnectionClosed:
            print("📥 [RECEIVER] WebSocket connection closed")
        except Exception as e:
            print(f"❌ [RECEIVER] Error listening for messages: {e}")

    async def disconnect(self):
        """Disconnect from WebSocket"""
        self.running = False
        if self.websocket:
            await self.websocket.close()
            print("📥 [RECEIVER] Disconnected from WebSocket")

def test_create_board(event_id, user_magic_events_tag):
    """Create a board before testing chat"""
    url = f"{BASE_URL}/board/createBoard"
    payload = {
        "eventID": event_id,
        "title": "Test Board for Chat",
        "description": "This is a test board for WebSocket chat testing",
        "userMagicEventsTag": user_magic_events_tag
    }

    try:
        response = requests.post(url, json=payload, auth=AUTH, timeout=10)
        print(f"Create Board: {response.status_code}")
        if response.status_code == 200:
            result = response.json()
            print(f"✅ Board created successfully: {result}")
            return True
        else:
            print(f"❌ Failed to create board: {response.text}")
            return False
    except requests.exceptions.ConnectionError:
        print("❌ Cannot connect to board service. Make sure it's running on port 8081")
        return False
    except requests.exceptions.Timeout:
        print("❌ Request timeout. Board service might be slow to respond")
        return False

def check_board_exists(event_id):
    """Check if board exists"""
    url = f"{BASE_URL}/board/isBoardExists/{event_id}"
    try:
        response = requests.get(url, auth=AUTH, timeout=10)
        if response.status_code == 200:
            exists = response.json()
            print(f"Board exists for event {event_id}: {exists}")
            return exists
        return False
    except requests.exceptions.ConnectionError:
        print("❌ Cannot connect to board service")
        return False
    except requests.exceptions.Timeout:
        print("❌ Request timeout")
        return False

def get_board_messages(event_id, user_magic_events_tag, page_number=0):
    """Get board messages to find message IDs for deletion testing"""
    url = f"{BASE_URL}/board/getBoard/{event_id}/{page_number}"
    params = {"userMagicEventsTag": user_magic_events_tag}

    try:
        response = requests.get(url, params=params, auth=AUTH, timeout=10)
        if response.status_code == 200:
            board_data = response.json()
            messages = board_data.get('messages', [])
            print(f"📋 Found {len(messages)} messages in board")
            return messages
        elif response.status_code == 403:
            print(f"❌ Forbidden: User not authorized to access board")
            return []
        elif response.status_code == 404:
            print(f"❌ Board not found for event {event_id}")
            return []
        else:
            print(f"❌ Failed to get board messages: {response.status_code} - {response.text}")
            return []
    except requests.exceptions.ConnectionError:
        print("❌ Cannot connect to board service")
        return []
    except requests.exceptions.Timeout:
        print("❌ Request timeout")
        return []

async def receiver_thread(event_id):
    """Thread function for receiving messages"""
    receiver = WebSocketReceiver(event_id)

    if not await receiver.connect():
        print("❌ [RECEIVER] Cannot connect to WebSocket")
        return receiver

    try:
        await receiver.send_stomp_connect()
        await asyncio.sleep(1)
        await receiver.subscribe_to_topics()
        await asyncio.sleep(1)

        print("📥 [RECEIVER] Starting to listen for messages and delete notifications...")
        await receiver.listen_for_messages()

    except Exception as e:
        print(f"❌ [RECEIVER] Error: {e}")
    finally:
        await receiver.disconnect()

    return receiver

async def sender_thread(event_id, username, user_magic_events_tag):
    """Thread function for sending messages and testing deletion"""
    sender = WebSocketSender(event_id, username, user_magic_events_tag)

    if not await sender.connect():
        print("❌ [SENDER] Cannot connect to WebSocket")
        return

    try:
        await sender.send_stomp_connect()
        await asyncio.sleep(2)  # Increased wait time

        print("📤 [SENDER] Ready to send messages...")

        # Automated test messages
        test_messages = [
            "Hello everyone! 👋",
            "How is everyone doing?",
            "This dual-thread WebSocket chat is working great! 🎉",
            "Testing message delivery...",
            "This message will be deleted soon! 🗑️",
            "Last test message! 🎊"
        ]

        for i, content in enumerate(test_messages, 1):
            await asyncio.sleep(3)  # Wait between messages
            await sender.send_message(f"[Auto-{i}] {content}")

        print("📤 [SENDER] All test messages sent!")

        # Wait a bit for messages to be processed
        await asyncio.sleep(5)

        # Test message deletion
        print("🗑️ [SENDER] Testing message deletion...")

        # Get current messages to find message IDs
        messages = get_board_messages(event_id, user_magic_events_tag)
        if messages:
            # Try to delete the first message (most recent)
            first_message = messages[0]
            message_id = first_message.get('messageID')
            if message_id:
                print(f"🗑️ [SENDER] Attempting to delete message ID: {message_id}")
                await sender.delete_message(message_id, username)
                await asyncio.sleep(2)
            else:
                print("❌ [SENDER] No message ID found for deletion test")
        else:
            print("❌ [SENDER] No messages found for deletion test")

    except Exception as e:
        print(f"❌ [SENDER] Error: {e}")
    finally:
        await sender.disconnect()

async def interactive_sender_thread(event_id, username, user_magic_events_tag):
    """Interactive thread function for sending messages and deleting them"""
    sender = WebSocketSender(event_id, username, user_magic_events_tag)

    if not await sender.connect():
        print("❌ [SENDER] Cannot connect to WebSocket")
        return

    try:
        await sender.send_stomp_connect()
        await asyncio.sleep(2)  # Increased wait time

        print(f"📤 [SENDER] Connected as {username}. Type messages or commands:")
        print("  - Normal message: just type and press Enter")
        print("  - Delete message: type 'delete <messageID>'")
        print("  - List messages: type 'list'")
        print("  - Quit: type 'quit'")

        while True:
            try:
                user_input = await asyncio.to_thread(input, f"[{username}] > ")

                if user_input.lower() == 'quit':
                    break
                elif user_input.lower() == 'list':
                    messages = get_board_messages(event_id, user_magic_events_tag)
                    if messages:
                        print("📋 Current messages:")
                        for msg in messages[:5]:  # Show only latest 5 messages
                            print(f"  ID: {msg.get('messageID')} - {msg.get('username')}: {msg.get('content')}")
                    else:
                        print("📋 No messages found")
                elif user_input.lower().startswith('delete '):
                    try:
                        message_id = int(user_input.split(' ', 1)[1])
                        await sender.delete_message(message_id, username)
                    except (ValueError, IndexError):
                        print("❌ Invalid delete command. Use: delete <messageID>")
                else:
                    await sender.send_message(user_input)

            except KeyboardInterrupt:
                break

    except Exception as e:
        print(f"❌ [SENDER] Error: {e}")
    finally:
        await sender.disconnect()

async def run_dual_thread_test():
    """Run automated test with dual threads including deletion testing"""
    event_id = 46
    username = "TestUser"
    user_magic_events_tag = 2  # Default test user tag

    print("🚀 Starting Dual-Thread WebSocket Chat Test (with Deletion)")
    print("=" * 60)

    # Step 1: Check if board service is running and create board
    print("1. Checking board service...")
    if not check_board_exists(event_id):
        print("2. Creating board...")
        if not test_create_board(event_id, user_magic_events_tag):
            print("❌ Cannot proceed without a board. Exiting.")
            return

    print("3. Starting dual-thread WebSocket test...")

    # Start receiver thread
    receiver_task = asyncio.create_task(receiver_thread(event_id))
    await asyncio.sleep(3)  # Give receiver more time to connect and subscribe

    # Start sender thread (with deletion testing)
    sender_task = asyncio.create_task(sender_thread(event_id, username, user_magic_events_tag))

    # Wait for sender to finish
    await sender_task

    # Let receiver run for a bit more to capture any remaining messages
    print("4. Waiting for final messages...")
    await asyncio.sleep(5)

    # Cancel receiver
    receiver_task.cancel()

    try:
        await receiver_task
    except asyncio.CancelledError:
        pass

    print("✅ Dual-thread WebSocket chat test (with deletion) completed!")

async def run_interactive_dual_thread():
    """Run interactive chat with dual threads and deletion capability"""
    event_id = int(input("Enter event ID: "))
    username = input("Enter your username: ")
    user_magic_events_tag = int(input("Enter your Magic Events Tag (user ID): "))

    print("🚀 Starting Interactive Dual-Thread WebSocket Chat (with Deletion)")
    print("=" * 70)

    # Check board exists
    if not check_board_exists(event_id):
        print("Creating board...")
        if not test_create_board(event_id, user_magic_events_tag):
            print("❌ Cannot proceed without a board. Exiting.")
            return

    print("Starting dual-thread interactive chat...")

    # Start receiver thread
    receiver_task = asyncio.create_task(receiver_thread(event_id))
    await asyncio.sleep(3)  # Give receiver more time to connect

    # Start interactive sender thread
    sender_task = asyncio.create_task(interactive_sender_thread(event_id, username, user_magic_events_tag))

    # Wait for sender (user) to quit
    await sender_task

    # Cancel receiver
    receiver_task.cancel()

    try:
        await receiver_task
    except asyncio.CancelledError:
        pass

    print("✅ Interactive chat session ended!")

if __name__ == "__main__":
    print("Dual-Thread WebSocket Chat Tester (with Message Deletion)")
    print("=" * 60)
    print("1. Run automated dual-thread test (includes deletion testing)")
    print("2. Interactive dual-thread chat mode (with deletion commands)")

    choice = input("Choose option (1 or 2): ")

    if choice == "1":
        asyncio.run(run_dual_thread_test())
    elif choice == "2":
        asyncio.run(run_interactive_dual_thread())
    else:
        print("Invalid choice")
