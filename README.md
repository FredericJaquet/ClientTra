# ClientTra

**ClientTra** is a backend system designed for translators and small businesses to manage clients, providers, orders, and financial documents such as invoices, quotes, and purchase orders.

Built with **Java 21**, **Spring Boot**, and **MySQL 8**, this project follows a clean and modular architecture, supporting multi-company use, multilingual capabilities, and role-based security.

---

## ✨ Features

* ✅ Company and user registration with roles and freemium plan assignment
* ✅ Full management of:

  * Clients & providers
  * Addresses, phones, bank accounts, contacts
  * Orders and order lines
  * Customer and provider invoices, quotes, POs
* ✅ Soft delete for users and documents
* ✅ Automatic calculation of due dates, taxes, totals, and currency conversion
* ✅ Secure endpoints with JWT authentication and role checks
* ✅ Multi-language support (I18n ready)
* ✅ Document versioning (via "MODIFIED" status)

---

## 📁 Project Structure

```
src/main/java/com/frederic/clienttra/
├── ClienttraApplication.java
├── config             # Configuration files (CORS, WebSecurity, etc.)
├── controllers        # REST controllers (User, Company, Document, etc.)
├── dto                # DTOs for create/read/update operations
├── entities           # JPA entities representing database tables
├── enums              # Enums (DocumentType, Status, Roles, etc.)
├── exceptions         # Custom exceptions
├── mappers            # MapStruct mappers (entity ↔ DTO)
├── projections        # Spring JPA projections for optimized queries
├── repositories       # Spring Data JPA repositories
├── security           # JWT auth, filters, security config
├── services           # Business logic layer
├── utils              # Helpers for calculations (e.g., DocumentUtils)
├── validators         # Custom bean validators (IBAN, email, etc.)
└── DataLoader.java    # Initial data setup for development
```

---

## 🧪 Tech Stack

* **Backend:** Java 21, Spring Boot 3
* **Database:** MySQL 8
* **ORM:** Spring Data JPA, Hibernate
* **Security:** Spring Security with JWT
* **Validation:** Jakarta Bean Validation, custom validators
* **Build Tool:** Maven

---

## 🚀 Getting Started

### Prerequisites

* Java 21
* MySQL 8
* Maven

### Setup

1. Clone the repo:

   ```bash
   git clone https://github.com/your-username/clienttra.git
   cd clienttra
   ```

2. Configure your `application.properties` (or `application.yml`) for DB credentials.

3. Build and run:

   ```bash
   mvn clean install
   java -jar target/clienttra-0.0.1-SNAPSHOT.jar
   ```

4. Access API at: `http://localhost:8080/api/`

---

## 📄 API Overview

* `POST /api/register` → Register a new company + first admin user
* `POST /api/users` → Create new users (admin only)
* `GET /api/customer-invoices` → Get all customer invoices
* `POST /api/customer-invoices/create/{idCompany}` → Create new customer invoice
* `POST /api/customer-invoices/modify-to-new-version/{idDocument}` → Create new version of invoice

*(...more endpoints available)*

---

## 🛡️ Security

* Role-based access (`ROLE_ADMIN`, `ROLE_USER`, etc.)
* JWT-based authentication
* Passwords encrypted using BCrypt
* Soft delete system for users and documents

---

## 📌 Current Status

✅ User, company, and client/provider management complete

✅ Orders and all document types fully implemented:

Customer invoices

Provider invoices

Quotes

Purchase orders


✅ Demo data loading and deletion functionality completed

🔮 Report generation and statistics planned

🖥️ Frontend to be implemented (React planned)
---

## 📒 License

This project is currently under private development by [Frédéric Jaquet](https://github.com/your-username) and is not yet licensed for public or commercial use.

---

## 📢 Contact

If you're interested in collaborating or have any questions, feel free to reach out!

> "Learning by building. Professionalism by care." 🚀
