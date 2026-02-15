# Music Tracker Backend

A **Spring Boot** REST API that fetches music track metadata from **Spotify** by **ISRC** (International Standard Recording Code), stores it in **PostgreSQL**, and serves track data and cover images.

---

## Features

- **Register track by ISRC** – Fetches metadata and cover from Spotify, persists to DB and local storage
- **Get track metadata** – Returns stored track info by ISRC
- **Get cover image** – Returns the track cover image (JPEG) by ISRC
- **HTTP Basic authentication** – API secured with username and password
- **OpenAPI / Swagger UI** – Interactive API docs at `/tracker/swagger-ui.html`
- **Health endpoint** – Actuator health for containers and orchestration (unauthenticated)
- **Structured error responses** – RFC 7807 Problem Details for all error cases

---

## Tech Stack

| Layer        | Technology                          |
| ------------ | ----------------------------------- |
| Runtime      | Java 21                             |
| Framework    | Spring Boot 3.5                     |
| Build        | Gradle                              |
| API          | Spring Web MVC, SpringDoc OpenAPI   |
| Persistence  | Spring Data JPA, PostgreSQL, Flyway |
| External API | Spotify Web API (RestClient)       |
| Security     | Spring Security (HTTP Basic)        |
| Validation   | Bean Validation                     |
| Mapping      | MapStruct, Lombok                   |

---

## Prerequisites

