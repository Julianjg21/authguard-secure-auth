# 🛡️ AuthGuard - Secure JWT Authentication System

## 📚 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [Environment Variables](#environment-variables)
- [API Endpoints](#api-endpoints)
- [Security](#security)
- [License](#license)
- [Project Structure](#project-structure)

## 📖 Overview

AuthGuard is a secure authentication backend built using Java and Spring Boot. It implements JWT-based authentication with refresh token management, encrypted password storage, and role-based authorization.

## 🚀 Features

- User registration and login
- JWT access and refresh token generation
- Secure token storage and validation
- Role-based access control
- Password encryption using BCrypt
- Token expiration management

## 🛠️ Tech Stack

- Java 24
- Spring Boot 3
- Spring Security
- JWT (JSON Web Token)
- PostgreSQL
- Hibernate (JPA)

## ⚙️ Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/Julianjg21/authguard-secure-auth
cd authguard-secure-auth
```

### 2. Configure environment variables

Create an `.env` file or use system environment variables as shown in the next section.

### 3. Build and run the app

```bash
./mvnw spring-boot:run
```

App runs on: `http://localhost:8080`

## 🔧 Environment Variables

These variables are used for secure configuration:

```properties
# JWT Secret Key
jwt.secretKey=your_secret_key

# PostgreSQL Database
spring.datasource.url=jdbc:postgresql://localhost/authguard_db
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

## 📮 API Endpoints

### 🔐 Register

```http
POST /api/auth/register
```

**Body:**

```json
{
  "firstName": "Julian",
  "lastName": "Jimenez",
  "email": "julian@example.com",
  "password": "securepass123"
}
```

### 🔑 Login

```http
POST /api/auth/login
```

**Body:**

```json
{
  "email": "julian@example.com",
  "password": "securepass123"
}
```

### 🔄 Refresh Token

```http
POST /api/auth/refresh-token
```

**Body:**

```json
{
  "token": "your_refresh_token"
}
```

## 🔐 Security

- **Password Encryption:** All passwords are stored using BCrypt hashing.
- **JWT Token:** Access and refresh tokens are generated with secret-based signing.
- **Refresh Token Expiry:** Refresh tokens are stored in the database with a 7-day expiry.
- **Role Management:** Uses `ROLE_USER` for basic accounts; customizable per project.

## 🗂 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── Julianj/
│   │           └── authguard/
│   │               ├── config/
│   │               ├── controller/
│   │               ├── dto/
│   │               ├── entity/
│   │               ├── repository/
│   │               ├── security/
│   │               ├── service/
│   │               ├── exception/
│   │               └── AuthGuardApplication.java
│   └── resources/
│       ├── application.properties
│       └── static/
└── test/
    └── java/
        └── com/
            └── Julianj/
                └── authguard/
```

## 📝 License

This project is licensed under the MIT License.

---

> Developed by Julian Jimenez💎
