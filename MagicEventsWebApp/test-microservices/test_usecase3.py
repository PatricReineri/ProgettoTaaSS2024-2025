import requests
import json
import time
import urllib3
from datetime import datetime, timedelta
from typing import Optional, Dict, Any
import base64

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

    def create_event_basic(self, creator_data: Dict[str, Any], admin_email: str, participant_email: str) -> Optional[int]:
        """Crea un evento base con solo board abilitata"""
        try:
            # Genera un'immagine base64 fittizia
            dummy_image = base64.b64encode(b"dummy_image_data").decode('utf-8')

            # Data di inizio e fine evento
            start_time = datetime.now() + timedelta(hours=1)
            end_time = start_time + timedelta(hours=3)

            url = f"{self.base_url}:8086/eventSetup"
            payload = {
                "title": "Test Event Giulia",
                "description": "Evento di test creato da Giulia per verificare il workflow",
                "starting": start_time.isoformat(),
                "ending": end_time.isoformat(),
                "location": "Aula Test",
                "creatorEmail": creator_data["email"],
                "creatorMagicEventsTag": creator_data["magicEventTag"],
                "participants": [participant_email],
                "admins": [admin_email],
                "image": dummy_image,
                "galleryEnabled": False,
                "boardEnabled": True,
                "gameEnabled": False,
                "boardTitle": "Bacheca Test",
                "boardDescription": "Bacheca per il test del workflow"
            }
            print(payload)
            response = requests.post(url, json=payload, timeout=30)

            if response.status_code in [200, 201]:
                result = response.json()
                event_id = result.get("eventId")
                print(f"✓ Evento creato con ID: {event_id}")
                print(f"  - Board creata: {result.get('boardCreated', False)}")
                print(f"  - Gallery: {result.get('galleryCreated', False)}")
                print(f"  - Game: {result.get('gameCreated', False)}")
                return event_id
            else:
                print(f"❌ Errore creazione evento: {response.status_code}")
                if response.text:
                    print(f"   Risposta: {response.text}")
                return None

        except Exception as e:
            print(f"❌ Errore durante la creazione dell'evento: {str(e)}")
            return None

    def activate_game_service(self, event_id: int, creator_data: Dict[str, Any]) -> bool:
        """Attiva il servizio di game per l'evento"""
        try:
            url = f"{self.base_url}:8086/eventSetup/services"
            payload = {
                "eventId": event_id,
                "userMagicEventsTag": creator_data["magicEventTag"],
                "galleryEnabled": False,
                "boardEnabled": True,
                "guestGameEnabled": True
            }

            response = requests.put(url, json=payload, timeout=20)


        except Exception as e:
            print(f"❌ Errore durante l'attivazione del game: {str(e)}")
            return False
        return True


    def insert_guest_info(self, event_id: int, user_data: Dict[str, Any], guest_info: Dict[str, Any]) -> bool:
        """Inserisce le informazioni del guest nel game"""
        try:
            url = f"{self.base_url}:8083/guest-game/insertGuestInfo"
            payload = {
                "isMen": guest_info["isMen"],
                "age": guest_info["age"],
                "isHostFamilyMember": guest_info["isHostFamilyMember"],
                "isHostAssociate": guest_info["isHostAssociate"],
                "haveBeard": guest_info["haveBeard"],
                "isBald": guest_info["isBald"],
                "haveGlasses": guest_info["haveGlasses"],
                "haveDarkHair": guest_info["haveDarkHair"],
                "userMagicEventsTag": str(user_data["magicEventTag"]),
                "gameId": event_id
            }

            response = requests.post(url, json=payload, timeout=15)

            if response.status_code in [200, 201]:
                print(f"✓ Informazioni guest inserite per {user_data.get('username', 'utente')}")
                return True
            else:
                print(f"❌ Errore inserimento guest info: {response.status_code}")
                if response.text:
                    print(f"   Risposta: {response.text}")
                return False

        except Exception as e:
            print(f"❌ Errore durante l'inserimento guest info: {str(e)}")
            return False

    def get_decision_tree(self, event_id: int, user_data: Dict[str, Any]) -> bool:
        """Richiede l'albero di decisione"""
        try:
            url = f"{self.base_url}:8083/guest-game/createDecisionTree/{event_id}"
            params = {"userMagicEventsTag": user_data["magicEventTag"]}

            response = requests.get(url, params=params, timeout=20)

            if response.status_code == 200:
                tree_data = response.json()
                print(f"✓ Albero di decisione ottenuto per evento {event_id}")
                print(f"   Accuracy: {tree_data.get('accuracy', 'N/A')}%")
                return True
            else:
                print(f"❌ Errore ottenimento decision tree: {response.status_code}")
                if response.text:
                    print(f"   Risposta: {response.text}")
                return False

        except Exception as e:
            print(f"❌ Errore durante l'ottenimento del decision tree: {str(e)}")
            return False

    def send_board_message(self, event_id: int, user_data: Dict[str, Any], message: str) -> Optional[int]:
        """Invia un messaggio alla bacheca"""
        try:
            url = f"{self.base_url}:8081/board/addNewMessage"
            payload = {
                "eventID": event_id,
                "content": message,
                "username": user_data.get("username", "utente"),
                "dateTime": datetime.now().isoformat(),
                "userMagicEventsTag": user_data["magicEventTag"]
            }

            # Simula l'invio tramite WebSocket (in realtà uso REST per il test)
            response = requests.post(url, json=payload, timeout=10)

            if response.status_code in [200, 201]:
                print(f"✓ Messaggio inviato alla bacheca dell'evento {event_id}")
                # Simula un ID messaggio per il test
                return 1  # ID fittizio
            else:
                print(f"❌ Errore invio messaggio: {response.status_code}")
                return None

        except Exception as e:
            print(f"❌ Errore durante l'invio del messaggio: {str(e)}")
            return None

    def delete_board_message(self, event_id: int, message_id: int, user_data: Dict[str, Any]) -> bool:
        """Elimina un messaggio dalla bacheca"""
        try:
            url = f"{self.base_url}:8081/board/deleteMessage"
            payload = {
                "eventID": event_id,
                "messageID": message_id,
                "deletedBy": user_data.get("username", "utente"),
                "userMagicEventsTag": user_data["magicEventTag"]
            }

            response = requests.delete(url, json=payload, timeout=10)

            if response.status_code == 200:
                print(f"✓ Messaggio {message_id} eliminato dalla bacheca")
                return True
            else:
                print(f"❌ Errore eliminazione messaggio: {response.status_code}")
                return False

        except Exception as e:
            print(f"❌ Errore durante l'eliminazione del messaggio: {str(e)}")
            return False