- **JDK 21**
- **PostgreSQL** (or Docker)
- **Spotify Developer** credentials ([Dashboard](https://developer.spotify.com/dashboard) → Create App → Client ID & Client Secret)

---

## Quick Start

### 1. Clone and build

```bash
git clone https://github.com/ferneybaron/full-stack-code-challenge.git
cd full-stack-code-challenge/music-tracker-backend
./gradlew build
```

### 2. Database

Create the database (if not using Docker):

```bash
createdb trackerdb
```

Default connection: `localhost:5432`, database `trackerdb`, user `postgres`, password `postgres`. Override via environment variables (see [Configuration](#configuration)).

### 3. Spotify credentials

Set your Spotify app credentials:

```bash
export SPOTIFY_CLIENT_ID=your_client_id
export SPOTIFY_CLIENT_SECRET=your_client_secret
```

### 4. API credentials (optional)

Default username and password are `admin` / `admin`. Override with env vars (see [Configuration](#configuration)).

### 5. Run

```bash
./gradlew bootRun
```

The API is available at **http://localhost:8080/tracker** (context path `/tracker`). All API endpoints require HTTP Basic authentication.

---

## Configuration

Configuration is driven by `application.yaml` and environment variables.

| Variable              | Description                    | Default                    |
| --------------------- | ------------------------------ | -------------------------- |
| `SERVER_PORT`         | Server port                    | `8080`                     |
| `DATABASE_HOST`       | PostgreSQL host                | `127.0.0.1`                |
| `DATABASE_PORT`       | PostgreSQL port                | `5432`                     |
| `DATABASE_NAME`       | Database name                  | `trackerdb`                |
| `DATABASE_USERNAME`   | DB user                        | `postgres`                 |
| `DATABASE_PASSWORD`   | DB password                    | `postgres`                 |
| `SPOTIFY_CLIENT_ID`   | Spotify app client ID          | *(required)*               |
| `SPOTIFY_CLIENT_SECRET` | Spotify app client secret   | *(required)*               |
| `API_USERNAME`       | HTTP Basic username for API    | `admin`              |
| `API_PASSWORD`       | HTTP Basic password for API    | `admin`           |
| `STORAGE_LOCATION`   | Directory for cover images     | `./local-storage/covers`   |
| `DOCS_ENABLED`       | Enable Swagger UI & api-docs   | `true`                     |
| `ENVIRONMENT`        | Active Spring profile          | `local`                    |

---

## API Overview

Base path: **`/tracker/api/v1/tracks`**

| Method | Path              | Description                              |
| ------ | ----------------- | ---------------------------------------- |
| POST   | `/`               | Register a track by ISRC (body: `{"isrCode": "USMC18620549"}`). Returns 201 if created, 200 if already exists. |
| GET    | `/{isrCode}`      | Get stored track metadata by ISRC        |
| GET    | `/{isrCode}/cover`| Get track cover image (JPEG) by ISRC     |

### Example: Register and fetch a track

All requests require HTTP Basic auth. Use `-u username:password` (default: `admin` / `admin`).

```bash
# Register (fetches from Spotify and stores)
curl -u admin:admin -X POST http://localhost:8080/tracker/api/v1/tracks \
  -H "Content-Type: application/json" \
  -d '{"isrCode": "USMC18620549"}'

# Get metadata
curl -u admin:admin http://localhost:8080/tracker/api/v1/tracks/USMC18620549

# Get cover image
curl -u admin:admin -o cover.jpg http://localhost:8080/tracker/api/v1/tracks/USMC18620549/cover
```

Without valid credentials, the API returns **401 Unauthorized**.

### Documentation

- **Swagger UI:** http://localhost:8080/tracker/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/tracker/api-docs

### Health

- **Actuator health:** http://localhost:8080/tracker/actuator/health

---

## Project Structure

```
src/main/java/com/fbaron/tracker/
├── Application.java
├── core/                    # Domain & application logic
│   ├── model/               # Track, RegistrationResult
│   ├── usecase/             # RegisterTrack, GetTrack, GetCover
│   ├── repository/          # Port interfaces
│   ├── service/             # TrackService (use case implementation)
│   └── exception/           # Domain exceptions
├── data/                    # Adapters (infrastructure)
│   ├── config/              # Beans (TrackService, RestClient)
│   ├── jpa/                 # JPA entity, repository, adapter
│   ├── spotify/             # Spotify API client, DTOs, mapper
│   └── storage/             # File storage for cover images
└── web/                     # REST API
    ├── rest/                # Controller, API interface
    ├── dto/                 # Request/response DTOs
    ├── mapper/              # TrackDtoMapper
    ├── exception/           # GlobalExceptionHandler
    └── config/              # OpenAPI config (optional)
```

The design follows **hexagonal architecture** (ports & adapters): the **core** defines use cases and ports; **data** and **web** provide adapters.

---

## Testing

```bash
./gradlew test
```

- **H2** is used for integration tests (`testImplementation`) so no running PostgreSQL is required. Test config is in `src/test/resources/application.yaml`.
- **Spotify** is not called in tests: in integration tests we **mock** `MusicProviderRepository` (`@MockBean`), so the full flow (REST → service → JPA → file storage) runs with canned track and cover data. Real Spotify is only used when running the app manually.

---

## Security

- **Spotify credentials:** Never log `SPOTIFY_CLIENT_ID`, `SPOTIFY_CLIENT_SECRET`, the Spotify access token, or the token response. The adapter is written to avoid logging any of these.
- **API credentials:** Use env vars (`API_USERNAME`, `API_PASSWORD`) in production; do not log them.
- **Logging:** Avoid enabling DEBUG for `org.springframework.web.client` in production, as it may log request/response headers (including `Authorization`).

---

## Building for production

```bash
./gradlew bootJar
```

The runnable JAR is in `build/libs/`. Run with:

```bash
java -jar build/libs/music-track-backend-0.0.1-SNAPSHOT.jar
```

Set `DOCS_ENABLED=false` in production if you want to disable Swagger UI and api-docs.

---

## Step-by-step tutorial

To build this project from scratch, check out:
- **[My Blog](https://blog.fbaron.com)** – Full written tutorial (coming soon)
- **[My YouTube Channel](https://youtube.com/@_fbaron)** – Video walkthrough (coming soon)

---

## 👨‍💻 Developer Information
* **Name:** Ferney Estupiñán Barón
* **Role:** Senior Full Stack Engineer
* **Email:** [ferney.estupinanb@gmail.com]
* **GitHub:** [github.com/ferneybaron](https://github.com/ferneybaron)
* **LinkedIn:** [linkedin.com/in/ferney-estupinan-baron](https://www.linkedin.com/in/ferney-estupinan-baron)

## 📄 License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
