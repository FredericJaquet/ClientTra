# ClientTra

**ClientTra** is a backend system designed for freelance translators and small businesses to manage clients, providers, orders, and financial documents such as invoices, quotes, and purchase orders.

Built with **Java 21**, **Spring Boot 3**, and **MySQL 8**, the project follows a clean and modular architecture. It supports multi-company environments, multilingual capabilities, and role-based security.

---

## ✨ Features
✅ Company and user registration with role and freemium plan assignment  
✅ Full CRUD management for:
 - Clients & providers
 - Addresses, phone numbers, bank accounts, contacts

✅ Orders and order lines with automatic total and VAT calculation  
✅ Customer and provider invoices, quotes, and purchase orders  
✅ Soft delete system for users and documents  
✅ Automatic tax, total, due date and currency conversion  
✅ Document versioning via `"MODIFIED"` status  
✅ Secure endpoints using JWT and role-based access  
✅ I18n-ready (multi-language support)

---

## 📁 Project Structure
src/main/java/com/frederic/clienttra/

├── ClienttraApplication.java

├── config # Configuration (CORS, WebSecurity, etc.)

├── controllers # REST controllers

├── dto # DTOs for create/read/update operations

├── entities # JPA entities

├── enums # DocumentType, Status, Role, etc.

├── exceptions # Custom exceptions

├── mappers # MapStruct mappers

├── projections # Optimized JPA queries

├── repositories # Spring Data JPA repositories

├── security # JWT filters and auth

├── services # Business logic

├── utils # Calculation helpers (DocumentUtils, etc.)

├── validators # Custom validators (IBAN, email, etc.)

└── DataLoader.java # Initial development/demo data

---

## 🧭 Architecture Overview

ClientTra follows a layered architecture that separates concerns clearly between web, service, and data layers:

<img width="512" height="768" alt="Architecture" src="https://github.com/user-attachments/assets/eec2f2e7-8713-4f06-885c-fb68dfbb5f11" />

This structure improves testability, scalability, and maintainability.  
Each layer has a focused responsibility:

- **Controllers**: Handle HTTP requests and map them to services.
- **Services**: Contain business logic and orchestrate operations.
- **DTOs/Mappers**: Decouple internal entities from API output.
- **Repositories**: Interface with the database using JPA.
- **Entities**: Represent persistent domain models.

---

## 🧪 Testing

- ✅ Over **200 unit tests** implemented with JUnit and Mockito
- ✅ Focused on core services, business rules, and security
- ⚠️ Code coverage not exhaustive by choice (quality over quantity)

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

## 📌 Project Status
✅ Backend completed (code, logic, security, tests)

✅ All document types implemented:

   - Customer invoices
   - Provider invoices
   - Quotes
   - Purchase Orders

✅ Demo data loader + deletion functionality

✅ Multicompany, multilanguage, multirole support

✅ Testing coverage for all core services

✅ Reports: income, expenses, and pending payments

🧾 Documentation in progress (expected this week)

--- 

## 🖥️ Frontend to be implemented (React planned)
Planned stack: React + TailwindCSS + shadcn/ui

Responsive layout with sidebar navigation, dashboard, and modals

Auth, charts, and real-time data planned for production phase

---

## 📒 License

This project is currently under private development by [Frédéric Jaquet](https://github.com/FredericJaquet) and is not yet licensed for public or commercial use.

🧠 Important:
The author explicitly prohibits the use of this codebase for AI model training or data extraction, whether partial or full, without written consent.

---

## 📢 Contact

If you're interested in collaborating or have any questions, feel free to reach out!

> "Learning by building. Professionalism by care." 🚀
