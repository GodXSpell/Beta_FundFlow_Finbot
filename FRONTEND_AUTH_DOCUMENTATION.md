# FinBot API - Authentication & Authorization Documentation for Frontend Team

## 📋 Table of Contents
1. [Overview](#overview)
2. [Authentication Flow](#authentication-flow)
3. [API Endpoints](#api-endpoints)
4. [Request/Response Examples](#request-response-examples)
5. [JWT Token Details](#jwt-token-details)
6. [Error Handling](#error-handling)
7. [Security Implementation](#security-implementation)
8. [Frontend Integration Guide](#frontend-integration-guide)

---

## 🔐 Overview

### Authentication Method
- **Type:** JWT (JSON Web Token) based authentication
- **Algorithm:** HMAC-SHA256 (HS256)
- **Token Expiration:** 24 hours (86400000 milliseconds)
- **Password Storage:** BCrypt with strength factor 12
- **Session Management:** Stateless (no server-side sessions)

### Base URL
```
Production: http://localhost:8081 (Docker)
Development: http://localhost:8080 (Local)
```

---

## 🔄 Authentication Flow

### 1. User Registration Flow
```
Frontend → POST /api/users/signup → Backend
                                    ↓
                            Password Hashed (BCrypt)
                                    ↓
                            User Saved to Database
                                    ↓
Frontend ← User Details (without password)
```

### 2. User Login Flow
```
Frontend → POST /api/users/login → Backend
                                    ↓
                            Validate Email & Password
                                    ↓
                            Generate JWT Token
                                    ↓
Frontend ← JWT Token + User Details
         ↓
    Store Token (localStorage/sessionStorage)
         ↓
    Use Token for All Protected Requests
```

### 3. Protected Endpoint Access Flow
```
Frontend → Protected Endpoint + JWT Token → Backend
                                              ↓
                                    Extract & Validate Token
                                              ↓
                                    Verify User Exists
                                              ↓
                                    Check Token Expiration
                                              ↓
Frontend ← Success Response / 401 Unauthorized
```

---

## 🌐 API Endpoints

### Public Endpoints (No Authentication Required)

#### 1. User Signup
```http
POST /api/users/signup
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePassword123!",
  "confirmPassword": "SecurePassword123!"
}
```

**Success Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Doe",
  "email": "john@example.com",
  "registeredOn": "2025-11-01"
}
```

**Validation Rules:**
- ✅ Name: Required, max 100 characters
- ✅ Email: Required, valid email format, unique
- ✅ Password: Required, must match confirmPassword
- ✅ Password is hashed with BCrypt before storage

---

#### 2. User Login
```http
POST /api/users/login
Content-Type: application/json
```

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "SecurePassword123!"
}
```

**Success Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1NTBlODQwMC1lMjliLTQxZDQtYTcxNi00NDY2NTU0NDAwMDAiLCJpYXQiOjE3MzA0NjcyMDAsImV4cCI6MTczMDU1MzYwMH0.xyz123...",
  "tokenType": "Bearer",
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "John Doe",
    "email": "john@example.com",
    "registeredOn": "2025-11-01"
  }
}
```

**Important:** 
- 🔑 Store the `token` securely (localStorage or sessionStorage)
- 🔑 Include this token in all subsequent protected endpoint requests
- 🔑 Token expires after 24 hours - user must login again

---

### Protected Endpoints (JWT Token Required)

All protected endpoints require the JWT token in the Authorization header:

```http
Authorization: Bearer YOUR_JWT_TOKEN_HERE
```

#### 3. Update User Password
```http
PUT /api/users/update/password/{userId}
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json
```

**Request Body:**
```json
{
  "newPassword": "NewSecurePassword456!"
}
```

**Success Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Doe",
  "email": "john@example.com",
  "registeredOn": "2025-11-01"
}
```

---

#### 4. Update User Email
```http
PUT /api/users/update/email/{userId}
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json
```

**Request Body:**
```json
{
  "newEmail": "newemail@example.com"
}
```

**Success Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Doe",
  "email": "newemail@example.com",
  "registeredOn": "2025-11-01"
}
```

**Validation:**
- ✅ New email must be unique (not already in use)
- ✅ Valid email format required

---

#### 5. Update All User Details
```http
PUT /api/users/updateAll/{userId}
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json
```

**Request Body:**
```json
{
  "name": "John Updated",
  "email": "updated@example.com",
  "password": "BrandNewPassword789!"
}
```

**Success Response (200 OK):**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "John Updated",
  "email": "updated@example.com",
  "registeredOn": "2025-11-01"
}
```

**Note:** All fields are optional. Only provided fields will be updated.

---

#### 6. Delete User Account
```http
DELETE /api/users/delete/{userId}
Authorization: Bearer YOUR_JWT_TOKEN
```

**Success Response (200 OK):**
```
User deleted successfully
```

**Warning:** This is a permanent action and cannot be undone.

---

## 🎯 Request/Response Examples

### Example 1: Complete Login Flow

**Step 1: User Login**
```javascript
// Frontend JavaScript Example
const loginUser = async (email, password) => {
  try {
    const response = await fetch('http://localhost:8081/api/users/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password })
    });
    
    if (!response.ok) {
      throw new Error('Login failed');
    }
    
    const data = await response.json();
    
    // Store token for future requests
    localStorage.setItem('authToken', data.token);
    localStorage.setItem('user', JSON.stringify(data.user));
    
    return data;
  } catch (error) {
    console.error('Login error:', error);
    throw error;
  }
};
```

**Step 2: Use Token for Protected Request**
```javascript
// Frontend JavaScript Example
const updatePassword = async (userId, newPassword) => {
  const token = localStorage.getItem('authToken');
  
  if (!token) {
    throw new Error('No authentication token found');
  }
  
  try {
    const response = await fetch(`http://localhost:8081/api/users/update/password/${userId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`  // Important: Include Bearer prefix
      },
      body: JSON.stringify({ newPassword })
    });
    
    if (!response.ok) {
      if (response.status === 401) {
        // Token expired or invalid - redirect to login
        localStorage.removeItem('authToken');
        window.location.href = '/login';
      }
      throw new Error('Update failed');
    }
    
    return await response.json();
  } catch (error) {
    console.error('Update password error:', error);
    throw error;
  }
};
```

---

## 🔑 JWT Token Details

### Token Structure

JWT tokens have 3 parts separated by dots (`.`):

```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1NTBlODQwMC1lMjliLTQxZDQtYTcxNi00NDY2NTU0NDAwMDAiLCJpYXQiOjE3MzA0NjcyMDAsImV4cCI6MTczMDU1MzYwMH0.xyz123...
     ^HEADER^              ^PAYLOAD (Base64 Encoded)^                                    ^SIGNATURE^
