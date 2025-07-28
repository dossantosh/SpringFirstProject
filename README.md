# SpringFirstProject

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/dossantosh/SpringFirstProject)

## 📖 Table of Contents

- [📌 Project Overview](#-project-overview)  
- [✨ Features](#-features)  
  - [👥 User & Security](#-user--security)  
  - [🌐 Web & API](#-web--api)  
  - [📦 Architecture](#-architecture)  
  - [📊 Reports & Exports](#-reports--exports)  
  - [⚙️ Dev & Ops](#️-dev--ops)  
- [🗂️ Project Structure](#️-project-structure)  
- [🛠️ Tech Stack](#️-tech-stack)  
- [🧾 Environment Configuration (.env)](#-environment-configuration-env)  
- [⚙️ Application Properties](#️-application-properties)  
- [🚀 Getting Started](#-getting-started)  
  - [Prerequisites](#prerequisites)  
  - [Setup](#setup)  
  - [Build and Run](#build-and-run)  
- [🔒 Security](#-security)  
- [🧪 Testing](#-testing)  
- [👤 Author](#-author)  
- [📬 Contact](#-contact)  
- [🙏 Acknowledgements](#-acknowledgements)  

## 📌 Project Overview

A web application for user management, news, and a perfume catalog, built with Spring Boot, Thymeleaf, and PostgreSQL. It demonstrates secure authentication, session management, and a modular architecture, supporting both server-side rendering and RESTful APIs.

Designed as a foundation for enterprise-grade web applications, it emphasizes security with fine-grained user role and permission separation managed via custom annotations, AOP, and JDBC session handling. The system ensures real-time data consistency and concurrency to prevent reader-writer conflicts, while providing comprehensive user monitoring and auditing capabilities.

## ✨ Features

### 👥 User & Security
- Secure user registration and login with CAPTCHA and email token confirmation
- Role-based access control (RBAC) using Aspects and Sessions 
- Custom permission annotations for fine-grained access control
- Session management using Spring Session JDBC
- All endpoints secured with method-level annotations (`@RequireModule`)
- Submodule custom annotations to use with thymeleaf

### 🌐 Web & API
- Thymeleaf-based dynamic HTML views
- RESTful endpoints using DTOs
- Internationalization (i18n)
- Responsive UI with Bootstrap 5

### 📦 Architecture
- Modular package structure: `user`, `pref`, `perfume`, `news` and `common` we ensure modularity by using interfaces and events to connect the different modules.
- Optimistic and pessimistic locking for data integrity with Scheduling to reduce waiting times

### 📊 Reports & Exports
- PDF and Excel report generation (OpenPDF, Apache POI)

### ⚙️ Dev & Ops
- Dotenv for dynamic configuration via `.env`
- OpenAPI documentation with SpringDoc
- Admin-only Actuator monitoring endpoints

## 🗂️ Project Structure

```
com.dossantosh.springfirstproject
├── common             # Shared components and utilities
│   ├── config           # Language Localization
│   ├── controllers      # Generic Controller
│   ├── global           # Global exception handling + preferences + Log Out event
│   └── security         # Custom spring security and filters + Session management + Authentication +  Custom roles and annotations
├── news               # News management module
├── perfume            # Product management module
│   ├── controllers      # Web controllers
│   ├── models           # Domain entities
│   ├── repositories     # Data repositories
│   ├── services         # Business logic
│   └── utils            # Utility classes: pessimistic locking, JPA specifications, Excel/PDF exports
├── preferences        # Preferences management module
├── user               # User management module
│   ├── models           # Domain entities
│   ├── repositories     # Data repositories
│   ├── services         # Business logic
│   ├── controllers      # Web controllers
│   └── utils            # Utility classes: pessimistic locking, JPA specifications, Excel/PDF exports
```

## 🛠️ Tech Stack

| Stack                     | Description                   |
|---------------------------|-------------------------------|
| **Java 21**               | Language level                |
| **Spring Boot 3.4.6**     | Framework core                |
| **Spring Web**            | REST endpoints, MVC           |
| **Spring Data JPA**       | ORM with Hibernate            |
| **Spring Security**       | Authentication & Authorization|
| **Spring Security extras**| Thymeleaf Authorization       |
| **Thymeleaf**             | Server-side rendering         |
| **Spring Session Core**   | Modify Cookies                |
| **Spring Session JDBC**   | Persistent session storage    |
| **PostgreSQL**            | Primary relational database   |
| **Lombok**                | Boilerplate reduction         |
| **AOP (Spring AOP)**      | Logging, profiling            |
| **Spring Starter Mail**   | Mail verification             |
| **Dotenv**                | Externalized configuration    |
| **OpenAPI (SpringDoc)**   | API documentation             |
| **Apache POI**            | Excel export                  |
| **OpenPDF**               | PDF export                    |
| **JUnit / Mockito**       | Testing                       |
| **H2**                    | In memory database to tests   |

## 🧾 Environment Configuration (.env)

Environment variables are loaded using [`dotenv-java`](https://github.com/cdimascio/dotenv-java):

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=springdb
DB_USER=admin
DB_PASSWORD=secret

SERVER_PORT=8080
MANAGEMENT_PORT=8081

EMAIL_HOST=smtp.example.com
EMAIL_PORT=587
EMAIL_USERNAME=your_email@example.com
EMAIL_PASSWORD=your_email_password

RECAPTCHA_SITE_KEY=your_site_key
RECAPTCHA_SECRET_KEY=your_secret_key
RECAPTCHA_VERIFY_URL=https://www.google.com/recaptcha/api/siteverify
RECAPTCHA_SCORE=0.5
RECAPTCHA_ENABLED=true
```

## ⚙️ Application Properties

Key settings in `src/main/resources/application.properties`:

- Database, JPA, and session configuration
- Thymeleaf and static resources
- Sessions management
- Sql initializer
- Actuator and management endpoints
- Email and i18n
- Captcha and security

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.8+
- PostgreSQL database

### Setup
1. **Clone the repository:**
   ```sh
   git clone https://github.com/dossantosh/SpringFirstProject.git
   cd SpringFirstProject
   ```
2. **Configure environment variables:**
   - Create a `.env` file as shown above, or set variables in your environment.
3. **Configure PostgreSQL:**
   - Create a database and user matching your `.env` or environment variables.
4. **(Optional) Demo Data:**
   - The app can load demo data from `src/main/resources/data-postgres.sql` if present.
4. **Test Data:**
   - The app can create test database from `src/test/resources/schema-h2.sql` if present.
   - The app can load test data from `src/test/resources/data-h2.sql` if present.

### Build and Run

```sh
mvn clean install
mvn spring-boot:run
```

The app will be available at [http://localhost:8083](http://localhost:8083) (or your configured port).

## 🔒 Security

- UserAuth as UserDetails to improve efficiency
- UserContextService as interface to protect UserAuth and ensure module "independence"
- Passwords are hashed with BCrypt
- CSRF protection enabled
- Captcha Validation (anti bots)
- Controllers security with AOPs aspects
- Submodule thymeleaf custom annotations
- Content Security Policy
- iFrames protections
- Strict Https
- Referrer protection

## 🧪 Testing

- Unit and integration tests with JUnit and Mockito

## 👤 Author

- Sebastián Dos Santos

## 📬 Contact

- Email: sebastiandossantosh@gmail.com
- LinkedIn: [dossantosh](https://www.linkedin.com/in/dossantosh/)
- GitHub: [dossantosh](https://github.com/dossantosh)

## 🙏 Acknowledgements

- Senior internship mentorship
- Helpful insights from articles and online discussions
