# FinBot API - JWT Authentication Testing Guide

## 🔐 Security Implementation Summary

Your application now has complete JWT authentication with BCrypt password hashing implemented. Here's what's configured:

### Components Implemented:
✅ **BCrypt Password Encoding** (Strength: 12)
✅ **JWT Token Generation & Validation**
✅ **JWT Authentication Filter**
✅ **Custom User Details Service**
✅ **Authentication Entry Point**
✅ **Security Configuration**

---

## 📍 JWT Secret Configuration

Your JWT secret is stored in multiple locations for different environments:

### 1. Local Development (`.env` file)
```env
JWT_SECRET=PSHtmRLFhwnMjducwJK0CnLEWXigpj2SW0aM2AjlP+wUdwFdnSgTZNyYeIH+gMfPL9d0LI1goHMJwvejcStg0g==
JWT_EXPIRATION=86400000
```

### 2. Application Properties (`application.properties`)
```properties
jwt.secret=${JWT_SECRET:mySecretKeyForJWTTokenGenerationAndValidation12345678901234567890}
jwt.expiration=${JWT_EXPIRATION:86400000}
```

### 3. Docker Environment (`docker-compose.yml`)
```yaml
environment:
  - JWT_SECRET=${JWT_SECRET}
  - JWT_EXPIRATION=${JWT_EXPIRATION}
```

**🔴 IMPORTANT:** Never commit the `.env` file to version control!

---

## 🌐 API Endpoints

### Public Endpoints (No Authentication Required)
- `POST /api/users/signup` - Register new user
- `POST /api/users/login` - Login and get JWT token

### Protected Endpoints (JWT Token Required)
- `PUT /api/users/update/password/{userId}` - Update password
- `PUT /api/users/update/email/{userId}` - Update email
- `PUT /api/users/updateAll/{userId}` - Update all user details
- `DELETE /api/users/delete/{userId}` - Delete user account

**Base URL:** `http://localhost:8081` (or `http://localhost:8080` if running directly)

---

## 🧪 Testing with Postman

### Method 1: Import the Collection (RECOMMENDED)

1. Open Postman
2. Click **Import** button (top-left)
3. Select the file: `Postman_Collection_FinBot_API.json`
4. The collection will be imported with all tests pre-configured

### Method 2: Manual Setup

#### Step 1: Create Environment Variables

1. Click on **Environments** in Postman
2. Create new environment: "FinBot Local"
3. Add variables:
   - `baseUrl` = `http://localhost:8081`
   - `token` = (leave empty - will be auto-populated)
   - `userId` = (leave empty - will be auto-populated)

#### Step 2: Test Authentication Flow

##### Test 1: Signup (Register New User)

**Request:**
```
POST http://localhost:8081/api/users/signup
Content-Type: application/json

{
  "name": "Test User",
  "email": "testuser@example.com",
  "password": "SecurePassword123!",
  "confirmPassword": "SecurePassword123!"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Test User",
  "email": "testuser@example.com",
  "registeredOn": "2025-01-15"
}
```

**Save the `id` value - you'll need it for other requests!**

---

##### Test 2: Login (Get JWT Token)

**Request:**
```
POST http://localhost:8081/api/users/login
Content-Type: application/json

{
  "email": "testuser@example.com",
  "password": "SecurePassword123!"
}
```

**Expected Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1NTBlODQwMC1lMjliLTQxZDQtYTcxNi00NDY2NTU0NDAwMDAiLCJpYXQiOjE3MDUzMjg0MDAsImV4cCI6MTcwNTQxNDgwMH0.xyz123...",
  "tokenType": "Bearer",
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Test User",
    "email": "testuser@example.com",
    "registeredOn": "2025-01-15"
  }
}
```

**🔑 COPY THE TOKEN! You'll need it for protected endpoints.**

---

#### Step 3: Test Protected Endpoints

For all protected endpoints, you MUST include the JWT token in the Authorization header:

**Authorization Header Format:**
```
Authorization: Bearer YOUR_JWT_TOKEN_HERE
```

##### Test 3: Update Password

**Request:**
```
PUT http://localhost:8081/api/users/update/password/{userId}
Authorization: Bearer YOUR_JWT_TOKEN_HERE
Content-Type: application/json

{
  "newPassword": "NewSecurePassword456!"
}
```

Replace `{userId}` with the actual user ID from signup/login response.

**Expected Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Test User",
  "email": "testuser@example.com",
  "registeredOn": "2025-01-15"
}
```

---

##### Test 4: Update Email

**Request:**
```
PUT http://localhost:8081/api/users/update/email/{userId}
Authorization: Bearer YOUR_JWT_TOKEN_HERE
Content-Type: application/json

{
  "newEmail": "newemail@example.com"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Test User",
  "email": "newemail@example.com",
  "registeredOn": "2025-01-15"
}
```

---

##### Test 5: Update All User Details

