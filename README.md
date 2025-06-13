# SpringFirstProject

## 📌 Project Overview

A web application for user management, news, and perfume catalog, built with Spring Boot, Thymeleaf, and PostgreSQL. This project showcases secure authentication, session management, and modular architecture, supporting both server-side rendering and RESTful APIs for modern frontends. 

This project serves as a foundation for developing enterprise-grade web applications, focusing on security and fine-grained user role/permission separation, Controlled via custom permission roles, modules, and submodules through custom annotations and JDBC session management.

---

## ✨ Features

# 👥 User & Security
- Secure user registration and login with CAPTCHA and email token confirmation
- Role-based access control (RBAC) with modules and submodules
- Custom permission annotations for fine-grained access control
- Session management using Spring Session JDBC
# 🌐 Web & API
- Thymeleaf-based dynamic HTML views
- RESTful endpoints using DTOs
- Internationalization (i18n)
# 📦 Architecture
- Modular package structure: `user`, `perfume`, `news`, `common`
- Optimistic and pessimistic locking for data integrity
# 📊 Reports & Exports
- PDF and Excel report generation (OpenPDF, Apache POI)
# ⚙️ Dev & Ops
- Dotenv for dynamic configuration via `.env`
- OpenAPI documentation with SpringDoc
- Admin-only Actuator monitoring endpoints
# 👥 User & Security
- Secure user registration and login with CAPTCHA and email token confirmation
- Role-based access control (RBAC) with modules and submodules
- Custom permission annotations for fine-grained access control
- Session management using Spring Session JDBC
- All endpoints secured with method-level annotations (`@RequiereModule`)
- Submodule custom annotations to use with thymeleaf 

### 🌐 Web & API
- Thymeleaf-based dynamic HTML views
- RESTful endpoints using DTOs
- Internationalization (i18n)
- Responsive UI with Bootstrap 5

### 📦 Architecture
- Modular package structure: `user`, `perfume`, `news`, `common`
- Optimistic and pessimistic locking for data integrity

### 📊 Reports & Exports
- PDF and Excel report generation (OpenPDF, Apache POI)

### ⚙️ Dev & Ops
- Dotenv for dynamic configuration via `.env`
- OpenAPI documentation with SpringDoc
- Admin-only Actuator monitoring endpoints

---

## 🗂️ Project Structure

```
com.dossantosh.springfirstproject
├── common             # Shared components and utilities
│   ├── config           # Language Localization
│   ├── controllers      # Generic Controller
│   ├── global           # Global exception handling + global preferences
│   └── security         # Security and filters + Session management + Custom annotations
├── news               # News management module
├── perfume            # Product management module
│   ├── controllers      # Web controllers
│   ├── models           # Domain entities
│   ├── repositories     # Data repositories
│   ├── services         # Business logic
│   └── utils            # Utility classes: pessimistic locking, JPA specifications, Excel/PDF exports
├── user               # User management module
│   ├── models           # Domain entities
│   ├── repositories     # Data repositories
│   ├── services         # Business logic
│   ├── controllers      # Web controllers
│   └── utils            # Utility classes: pessimistic locking, JPA specifications, Excel/PDF exports
```

---

## 🛠️ Tech Stack

| Stack                | Description                  |
|----------------------|------------------------------|
| **Java 21**          | Language level               |
| **Spring Boot 3.4.6**| Framework core               |
| **Spring Web**       | REST endpoints, MVC          |
| **Spring Data JPA**  | ORM with Hibernate           |
| **Spring Security**  | Authentication & Authorization|
| **Thymeleaf**        | Server-side rendering        |
| **Spring Session JDBC** | Persistent session storage |
| **PostgreSQL**       | Primary relational database  |
| **Lombok**           | Boilerplate reduction        |
| **AOP (AspectJ)**    | Logging, profiling           |
| **Dotenv**           | Externalized configuration   |
| **OpenAPI (SpringDoc)** | API documentation         |
| **Apache POI**       | Excel export                 |
| **OpenPDF**          | PDF export                   |
| **JUnit / Mockito**  | Testing                      |

---

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

---

## ⚙️ Application Properties

Key settings in `src/main/resources/application.properties`:

- Database, JPA, and session configuration
- Thymeleaf and static resources
- Actuator and management endpoints
- Email and i18n
- Captcha and security

---

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
   - The app can load demo data from `src/main/resources/data.sql` if present.

### Build and Run

```sh
mvn clean install
mvn spring-boot:run
```

The app will be available at [http://localhost:8083](http://localhost:8083) (or your configured port).

---

## 🔒 Security

- Passwords are hashed with BCrypt
- Session management via Spring Session JDBC
- CSRF protection enabled
- Role-based access for all endpoints
- Custom permission checks via annotations and utility classes which allows me to secure controllers

---

## 🧪 Testing

- Unit and integration tests with JUnit and Mockito

---


## 👤 Author

- Sebastián Dos Santos

---

## 📬 Contact

- Email: sebastiandossantosh@gmail.com
- Linkeding: [dossantosh](linkedin.com/in/dossantosh)
- GitHub: [dossantosh](https://github.com/dossantosh)

---

## 🙏 Acknowledgements

- Senior internship mentorship
- Helpful insights from Reddit discussions