```

### Token Payload (Decoded)

```json
{
  "sub": "550e8400-e29b-41d4-a716-446655440000",  // User ID (UUID)
  "iat": 1730467200,                               // Issued At (Unix timestamp)
  "exp": 1730553600                                // Expiration (Unix timestamp)
}
```

**Important Token Properties:**
- **sub (Subject):** User's UUID - uniquely identifies the user
- **iat (Issued At):** When the token was created
- **exp (Expiration):** When the token expires (24 hours after iat)

### Token Validation

The backend validates:
1. ✅ Token signature is valid (using secret key)
2. ✅ Token hasn't expired
3. ✅ User ID (sub) exists in database
4. ✅ User account is active

---

## ❌ Error Handling

### Common Error Responses

#### 1. Invalid Credentials (Login)
```http
Status: 401 Unauthorized
```
```json
{
  "error": "Unauthorized",
  "message": "Invalid email or password"
}
```

**Frontend Action:** Show error message to user

---

#### 2. Validation Errors (Signup/Update)
```http
Status: 400 Bad Request
```
```json
{
  "error": "Bad Request",
  "message": "Passwords do not match"
}
```

**Frontend Action:** Display validation error to user

---

#### 3. User Already Exists (Signup)
```http
Status: 409 Conflict
```
```json
{
  "error": "Conflict",
  "message": "User already exists, try login"
}
```

**Frontend Action:** Redirect to login or suggest password reset

---

#### 4. Unauthorized Access (Protected Endpoints)
```http
Status: 401 Unauthorized
```
```json
{
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource"
}
```

**Frontend Action:** 
- Clear stored token
- Redirect to login page
- Show "Session expired" message

---

#### 5. Token Expired
```http
Status: 401 Unauthorized
```
```json
{
  "error": "Unauthorized",
  "message": "JWT token has expired"
}
```

**Frontend Action:**
- Clear stored token
- Redirect to login page
- Show "Session expired, please login again" message

---

#### 6. User Not Found
```http
Status: 404 Not Found
```
```json
{
  "error": "Not Found",
  "message": "User not found"
}
```

**Frontend Action:** Show error message and redirect appropriately

---

## 🔒 Security Implementation

### Backend Security Features

#### 1. Password Security
- **Hashing Algorithm:** BCrypt
- **Strength Factor:** 12 (very secure)
- **Salt:** Automatically generated per password
- **Storage:** Only hashed passwords stored, never plain text

#### 2. JWT Security
- **Algorithm:** HMAC-SHA256 (HS256)
- **Secret Key:** 88-character random string (highly secure)
- **Token Signing:** Prevents tampering
- **Token Expiration:** 24 hours automatic expiry

#### 3. Database Security
- **Email Index:** Unique constraint on email field
- **SQL Injection Protection:** JPA/Hibernate parameterized queries
- **Connection Pooling:** Secure database connections

#### 4. CORS Configuration
```
Allowed Origins:
- http://localhost:5500
- http://127.0.0.1:5500
- http://localhost:5173
- Other configured origins

