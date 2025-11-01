# JWT Authentication - Postman Testing Guide

## Base URL
```
http://localhost:8081
```

---

## 1. User Signup (No Authentication Required)

**Endpoint:** `POST http://localhost:8081/api/users/signup`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "password": "SecurePassword123",
  "confirmPassword": "SecurePassword123"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "registeredOn": "2025-01-15"
}
```

**Notes:**
- Password is hashed with BCrypt before storing
- No JWT token returned on signup (user must login)

---

## 2. User Login (Get JWT Token)

**Endpoint:** `POST http://localhost:8081/api/users/login`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePassword123"
}
```

**Expected Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "John Doe",
    "email": "john.doe@example.com",
    "registeredOn": "2025-01-15"
  }
}
```

**Important:** 
- Save the `token` value for use in subsequent requests
- Token is valid for 24 hours by default
- Password is verified using BCrypt

**Postman Script to Auto-Save Token:**
Add this to the "Tests" tab in Postman:
```javascript
var jsonData = pm.response.json();
if (jsonData.token) {
    pm.environment.set("jwt_token", jsonData.token);
    pm.environment.set("user_id", jsonData.user.id);
    console.log("Token saved: " + jsonData.token.substring(0, 20) + "...");
}
```

---

## 3. Update User Password (Requires Authentication)

**Endpoint:** `PUT http://localhost:8081/api/users/update/password/{userId}`

**Example:** `PUT http://localhost:8081/api/users/update/password/550e8400-e29b-41d4-a716-446655440000`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {your_jwt_token_here}
```

**Request Body:**
```json
{
  "newPassword": "NewSecurePassword456"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Doe",
  "email": "john.doe@example.com",
  "registeredOn": "2025-01-15"
}
```

**Using Postman Variables:**
- URL: `http://localhost:8081/api/users/update/password/{{user_id}}`
- Authorization: `Bearer {{jwt_token}}`

---

## 4. Update User Email (Requires Authentication)

**Endpoint:** `PUT http://localhost:8081/api/users/update/email/{userId}`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {your_jwt_token_here}
```

**Request Body:**
```json
{
  "newEmail": "john.updated@example.com"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Doe",
  "email": "john.updated@example.com",
  "registeredOn": "2025-01-15"
}
```

---

## 5. Update All User Details (Requires Authentication)

**Endpoint:** `PUT http://localhost:8081/api/users/updateAll/{userId}`

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {your_jwt_token_here}
```

**Request Body:**
```json
{
  "name": "John Updated",
  "email": "john.newemail@example.com",
  "password": "BrandNewPassword789"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Updated",
  "email": "john.newemail@example.com",
  "registeredOn": "2025-01-15"
}
```

**Notes:**
- All fields are optional - only provided fields will be updated
- Password will be hashed before storage
- Email uniqueness is validated

---

## 6. Delete User (Requires Authentication)

**Endpoint:** `DELETE http://localhost:8081/api/users/delete/{userId}`

**Headers:**
```
Authorization: Bearer {your_jwt_token_here}
```

**No Request Body Required**

**Expected Response (200 OK):**
```
User deleted successfully
```

---

## Error Responses

### 401 Unauthorized (Missing or Invalid Token)
```json
{
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource"
}
```

### 401 Unauthorized (Expired Token)
```json
{
  "error": "Unauthorized",
  "message": "JWT token has expired"
}
```

### 400 Bad Request (Invalid Credentials)
```json
{
  "error": "Bad Request",
  "message": "Invalid email or password"
}
```

### 409 Conflict (Email Already Exists)
```json
{
  "error": "Conflict",
  "message": "User already exists, try login"
}
```

---

## Postman Collection Setup

### 1. Create Environment Variables
- `base_url`: `http://localhost:8081`
- `jwt_token`: (will be auto-populated on login)
- `user_id`: (will be auto-populated on login)

### 2. Set Up Authorization for Protected Endpoints
For all protected endpoints (update/delete):
1. Go to "Authorization" tab
2. Select "Type: Bearer Token"
3. Token: `{{jwt_token}}`

### 3. Test Flow Sequence
1. **Signup** → Creates new user
2. **Login** → Get JWT token (saved automatically)
3. **Update Password** → Change password
4. **Update Email** → Change email
5. **Update All** → Change multiple fields
6. **Delete** → Remove user account

---

## Security Features Implemented

✅ **BCrypt Password Hashing**
- Passwords are hashed with strength factor 12
- Never stored or transmitted in plain text

✅ **JWT Token Authentication**
- Stateless authentication
- Token expires after 24 hours
- Tokens include user ID in payload

✅ **Protected Endpoints**
- All update/delete operations require valid JWT
- Signup and login are public endpoints

✅ **CORS Configuration**
- Pre-configured for frontend integration
- Supports multiple origins

---

## Token Expiration

Default token expiration: **24 hours (86400000 ms)**

To modify, update in `application.properties`:
```properties
jwt.expiration=3600000  # 1 hour in milliseconds
```

Or set environment variable:
```
JWT_EXPIRATION=7200000  # 2 hours
```

---

## Testing Tips

1. **Save Token Automatically**: Use the Postman script provided in the Login section
2. **Test Token Expiration**: Set a short expiration (e.g., 60000ms = 1 minute) and test
3. **Test Without Token**: Try protected endpoints without Authorization header
4. **Test Invalid Token**: Modify the token string and attempt requests
5. **Test Expired Token**: Wait for token to expire and retry requests

---

## Common Issues & Solutions

### Issue: "401 Unauthorized" on protected endpoints
**Solution:** Ensure you've logged in and the JWT token is set in the Authorization header

### Issue: "Cannot resolve symbol BCryptPasswordEncoder"
**Solution:** Check that spring-boot-starter-security is in pom.xml and Maven dependencies are updated

### Issue: Token not being accepted
**Solution:** Verify the token format is `Bearer {token}` with a space after "Bearer"

### Issue: CORS errors
**Solution:** Check that your frontend origin is listed in application.properties CORS configuration

---

## Next Steps

1. Test all endpoints in the order specified
2. Verify password hashing by checking database (passwords should be encrypted)
3. Test token expiration by setting a short expiration time
4. Integrate with your frontend application using the token-based authentication

---

**Last Updated:** November 1, 2025
**API Version:** 1.0
**Port:** 8081 (Docker) / 8080 (Local)

