# Banking System REST API

A secure banking system REST API built with Spring Boot, featuring JWT authentication, wallet management, and money transfers with pessimistic locking for concurrent transactions.

🔗 **Live URL:** https://banking-system-rest-api-production.up.railway.app

📄 **Swagger UI:** https://banking-system-rest-api-production.up.railway.app/swagger-ui/index.html

---

## 🚀 Features

- User registration and authentication with JWT
- Secure wallet management
- Money transfer between users
- Transaction history tracking
- Role-based access control
- Pessimistic locking for race condition prevention
- Input validation with detailed error messages

---

## 🛠️ Tech Stack

- **Backend:** Spring Boot 3.4, Java 21
- **Security:** Spring Security, JWT (HS512)
- **Database:** MySQL (Railway)
- **ORM:** JPA/Hibernate
- **Validation:** Bean Validation
- **Deployment:** Railway
- **API Documentation:** Swagger/OpenAPI

---

## 📡 API Endpoints

### Authentication

#### Register User
```http
POST /auth/register
Content-Type: application/json
```

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "SecurePass@123",
  "phoneNumber": "9876543210"
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": "9876543210"
}
```

---

#### Login
```http
POST /auth/login
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePass@123"
}
```

**Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTcwOTczNjAwMCwiZXhwIjoxNzA5ODIyNDAwfQ...",
  "type": "Bearer"
}
```

---

### Wallet Operations (Requires Authentication)

> **Note:** All wallet endpoints require `Authorization: Bearer <token>` header

#### Get Wallet Details
```http
GET /api/wallet
Authorization: Bearer <your-jwt-token>
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "userId": 1,
  "balance": 5000.00,
  "createdAt": "2024-03-06T10:30:00"
}
```

---

#### Transfer Money
```http
POST /api/transfer
Authorization: Bearer <your-jwt-token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "reciverEmail": "jane.smith@example.com",
  "amount": 500.00,
  "description": "Payment for dinner"
}
```

**Response:** `200 OK`
```json
{
  "id": 15,
  "senderWalletId": 1,
  "receiverWalletId": 2,
  "amount": 500.00,
  "type": "TRANSFER",
  "status": "COMPLETED",
  "description": "Payment for dinner",
  "timestamp": "2024-03-06T14:20:00"
}
```

---

#### Get Transaction History
```http
GET /api/transactions
Authorization: Bearer <your-jwt-token>
```

**Response:** `200 OK`
```json
[
  {
    "id": 15,
    "senderWalletId": 1,
    "receiverWalletId": 2,
    "amount": 500.00,
    "type": "TRANSFER",
    "status": "COMPLETED",
    "description": "Payment for dinner",
    "timestamp": "2024-03-06T14:20:00"
  }
]
```

---

## 🧪 Testing the API

### Option 1: Using Swagger UI (Recommended)

1. Visit: https://banking-system-rest-api-production.up.railway.app/swagger-ui/index.html
2. **Register** a new user via `POST /auth/register`
3. **Login** via `POST /auth/login` and copy the JWT token from response
4. Click **"Authorize"** button (🔓 icon, top right)
5. Enter token in format: `Bearer <paste-token-here>`
6. Click **"Authorize"** then **"Close"**
7. Now test any protected endpoint (wallet, transfer, transactions)

---

### Option 2: Using Postman

#### Step 1: Register User
- **Method:** `POST`
- **URL:** `https://banking-system-rest-api-production.up.railway.app/auth/register`
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "firstName": "Test",
  "lastName": "User",
  "email": "test@example.com",
  "password": "Test@123",
  "phoneNumber": "9876543210"
}
```

#### Step 2: Login
- **Method:** `POST`
- **URL:** `https://banking-system-rest-api-production.up.railway.app/auth/login`
- **Headers:** `Content-Type: application/json`
- **Body (raw JSON):**
```json
{
  "email": "test@example.com",
  "password": "Test@123"
}
```
- **Copy the `token` from response**

#### Step 3: Get Wallet (Protected)
- **Method:** `GET`
- **URL:** `https://banking-system-rest-api-production.up.railway.app/api/wallet`
- **Headers:** 
  - `Authorization: Bearer <paste-your-token>`

#### Step 4: Transfer Money (Protected)
- **Method:** `POST`
- **URL:** `https://banking-system-rest-api-production.up.railway.app/api/transfer`
- **Headers:**
  - `Content-Type: application/json`
  - `Authorization: Bearer <paste-your-token>`
- **Body (raw JSON):**
```json
{
  "reciverEmail": "receiver@example.com",
  "amount": 100.00,
  "description": "Test transfer"
}
```