Allowed Methods: GET, POST, PUT, DELETE, OPTIONS
Allowed Headers: All (*)
Credentials: Allowed
```

#### 5. Session Management
- **Type:** Stateless (no server-side sessions)
- **Storage:** Client-side only (JWT token)
- **Benefits:** Scalable, no session storage required

---

## 💻 Frontend Integration Guide

### 1. Token Storage

**Recommended Approach:**
```javascript
// Store token after successful login
localStorage.setItem('authToken', data.token);
localStorage.setItem('userId', data.user.id);
localStorage.setItem('userEmail', data.user.email);

// Retrieve token for API calls
const token = localStorage.getItem('authToken');

// Clear token on logout
const logout = () => {
  localStorage.removeItem('authToken');
  localStorage.removeItem('userId');
  localStorage.removeItem('userEmail');
  window.location.href = '/login';
};
```

**Security Considerations:**
- ⚠️ localStorage is vulnerable to XSS attacks
- ✅ Ensure your frontend is protected against XSS
- ✅ Consider using httpOnly cookies for production (requires backend changes)

---

### 2. API Request Helper

```javascript
// Create a reusable API helper
const api = {
  baseURL: 'http://localhost:8081/api',
  
  // Helper to get auth headers
  getAuthHeaders: () => {
    const token = localStorage.getItem('authToken');
    return {
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : ''
    };
  },
  
  // Generic request method
  request: async (endpoint, options = {}) => {
    try {
      const response = await fetch(`${api.baseURL}${endpoint}`, {
        ...options,
        headers: {
          ...api.getAuthHeaders(),
          ...options.headers
        }
      });
      
      // Handle 401 Unauthorized
      if (response.status === 401) {
        localStorage.removeItem('authToken');
        window.location.href = '/login';
        throw new Error('Session expired');
      }
      
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Request failed');
      }
      
      // Handle empty responses (like delete)
      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        return await response.json();
      }
      return await response.text();
      
    } catch (error) {
      console.error('API Error:', error);
      throw error;
    }
  }
};

// Usage Examples:
// Login
const loginResponse = await api.request('/users/login', {
  method: 'POST',
  body: JSON.stringify({ email, password })
});

