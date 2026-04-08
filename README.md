# FundFlow - Personal Finance Management API

FundFlow (FinBot API) is a robust and secure RESTful backend system designed to help users intelligently manage their personal finances. Built with Java 17 and Spring Boot 3, it offers comprehensive tools for tracking bank accounts, monitoring budgets, and logging daily financial transactions. 

The API uses Spring Security and stateless JWTs for robust authentication, ensuring user data privacy and session integrity across requests. Future features allow for intelligent financial insight integration.

---

## 🌟 Key Features

- **User Security & Authentication:** 
  - Registration and login secured via Spring Security and JWT.
  - Update credentials, email, and user details dynamically.
- **Bank Account Management:** 
  - Add, track, and manage multiple bank accounts (Checking, Savings, Credit).
  - Monitors real-time balances.
- **Budget Tracking:** 
  - Create and manage budgets across various categories (e.g., Groceries, Rent, Entertainment).
  - Configurable periodic limits (Daily, Weekly, Monthly, Yearly).
- **Transaction Logging & Analytics:** 
  - Track every debit and credit transaction.
  - Filter and query transactions by Bank Account, Date Range, and Category.
- **Robust Error Handling:** Global exception handling for clean, descriptive REST responses.

---

## 🛠️ Technologies Used

- **Java 17**
- **Spring Boot 3.x** (Web, Validation)
- **Spring Security & JWT** (io.jsonwebtoken)
- **Spring Data JPA / Hibernate**
- **MySQL / PostgreSQL** (Data Persistence)
- **Maven** (Dependency Management)
- **Docker** (Containerization)

---

## 📂 Project Structure

```
Beta/
├── src/main/java/com/finbot/Beta/
│   ├── config/          # CORS, Security, Web configs
│   ├── controller/      # REST API Controllers
│   ├── Dto/             # Data Transfer Objects (Requests & Responses)
│   ├── entity/          # JPA Database Entities
│   ├── Exceptions/      # Custom Exceptions & Global Handler
│   ├── repository/      # Spring Data Repositories
│   ├── security/        # JWT Filters, UserDetails & EntryPoints
│   └── service/         # Business Logic Interfaces and Implementations
└── src/main/resources/
    └── application.properties # Database & Application environments
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 17+**
- **Maven 3.8+**
- **MySQL Database Engine** (Or Postgres/H2 based on your setup)

---

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository_url>
   cd FundFlowBackend/Beta
   ```

2. **Configure the Environment**
   Edit `src/main/resources/application.properties` with your local database credentials:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/finbot_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Build and test the application**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```
   The API will start locally on: `http://localhost:8080`

---

## 🔌 API Overview

*All endpoints except signup and login require a valid `Authorization: Bearer <token>` header.*

### 👤 Authentication (`/api/users`)
- `POST /api/users/signup` — Register a new user
- `POST /api/users/login` — Authenticate and receive a JWT
- `PUT /api/users/update/password/{id}` — Update user password
- `DELETE /api/users/delete/{id}` — Delete user account

### 🏦 Bank Accounts (`/api/accounts`)
- `POST /api/accounts/create` — Add a new bank account
- `GET /api/accounts/all` — List all user bank accounts
- `GET /api/accounts/get/{id}` — Get details of a specific account

### 📊 Budgets (`/api/budgets`)
- `POST /api/budgets` — Create a new budget
- `GET /api/budgets` — List all active budgets
- `PUT /api/budgets/{id}` — Modify an existing budget

### 💸 Transactions (`/api/transactions`)
- `POST /api/transactions` — Add a debit or credit transaction
- `GET /api/transactions` — Paginated list of all transactions
- `GET /api/transactions/daterange` — Filter transactions by start and end dates
- `GET /api/transactions/category/{category}` — Get transactions by category

> **Note:** For deep API testing, refer to the included Postman Collection `FinBot_API.postman_collection.json`.

---

## 🐳 Docker Support

To run the application inside a Docker container:

```bash
docker build -t beta-finbot .
docker run -p 8080:8080 beta-finbot
```

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## Author
- **[Tarunpreet Singh]** - [GitHub Profile](https://github.com/GodXSpell)
