# STLabs Railway Portal

A full-stack railway booking application built as a hands-on software testing lab. The project includes a React/Vite frontend, Java 11 + Spring Boot REST API, MySQL persistence, OpenAPI/Swagger documentation, sample railway data, student exercises, and an instructor-only guide to 40 seeded defects.

> **Lab warning:** The application intentionally contains defects. Use it only as a local learning environment; it is not suitable for real reservations, payments, or personal data.

The passenger-train hero photograph is by [Harshul12345](https://commons.wikimedia.org/wiki/User:Harshul12345), sourced from [Wikimedia Commons](https://commons.wikimedia.org/wiki/File:Howrah_Puri_Vande_Bharat_Express_standing_at_Bhadrak.jpg) under [CC BY-SA 4.0](https://creativecommons.org/licenses/by-sa/4.0/).

## What’s included

- Search direct trains for all 132 ordered origin/destination combinations between the 12 timetable stations (a station cannot be both origin and destination), with daily availability seeded for the next 30 days.
- Create an account and sign in with a server-side session and CSRF protection.
- Search one-way or round-trip journeys, add up to six named passengers, and book/cancel a trip.
- See a dashboard and booking history.
- Add optional travel insurance during checkout for ₹1 per passenger; the training-only policy provides up to ₹2 lakh accidental cover per passenger and displays a unique policy number.
- Browse synthetic destination-city hotels for train bookings, reserve stays on selected dates, and view hotel confirmation details.
- Spring Boot REST API with MySQL storage and demo seed data.
- Swagger UI at `/swagger-ui.html`.
- Forty intentionally seeded, documented testing exercises: [`docs/STUDENT_EXERCISES.md`](docs/STUDENT_EXERCISES.md) and [`docs/INSTRUCTOR_ANSWER_KEY.md`](docs/INSTRUCTOR_ANSWER_KEY.md).
- API reference: [`docs/API.md`](docs/API.md).

## Requirements

- Node.js 20+ and npm
- Java 11+
- Maven 3.6+
- MySQL 8+, or Docker Desktop to run the included MySQL service

## Run locally

### Quick local run (no MySQL installation required)

The local profile uses an in-memory H2 database in MySQL compatibility mode. It is convenient for trying the application, but its data is discarded when the backend stops.

1. Start the API from the `backend` directory:

   ```powershell
   cd backend
   mvn spring-boot:run "-Dspring-boot.run.profiles=local"
   ```

2. In a second terminal, start the frontend from the `frontend` directory:

   ```powershell
   cd frontend
   npm install
   npm run dev -- --host 127.0.0.1
   ```

3. Open `http://localhost:5173`. The API listens on `http://localhost:8080`; visit `http://localhost:8080/swagger-ui.html` for the API docs.

### Run with MySQL (persistent database)

1. Start MySQL. With Docker Compose:

   ```powershell
   docker compose up -d mysql
   ```

   This creates database `railway` and user/password `railway` on port `3306`. To use an existing MySQL installation, create that database/user or set `DB_URL`, `DB_USER`, and `DB_PASSWORD` for the backend.

2. Start the API from the `backend` directory (default profile uses MySQL):

   ```powershell
   cd backend
   mvn spring-boot:run
   ```

   The API listens on `http://localhost:8080`. Startup creates tables, adds any missing sample trains, ensures at least one direct train for every distinct origin/destination station pair, and seeds date-specific availability for the next 30 calendar days.

3. In a second terminal, start the frontend from the `frontend` directory:

   ```powershell
   cd frontend
   npm install
   npm run dev
   ```

   Open the Vite URL printed in the terminal (usually `http://localhost:5173`). Vite proxies `/api` requests to port `8080`.

4. Visit `http://localhost:8080/swagger-ui.html` to explore the API, or check `http://localhost:8080/api/health`.

### Backend environment variables

| Variable | Default | Description |
|---|---|---|
| `SERVER_PORT` | `8080` | API port |
| `DB_URL` | `jdbc:mysql://localhost:3306/railway?...` | JDBC URL |
| `DB_USER` | `railway` | Database user |
| `DB_PASSWORD` | `railway` | Database password |

For a separately hosted frontend, set `VITE_API_BASE_URL` to the backend origin before building. Configure CORS at the backend/reverse proxy when frontend and API are on different origins.

## Verify

```powershell
cd frontend
npm install
npm run build

cd ..\backend
mvn test
```

Backend tests use an in-memory H2 database in MySQL compatibility mode; the running application uses MySQL.

## Documentation

- [Student exercises](docs/STUDENT_EXERCISES.md)
- [Instructor answer key — do not distribute with student materials](docs/INSTRUCTOR_ANSWER_KEY.md)
- [API documentation](docs/API.md)