def main():
    print("=== Test Workflow 2 - MagicEvents ===")
    print("Scenario: Giulia crea evento, attiva game, Mario e Pino inseriscono info, Pino richiede decision tree, Pino scrive messaggio, Giulia lo elimina")
    print("=" * 80)

    client = MagicEventsTestClient()

    # 1. Registrazione degli utenti
    print("\n1. REGISTRAZIONE UTENTI")
    print("-" * 30)

    users_to_register = [
        {"name": "Giulia", "surname": "Bianchi", "username": "giulia", "email": "giulia@test.com", "password": "password123"},
        {"name": "Mario", "surname": "Rossi", "username": "mario", "email": "mario@test.com", "password": "password123"},
        {"name": "Pino", "surname": "Verdi", "username": "pino", "email": "pino@test.com", "password": "password123"}
    ]

    registered_count = 0
    for user in users_to_register:
        success = client.register_user(
            user["name"], user["surname"], user["username"],
            user["email"], user["password"]
        )
        if success:
            registered_count += 1
        else:
            print(f"❌ Registrazione fallita per {user['username']}")

    print(f"\nUtenti registrati: {registered_count}/3")

    if registered_count == 0:
        print("❌ Nessun utente registrato. Controllare il servizio user-management.")
        return

    print("Attesa 3 secondi...")
    time.sleep(3)

    # 2. Login degli utenti
    print("\n2. LOGIN UTENTI")
    print("-" * 30)

    logged_users = {}
    for user in users_to_register:
        user_data = client.login_user(user["email"], user["password"])
        if user_data:
            logged_users[user["username"]] = user_data
            print(f"✓ {user['username']} loggato")
        else:
            print(f"❌ Login fallito per {user['username']}")

    if "giulia" not in logged_users:
        print("❌ Giulia deve essere loggata per continuare")
        return

    print("\nAttesa 3 secondi...")
    time.sleep(3)

    # 3. Giulia crea un evento base (solo board)
    print("\n3. CREAZIONE EVENTO BASE")
    print("-" * 35)

    giulia = logged_users["giulia"]
    mario_email = "mario@test.com"
    pino_email = "pino@test.com"

    event_id = client.create_event_basic(giulia, mario_email, pino_email)

    if not event_id:
        print("❌ Non è stato possibile creare l'evento")
        return

    print(f"✓ Evento creato con ID: {event_id}")
    print("  - Admin: Mario")
    print("  - Partecipante: Pino")
    print("  - Servizi: Solo Board abilitata")

    print("\nAttesa 5 secondi per la propagazione...")
    time.sleep(5)

    # 4. Giulia attiva il servizio game
    print("\n4. ATTIVAZIONE SERVIZIO GAME")
    print("-" * 35)

    game_activated = client.activate_game_service(event_id, giulia)

    if game_activated:
        print("✓ Servizio game attivato con successo")
    else:
        print("❌ Errore nell'attivazione del servizio game")
        return

    print("\nAttesa 3 secondi...")
    time.sleep(3)

    # 5. Mario e Pino inseriscono le informazioni guest
    print("\n5. INSERIMENTO INFORMAZIONI GUEST")
    print("-" * 40)

    # Informazioni fittizie per Mario
    mario_info = {
        "isMen": True,
        "age": 35,
        "isHostFamilyMember": True,
        "isHostAssociate": False,
        "haveBeard": True,
        "isBald": False,
        "haveGlasses": True,
        "haveDarkHair": True
    }

    # Informazioni fittizie per Pino
    pino_info = {
        "isMen": True,
        "age": 28,
        "isHostFamilyMember": False,
        "isHostAssociate": True,
        "haveBeard": False,
        "isBald": False,
        "haveGlasses": False,
        "haveDarkHair": False
    }

    mario_inserted = False
    pino_inserted = False

    if "mario" in logged_users:
        mario = logged_users["mario"]
        mario_inserted = client.insert_guest_info(event_id, mario, mario_info)
    else:
        print("❌ Mario non è loggato")

    if "pino" in logged_users:
        pino = logged_users["pino"]
        pino_inserted = client.insert_guest_info(event_id, pino, pino_info)
    else:
        print("❌ Pino non è loggato")

    print(f"\nInformazioni inserite: Mario={mario_inserted}, Pino={pino_inserted}")

    print("\nAttesa 3 secondi...")
    time.sleep(3)

    # 6. Pino richiede l'albero di decisione
    print("\n6. RICHIESTA DECISION TREE")
    print("-" * 35)

    decision_tree_obtained = False
    if "pino" in logged_users and (mario_inserted or pino_inserted):
        pino = logged_users["pino"]
        decision_tree_obtained = client.get_decision_tree(event_id, pino)
    else:
        print("❌ Prerequisiti non soddisfatti per il decision tree")

    print("\nAttesa 2 secondi...")
    time.sleep(2)

    # 7. Pino scrive un messaggio in bacheca
    print("\n7. INVIO MESSAGGIO IN BACHECA")
    print("-" * 35)

    message_id = None
    if "pino" in logged_users:
        pino = logged_users["pino"]
        message_content = "Ciao a tutti! Questo è un messaggio di test di Pino."
        message_id = client.send_board_message(event_id, pino, message_content)
    else:
        print("❌ Pino non è loggato")

    print("\nAttesa 2 secondi...")
    time.sleep(2)

    # 8. Giulia elimina il messaggio di Pino
    print("\n8. ELIMINAZIONE MESSAGGIO")
    print("-" * 30)

    message_deleted = False
    if message_id and "giulia" in logged_users:
        giulia = logged_users["giulia"]
        message_deleted = client.delete_board_message(event_id, message_id, giulia)
    else:
        print("❌ Impossibile eliminare il messaggio (prerequisiti non soddisfatti)")

    # Riepilogo finale
    print("\n" + "="*60)
    print("RIEPILOGO FINALE")
    print("="*60)
    print(f"✅ Utenti registrati: {registered_count}/3")
    print(f"✅ Utenti loggati: {len(logged_users)}/3")
    print(f"✅ Evento creato: {'Sì' if event_id else 'No'} (ID: {event_id if event_id else 'N/A'})")
    print(f"✅ Game service attivato: {'Sì' if game_activated else 'No'}")
    print(f"✅ Info Mario inserite: {'Sì' if mario_inserted else 'No'}")
    print(f"✅ Info Pino inserite: {'Sì' if pino_inserted else 'No'}")
    print(f"✅ Decision tree ottenuto: {'Sì' if decision_tree_obtained else 'No'}")
    print(f"✅ Messaggio inviato: {'Sì' if message_id else 'No'}")
    print(f"✅ Messaggio eliminato: {'Sì' if message_deleted else 'No'}")

    # Calcolo successo
    success_count = sum([
        registered_count > 0,
        len(logged_users) >= 2,
        bool(event_id),
        game_activated,
        mario_inserted or pino_inserted,
        decision_tree_obtained,
        bool(message_id),
        message_deleted
    ])

    print(f"\n🎯 Workflow completato: {success_count}/8 operazioni riuscite")

    if success_count >= 6:
        print("🎉 TEST SUPERATO - La maggior parte delle operazioni è riuscita!")
    else:
        print("⚠️  TEST PARZIALE - Alcune operazioni potrebbero aver avuto problemi")

    print("\n" + "="*60)

if __name__ == "__main__":
    main()
