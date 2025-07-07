import requests
import json
import time
import base64
from datetime import datetime, timedelta
from typing import Optional, Dict, Any, List
import urllib3
import websocket
import threading

# Disabilita i warning SSL per i test
urllib3.disable_warnings(urllib3.exceptions.InsecureRequestWarning)

class MagicEventsK8sTestClient:
    def __init__(self, base_url: str = "http://magicevents.192.168.49.2.nip.io"):
        self.base_url = base_url
        self.base_url_https = "https://magicevents.192.168.49.2.nip.io"
        self.users = {}
        self.events = {}
        self.session = requests.Session()

    def _make_request(self, method: str, url: str, **kwargs) -> requests.Response:
        """Helper per fare richieste HTTP con gestione errori"""
        try:
            response = self.session.request(method, url, **kwargs)
            print(f"[{method}] {url} -> {response.status_code}")
            if response.status_code >= 400:
                print(f"Response body: {response.text}")
            return response
        except Exception as e:
            print(f"[ERROR] {method} {url}: {e}")
            raise

    # ============ USER MANAGEMENT SERVICE ============
    def register_user(self, name: str, surname: str, username: str, email: str, password: str) -> bool:
        """Registra un nuovo utente tramite ingress HTTPS"""
        try:
            url = f"{self.base_url_https}/api/users/login/register"
            params = {
                "name": name,
                "surname": surname,
                "username": username,
                "email": email,
                "password": password
            }

            response = self._make_request("POST", url, params=params, verify=False)
            success = response.status_code == 200
            if success:
                print(f"✓ Utente {username} registrato con successo")
            else:
                print(f"✗ Errore registrazione {username}: {response.text}")
            return success
        except Exception as e:
            print(f"✗ Errore registrazione {username}: {e}")
            return False

    def login_user(self, email: str, password: str) -> Optional[Dict]:
        """Login utente e ottieni token"""
        try:
            url = f"{self.base_url_https}/api/users/login/form"
            params = {"email": email, "password": password}

            response = self._make_request("GET", url, params=params, verify=False)
            if response.status_code == 200:
                user_data = response.json()
                self.users[email] = user_data
                print(f"✓ Login effettuato per {email}")
                return user_data
            else:
                print(f"✗ Errore login {email}: {response.text}")
                return None
        except Exception as e:
            print(f"✗ Errore login {email}: {e}")
            return None

    def get_user_info(self, access_token: str) -> Optional[Dict]:
        """Ottieni informazioni utente tramite token"""
        try:
            url = f"{self.base_url_https}/api/users/login/userprofile"
            params = {"accessToken": access_token}

            response = self._make_request("GET", url, params=params, verify=False)
            if response.status_code == 200:
                return response.json()
            return None
        except Exception as e:
            print(f"✗ Errore get_user_info: {e}")
            return None

    def is_authenticated(self, email: str) -> bool:
        """Verifica se utente è autenticato"""
        try:
            url = f"{self.base_url_https}/api/users/info/isauthenticated"
            params = {"email": email}

            response = self._make_request("GET", url, params=params, verify=False)
            return response.status_code == 200 and response.json()
        except Exception as e:
            print(f"✗ Errore is_authenticated: {e}")
            return False

    def get_users_email(self, emails: List[str]) -> Optional[Dict]:
        """Ottieni mapping email->magic_events_tag"""
        try:
            url = f"{self.base_url_https}/api/users/info"
            headers = {"Content-Type": "application/json"}

            response = self._make_request("POST", url, json=emails, headers=headers, verify=False)
            if response.status_code == 200:
                return response.json()
            return None
        except Exception as e:
            print(f"✗ Errore get_users_email: {e}")
            return None

    # ============ EVENTS MANAGEMENT SERVICE (CORRETTI) ============
    def create_event(self, event_data: Dict, creator_email: str) -> Optional[Dict]:
        """Crea un nuovo evento"""
        try:
            url = f"{self.base_url}/api/events/gestion/create"
            headers = {"Content-Type": "application/json"}
            params = {"creatorEmail": creator_email}

            response = self._make_request("POST", url, json=event_data, headers=headers, params=params)
            if response.status_code == 201:  # CREATED
                event_id = response.json()
                print(f"✓ Evento creato con ID: {event_id}")
                self.events[event_id] = event_data
                return {"eventId": event_id, "eventData": event_data}
            else:
                print(f"✗ Errore creazione evento: {response.text}")
                return None
        except Exception as e:
            print(f"✗ Errore create_event: {e}")
            return None

    def get_event_info(self, event_id: int) -> Optional[Dict]:
        """Ottieni informazioni evento per ID"""
        try:
            url = f"{self.base_url}/api/events/gestion/geteventinfo"
            params = {"eventId": event_id}

            response = self._make_request("GET", url, params=params)
            if response.status_code == 200:
                return response.json()
            return None
        except Exception as e:
            print(f"✗ Errore get_event_info: {e}")
            return None

    def modify_event(self, event_id: int, creator_id: int, event_data: Dict) -> Optional[str]:
        """Modifica un evento"""
        try:
            url = f"{self.base_url}/api/events/gestion/modify"
            headers = {"Content-Type": "application/json"}
            params = {"eventId": event_id, "magicEventsTag": creator_id}

            response = self._make_request("PUT", url, json=event_data, headers=headers, params=params)
            if response.status_code == 200:
                result = response.text
                print(f"✓ Evento {event_id} modificato: {result}")
                return result
            return None
        except Exception as e:
            print(f"✗ Errore modify_event: {e}")
            return None

    def delete_event(self, event_id: int, creator_id: int) -> bool:
        """Elimina un evento"""
        try:
            url = f"{self.base_url}/api/events/gestion/delete"
            params = {"eventId": event_id, "magicEventsTag": creator_id}

            response = self._make_request("DELETE", url, params=params)
            success = response.status_code == 200 and response.json()
            if success:
                print(f"✓ Evento {event_id} eliminato")
            return success
        except Exception as e:
            print(f"✗ Errore delete_event: {e}")
            return False

    def annull_event(self, event_id: int, creator_id: int) -> Optional[str]:
        """Annulla un evento"""
        try:
            url = f"{self.base_url}/api/events/gestion/annullevent"
            params = {"eventId": event_id, "magicEventsTag": creator_id}

            response = self._make_request("PUT", url, params=params)
            if response.status_code == 202:  # ACCEPTED
                result = response.text
                print(f"✓ Evento {event_id} annullato: {result}")
                return result
            return None
        except Exception as e:
            print(f"✗ Errore annull_event: {e}")
            return None

    def active_event(self, event_id: int, creator_id: int) -> Optional[str]:
        """Riattiva un evento annullato"""
        try:
            url = f"{self.base_url}/api/events/gestion/de-annullevent"
            params = {"eventId": event_id, "magicEventsTag": creator_id}

            response = self._make_request("PUT", url, params=params)
            if response.status_code == 202:  # ACCEPTED
                result = response.text
                print(f"✓ Evento {event_id} riattivato: {result}")
                return result
            return None
        except Exception as e:
            print(f"✗ Errore active_event: {e}")
            return None

    def active_services_event(self, event_id: int, creator_id: int, services_data: Dict) -> Optional[str]:
        """Attiva servizi per un evento"""
        try:
            url = f"{self.base_url}/api/events/gestion/activeservices"
            headers = {"Content-Type": "application/json"}
            params = {"eventId": event_id, "magicEventsTag": creator_id}

            response = self._make_request("PUT", url, json=services_data, headers=headers, params=params)
            if response.status_code == 202:  # ACCEPTED
                result = response.text
                print(f"✓ Servizi attivati per evento {event_id}: {result}")
                return result
            return None
        except Exception as e:
            print(f"✗ Errore active_services_event: {e}")
            return None

    def get_event_enabled_services(self, event_id: int, magic_events_tag: int) -> Optional[Dict]:
        """Ottieni servizi abilitati per un evento"""
        try:
            url = f"{self.base_url}/api/events/gestion/getEventEnabledServices"
            params = {"eventId": event_id, "magicEventsTag": magic_events_tag}

            response = self._make_request("GET", url, params=params)
            if response.status_code == 200:
                return response.json()
            return None
        except Exception as e:
            print(f"✗ Errore get_event_enabled_services: {e}")
            return None

    def is_participant(self, participant_id: int, event_id: int) -> bool:
        """Verifica se un utente è partecipante di un evento"""
        try:
            url = f"{self.base_url}/api/events/gestion/ispartecipant"
            params = {"partecipantId": participant_id, "eventId": event_id}

            response = self._make_request("GET", url, params=params)
            return response.status_code == 200 and response.json()
        except Exception as e:
            print(f"✗ Errore is_participant: {e}")
            return False

    def is_admin(self, participant_id: int, event_id: int) -> bool:
        """Verifica se un utente è admin di un evento"""
        try:
            url = f"{self.base_url}/api/events/gestion/isadmin"
            params = {"partecipantId": participant_id, "eventId": event_id}

            response = self._make_request("GET", url, params=params)
            return response.status_code == 200 and response.json()
        except Exception as e:
            print(f"✗ Errore is_admin: {e}")
            return False

    def is_creator(self, creator_id: int, event_id: int) -> bool:
        """Verifica se un utente è il creatore di un evento"""
        try:
            url = f"{self.base_url}/api/events/gestion/iscreator"
            params = {"creatorId": creator_id, "eventId": event_id}

            response = self._make_request("GET", url, params=params)
            return response.status_code == 200 and response.json()
        except Exception as e:
            print(f"✗ Errore is_creator: {e}")
            return False

    def get_events_created(self, creator_id: int) -> Optional[List]:
        """Ottieni eventi creati da un utente"""
        try:
            url = f"{self.base_url}/api/events/gestion/geteventslistc"
            params = {"creatorId": creator_id}

            response = self._make_request("GET", url, params=params)
            if response.status_code == 200:
                events = response.json()
                print(f"✓ Ottenuti {len(events)} eventi creati")
                return events
            return None
        except Exception as e:
            print(f"✗ Errore get_events_created: {e}")
            return None

    def get_events_participated(self, participant_id: int) -> Optional[List]:
        """Ottieni eventi a cui partecipa un utente"""
        try:
            url = f"{self.base_url}/api/events/gestion/geteventslistp"
            params = {"partecipantId": participant_id}

            response = self._make_request("GET", url, params=params)
            if response.status_code == 200:
                events = response.json()
                print(f"✓ Ottenuti {len(events)} eventi partecipati")
                return events
            return None
        except Exception as e:
            print(f"✗ Errore get_events_participated: {e}")
            return None

    def get_event_id(self, creator_id: int, title: str, day: str) -> Optional[List]:
        """Ottieni ID evento per creatore, titolo e giorno"""
        try:
            url = f"{self.base_url}/api/events/gestion/geteventid"
            params = {"creatorId": creator_id, "title": title, "day": day}

            response = self._make_request("GET", url, params=params)
            if response.status_code == 200:
                return response.json()
            return None
        except Exception as e:
            print(f"✗ Errore get_event_id: {e}")
            return None

    def update_event_admins(self, admins: List[str], event_id: int, creator_id: int) -> Optional[str]:
        """Aggiorna admin di un evento"""
        try:
            url = f"{self.base_url}/api/events/gestion/updateadmins"
            params = {"admins": admins, "eventId": event_id, "magicEventsTag": creator_id}

            response = self._make_request("PUT", url, params=params)
            if response.status_code == 200:
                return response.text
            return None
        except Exception as e:
            print(f"✗ Errore update_event_admins: {e}")
            return None

    def add_event_participants(self, participants: List[str], event_id: int, creator_id: int) -> Optional[str]:
        """Aggiungi partecipanti a un evento"""
        try:
            url = f"{self.base_url}/api/events/gestion/addpartecipants"
            params = {"partecipants": participants, "eventId": event_id, "magicEventsTag": creator_id}

            response = self._make_request("PUT", url, params=params)
            if response.status_code == 200:
                return response.text
            return None
        except Exception as e:
            print(f"✗ Errore add_event_participants: {e}")
            return None

    def get_admins_for_event(self, event_id: int, creator_id: int) -> Optional[List]:
        """Ottieni lista admin di un evento"""
        try:
            url = f"{self.base_url}/api/events/gestion/getadminsforevent"
            params = {"eventId": event_id, "magicEventsTag": creator_id}

            response = self._make_request("GET", url, params=params)
            if response.status_code == 200:
                return response.json()
            return None
        except Exception as e:
            print(f"✗ Errore get_admins_for_event: {e}")
            return None

    def get_participants_for_event(self, event_id: int) -> Optional[List]:
        """Ottieni lista partecipanti di un evento"""
        try:
            url = f"{self.base_url}/api/events/gestion/getpartecipantsforevent"
            params = {"eventId": event_id}

            response = self._make_request("GET", url, params=params)
            if response.status_code == 200:
                return response.json()
            return None
        except Exception as e:
            print(f"✗ Errore get_participants_for_event: {e}")
            return None

    # ============ BOARD SERVICE ============
    def create_board(self, event_id: int, title: str, description: str, user_magic_tag: int) -> bool:
        """Crea una board per un evento"""
        try:
            url = f"{self.base_url}/api/boards/board/createBoard"
            headers = {"Content-Type": "application/json"}
            data = {
                "eventID": event_id,
                "title": title,
                "description": description,
                "userMagicEventsTag": user_magic_tag
            }

            response = self._make_request("POST", url, json=data, headers=headers)
            success = response.status_code == 200 and response.json()
            if success:
                print(f"✓ Board creata per evento {event_id}")
            return success
        except Exception as e:
            print(f"✗ Errore create_board: {e}")
            return False

    def get_board(self, event_id: int, user_magic_tag: int, page: int = 0) -> Optional[Dict]:
        """Ottieni board di un evento"""
        try:
            url = f"{self.base_url}/api/boards/board/getBoard/{event_id}/{page}"
            params = {"userMagicEventsTag": user_magic_tag}

            response = self._make_request("GET", url, params=params)
            if response.status_code == 200:
                return response.json()
            return None
        except Exception as e:
            print(f"✗ Errore get_board: {e}")
            return None

    def is_board_exists(self, event_id: int) -> bool:
        """Verifica se esiste una board per l'evento"""
        try:
            url = f"{self.base_url}/api/boards/board/isBoardExists/{event_id}"

            response = self._make_request("GET", url)
            return response.status_code == 200 and response.json()
        except Exception as e:
            print(f"✗ Errore is_board_exists: {e}")
            return False

    def delete_board(self, event_id: int, user_magic_tag: int) -> bool:
        """Elimina board di un evento"""
        try:
            url = f"{self.base_url}/api/boards/board/deleteBoard/{event_id}"
            params = {"userMagicEventsTag": user_magic_tag}

            response = self._make_request("DELETE", url, params=params)
            success = response.status_code == 200 and response.json()
            if success:
                print(f"✓ Board eliminata per evento {event_id}")
            return success
        except Exception as e:
            print(f"✗ Errore delete_board: {e}")
            return False

    # ============ GALLERY SERVICE ============
    def create_gallery(self, event_id: int, title: str, user_magic_tag: int) -> bool:
        """Crea una galleria per un evento"""
        try:
            url = f"{self.base_url}/api/galleries/gallery/createGallery"
            headers = {"Content-Type": "application/json"}
            data = {
                "eventID": event_id,
                "title": title,
                "userMagicEventsTag": user_magic_tag
            }

            response = self._make_request("POST", url, json=data, headers=headers)
            success = response.status_code == 200
            if success:
                print(f"✓ Galleria creata per evento {event_id}")
            return success
        except Exception as e:
            print(f"✗ Errore create_gallery: {e}")
            return False

    def get_gallery(self, event_id: int, user_magic_tag: int, page: int = 0) -> Optional[Dict]:
        """Ottieni galleria di un evento"""
        try:
            url = f"{self.base_url}/api/galleries/gallery/getGallery/{event_id}/{page}"
            params = {"userMagicEventsTag": user_magic_tag}

            response = self._make_request("GET", url, params=params)
            if response.status_code == 200:
                return response.json()
            return None
        except Exception as e:
            print(f"✗ Errore get_gallery: {e}")
            return None

    def is_gallery_exists(self, event_id: int) -> bool:
        """Verifica se esiste una galleria per l'evento"""
        try:
            url = f"{self.base_url}/api/galleries/gallery/isGalleryExists/{event_id}"

            response = self._make_request("GET", url)
            return response.status_code == 200 and response.json()
        except Exception as e:
            print(f"✗ Errore is_gallery_exists: {e}")
            return False

    def delete_gallery(self, event_id: int, user_magic_tag: int) -> bool:
        """Elimina galleria di un evento"""
        try:
            url = f"{self.base_url}/api/galleries/gallery/deleteGallery/{event_id}"
            params = {"userMagicEventsTag": user_magic_tag}

            response = self._make_request("DELETE", url, params=params)
            success = response.status_code == 200
            if success:
                print(f"✓ Galleria eliminata per evento {event_id}")
            return success
        except Exception as e:
            print(f"✗ Errore delete_gallery: {e}")
            return False

    # ============ GUEST GAME SERVICE ============
    def test_games_service(self) -> bool:
        """Test del servizio games (guest-game-service)"""
        try:
            url = f"{self.base_url}/api/games/test"

            response = self._make_request("GET", url)
            success = response.status_code == 200
            if success:
                print(f"✓ Games service risponde: {response.text}")
            return success
        except Exception as e:
            print(f"✗ Errore test_games_service: {e}")
            return False

    # ============ UTILITY METHODS ============
    def create_sample_image(self) -> str:
        """Crea un'immagine base64 di esempio per i test"""
        # Immagine 1x1 pixel PNG trasparente
        png_data = base64.b64encode(b'\x89PNG\r\n\x1a\n\x00\x00\x00\rIHDR\x00\x00\x00\x01\x00\x00\x00\x01\x08\x06\x00\x00\x00\x1f\x15\xc4\x89\x00\x00\x00\nIDATx\x9cc\x00\x01\x00\x00\x05\x00\x01\r\n-\xdb\x00\x00\x00\x00IEND\xaeB`\x82').decode('utf-8')
        return f"data:image/png;base64,{png_data}"

    def create_sample_event_data(self, creator_magic_tag: int) -> Dict:
        """Crea dati di esempio per un evento"""
        now = datetime.now()
        start_time = now + timedelta(days=1)
        end_time = start_time + timedelta(hours=2)

        return {
            "title": f"Test Event {int(time.time())}",
            "description": "Evento di test creato automaticamente",
            "starting": start_time.isoformat(),
            "ending": end_time.isoformat(),
            "location": "Test Location",
            "creator": creator_magic_tag,
            "partecipants": [],
            "admins": [],
            "image": self.create_sample_image()
        }

    def create_sample_services_data(self) -> Dict:
        """Crea dati di esempio per i servizi"""
        return {
            "boardEnabled": True,
            "galleryEnabled": True,
            "gamesEnabled": False
        }

    # ============ TEST COMPLETO ============
    def run_complete_test(self):
        """Esegue un test completo di tutti i servizi"""
        print("=" * 60)
        print("AVVIO TEST COMPLETO MAGICEVENTS K8S")
        print("=" * 60)

        # 1. Test registrazione e login utenti
        print("\n1. TESTING USER MANAGEMENT SERVICE")
        print("-" * 40)

        # Registra utenti di test
        users_data = [
            ("Mario", "Rossi", "mario.rossi", "mario@test.com", "password123"),
            ("Luigi", "Verdi", "luigi.verdi", "luigi@test.com", "password123"),
            ("Anna", "Bianchi", "anna.bianchi", "anna@test.com", "password123")
        ]

        registered_users = []
        for name, surname, username, email, password in users_data:
            if self.register_user(name, surname, username, email, password):
                registered_users.append((email, password))
                time.sleep(1)  # Evita rate limiting

        # Login utenti
        logged_users = []
        for email, password in registered_users:
            user_info = self.login_user(email, password)
            if user_info:
                logged_users.append(user_info)
                time.sleep(1)

        if not logged_users:
            print("✗ Nessun utente loggato con successo. Termino test.")
            return

        # Test autenticazione
        for user in logged_users[:2]:  # Testa solo i primi 2
            email = user.get('email', '')
            if email:
                is_auth = self.is_authenticated(email)
                print(f"Utente {email} autenticato: {is_auth}")

        # 2. Test Events Management Service (CORRETTI)
        print("\n2. TESTING EVENTS MANAGEMENT SERVICE")
        print("-" * 40)

        creator = logged_users[0]
        creator_tag = creator.get('magicEventTag', 1)
        creator_email = creator.get('email', '')

        # Crea evento
        event_data = self.create_sample_event_data(creator_tag)
        created_event = self.create_event(event_data, creator_email)

        if created_event:
            event_id = created_event['eventId']

            # Test varie funzioni dell'evento
            event_info = self.get_event_info(event_id)
            if event_info:
                print(f"✓ Info evento ottenute: {event_info['title']}")

            # Test permessi
            is_creator = self.is_creator(creator_tag, event_id)
            print(f"Utente è creatore: {is_creator}")

            # Test lista eventi creati
            events_created = self.get_events_created(creator_tag)

            # Test servizi evento
            services_data = self.create_sample_services_data()
            services_result = self.active_services_event(event_id, creator_tag, services_data)

            # Ottieni servizi abilitati
            enabled_services = self.get_event_enabled_services(event_id, creator_tag)
            if enabled_services:
                print(f"✓ Servizi abilitati: {enabled_services}")

            # Test partecipazione (con secondo utente se disponibile)
            if len(logged_users) > 1:
                participant = logged_users[1]
                participant_tag = participant.get('magicEventTag', 2)
                participant_email = participant.get('email', '')

                # Aggiungi partecipante
                add_result = self.add_event_participants([participant_email], event_id, creator_tag)
                if add_result:
                    print(f"✓ Partecipante aggiunto: {add_result}")

                # Verifica se è partecipante
                is_participant = self.is_participant(participant_tag, event_id)
                print(f"Utente è partecipante: {is_participant}")

                # Ottieni lista partecipanti
                participants = self.get_participants_for_event(event_id)
                if participants:
                    print(f"✓ Partecipanti evento: {len(participants)}")

        # 3. Test Board Service
        print("\n3. TESTING BOARD SERVICE")
        print("-" * 40)

        if created_event:
            event_id = created_event['eventId']

            # Crea board
            if self.create_board(event_id, "Test Board", "Board di test", creator_tag):
                time.sleep(1)

                # Verifica esistenza board
                exists = self.is_board_exists(event_id)
                print(f"Board esiste: {exists}")

                # Ottieni board
                board_data = self.get_board(event_id, creator_tag)
                if board_data:
                    print(f"✓ Board ottenuta con {len(board_data.get('messages', []))} messaggi")

        # 4. Test Gallery Service
        print("\n4. TESTING GALLERY SERVICE")
        print("-" * 40)

        if created_event:
            event_id = created_event['eventId']

            # Crea galleria
            if self.create_gallery(event_id, "Test Gallery", creator_tag):
                time.sleep(1)

                # Verifica esistenza galleria
                exists = self.is_gallery_exists(event_id)
                print(f"Galleria esiste: {exists}")

                # Ottieni galleria
                gallery_data = self.get_gallery(event_id, creator_tag)
                if gallery_data:
                    print(f"✓ Galleria ottenuta con {len(gallery_data.get('images', []))} immagini")

        # 5. Test Games Service
        print("\n5. TESTING GAMES SERVICE")
        print("-" * 40)

        self.test_games_service()

        # 6. Test User Info Service
        print("\n6. TESTING USER INFO SERVICE")
        print("-" * 40)

        # Test mapping email->magic_events_tag
        emails = [user.get('email', '') for user in logged_users[:2] if user.get('email')]
        if emails:
            user_mapping = self.get_users_email(emails)
            if user_mapping:
                print(f"✓ Mapping utenti ottenuto: {user_mapping}")

        print("\n" + "=" * 60)
        print("TEST COMPLETO TERMINATO")
        print("=" * 60)

