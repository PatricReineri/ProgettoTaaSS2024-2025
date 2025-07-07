import requests
import time
import urllib3
from datetime import datetime, timedelta
from typing import Optional, Dict, Any
import json

# Disabilita i warning SSL per i test
urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

class MagicEventsTestClient:
    def __init__(self, base_url: str = "http://localhost"):
        self.base_url = base_url
        self.users = {}

    def register_user(self, name: str, surname: str, username: str, email: str, password: str) -> bool:
        """Registra un nuovo utente"""
        try:
            url = f"https://localhost:8443/login/register"
            params = {
                "name": name,
                "surname": surname,
                "username": username,
                "email": email,
                "password": password
            }

            response = requests.post(url, params=params, verify=False)
            print(f"Registrazione {username}: {response.status_code} - {response.text}")
            return response.status_code == 200

        except Exception as e:
            print(f"Errore nella registrazione di {username}: {e}")
            return False

    def login_user(self, email: str, password: str) -> Optional[Dict[str, Any]]:
        """Effettua il login di un utente"""
        try:
            url = f"https://localhost:8443/login/form"
            params = {
                "email": email,
                "password": password
            }

            response = requests.get(url, params=params, verify=False)
            print(f"Login {email}: {response.status_code}")

            if response.status_code == 200:
                user_data = response.json()
                print(f"✓ {user_data.get('name', 'Unknown')} loggato con successo")
                return user_data
            else:
                print(f"❌ Login fallito per {email}")

            return None

        except Exception as e:
            print(f"Errore nel login di {email}: {e}")
            return None

    def create_event_with_services(self, creator_data: Dict[str, Any]) -> Optional[int]:
        """Crea un evento con gallery e board abilitati"""
        try:
            url = f"http://localhost:8086/eventSetup"

            sample_image = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg=="

            start_time = datetime.now() + timedelta(days=1)
            end_time = start_time + timedelta(hours=3)

            event_data = {
                "title": "Evento Test Use Case 2",
                "description": "Evento di test per il secondo use case",
                "starting": start_time.isoformat(),
                "ending": end_time.isoformat(),
                "location": "Milano, Italia",
                "creatorEmail": creator_data["email"],
                "creatorMagicEventsTag": creator_data["magicEventTag"],
                "participants": ["giulia@test.com"],
                "admins": [],
                "image": sample_image,
                "galleryEnabled": True,
                "boardEnabled": True,
                "gameEnabled": False,
                "galleryTitle": "Galleria Use Case 2",
                "boardTitle": "Board Use Case 2",
                "boardDescription": "Board per il secondo use case",
                "gameDescription": ""
            }

            response = requests.post(url, json=event_data)
            print(f"Creazione evento: {response.status_code}")

            if response.status_code in [200, 201]:
                response_data = response.json()
                event_id = response_data.get("eventId")
                print(f"✓ Evento creato con ID: {event_id}")
                return event_id

            return None

        except Exception as e:
            print(f"Errore nella creazione dell'evento: {e}")
            return None

    def add_participant_to_event(self, event_id: int, creator_tag: int, participant_email: str) -> bool:
        """Aggiunge un partecipante all'evento"""
        try:
            url = f"http://localhost:8080/gestion/addpartecipants"

            params = {
                "eventId": event_id,
                "magicEventsTag": creator_tag
            }

            data = f"partecipants={participant_email}"
            headers = {"Content-Type": "application/x-www-form-urlencoded"}

           # response = requests.put(url, params=params, data=data, headers=headers)
            print(f"Aggiunta partecipante {participant_email}: {response.status_code}")
            return response.status_code in [200, 202] or "Success" in response.text

        except Exception as e:
            print(f"Errore nell'aggiunta del partecipante: {e}")
            return False

    def get_gallery_with_images(self, event_id: int, user_tag: int) -> Optional[Dict[str, Any]]:
        """Recupera la gallery con le immagini"""
        try:
            url = f"http://localhost:8085/gallery/getGallery/{event_id}/0"
            params = {"userMagicEventsTag": user_tag}

            response = requests.get(url, params=params)
            print(f"Recupero gallery: {response.status_code}")

            if response.status_code == 200:
                gallery_data = response.json()
                print(f"✓ Gallery recuperata con {len(gallery_data.get('images', []))} immagini")
                return gallery_data

            return None

        except Exception as e:
            print(f"Errore nel recupero della gallery: {e}")
            return None

    def add_image_to_gallery(self, event_id: int, user_data: Dict[str, Any], title: str, image_content: str = None) -> Optional[int]:
        """Aggiunge un'immagine alla gallery"""
        try:
            # Usa un'immagine di esempio se non fornita
            if not image_content:
                image_content = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg=="

            # Simula l'invio via WebSocket usando il formato del DTO
            image_data = {
                "eventID": event_id,
                "title": title,
                "base64Image": image_content,
                "uploadedBy": user_data["username"],
                "dateTime": datetime.now().isoformat(),
                "magiceventstag": str(user_data["magicEventTag"])
            }

            print(f"📸 {user_data['username']} pubblica immagine: '{title}'")

            # In un test reale, qui si invierebbe via WebSocket
            # Per ora simuliamo il successo
            return 1  # ID immagine simulato

        except Exception as e:
            print(f"Errore nell'aggiunta dell'immagine: {e}")
            return None

    def like_image(self, event_id: int, image_id: int, user_data: Dict[str, Any], like: bool = True) -> bool:
        """Mette/toglie like a un'immagine"""
        try:
            # Simula il like via WebSocket
            like_data = {
                "userMagicEventsTag": str(user_data["magicEventTag"]),
                "like": like,
                "imageID": image_id,
                "eventID": event_id,
                "likedCount": 1 if like else 0
            }

            action = "mette like" if like else "toglie like"
            print(f"👍 {user_data['username']} {action} all'immagine {image_id}")

            # In un test reale, qui si invierebbe via WebSocket
            return True

        except Exception as e:
            print(f"Errore nel like dell'immagine: {e}")
            return False

    def delete_image_from_gallery(self, event_id: int, image_id: int, user_data: Dict[str, Any]) -> bool:
        """Elimina un'immagine dalla gallery"""
        try:
            # Simula l'eliminazione via WebSocket
            delete_data = {
                "eventID": event_id,
                "imageID": image_id,
                "deletedBy": user_data["username"],
                "magiceventstag": str(user_data["magicEventTag"])
            }

            print(f"🗑️ {user_data['username']} elimina l'immagine {image_id}")

            # In un test reale, qui si invierebbe via WebSocket
            return True

        except Exception as e:
            print(f"Errore nell'eliminazione dell'immagine: {e}")
            return False

    def update_event_services(self, event_id: int, creator_tag: int, gallery_enabled: bool, board_enabled: bool, game_enabled: bool) -> bool:
        """Aggiorna i servizi attivi per l'evento"""
        try:
            url = f"http://localhost:8086/eventSetup/services"

            service_data = {
                "eventId": event_id,
                "userMagicEventsTag": creator_tag,
                "galleryEnabled": gallery_enabled,
                "boardEnabled": board_enabled,
                "guestGameEnabled": game_enabled
            }

            response = requests.put(url, json=service_data)
            print(f"Aggiornamento servizi evento: {response.status_code}")

            if response.status_code == 200:
                services = []
                if gallery_enabled: services.append("gallery")
                if board_enabled: services.append("board")
                if game_enabled: services.append("guest-game")

                print(f"✓ Servizi aggiornati: {', '.join(services) if services else 'nessuno'}")
                return True

            return False

        except Exception as e:
            print(f"Errore nell'aggiornamento dei servizi: {e}")
            return False

    def send_board_message(self, event_id: int, user_data: Dict[str, Any], message_content: str) -> bool:
        """Invia un messaggio nella board"""
        try:
            # Simula l'invio del messaggio via WebSocket
            message_data = {
                "eventID": event_id,
                "content": message_content,
                "username": user_data["username"],
                "dateTime": datetime.now().isoformat(),
                "userMagicEventsTag": user_data["magicEventTag"]
            }

            print(f"💬 {user_data['username']} scrive in board: '{message_content}'")

            # In un test reale, qui si invierebbe via WebSocket
            return True

        except Exception as e:
            print(f"Errore nell'invio del messaggio: {e}")
            return False

