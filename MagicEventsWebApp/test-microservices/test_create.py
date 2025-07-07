import requests
import json
from datetime import datetime, timedelta
from typing import Dict, List

# Configurazione base (dalla application.properties)
BASE_URL = "http://localhost:8080/gestion"  # Porta 8080 come da application.properties
AUTH = ('admin', 'admin123')  # Credenziali da application.properties

class EventsManagementTester:
    def __init__(self):
        self.base_url = BASE_URL
        self.created_events = []

        # Definizione utenti di test
        self.users = {
            "creator": {
                "id": 1,
                "name": "Marco Rossi",
                "email": "john.doe@example.com",
                "role": "Event Creator",
                "description": "Organizzatore principale eventi aziendali"
            },
            "admin": {
                "id": 2,
                "name": "Laura Bianchi",
                "email": "jane.admin@example.com",
                "role": "Event Administrator",
                "description": "Amministratore eventi e supporto organizzativo"
            },
            "participant": {
                "id": 3,
                "name": "Alessandro Verdi",
                "email": "alice.w@example.com",
                "role": "Event Participant",
                "description": "Partecipante attivo agli eventi"
            },
            "additional_user": {
                "id": 4,
                "name": "Sofia Neri",
                "email": "sofia.neri@example.com",
                "role": "Additional Participant",
                "description": "Partecipante secondario per test multi-utente"
            },
            "external_admin": {
                "id": 5,
                "name": "Giuseppe Blu",
                "email": "giuseppe.blu@example.com",
                "role": "External Admin",
                "description": "Admin esterno per test di autorizzazioni"
            }
        }

        # Endpoint mappings dal controller
        self.endpoints = {
            'create': '/create',
            'annull': '/annullevent',
            'update_admins': '/updateadmins',
            'add_participants': '/addpartecipants',
            'get_info': '/geteventinfo',
            'modify': '/modify',
            'is_participant': '/ispartecipant',
            'is_admin': '/isadmin',
            'is_creator': '/iscreator',
            'get_events_created': '/geteventslistc',
            'get_events_participated': '/geteventslistp',
            'get_event_id': '/geteventid',
            'get_admins': '/getadminsforevent',
            'get_participants': '/getpartecipantsforevent',
            'delete': '/delete'
        }

    def get_headers(self) -> Dict[str, str]:
        """Ritorna gli headers HTTP standard"""
        return {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }

    def get_dummy_base64_image(self) -> str:
        """Genera una stringa base64 fittizia per l'immagine"""
        # Questa è una piccola immagine PNG 1x1 pixel trasparente in base64
        return "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg=="

    def get_alternative_base64_image(self) -> str:
        """Genera una seconda immagine base64 per test di modifica"""
        # Immagine PNG 2x2 pixel per test di aggiornamento
        return "iVBORw0KGgoAAAANSUhEUgAAAAIAAAACCAYAAABytg0kAAAAEklEQVR42mP8z8BQz0AEYBwOABK/AX8kFQ6hAAAAAElFTkSuQmCC"

    def create_test_event_dto(self, creator_id: int, include_admins: bool = True, include_participants: bool = True) -> Dict:
        """Crea un EventDTO di test con formato corretto"""
        start_time = datetime.now() + timedelta(days=1)
        end_time = start_time + timedelta(hours=3)

        event_dto = {
            "title": f"Test Event - {datetime.now().strftime('%H:%M:%S')}",
            "description": "Test event description for multi-user testing",
            "starting": start_time.isoformat(),
            "ending": end_time.isoformat(),
            "location": "Test Location Via Roma 123, Milano",
            "creator": creator_id,
            "partecipants": [],
            "admins": [],
            "image": self.get_dummy_base64_image()
        }

        if include_admins:
            event_dto["admins"] = [self.users["admin"]["email"]]

        if include_participants:
            event_dto["partecipants"] = [self.users["participant"]["email"]]

        return event_dto

    def test_create_event(self) -> None:
        """Test POST /gestion/create"""
        print("\n=== TEST: Creazione Eventi (POST /gestion/create) ===")

        # Test 1: Creazione evento valido
        event_dto = self.create_test_event_dto(self.users["creator"]["id"])
        url = f"{self.base_url}{self.endpoints['create']}"

        response = requests.post(
            url,
            json=event_dto,
            params={"creatorEmail": self.users["creator"]["email"]},
            headers=self.get_headers(),
            auth=AUTH
        )

        if response.status_code == 200:  # Assumendo che ritorni 200 con l'ID
            try:
                event_id = response.json()
                self.created_events.append(event_id)
                print(f"✓ Evento creato con successo - ID: {event_id}")
                print(f"  Immagine inclusa: {len(event_dto['image'])} caratteri base64")
            except:
                print(f"✓ Evento creato - Response: {response.text}")
        else:
            print(f"✗ Fallimento creazione evento: {response.status_code} - {response.text}")

        # Test 2: Creazione evento senza admin e partecipanti
        event_dto_minimal = self.create_test_event_dto(
            self.users["creator"]["id"],
            include_admins=False,
            include_participants=False
        )

        response = requests.post(
            url,
            json=event_dto_minimal,
            params={"creatorEmail": self.users["creator"]["email"]},
            headers=self.get_headers(),
            auth=AUTH
        )

        if response.status_code == 200:
            try:
                event_id = response.json()
                self.created_events.append(event_id)
                print(f"✓ Evento minimal creato - ID: {event_id}")
            except:
                print(f"✓ Evento minimal creato - Response: {response.text}")
        else:
            print(f"✗ Fallimento creazione evento minimal: {response.status_code}")

        # Test 3: Tentativo di creazione con data fine precedente alla data inizio
        invalid_event = self.create_test_event_dto(self.users["creator"]["id"])
        # Inverti le date per renderle invalide
        invalid_event["starting"] = (datetime.now() + timedelta(days=2)).isoformat()
        invalid_event["ending"] = (datetime.now() + timedelta(days=1)).isoformat()

        response = requests.post(
            url,
            json=invalid_event,
            params={"creatorEmail": self.users["creator"]["email"]},
            headers=self.get_headers(),
            auth=AUTH
        )

        if response.status_code >= 400:  # Errore atteso
            print(f"✓ Correttamente rifiutato evento con date invalide: {response.status_code}")
        else:
            print(f"? Risposta inaspettata per evento con date invalide: {response.status_code}")

        # Test 4: Tentativo di creazione senza immagine (deve fallire per @NotBlank)
        print("--- Test senza immagine ---")
        event_without_image = self.create_test_event_dto(self.users["creator"]["id"])
        del event_without_image["image"]

        response = requests.post(
            url,
            json=event_without_image,
            params={"creatorEmail": self.users["creator"]["email"]},
            headers=self.get_headers(),
            auth=AUTH
        )

        if response.status_code >= 400:
            print(f"✓ Correttamente rifiutato evento senza immagine: {response.status_code}")
        else:
            print(f"? Risposta inaspettata per evento senza immagine: {response.status_code}")

        # Test 5: Tentativo di creazione con immagine vuota (deve fallire per @NotBlank)
        print("--- Test con immagine vuota ---")
        event_empty_image = self.create_test_event_dto(self.users["creator"]["id"])
        event_empty_image["image"] = ""

        response = requests.post(
            url,
            json=event_empty_image,
            params={"creatorEmail": self.users["creator"]["email"]},
            headers=self.get_headers(),
            auth=AUTH
        )

        if response.status_code >= 400:
            print(f"✓ Correttamente rifiutato evento con immagine vuota: {response.status_code}")
        else:
            print(f"? Risposta inaspettata per evento con immagine vuota: {response.status_code}")

    def test_get_event_info(self) -> None:
        """Test POST /gestion/geteventinfo"""
        print("\n=== TEST: Recupero Informazioni Evento (POST /gestion/geteventinfo) ===")

        if not self.created_events:
            print("✗ Nessun evento creato per il test")
            return

        event_id = self.created_events[0]
        url = f"{self.base_url}{self.endpoints['get_info']}"

        # Test recupero informazioni evento esistente
        response = requests.post(
            url,
            params={"eventId": event_id},
            headers=self.get_headers(),
            auth=AUTH
        )

        if response.status_code == 200:
            try:
                event_info = response.json()
                print(f"✓ Informazioni evento recuperate correttamente")
                print(f"  Titolo: {event_info.get('title')}")
                print(f"  Creator: {event_info.get('creator')}")
                print(f"  Admins: {event_info.get('admins')}")
                print(f"  Partecipants: {event_info.get('partecipants')}")
                image = event_info.get('image')
                if image:
                    print(f"  Immagine presente: {len(image)} caratteri base64")
                else:
                    print("  Immagine non presente nella risposta")
            except json.JSONDecodeError:
                print(f"✓ Informazioni recuperate (non JSON): {response.text}")
        else:
            print(f"✗ Fallimento recupero info: {response.status_code} - {response.text}")

        # Test con evento inesistente
        response = requests.post(
            url,
            params={"eventId": 99999},
            headers=self.get_headers(),
            auth=AUTH
        )

        if response.status_code >= 400:
            print("✓ Correttamente gestito evento inesistente")
        else:
            print(f"? Risposta per evento inesistente: {response.status_code}")

    def test_modify_event(self) -> None:
        """Test POST /gestion/modify"""
        print("\n=== TEST: Modifica Evento (POST /gestion/modify) ===")

        if not self.created_events:
            print("✗ Nessun evento creato per il test")
            return

        event_id = self.created_events[0]
        creator_id = self.users["creator"]["id"]
        url = f"{self.base_url}{self.endpoints['modify']}"

        # 1) Creator modifies the event (should succeed)
        modified_event_dto = self.create_test_event_dto(creator_id)
        modified_event_dto["title"] = "Evento Modificato dal Creator"
        modified_event_dto["description"] = "Descrizione modificata con nuovi dettagli"
        modified_event_dto["location"] = "Nuova Location - Via Nuova 456, Roma"
        # Modifica anche l'immagine per testare l'aggiornamento
        modified_event_dto["image"] = self.get_alternative_base64_image()

        response = requests.post(
            url,
            params={
                "eventId": event_id,
                "magicEventsTag": creator_id
            },
            json=modified_event_dto,
            headers=self.get_headers(),
            auth=AUTH
        )

        if response.status_code == 200:
            print("✓ Creator - Modifica evento riuscita")
            print(f"  Nuova immagine: {len(modified_event_dto['image'])} caratteri base64")
            print(f"  Response: {response.text}")
        else:
            print(f"✗ Creator - Fallimento modifica: {response.status_code} - {response.text}")

        # 2) Non-creator attempts modification (should fail)
        response = requests.post(
            url,
            params={
                "eventId": event_id,
                "magicEventsTag": self.users["participant"]["id"]
            },
            json=modified_event_dto,
            headers=self.get_headers(),
            auth=AUTH
        )

        if response.status_code == 200 and "Error" in response.text:
            print("✓ Non-creator - Correttamente negata modifica evento")
        elif response.status_code >= 400:
            print("✓ Non-creator - Correttamente negata modifica evento (HTTP error)")
        else:
            print(f"? Non-creator - Risposta inaspettata: {response.status_code} - {response.text}")

    def test_user_management_operations(self) -> None:
        """Test operazioni di gestione utenti"""
        print("\n=== TEST: Gestione Utenti (Admin/Partecipanti) ===")

        if not self.created_events:
            print("✗ Nessun evento creato per il test")
            return

        event_id = self.created_events[0]
        creator_id = self.users["creator"]["id"]

        # Test aggiunta admin
        print("--- Test aggiunta admin ---")
        new_admins = [self.users["external_admin"]["email"]]
        response = requests.get(
            f"{self.base_url}{self.endpoints['update_admins']}",
            params={
                "admins": new_admins,
                "eventId": event_id,
                "magicEventsTag": creator_id
            },
            headers=self.get_headers(),
            auth=AUTH
        )
        print(f"Aggiunta admin response: {response.status_code} - {response.text}")

        # Test aggiunta partecipanti
        print("--- Test aggiunta partecipanti ---")
        new_participants = [self.users["additional_user"]["email"]]
        response = requests.get(
            f"{self.base_url}{self.endpoints['add_participants']}",
            params={
                "partecipants": new_participants,
                "eventId": event_id,
                "magicEventsTag": creator_id
            },
            headers=self.get_headers(),
            auth=AUTH
        )
        print(f"Aggiunta partecipanti response: {response.status_code} - {response.text}")

        # Test verifica ruoli
        print("--- Test verifica ruoli ---")

        # Test isPartecipant
        response = requests.get(
            f"{self.base_url}{self.endpoints['is_participant']}",
            params={
                "partecipantId": self.users["participant"]["email"],
                "eventId": event_id
            },
            headers=self.get_headers(),
            auth=AUTH
        )
        print(f"Is participant check: {response.status_code} - {response.text}")

        # Test isCreator
        response = requests.get(
            f"{self.base_url}{self.endpoints['is_creator']}",
            params={
                "magicEventsTag": creator_id,
                "eventId": event_id
            },
            headers=self.get_headers(),
            auth=AUTH
        )
        print(f"Is creator check: {response.status_code} - {response.text}")

    def test_event_lists_retrieval(self) -> None:
        """Test recupero liste eventi"""
        print("\n=== TEST: Recupero Liste Eventi ===")

        creator_id = self.users["creator"]["id"]
        participant_id = self.users["participant"]["id"]

        # Test eventi creati
        print("--- Test eventi creati ---")
        response = requests.post(
            f"{self.base_url}{self.endpoints['get_events_created']}",
            params={"creatorId": creator_id},
            headers=self.get_headers(),
            auth=AUTH
        )
        print(f"Eventi creati response: {response.status_code}")
        if response.status_code == 200:
            try:
                events = response.json()
                print(f"  Numero eventi creati: {len(events) if isinstance(events, list) else 'N/A'}")
            except:
                print(f"  Response: {response.text}")

        # Test eventi partecipati
        print("--- Test eventi partecipati ---")
        response = requests.post(
            f"{self.base_url}{self.endpoints['get_events_participated']}",
            params={"partecipantId": participant_id},
            headers=self.get_headers(),
            auth=AUTH
        )
        print(f"Eventi partecipati response: {response.status_code}")
        if response.status_code == 200:
            try:
                events = response.json()
                print(f"  Numero eventi partecipati: {len(events) if isinstance(events, list) else 'N/A'}")
            except:
                print(f"  Response: {response.text}")

    def test_annull_event(self) -> None:
        """Test annullamento evento"""
        print("\n=== TEST: Annullamento Evento ===")

        if not self.created_events:
            print("✗ Nessun evento creato per il test")
            return

        event_id = self.created_events[0]
        creator_id = self.users["creator"]["id"]

        # Test annullamento da parte del creator
        response = requests.get(
            f"{self.base_url}{self.endpoints['annull']}",
            params={
                "eventId": event_id,
                "magicEventsTag": creator_id
            },
            headers=self.get_headers(),
            auth=AUTH
        )
        print(f"Annullamento evento response: {response.status_code} - {response.text}")

        # Test annullamento da parte di non-creator (dovrebbe fallire)
        if len(self.created_events) > 1:
            response = requests.get(
                f"{self.base_url}{self.endpoints['annull']}",
                params={
                    "eventId": self.created_events[1],
                    "magicEventsTag": self.users["participant"]["id"]
                },
                headers=self.get_headers(),
                auth=AUTH
            )
            print(f"Annullamento non autorizzato response: {response.status_code} - {response.text}")

    def test_multi_user_scenario(self) -> None:
        """Test scenario multi-utente complesso"""
        print("\n=== TEST: Scenario Multi-Utente Complesso ===")

        # Crea evento con Creator
        event_dto = self.create_test_event_dto(self.users["creator"]["id"])
        event_dto["title"] = "Evento Multi-Utente Complesso"
        event_dto["description"] = "Test completo con tutti i tipi di utenti"

        response = requests.post(
            f"{self.base_url}{self.endpoints['create']}",
            json=event_dto,
            params={"creatorEmail": self.users["creator"]["email"]},
            headers=self.get_headers(),
            auth=AUTH
        )

        if response.status_code != 200:
            print("✗ Impossibile creare evento per scenario multi-utente")
            return

        try:
            event_id = response.json()
            self.created_events.append(event_id)
            print(f"✓ Evento creato per scenario multi-utente - ID: {event_id}")
        except:
            print(f"✓ Evento creato per scenario multi-utente - Response: {response.text}")
            event_id = self.created_events[-1] if self.created_events else 1

        # Simula interazioni di più utenti
        print("--- Simulazione interazioni utenti ---")

        # Creator aggiunge nuovi admin
        new_admins = [self.users["admin"]["email"], self.users["external_admin"]["email"]]
        response = requests.get(
            f"{self.base_url}{self.endpoints['update_admins']}",
            params={
                "admins": new_admins,
                "eventId": event_id,
                "magicEventsTag": self.users["creator"]["id"]
            },
            headers=self.get_headers(),
            auth=AUTH
        )
        print(f"✓ Creator - Aggiunta admin: {response.text}")

        # Creator aggiunge partecipanti
        new_participants = [
            self.users["participant"]["email"],
            self.users["additional_user"]["email"]
        ]
        response = requests.get(
            f"{self.base_url}{self.endpoints['add_participants']}",
            params={
                "partecipants": new_participants,
                "eventId": event_id,
                "magicEventsTag": self.users["creator"]["id"]
            },
            headers=self.get_headers(),
            auth=AUTH
        )
        print(f"✓ Creator - Aggiunta partecipanti: {response.text}")

        # Recupera liste finali
        print("--- Recupero liste finali ---")

        # Lista admin
        response = requests.post(
            f"{self.base_url}{self.endpoints['get_admins']}",
            params={
                "eventId": event_id,
                "magicEventsTag": self.users["creator"]["id"]
            },
            headers=self.get_headers(),
            auth=AUTH
        )
        print(f"Lista admin finale: {response.text}")

        # Lista partecipanti
        response = requests.post(
            f"{self.base_url}{self.endpoints['get_participants']}",
            params={"eventId": event_id},
            headers=self.get_headers(),
            auth=AUTH
        )
        print(f"Lista partecipanti finale: {response.text}")

        # Verifica stato finale completo
        response = requests.post(
            f"{self.base_url}{self.endpoints['get_info']}",
            params={"eventId": event_id},
            headers=self.get_headers(),
            auth=AUTH
        )

        if response.status_code == 200:
            try:
                final_state = response.json()
                print(f"✓ Stato finale evento:")
                print(f"  Titolo: {final_state.get('title')}")
                print(f"  Admins: {final_state.get('admins')}")
                print(f"  Partecipants: {final_state.get('partecipants')}")
                image = final_state.get('image')
                if image:
                    print(f"  Immagine presente: {len(image)} caratteri base64")
                else:
                    print("  Immagine non presente nella risposta")
            except:
                print(f"✓ Stato finale evento: {response.text}")

    def run_all_tests(self) -> None:
        """Esegue tutti i test in sequenza"""
        print("🚀 INIZIO TEST SUITE - EVENTS MANAGEMENT SERVICE")
        print(f"Base URL: {self.base_url}")
        print(f"Auth: {AUTH[0]}/{'*' * len(AUTH[1])}")

        try:
            self.test_create_event()
            self.test_get_event_info()
            self.test_modify_event()
            self.test_user_management_operations()
            self.test_event_lists_retrieval()
            self.test_multi_user_scenario()
            self.test_annull_event()

            print(f"\n🏁 TEST COMPLETATI")
            print(f"Eventi creati durante i test: {len(self.created_events)}")
            if self.created_events:
                print(f"IDs eventi: {self.created_events}")

        except Exception as e:
            print(f"\n❌ ERRORE DURANTE I TEST: {str(e)}")

if __name__ == "__main__":
    tester = EventsManagementTester()
    tester.run_all_tests()