**Request:**
```
PUT http://localhost:8081/api/users/updateAll/{userId}
Authorization: Bearer YOUR_JWT_TOKEN_HERE
Content-Type: application/json

{
  "name": "Updated Name",
  "email": "updatedemail@example.com",
  "password": "BrandNewPassword789!"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Updated Name",
  "email": "updatedemail@example.com",
  "registeredOn": "2025-01-15"
}
```

---

##### Test 6: Delete User

**Request:**
```
DELETE http://localhost:8081/api/users/delete/{userId}
Authorization: Bearer YOUR_JWT_TOKEN_HERE
```

**Expected Response (200 OK):**
```
User deleted successfully
```

---

### 🔴 Testing Unauthorized Access

Try accessing a protected endpoint WITHOUT the Authorization header:

**Request:**
```
PUT http://localhost:8081/api/users/update/password/{userId}
Content-Type: application/json

{
  "newPassword": "HackerPassword123!"
}
```

**Expected Response (401 Unauthorized):**
```json
{
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource"
}
```

This confirms your JWT authentication is working correctly!

---

## 🎯 How JWT Authentication Works in Your App

### 1. **User Registration (Signup)**
- User sends credentials
- Password is hashed using BCrypt (strength 12)
- User saved to database with hashed password
- Response returns user details (NO password)

### 2. **User Login**
- User sends email + password
- System finds user by email
- BCrypt compares plain password with hashed password
- If valid: JWT token is generated and returned
- Token contains user ID and expiration time

### 3. **Accessing Protected Endpoints**
- Client includes JWT token in `Authorization: Bearer {token}` header
- `JwtAuthenticationFilter` intercepts the request
- Token is extracted and validated
- User ID is extracted from token
- `CustomUserDetailsService` loads user details
- If valid: Request proceeds
- If invalid: Returns 401 Unauthorized

### 4. **Token Expiration**
- Token expires after 24 hours (86400000 milliseconds)
- User must login again to get new token
- Expired tokens are automatically rejected

---

## 📝 Postman Scripts for Automatic Token Management

If you imported the collection, these scripts are already included:

### Login Test Script (Saves Token Automatically)
```javascript
// Parse response
var jsonData = pm.response.json();

// Save token for future requests
if (jsonData.token) {
    pm.collectionVariables.set('token', jsonData.token);
    console.log('JWT Token saved successfully');
}

// Save userId
if (jsonData.user && jsonData.user.id) {
    pm.collectionVariables.set('userId', jsonData.user.id);
    console.log('User ID saved: ' + jsonData.user.id);
}
```

This script automatically:
- Extracts the JWT token from login response
- Saves it to collection variables
- Makes it available for all subsequent requests

---

## 🐛 Troubleshooting

### Problem: "401 Unauthorized" on protected endpoints

**Solution:**
1. Make sure you've logged in and have a valid token
2. Check that Authorization header is set: `Authorization: Bearer {token}`
3. Verify token hasn't expired (24 hours validity)
4. Check for typos in the token (no extra spaces)

### Problem: "Cannot resolve symbol BCryptPasswordEncoder"

**Solution:**
1. Verify Spring Security dependency in `pom.xml`
2. Run `mvn clean install`
3. Invalidate IntelliJ caches: File > Invalidate Caches / Restart

### Problem: Token not being sent with requests

**Solution:**
1. In Postman, go to Authorization tab
2. Select Type: "Bearer Token"
3. Enter Token: `{{token}}` (uses collection variable)

### Problem: "User not found" error

**Solution:**
- Make sure you're using the correct userId from signup/login response
- Check database to verify user exists

---

## 🔒 Security Best Practices Implemented

✅ **Password Hashing:** BCrypt with strength 12 (very secure)
✅ **JWT Tokens:** Signed with HS256 algorithm
✅ **Token Expiration:** 24-hour validity
✅ **Stateless Sessions:** No server-side session storage
✅ **Protected Endpoints:** Require valid JWT token
✅ **CORS Configuration:** Configured for multiple origins
✅ **Error Handling:** Custom authentication entry point

---

## 📊 Testing Checklist

- [ ] Test user registration (signup)
- [ ] Test user login (get JWT token)
- [ ] Test update password with valid token
- [ ] Test update email with valid token
- [ ] Test update all details with valid token
- [ ] Test delete user with valid token
- [ ] Test protected endpoint without token (should fail with 401)
- [ ] Test with expired token (wait 24 hours or manually expire)
- [ ] Test with invalid token (should fail with 401)

---

## 🚀 Running the Application

### Local Development
```bash
cd Beta
mvn spring-boot:run
```
Access at: `http://localhost:8080`

### Docker
```bash
cd D:\Beta
docker-compose up --build
```
Access at: `http://localhost:8081`

---

## 📞 Support

If you encounter any issues:
1. Check application logs for error messages
2. Verify all environment variables are set correctly
3. Ensure database is running and accessible
4. Check that port 8081 (or 8080) is not in use

---

**Last Updated:** January 2025
**Version:** 1.0