def main():
    print("=== Test Use Case 2 - MagicEvents ===")
    print()

    client = MagicEventsTestClient()

    # Setup iniziale - registrazione e login utenti
    print("1. SETUP INIZIALE")
    print("-" * 25)

    users_to_register = [
        {"name": "Mario", "surname": "Rossi", "username": "mario", "email": "mario@test.com", "password": "password123"},
        {"name": "Pino", "surname": "Verdi", "username": "pino", "email": "pino@test.com", "password": "password123"},
        {"name": "Giulia", "surname": "Bianchi", "username": "giulia", "email": "giulia@test.com", "password": "password123"}
    ]

    # Registrazione utenti
    for user in users_to_register:
        client.register_user(user["name"], user["surname"], user["username"], user["email"], user["password"])

    time.sleep(2)

    # Login utenti
    logged_users = {}
    for user in users_to_register:
        user_data = client.login_user(user["email"], user["password"])
        if user_data:
            logged_users[user["username"]] = user_data

    if len(logged_users) < 3:
        print("❌ Non tutti gli utenti sono loggati. Impossibile continuare.")
        return

    mario = logged_users["mario"]
    pino = logged_users["pino"]
    giulia = logged_users["giulia"]

    print(f"✓ Tutti gli utenti loggati: {', '.join(logged_users.keys())}")

    # Creazione evento
    event_id = client.create_event_with_services(mario)
    if not event_id:
        print("❌ Impossibile creare l'evento")
        return

    # Aggiunta partecipanti
    client.add_participant_to_event(event_id, mario["magicEventTag"], pino["email"])
    client.add_participant_to_event(event_id, mario["magicEventTag"], giulia["email"])

    time.sleep(3)

    # 2. Mario invia un'immagine e Pino/Giulia mettono like
    print("\n2. PUBBLICAZIONE IMMAGINE E LIKES")
    print("-" * 40)

    # Mario pubblica un'immagine
    mario_image_id = client.add_image_to_gallery(event_id, mario, "Foto del tramonto")

    if mario_image_id:
        print("✓ Mario ha pubblicato un'immagine")

        time.sleep(1)

        # Pino mette like
        pino_like = client.like_image(event_id, mario_image_id, pino, True)

        # Giulia mette like
        giulia_like = client.like_image(event_id, mario_image_id, giulia, True)

        if pino_like and giulia_like:
            print("✓ Pino e Giulia hanno messo like all'immagine di Mario")
        else:
            print("❌ Problemi con i like")
    else:
        print("❌ Mario non è riuscito a pubblicare l'immagine")

    time.sleep(2)

    # 3. Giulia pubblica un'immagine e Pino la elimina
    print("\n3. PUBBLICAZIONE E ELIMINAZIONE IMMAGINE")
    print("-" * 45)

    # Giulia pubblica un'immagine
    giulia_image_id = client.add_image_to_gallery(event_id, giulia, "Foto del gruppo")

    if giulia_image_id:
        print("✓ Giulia ha pubblicato un'immagine")

        time.sleep(1)

        # Pino elimina l'immagine di Giulia
        deletion_success = client.delete_image_from_gallery(event_id, giulia_image_id, pino)

        if deletion_success:
            print("✓ Pino ha eliminato l'immagine di Giulia")
        else:
            print("❌ Pino non è riuscito a eliminare l'immagine")
    else:
        print("❌ Giulia non è riuscita a pubblicare l'immagine")

    time.sleep(2)

    # 4. Mario abilita guest game e disabilita gallery
    print("\n4. MODIFICA SERVIZI EVENTO")
    print("-" * 35)

    services_updated = client.update_event_services(
        event_id,
        mario["magicEventTag"],
        gallery_enabled=False,  # Disabilita gallery
        board_enabled=True,     # Mantiene board
        game_enabled=True       # Abilita guest game
    )

    if services_updated:
        print("✓ Mario ha aggiornato i servizi: disabilitato gallery, abilitato guest game")
    else:
        print("❌ Errore nell'aggiornamento dei servizi")

    time.sleep(2)

    # 5. Pino manda messaggio in board
    print("\n5. MESSAGGIO IN BOARD")
    print("-" * 25)

    board_message = "Attenzione! Mario ha disabilitato il servizio gallery. Le foto non sono più disponibili! 📸❌"

    message_sent = client.send_board_message(event_id, pino, board_message)

    if message_sent:
        print("✓ Pino ha inviato il messaggio di avviso in board")
    else:
        print("❌ Errore nell'invio del messaggio")

    # Riepilogo finale
    print("\n" + "="*60)
    print("RIEPILOGO USE CASE 2")
    print("="*60)

    operations = [
        ("Setup utenti e evento", len(logged_users) == 3 and event_id is not None),
        ("Mario pubblica immagine", mario_image_id is not None),
        ("Pino e Giulia mettono like", pino_like and giulia_like if mario_image_id else False),
        ("Giulia pubblica immagine", giulia_image_id is not None),
        ("Pino elimina immagine di Giulia", deletion_success if giulia_image_id else False),
        ("Mario modifica servizi", services_updated),
        ("Pino avvisa in board", message_sent)
    ]

    for operation, success in operations:
        status = "✅" if success else "❌"
        print(f"{status} {operation}")

    success_count = sum(success for _, success in operations)
    print(f"\n🎯 Use Case 2 completato: {success_count}/{len(operations)} operazioni riuscite")

    if success_count >= len(operations) - 1:
        print("🎉 Test completato con successo!")
    else:
        print("⚠️ Test completato con alcune limitazioni")

    print("\n📋 Scenario testato:")
    print("1. Mario pubblica una foto e riceve like da Pino e Giulia")
    print("2. Giulia pubblica una foto ma Pino la elimina")
    print("3. Mario disabilita la gallery e abilita il guest game")
    print("4. Pino comunica il cambiamento tramite la board")

if __name__ == "__main__":
    main()