---

### Option 3: Using cURL
```bash
# 1. Register User
curl -X POST https://banking-system-rest-api-production.up.railway.app/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Test",
    "lastName": "User",
    "email": "test@example.com",
    "password": "Test@123",
    "phoneNumber": "9876543210"
  }'

# 2. Login (save the token from response)
curl -X POST https://banking-system-rest-api-production.up.railway.app/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test@123"
  }'

# 3. Get Wallet (replace YOUR_TOKEN_HERE)
curl -X GET https://banking-system-rest-api-production.up.railway.app/api/wallet \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# 4. Transfer Money (replace YOUR_TOKEN_HERE)
curl -X POST https://banking-system-rest-api-production.up.railway.app/api/transfer \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "reciverEmail": "receiver@example.com",
    "amount": 100.00,
    "description": "Test payment"
  }'
```

---

## 🔒 Security Features

- **Password Encryption:** BCrypt hashing algorithm
- **JWT Authentication:** Stateless token-based auth with HS512 signature
- **Token Expiration:** 24 hours (configurable)
- **Pessimistic Locking:** Prevents race conditions during concurrent transfers
- **Input Validation:** Bean Validation with custom error messages
- **Role-Based Authorization:** Spring Security method-level security

---

## 🏗️ Architecture

**Layered Architecture:**
- **Controller Layer:** Handles HTTP requests, validation
- **Service Layer:** Business logic, transaction management
- **Repository Layer:** Data access with JPA
- **Security Layer:** JWT filter, authentication manager

**Key Design Decisions:**
- Used pessimistic locking (`@Lock`) for money transfers to prevent double-spending
- Implemented `@Transactional` for ACID compliance
- Separated concerns with DTOs for request/response

---

## 💾 Database Schema

**Users Table:**
- id (Primary Key)
- first_name
- last_name
- email (Unique)
- password (BCrypt encrypted)
- phone_number
- role
- created_at

**Wallets Table:**
- id (Primary Key)
- user_id (Foreign Key → Users)
- balance (DECIMAL)
- created_at

**Transactions Table:**
- id (Primary Key)
- sender_wallet_id (Foreign Key → Wallets)
- receiver_wallet_id (Foreign Key → Wallets)
- amount (DECIMAL)
- type (ENUM: TRANSFER, DEPOSIT, WITHDRAWAL)
- status (ENUM: PENDING, COMPLETED, FAILED)
- description
- timestamp

---

## 🚀 Running Locally

### Prerequisites
- Java 21+
- Maven 3.6+
- MySQL 8.0+

### Steps

1. **Clone the repository:**
```bash
git clone https://github.com/Ayushpandeyff/banking-system-rest-api.git
cd banking-system-rest-api
```

2. **Create MySQL database:**
```sql
CREATE DATABASE banking_db;
```

3. **Configure `application.properties`:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/banking_db
spring.datasource.username=root
spring.datasource.password=yourpassword
jwt.secret=your-secret-key-here
```

4. **Build and run:**
```bash
mvn clean install
mvn spring-boot:run
```

5. **Access the application:**
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

---

## 📊 API Response Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 201 | Created |
| 400 | Bad Request (Validation error) |
| 401 | Unauthorized (Invalid/missing token) |
| 403 | Forbidden (Insufficient permissions) |
| 404 | Not Found |
| 500 | Internal Server Error |

---

## 🐛 Common Errors

**"User not found"**
- Email doesn't exist in database
- Register first or check email spelling

**"Insufficient balance"**
- Wallet balance less than transfer amount
- Check balance with `GET /api/wallet`

**"Cannot do self transfer"**
- Sender and receiver emails are the same
- Use different receiver email

**"Amount should be greater than 0"**
- Transfer amount is zero or negative
- Provide positive amount

---

## 📝 Future Enhancements

- [ ] Docker containerization
- [ ] Redis caching for improved performance
- [ ] WebSocket notifications for real-time updates
- [ ] Transaction rollback mechanisms
- [ ] Email notifications
- [ ] Multi-currency support
- [ ] Admin dashboard

---

## 📄 License

This project is open source and available under the MIT License.

---

## 👨‍💻 Author

**Ayush Pandey**

- GitHub: [@Ayushpandeyff](https://github.com/Ayushpandeyff)
- LinkedIn: [Your LinkedIn URL]
- Email: [Your Email]

---

## 🙏 Acknowledgments

- Built with **Spring Boot 3.4**
- Deployed on **Railway**
- Secured with **JWT**
- Documented with **Swagger/OpenAPI**

---

**⭐ If you find this project useful, please give it a star!**
