# Music Tracker Frontend

A **React** single-page application that consumes the **Music Tracker Backend** API. It lets users sign in with HTTP Basic credentials, **register** music tracks by **ISRC** (fetching from the backend/Spotify), **look up** stored track metadata, and view track details with cover images.

---

## Features

- **Login** – HTTP Basic authentication; credentials are stored in Redux and sent with all API requests
- **Register track by ISRC** – Submits ISRC to the backend; backend fetches from Spotify and persists
- **Look up track by ISRC** – Fetches stored track metadata from the backend
- **Track detail page** – Displays metadata and cover image for a track by ISRC
- **Responsive UI** – Radix UI components, Tailwind CSS, dark/light theme support
- **Structured error handling** – Displays API error details (e.g. RFC 7807 Problem Details) to the user

---

## Tech Stack

| Layer           | Technology                    |
| --------------- | ----------------------------- |
| Runtime         | Node.js 18+                  |
| Framework       | React 19                      |
| Build / Dev     | Vite 7                        |
| Language        | TypeScript 5.9                |
| State / API     | Redux Toolkit, RTK Query     |
| Routing         | React Router 7               |
| Styling         | Tailwind CSS 4, Radix Themes |
| Icons           | Lucide React                 |
| Testing         | Vitest, Testing Library, jsdom |

---

## Prerequisites

- **Node.js** 18+ (or use a version manager such as fnm/nvm)
- **pnpm** (or npm/yarn)
- **Music Tracker Backend** running (see [music-tracker-backend](../music-tracker-backend/README.md)) – default base URL: `http://localhost:8080/tracker`

---

## Quick Start

### 1. Clone and install

```bash
git clone https://github.com/ferneybaron/full-stack-code-challenge.git
cd full-stack-code-challenge/music-tracker-frontend
pnpm install
```

### 2. Backend API URL (optional)

By default the app uses `http://localhost:8080/tracker`. To point to another backend, create a `.env` file:

```bash
# .env
VITE_BACKEND_API=http://localhost:8080/tracker
```

### 3. Run

```bash
pnpm dev
```

The app is available at **http://localhost:5173**. Sign in with the same credentials used for the backend API (default: `admin` / `admin`).

---

## Configuration

Configuration is driven by environment variables. Variables must be prefixed with `VITE_` to be exposed to the client.

| Variable            | Description                          | Default                        |
| ------------------- | ------------------------------------ | ------------------------------ |
| `VITE_BACKEND_API`  | Base URL of the Music Tracker API    | `http://localhost:8080/tracker`|

The app calls:

- `{VITE_BACKEND_API}/api/v1/tracks` – register and fetch tracks, cover image

All requests use **HTTP Basic** authentication with the credentials entered on the login page.

---

## Project Structure

```
src/
├── main.tsx                 # Entry: React root, Redux Provider, ThemeProvider
├── App.tsx                  # Routes: / (login), /tracks, /tracks/register, /tracks/:id
├── app/
│   ├── config/              # Redux store, auth slice, ProtectedRoutes, theme
│   ├── login/               # LoginPage (form, Basic auth)
│   └── tracks/              # Track feature
│       ├── TrackPage.tsx    # Tabs: Register track / Look up track
│       ├── form/            # TrackTab (search form, results)
│       ├── details/         # TrackDetail (metadata + cover)
│       ├── reducer/         # trackSlice
│       ├── service/         # trackService (RTK Query API)
│       └── model/           # Track type
└── shared/
    └── components/          # UI: cards, buttons, inputs, labels, tabs, search, lists, errors, skeletons
```

---

## Testing

```bash
pnpm test        # watch mode
pnpm test:run    # single run (CI)
```

- **Vitest** with **jsdom** and **React Testing Library**
- Setup: `src/test/setup.ts`
- Tests live next to source: `*.test.ts` / `*.test.tsx`

---

## Building for production

```bash
pnpm build
```

Output is in `dist/`. To preview the production build locally:

```bash
pnpm preview
```

Serve `dist/` with any static host. Ensure `VITE_BACKEND_API` is set at build time if you need a non-default API URL.

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
This project is licensed under the MIT License - see the [LICENSE](../LICENSE) file for details.
