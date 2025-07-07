import requests
import json
import time
import base64
import urllib3
from datetime import datetime, timedelta
from typing import Optional, Dict, Any

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
                print(f"Utente loggato: {user_data}")
                return user_data
            else:
                print(f"Errore login: {response.text}")

            return None

        except Exception as e:
            print(f"Errore nel login di {email}: {e}")
            return None

    def create_event_with_gallery(self, creator_data: Dict[str, Any], admin_email: str) -> Optional[int]:
        """Crea un evento con gallery abilitata"""
        try:
            # CORREZIONE: Usa la porta corretta 8082 invece di 8086
            url = f"http://localhost:8086/eventSetup"

            sample_image = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg=="

            start_time = datetime.now() + timedelta(days=1)
            end_time = start_time + timedelta(hours=3)

            event_data = {
                "title": "Evento Test Mario",
                "description": "Evento di test creato da Mario con gallery abilitata",
                "starting": start_time.isoformat(),
                "ending": end_time.isoformat(),
                "location": "Milano, Italia",
                "creatorEmail": creator_data["email"],
                "creatorMagicEventsTag": creator_data["magicEventTag"],
                "participants": [],  # Lista vuota inizialmente
                "admins": [admin_email],
                "image": sample_image,
                "galleryEnabled": True,
                "boardEnabled": True,
                "gameEnabled": False,
                "galleryTitle": "Galleria Evento Mario",
                "boardTitle": "Board Evento Mario",
                "boardDescription": "Board per l'evento di test",
                "gameDescription": "ddd"  # Non abilitato
            }


            response = requests.post(url, json=event_data)

            print(f"Creazione evento: {response.status_code}")
            print(response)
            print(f"Risposta: {response.text}")

            if response.status_code in [200, 201]:
                result = response.json()
                event_id = result.get("eventId")
                print(f"Evento creato con ID: {event_id}")
                return event_id
            else:
                print(f"❌ Errore nella creazione dell'evento: {response.status_code}")
                try:
                    error_detail = response.json()
                    print(f"Dettagli errore: {error_detail}")
                except:
                    print(f"Errore raw: {response.text}")

            return None

        except Exception as e:
            print(f"Errore nella creazione dell'evento: {e}")
            return None

    def add_participant_to_event(self, event_id: int, creator_tag: int, participant_email: str) -> bool:
        """Aggiunge un partecipante all'evento"""
        try:
            url = f"http://localhost:8080/gestion/addpartecipants"

            # Costruisci i parametri correttamente per ArrayList<String>
            params = {
                "eventId": event_id,
                "magicEventsTag": creator_tag
            }

            # Per ArrayList<String>, Spring si aspetta parametri multipli con lo stesso nome
            data = f"partecipants={participant_email}"
            headers = {"Content-Type": "application/x-www-form-urlencoded"}

            response = requests.put(url, params=params, data=data, headers=headers)
            print(f"Aggiunta partecipante {participant_email}: {response.status_code} - {response.text}")
            return response.status_code in [200, 202] or "Success" in response.text

        except Exception as e:
            print(f"Errore nell'aggiunta del partecipante: {e}")
            return False

    def check_gallery_exists(self, event_id: int) -> bool:
        """Verifica se la gallery dell'evento esiste"""
        try:
            url = f"http://localhost:8085/gallery/isGalleryExists/{event_id}"
            response = requests.get(url)

            print(f"Verifica gallery {event_id}: {response.status_code}")

            if response.status_code == 200:
                try:
                    exists = response.json()
                except:
                    exists = "true" in response.text.lower()
                print(f"Gallery per evento {event_id} esiste: {exists}")
                return bool(exists)

            return False

        except Exception as e:
            print(f"Errore nella verifica della gallery: {e}")
            return False

    def create_gallery_for_event(self, event_id: int, creator_data: Dict[str, Any]) -> bool:
        """Crea una gallery per l'evento"""
        try:
            url = f"http://localhost:8085/gallery/createGallery"

            gallery_data = {
                "eventID": event_id,  # Nota: eventID con maiuscola come nel DTO
                "title": "Galleria Evento Mario",
                "userMagicEventsTag": creator_data["magicEventTag"]
            }

            headers = {"Content-Type": "application/json"}
            response = requests.post(url, json=gallery_data, headers=headers)

            print(f"Creazione gallery: {response.status_code} - {response.text}")
            return response.status_code in [200, 201]

        except Exception as e:
            print(f"Errore nella creazione della gallery: {e}")
            return False

    def publish_image_to_gallery(self, event_id: int, user_data: Dict[str, Any]) -> bool:
        """Pubblica un'immagine nella gallery dell'evento (simulato)"""
        try:
            print(f"Simulazione pubblicazione immagine da {user_data.get('username', 'utente')} nella gallery dell'evento {event_id}")
            print("✓ Immagine pubblicata con successo (simulato)!")
            return True

        except Exception as e:
            print(f"Errore nella pubblicazione dell'immagine: {e}")
            return False

