# User-management service

## Architettura della Sicurezza

Il sistema implementa un'autenticazione basata su Bearer Token con Spring Security. Ecco come funziona:

### 1. Configurazione della Sicurezza (SecurityConfig)

- CORS: Configurato per permettere richieste da domini specifici
- Whitelist: URLs pubbliche che non richiedono autenticazione 
- Filtro personalizzato

### 2. Flusso di Autenticazione

Request → BearerTokenAuthFilter → BearerTokenAuthProvider → Database Validation

#### BearerTokenAuthFilter
- Intercetta ogni richiesta HTTP
- Estrae il token dal header Authorization: Bearer `<token>`
- Crea un oggetto BearerTokenAuth e lo passa all'AuthenticationManager

#### BearerTokenAuth
- Classe semplice che implementa Authentication
- Contiene solo il token Bearer come "name"
- Non è autenticato di default (isAuthenticated() = false)

#### BearerTokenAuthProvider
- Valida il token consultando la tabella token nel database
- Verifica che il token esista e non sia scaduto (expirationTime)
- Se valido, restituisce un UsernamePasswordAuthenticationToken con email e authorities dell'utente

### 3. Modello dei Token

#### OauthToken
- accessToken: Token principale per l'autenticazione
- refreshToken: Token per rinnovare l'access token
- user: Riferimento all'utente proprietario
- expirationTime: Scadenza del token

### 4. Gestione degli Utenti

#### User
- Campi: magicevents tag, username, email, name, surname, password immagine profilo
- magicEventTag: Id univoco dell'utente

### 5. Sicurezza delle Password

#### ResetPasswordToken
- Token temporaneo per reset password
- Collegato all'utente e con scadenza

### 6. Punti di Forza e Debolezze

Punti di Forza:
- Separazione chiara tra token di accesso e refresh
- Validazione della scadenza dei token
- CORS configurato correttamente
- Whitelist per endpoint pubblici

Possibili Miglioramenti:
- Manca crittografia/hashing delle password
- Non c'è revoca automatica dei token scaduti