def main():
    """Funzione principale per eseguire i test"""
    print("MagicEvents K8s Test Client - VERSIONE CORRETTA")
    print("Assicurati che il cluster Kubernetes sia attivo e i servizi deployati!")
    print()

    # Inizializza client
    client = MagicEventsK8sTestClient()

    # Menu interattivo
    while True:
        print("\nScegli un'opzione:")
        print("1. Esegui test completo")
        print("2. Test singolo servizio utenti")
        print("3. Test singolo servizio eventi")
        print("4. Test singolo servizio board")
        print("5. Test singolo servizio galleria")
        print("6. Test singolo servizio games")
        print("0. Esci")

        choice = input("\nInserisci la tua scelta: ")

        if choice == "1":
            client.run_complete_test()
        elif choice == "2":
            # Test rapido user service
            print("\nTest User Service...")
            success = client.register_user("Test", "User", "testuser", "test@example.com", "password123")
            if success:
                client.login_user("test@example.com", "password123")
        elif choice == "3":
            # Test eventi (richiede utente loggato)
            print("Test Events Service richiede utente autenticato. Esegui prima test completo.")
        elif choice == "4":
            print("Test Board Service richiede evento esistente. Esegui prima test completo.")
        elif choice == "5":
            print("Test Gallery Service richiede evento esistente. Esegui prima test completo.")
        elif choice == "6":
            client.test_games_service()
        elif choice == "0":
            print("Arrivederci!")
            break
        else:
            print("Scelta non valida!")

if __name__ == "__main__":
    main()
