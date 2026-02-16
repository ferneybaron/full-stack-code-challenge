# Full Stack Code Challenge

Full-stack app for requesting **metadata of a music track** from external streaming services. The backend fetches track data from **Spotify** by **ISRC**, stores it in **PostgreSQL**, and serves it via a REST API; the frontend is a **React** SPA to register and look up tracks.

| App | Description |
|-----|-------------|
| [music-tracker-backend](music-tracker-backend/README.md) | Spring Boot REST API (Spotify, PostgreSQL, HTTP Basic) |
| [music-tracker-frontend](music-tracker-frontend/README.md) | React + Vite SPA (login, register/lookup by ISRC, track details) |

---

## Run locally

Run **backend** first, then **frontend**. The frontend talks to the backend at `http://localhost:8080/tracker`.

### Prerequisites

- **JDK 21** (backend)
- **Node.js** 18+ and **pnpm** (frontend)
- **PostgreSQL** (or Docker)
- **Spotify** app credentials: [Dashboard](https://developer.spotify.com/dashboard) → Create App → Client ID & Client Secret

### 1. Clone

```bash
git clone https://github.com/ferneybaron/full-stack-code-challenge.git
cd full-stack-code-challenge
```

### 2. Backend

Create the database and set Spotify credentials, then run the API:

```bash
# Create DB (if not using Docker)
createdb trackerdb

# Spotify credentials (required)
export SPOTIFY_CLIENT_ID=your_client_id
export SPOTIFY_CLIENT_SECRET=your_client_secret

# Optional: API auth (default admin / admin)
# export API_USERNAME=admin
# export API_PASSWORD=admin

cd music-tracker-backend
./gradlew build
./gradlew bootRun
```

Backend runs at **http://localhost:8080/tracker** (Swagger: http://localhost:8080/tracker/swagger-ui.html). Default DB: `localhost:5432`, database `trackerdb`, user `postgres`, password `postgres`.

### 3. Frontend

In a new terminal, from the repo root:

```bash
cd music-tracker-frontend
pnpm install
pnpm dev
```

Frontend runs at **http://localhost:5173**. Sign in with the same API credentials (default `admin` / `admin`). By default it uses `http://localhost:8080/tracker` as the backend URL; override with a `.env` file and `VITE_BACKEND_API=...` if needed.

### Summary

| Service   | URL                          | Credentials   |
|----------|------------------------------|---------------|
| Backend  | http://localhost:8080/tracker | `admin` / `admin` (API Basic) |
| Frontend | http://localhost:5173        | Same as backend (login)       |

For more options (ports, env vars, tests, production build), see [music-tracker-backend/README.md](music-tracker-backend/README.md) and [music-tracker-frontend/README.md](music-tracker-frontend/README.md).

---

## 👨‍💻 Developer Information
* **Name:** Ferney Estupiñán Barón
* **Role:** Senior Full Stack Engineer
* **Email:** [ferney.estupinanb@gmail.com]
* **GitHub:** [github.com/ferneybaron](https://github.com/ferneybaron)
* **LinkedIn:** [linkedin.com/in/ferney-estupinan-baron](https://www.linkedin.com/in/ferney-estupinan-baron)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
