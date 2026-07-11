# VaultNote

A secure, JWT-authenticated REST API for private note-taking, built with Spring Boot.

Users sign up, log in, and get a JSON Web Token. Every note is scoped to its owner — no one can view, edit, or delete a note that isn't theirs, enforced at the API level.

This project was built to demonstrate core backend fundamentals: REST API design, database persistence, password security, and token-based authentication — end to end, without shortcuts.

## Features

- User signup and login
- Passwords hashed with BCrypt (never stored in plain text)
- JWT-based authentication — no server-side sessions
- Full notes CRUD (create, read, update, delete)
- Notes are private: every request is scoped to the logged-in user, verified via their token

## Tech Stack

- **Java 21**
- **Spring Boot 4** (Web, Security, Data JPA)
- **PostgreSQL**
- **JWT** via `jjwt`
- **Maven**

## Getting Started

### Prerequisites

- Java 21+
- Maven (or use the included `mvnw` wrapper — no separate install needed)
- PostgreSQL running locally

### 1. Clone the repo

```bash
git clone https://github.com/YOUR_USERNAME/vaultnote.git
cd vaultnote
```

### 2. Create the database

```sql
CREATE DATABASE vaultnote;
```

### 3. Configure environment variables

Create a `.env` file in the project root:

```
DB_USERNAME=your_postgres_username
DB_PASSWORD=your_postgres_password
JWT_SECRET=a_long_random_string_at_least_32_characters
```

`.env` is gitignored — never commit real credentials.

### 4. Run the app

```bash
./run.sh
```

This loads the environment variables from `.env` and starts the app on `http://localhost:8080`.

## API Reference

### Auth

| Method | Endpoint | Body | Description |
|---|---|---|---|
| POST | `/auth/signup` | `{ "username", "password" }` | Create a new account |
| POST | `/auth/login` | `{ "username", "password" }` | Returns a JWT token |

### Notes

All notes endpoints require an `Authorization: Bearer <token>` header.

| Method | Endpoint | Body | Description |
|---|---|---|---|
| POST | `/notes` | `{ "title", "content" }` | Create a note |
| GET | `/notes` | — | List your notes |
| PUT | `/notes/{id}` | `{ "title", "content" }` | Update your note |
| DELETE | `/notes/{id}` | — | Delete your note |

### Example

```bash
# Sign up
curl -X POST http://localhost:8080/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"1234"}'

# Log in and get a token
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"1234"}'

# Create a note (replace TOKEN with the value returned above)
curl -X POST http://localhost:8080/notes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"title":"First note","content":"Hello world"}'
```

## How Auth Works

1. User signs up — their password is hashed with BCrypt before it's stored.
2. User logs in — if the password matches the hash, the server issues a signed JWT containing their username and an expiry time.
3. On every subsequent request, the client sends that token in the `Authorization` header.
4. A security filter verifies the token's signature on each request and identifies the user — no database lookup or password required after login.
5. Every notes endpoint uses that verified identity to filter or restrict data, so users can only ever see or modify their own notes.

## Project Status

This is the base version of VaultNote — a complete, working implementation of auth and notes CRUD. Planned future work (in a v2 branch/fork) may include things like refresh tokens, pagination, note sharing, and automated tests.