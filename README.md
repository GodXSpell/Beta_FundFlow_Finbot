# FundFlow - Personal Finance Management API

FundFlow is a Spring Boot REST API designed to help users manage their personal finances, including budget tracking, bank account management, and secure user authentication.

---

## Features

- **User Authentication:** Registration and login with JWT-based security (Spring Security).
- **Budget Management:** Full CRUD operations for budgets.
- **Bank Account Management:** Add and track multiple bank accounts.
- **Robust Security:** JWT authentication and best practices using Spring Security.
- **Data Persistence:** Reliable storage via JPA/Hibernate.

---

## Technologies Used

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security**
- **Spring Data JPA**
- **Maven**
- **Docker** (optional)

---

## Project Structure

```
src/
├── main/
│   ├── java/com/finbot/Beta/
│   │   ├── BetaApplication.java
│   │   ├── config/
│   │   ├── controller/
│   │   ├── Dto/
│   │   ├── entity/
│   │   ├── Exceptions/
│   │   ├── repository/
│   │   ├── service/
│   │   └── util/
│   └── resources/
│       ├── application.properties
│       ├── static/
│       └── templates/
└── test/
    └── java/com/finbot/Beta/
```

---

## Getting Started

### Prerequisites

- **Java 17+**
- **Maven**
- **MySQL / PostgreSQL / H2 Database**

---

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/GodXSpell/Beta.git
   cd Beta
   ```

2. **Configure the database**

   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/finbot_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Build and run the application**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   The API will be available at: [http://localhost:8080](http://localhost:8081)

---

### Docker (Optional)

To run the application with Docker:

```bash
docker build -t beta-finbot .
docker run -p 8080:8081 beta-finbot
```

---

### API Endpoints

**Authentication**
- `POST /api/auth/signup` — Register a new user
- `POST /api/auth/login` — User login

**Budget**
- `GET /api/budgets` — List budgets
- `POST /api/budgets` — Create a new budget
- `PUT /api/budgets/{id}` — Update a budget
- `DELETE /api/budgets/{id}` — Delete a budget

**Bank Accounts**
- `GET /api/accounts` — List bank accounts
- `POST /api/accounts` — Add a new bank account

---

### Testing

Run all tests:
```bash
mvn test
```

---

### Troubleshooting

- Ensure service implementations are in `com.finbot.Beta.service.impl`
- Confirm `@Service` and `@Component` annotations are present
- Verify your database credentials and that the database server is running
- Check application logs for errors

---

## License

This project is licensed under the [MIT License](LICENSE).

---

## Author

**GodXSpell**

---
