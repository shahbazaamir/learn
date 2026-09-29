# Chat System

- WebSocket backend using Spring Boot STOMP over SockJS.
- React frontend subscribing to /topic/messages.
- One-to-one messaging can be implemented by filtering messages by from and to.
- No authentication, no persistence (for simplicity).

## Using React js as Frontend

## Using Spring boot as Backend

## Running the app

### 1. Start the backend
Open a terminal in the Spring Boot project folder and run:

```bash
./mvnw spring-boot:run
```

If Maven is installed globally:

```bash
mvn spring-boot:run
```

The backend will start on:

```text
http://localhost:8246
```

---

### 2. Start the frontend
Open a second terminal in the React project folder and install dependencies if needed:

```bash
npm install
```

Then start the app:

```bash
npm start
```

The frontend usually runs on:

```text
http://localhost:3000
```

---

### 3. Open the app
Visit:

```text
http://localhost:3000
```

Use the chat UI to send and receive messages.

---

### 4. WebSocket details
The app uses:
- Spring Boot WebSocket
- STOMP over SockJS
- frontend subscription to `/topic/messages`

If you want one-to-one messaging, you can extend the message payload with `from` and `to` fields and filter on the client side.

---

### 5. Troubleshooting
- If the frontend cannot connect, confirm the backend is running on port 8080.
- If the browser shows a WebSocket connection error, verify the STOMP endpoint and topic names match the backend configuration.
- If `npm start` fails, ensure Node.js and npm are installed.