// Update Password (Protected)
const updateResponse = await api.request(`/users/update/password/${userId}`, {
  method: 'PUT',
  body: JSON.stringify({ newPassword })
});
```

---

### 3. React/Vue Example (Authentication Context)

```javascript
// React Context Example
import React, { createContext, useState, useEffect } from 'react';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);
  
  // Load auth state from localStorage on mount
  useEffect(() => {
    const storedToken = localStorage.getItem('authToken');
    const storedUser = localStorage.getItem('user');
    
    if (storedToken && storedUser) {
      setToken(storedToken);
      setUser(JSON.parse(storedUser));
    }
    setLoading(false);
  }, []);
  
  const login = async (email, password) => {
    const response = await fetch('http://localhost:8081/api/users/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });
    
    if (!response.ok) {
      throw new Error('Login failed');
    }
    
    const data = await response.json();
    
    localStorage.setItem('authToken', data.token);
    localStorage.setItem('user', JSON.stringify(data.user));
    
    setToken(data.token);
    setUser(data.user);
    
    return data;
  };
  
  const logout = () => {
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
    setToken(null);
    setUser(null);
  };
  
  const value = {
    user,
    token,
    login,
    logout,
    isAuthenticated: !!token
  };
  
  if (loading) {
    return <div>Loading...</div>;
  }
  
  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
```

---

### 4. Protected Route Example

```javascript
// React Router Protected Route
import { Navigate } from 'react-router-dom';
import { useContext } from 'react';
import { AuthContext } from './AuthContext';

const ProtectedRoute = ({ children }) => {
  const { isAuthenticated } = useContext(AuthContext);
  
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  
  return children;
};

// Usage:
<Route path="/dashboard" element={
  <ProtectedRoute>
    <Dashboard />
  </ProtectedRoute>
} />
```

---

## 🧪 Testing Guide

### Using Postman

#### Test 1: User Signup
```
POST http://localhost:8081/api/users/signup

Body:
{
  "name": "Test User",
  "email": "test@example.com",
  "password": "TestPass123!",
  "confirmPassword": "TestPass123!"
}

Expected: 200 OK with user details
```

#### Test 2: User Login
```
POST http://localhost:8081/api/users/login

Body:
{
  "email": "test@example.com",
  "password": "TestPass123!"
}

Expected: 200 OK with token and user details
Action: Copy the token for next tests
```

#### Test 3: Update Password (Protected)
```
PUT http://localhost:8081/api/users/update/password/{userId}

Headers:
Authorization: Bearer YOUR_TOKEN_HERE

Body:
{
  "newPassword": "NewTestPass456!"
}

Expected: 200 OK with updated user details
```

---

## 📊 Data Flow Diagram

```
┌─────────────┐
│   Frontend  │
│   (React/   │
│    Vue)     │
└──────┬──────┘
       │
       │ 1. POST /login
       │    { email, password }
       ↓
┌─────────────────────────────┐
│   Backend API Gateway       │
│   (Spring Security Filter)  │
└──────┬──────────────────────┘
       │
       │ 2. Validate Credentials
       │    (BCrypt password check)
       ↓
┌─────────────────────────────┐
│   UserService               │
│   - Authenticate user       │
│   - Generate JWT token      │
└──────┬──────────────────────┘
       │
       │ 3. Return JWT + User Data
       │    { token, tokenType, user }
       ↓
┌─────────────┐
│   Frontend  │
│   Store:    │
│   - Token   │
│   - User    │
└──────┬──────┘
       │
       │ 4. PUT /update/password
       │    Headers: Authorization: Bearer TOKEN
       ↓
┌─────────────────────────────┐
│   JWT Authentication Filter │
│   - Extract token           │
│   - Validate signature      │
│   - Check expiration        │
│   - Load user details       │
└──────┬──────────────────────┘
       │
       │ 5. Token Valid?
       │    YES → Proceed
       │    NO  → 401 Unauthorized
       ↓
┌─────────────────────────────┐
│   UserController            │
│   Process update request    │
└──────┬──────────────────────┘
       │
       │ 6. Return updated user
       ↓
┌─────────────┐
│   Frontend  │
│   Update UI │
└─────────────┘
```

---

## 🎯 Quick Reference

### Required Headers for Protected Endpoints
```http
Content-Type: application/json
Authorization: Bearer YOUR_JWT_TOKEN_HERE
```

### Token Lifetime
- **Duration:** 24 hours (86400000 ms)
- **Action on Expiry:** User must login again
- **Frontend Handling:** Clear token and redirect to login

### User ID Format
- **Type:** UUID (Universally Unique Identifier)
- **Example:** `550e8400-e29b-41d4-a716-446655440000`
- **Format:** 8-4-4-4-12 hexadecimal digits

### Password Requirements
- **Minimum Length:** Not enforced (recommend 8+ characters)
- **Complexity:** Not enforced (recommend uppercase, lowercase, numbers, symbols)
- **Confirmation:** Must match confirmPassword on signup

---

## 🚨 Important Notes for Frontend Team

### 1. Token Refresh
⚠️ **Currently NOT Implemented**
- Tokens expire after 24 hours
- No refresh token mechanism
- User must login again after expiration
- **TODO:** Consider implementing refresh tokens for better UX

### 2. Password Reset
⚠️ **Currently NOT Implemented**
- No "Forgot Password" functionality yet
- Users cannot reset password via email
- **TODO:** Implement email-based password reset

### 3. Email Verification
⚠️ **Currently NOT Implemented**
- No email verification on signup
- Users can login immediately after signup
- **TODO:** Consider adding email verification

### 4. Rate Limiting
⚠️ **Currently NOT Implemented**
- No rate limiting on login attempts
- No account lockout after failed attempts
- **TODO:** Implement rate limiting for security

### 5. Multi-Factor Authentication (MFA)
⚠️ **Currently NOT Implemented**
- Single-factor authentication only (password)
- **TODO:** Consider adding MFA for enhanced security

---

## 📞 Support & Contact

**Backend Developer:** [Your Name]
**API Documentation:** This file
**Postman Collection:** `Postman_Collection_FinBot_API.json`
**Testing Guide:** `JWT_TESTING_GUIDE.md`
**Troubleshooting:** `JWT_TROUBLESHOOTING.md`

---

## ✅ Implementation Checklist for Frontend

- [ ] Implement login form
- [ ] Store JWT token in localStorage/sessionStorage
- [ ] Create API helper with auth headers
- [ ] Implement protected routes
- [ ] Handle 401 errors (token expiration)
- [ ] Implement logout functionality
- [ ] Add token to all protected API calls
- [ ] Show appropriate error messages
- [ ] Implement user profile display
- [ ] Add password update form
- [ ] Add email update form
- [ ] Implement account deletion confirmation
- [ ] Test all authentication flows
- [ ] Handle edge cases (network errors, etc.)

---

**Last Updated:** November 1, 2025  
**Version:** 1.0  
**Status:** Production Ready ✅

