# Spring Spotify Stats — Full-Stack Analytics Application

## 1. Project Overview

I built this full-stack **Spotify Wrapped - like** application to provide personal listening analytics on demand, without having to wait for the annual December recap. Featuring a clean, Spotify-inspired dark-mode interface, the platform connects directly to the Spotify Web API to deliver musical insights while giving users full transparency and control over their stored data.

Under the hood, I designed a multi-tier architecture featuring a **Java & Spring Boot** REST API and a **React** single-page frontend. The application is fully containerized with **Docker**, covered by automated integration test suites (**Testcontainers** and **Vitest**), and is using a **GitHub Actions CI/CD** pipeline.

### Key Features:
* **Spotify OAuth2 Authentication:** Fast and secure login using your genuine Spotify account.
* **Listening Analytics:** Daily aggregated summaries of your Top Tracks, Top Artists, calculated Top Genres, and live "Currently Playing" status.
* **Playback History Sync:** Automatic synchronization and chronological browsing of your recent playback activity.
* **Privacy Management:** User-driven data controls allowing you to request the deletion of listening records older than a selected number of days, or withdraw pending requests at any time.
* **Dedicated Admin Dashboard:** A role-protected management portal to monitor registered accounts, review and process deletion requests, and perform database retention cleanups.

---

## 2. Architecture & Technology Stack

### Backend
* **Language & Platform:** Java 25
* **Framework:** Spring Boot 3.5.0
* **API Layer:** Spring Web MVC (**REST API**, JSON serialization, CORS configuration)
* **Security & Auth:** Spring Security configured with two authentication paths:
    * **OAuth2 Client:** Spotify Authorization Code with token clients and session-based state protection.
    * **Form Login:** custom administrative portal authenticating against database records, hashed with `BCryptPasswordEncoder`.
* **Polyglot Persistence:**
    * **Hibernate** & **Spring Data JPA:** Relational storage in **PostgreSQL** for user accounts, administrative credentials, and deletion tickets.
    * **Spring Data MongoDB:** Document storage in **MongoDB** for playback logs and daily aggregated stats.
* **Build System:** Apache Maven

### Frontend
* **UI Library:** React 19 (Single Page Application)
* **Build Tool & Server:** Vite with `@vitejs/plugin-react`
* **Routing:** React Router DOM 
* **Styling:** Tailwind CSS 
* **Icons:** Lucide React

### Production Server & Deployment
* **Nginx (Alpine):** Multi-stage Docker build serving optimized static production assets 
* **Docker & Docker Compose:** Orchestration of four connected services

### Testing & Quality Assurance
* **Backend Testing:** JUnit 5 (Jupiter), Mockito, and **Testcontainers**.
* **Frontend Testing:** Vitest, React Testing Library, and `jsdom`.
* **CI/CD:** Automated GitHub Actions pipeline triggered on pushes and pull requests to `main`/`master`, running complete test suites for both services and verifying production builds.

---

## 3. Environment Configuration (`.env`)

Before starting the app, create a `.env` file in the project root directory. Both Docker Compose and Spring Boot read their secrets from this file:

```properties
# PostgreSQL Configuration
POSTGRES_USER=***
POSTGRES_PASSWORD=***
POSTGRES_DB=***

# MongoDB Configuration
MONGO_USER=***
MONGO_PASSWORD=***

# Spotify Developer API Credentials
SPOTIFY_CLIENT_ID=***
SPOTIFY_CLIENT_SECRET=***

# Application Admin Credentials (Hashed via BCrypt on startup)
ADMIN_DEFAULT_USERNAME=admin
ADMIN_DEFAULT_PASSWORD=***
```

> **Important:** In your Spotify Developer Dashboard, ensure your Redirect URI is set to:  
> `http://127.0.0.1:8080/login/oauth2/code/spotify`

---

## 4. Running the Application Locally (Development Mode)

### Prerequisites
* **Java Development Kit (JDK):** Version 25
* **Node.js:** Version 20 or 22 (LTS) & `npm`
* **Docker:** Required for running local database instances

### Step 1: Start the Local Databases
Launch isolated instances of PostgreSQL and MongoDB using Docker:

```bash
docker run -d --name local-postgres -p 5432:5432 -e POSTGRES_USER=*** -e POSTGRES_PASSWORD=*** -e POSTGRES_DB=*** postgres:16
```

```bash
docker run -d --name local-mongo -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=*** -e MONGO_INITDB_ROOT_PASSWORD=*** mongo:7.0
```

### Step 2: Run the Backend (e.x. IntelliJ IDEA)
1. Open the project root or `spotify-backend` directory in **IntelliJ IDEA**.
2. Make sure your project SDK is set to **Java 25**.
3. Run `SpotifyWrappedApplication.java`.
    * The backend REST API will start on `http://127.0.0.1:8080`.
    * On first boot, the `DataInitializer` component automatically creates your admin account in PostgreSQL using the credentials from `.env`.

### Step 3: Run the Frontend (React + Vite)
1. Open a new terminal window in the `spotify-frontend` directory:
   ```bash
   cd spotify-frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm run dev
   ```
4. Open your browser at `http://127.0.0.1:5173`.

---

## 5. Running with Docker Compose (Production Setup)

You can launch the complete full-stack environment inside Docker with a single command:

1. Make sure your `.env` file is present in the directory containing `docker-compose.yml`.
2. Build and start all containers in detached mode:
   ```bash
   docker compose up --build -d
   ```
3. **Containers Running:**
    * `postgres-db`: PostgreSQL 16 on port `5432`
    * `mongo-db`: MongoDB 7.0 on port `27017`
    * `spotify_backend`: Spring Boot JAR on port `8080`
    * `spotify_frontend`: Nginx serving static assets on port `5173`

4. Access the application in your browser at `http://127.0.0.1:5173`.

To stop and remove all running containers:
```bash
docker compose down
```

---

## 6. Testing & CI/CD Pipelines

### Running Backend Tests
Integration tests spin up lightweight, disappearing databases using Testcontainers:
```bash
cd spotify-backend
mvn clean verify
```

### Running Frontend Tests
Run unit and integration component tests in a browser-like environment:
```bash
cd spotify-frontend
npx vitest run
```

### CI/CD Workflow
The automated GitHub Actions workflow (`.github/workflows/ci.yml`) runs on every push and pull request to `main`:
1. **Backend Job:** Configures JDK 25, runs `mvn clean verify`, and confirms all Testcontainers database suites pass.
2. **Frontend Job:** Sets up Node.js 22, installs dependencies via `npm ci`, executes Vitest, and confirms successful production compilation via `npm run build`.