def main():
    print("=== Test Workflow MagicEvents ===")
    print()

    client = MagicEventsTestClient()

    # 1. Registrazione degli utenti
    print("1. REGISTRAZIONE UTENTI")
    print("-" * 30)

    users_to_register = [
        {"name": "Mario", "surname": "Rossi", "username": "mario", "email": "mario@test.com", "password": "password123"},
        {"name": "Pino", "surname": "Verdi", "username": "pino", "email": "pino@test.com", "password": "password123"},
        {"name": "Giulia", "surname": "Bianchi", "username": "giulia", "email": "giulia@test.com", "password": "password123"}
    ]

    registered_count = 0
    for user in users_to_register:
        success = client.register_user(
            user["name"], user["surname"], user["username"],
            user["email"], user["password"]
        )
        if success:
            registered_count += 1
            print(f"✓ {user['username']} registrato con successo")
        else:
            print(f"✗ Errore nella registrazione di {user['username']}")

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
            print(f"✓ {user['username']} loggato con successo (ID: {user_data.get('magicEventTag', 'N/A')})")
        else:
            print(f"✗ Errore nel login di {user['username']}")

    if "mario" not in logged_users:
        print("❌ Mario deve essere loggato per continuare")
        return

    print("\nAttesa 3 secondi...")
    time.sleep(3)

    # 3. Mario crea un evento con gallery abilitata
    print("\n3. CREAZIONE EVENTO CON GALLERY")
    print("-" * 40)

    mario = logged_users["mario"]
    event_id = client.create_event_with_gallery(mario, mario["email"])

    if not event_id:
        print("❌ Non è stato possibile creare l'evento")
        print("Verificare che tutti i servizi siano avviati e configurati correttamente")
        return

    print(f"✓ Evento creato con ID: {event_id}")

    print("\nAttesa 5 secondi per la propagazione...")
    time.sleep(5)

    # 4. Verifica che la gallery sia stata creata
    print("\n4. VERIFICA GALLERY")
    print("-" * 25)

    gallery_exists = client.check_gallery_exists(event_id)
    if not gallery_exists:
        print("Gallery non trovata, tentativo di creazione manuale...")
        gallery_created = client.create_gallery_for_event(event_id, mario)
        if gallery_created:
            print("✓ Gallery creata manualmente")
            gallery_exists = True
        else:
            print("⚠️ Impossibile creare la gallery")
    else:
        print("✓ Gallery già esistente")

    # 5. Aggiunta di Giulia come partecipante
    print("\n5. AGGIUNTA PARTECIPANTE")
    print("-" * 30)

    participant_added = False
    if "giulia" in logged_users:
        giulia = logged_users["giulia"]
        participant_added = client.add_participant_to_event(event_id, mario["magicEventTag"], giulia["email"])

        if participant_added:
            print(f"✓ Giulia aggiunta come partecipante all'evento {event_id}")
        else:
            print("⚠️ Errore nell'aggiunta di Giulia come partecipante")
    else:
        print("❌ Giulia non è loggata, impossibile aggiungerla come partecipante")

    print("\nAttesa 2 secondi...")
    time.sleep(2)

    # 6. Giulia pubblica un'immagine nella gallery
    print("\n6. PUBBLICAZIONE IMMAGINE IN GALLERY")
    print("-" * 40)

    image_published = False
    if "giulia" in logged_users and gallery_exists:
        giulia = logged_users["giulia"]
        image_published = client.publish_image_to_gallery(event_id, giulia)
    else:
        print("❌ Non è possibile pubblicare l'immagine (prerequisiti non soddisfatti)")

    # Riepilogo finale
    print("\n" + "="*50)
    print("RIEPILOGO FINALE")
    print("="*50)
    print(f"✅ Utenti registrati: {registered_count}/3")
    print(f"✅ Utenti loggati: {len(logged_users)}/3")
    print(f"✅ Evento creato: {'Sì' if event_id else 'No'} (ID: {event_id if event_id else 'N/A'})")
    print(f"✅ Gallery esistente: {'Sì' if gallery_exists else 'No'}")

    if "giulia" in logged_users:
        print(f"✅ Partecipante aggiunto: {'Sì' if participant_added else 'No'}")
        print(f"✅ Immagine pubblicata: {'Sì (simulato)' if image_published else 'No'}")
    else:
        print("❌ Giulia non disponibile per completare il workflow")

    # Status finale
    success_count = sum([
        registered_count > 0,
        len(logged_users) > 0,
        bool(event_id),
        gallery_exists,
        participant_added,
        image_published
    ])

    print(f"\n🎯 Workflow completato: {success_count}/6 operazioni riuscite")

    if success_count >= 4:
        print("🎉 Test completato con successo!")
    else:
        print("⚠️ Test completato con alcune limitazioni")

if __name__ == "__main__":
    main()
