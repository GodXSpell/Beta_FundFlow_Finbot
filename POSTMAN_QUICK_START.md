# 🚀 Postman Quick Start Guide - FinBot API

## You DON'T Need to Configure JWT Secret in Postman!

The JWT secret is only used by your **backend server** to sign tokens. Postman just receives and uses the tokens that your server generates.

---

## ✅ Step-by-Step Testing Guide

### Step 1: Import the Collection

1. **Open Postman**
2. Click **Import** button (top-left corner)
3. Select the file: `D:\Beta\Postman_Collection_FinBot_API.json`
4. Click **Import**

You should see "FinBot API - Complete Test Suite" in your collections.

---

### Step 2: Start Your Application

Make sure your application is running:

**Option A: Docker** (Port 8081)
```bash
cd D:\Beta
docker-compose up
```
Access at: `http://localhost:8081`

**Option B: IntelliJ** (Port 8080)
```bash
cd D:\Beta\Beta
mvn spring-boot:run
```
Access at: `http://localhost:8080`

**Note:** The Postman collection is configured for port 8081 (Docker). If using port 8080, update the `baseUrl` variable in Postman to `http://localhost:8080`.

---

### Step 3: Run the Tests in Order

The collection is designed to run sequentially. **Important:** Run them in this exact order!

#### Test 1: Signup (Register New User)

1. Open: **Authentication** → **1. Signup - Register New User**
2. Click **Send**
3. ✅ You should get status 200 with user details
4. 📝 The user ID is automatically saved

**Expected Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Test User",
  "email": "testuser@example.com",
  "registeredOn": "2025-11-01"
}
```

---

#### Test 2: Login (Get JWT Token) - MOST IMPORTANT!

1. Open: **Authentication** → **2. Login - Get JWT Token**
2. Click **Send**
3. ✅ You should get status 200 with token and user data
4. 🔑 **The JWT token is automatically saved!** Check the Postman Console to see:
   ```
   JWT Token saved successfully
   User ID saved: 550e8400-e29b-41d4-a716-446655440000
   ```

**Expected Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI1NTBlODQwMC1lMjliLTQxZDQtYTcxNi00NDY2NTU0NDAwMDAiLCJpYXQiOjE3MzA0NjcyMDAsImV4cCI6MTczMDU1MzYwMH0.abc123...",
  "tokenType": "Bearer",
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440000",
    "name": "Test User",
    "email": "testuser@example.com",
    "registeredOn": "2025-11-01"
  }
}
```

---

#### Test 3-6: Protected Endpoints (Use Saved Token)

Now you can test all protected endpoints! The token is automatically included.

**Test 3: Update Password**
1. Open: **User Management (Protected)** → **3. Update Password**
2. Click **Send**
3. ✅ Should return updated user details (status 200)

**Test 4: Update Email**
1. Open: **User Management (Protected)** → **4. Update Email**
2. Click **Send**
3. ✅ Should return user with new email (status 200)

**Test 5: Update All Details**
1. Open: **User Management (Protected)** → **5. Update All User Details**
2. Click **Send**
3. ✅ Should return fully updated user (status 200)

**Test 6: Delete User**
1. Open: **User Management (Protected)** → **6. Delete User**
2. Click **Send**
3. ✅ Should return "User deleted successfully" (status 200)

---

#### Test 7: Verify Security (Unauthorized Access)

1. Open: **Test Unauthorized Access** → **Try Update Without Token**
2. Click **Send**
3. ✅ Should FAIL with status 401 Unauthorized

This proves your JWT authentication is working!

---

## 🔍 How to View Saved Variables

1. Click on the collection name: **FinBot API - Complete Test Suite**
2. Go to the **Variables** tab
3. You'll see:
   - `baseUrl` = http://localhost:8081
   - `token` = (your JWT token after login)
   - `userId` = (your user ID after signup/login)

---

## 🐛 Troubleshooting

### Problem: "Could not get any response" or Connection Error

**Solution:**
- Make sure your application is running
- Check if you're using the correct port (8081 for Docker, 8080 for IntelliJ)
- Update `baseUrl` variable if needed

### Problem: Protected endpoints return 401 Unauthorized

**Solution:**
- Make sure you ran the **Login** test first
- Check the Console (View → Show Postman Console) to verify token was saved
- Check the Variables tab to see if `token` has a value
- Token expires after 24 hours - login again to get a new token

### Problem: "User already exists" on Signup

**Solution:**
- Either change the email in the signup request
- Or delete the existing user from your database
- Or use a different email address

### Problem: Tests are failing

**Solution:**
1. Clear all variables (in Variables tab)
2. Run tests in order: Signup → Login → Others
3. Check Postman Console for error messages (View → Show Postman Console)

---

## 📊 Viewing Test Results

After running each request, check the **Test Results** tab at the bottom:

✅ Green checkmarks = Tests passed
❌ Red X = Tests failed

Example after successful login:
```
✓ Status code is 200
✓ Response has token and user data
✓ Token type is Bearer
✓ User data is correct
```

---

## 🎯 Quick Test Sequence

For a complete test run, execute in this order:

1. ✅ **Signup** - Creates user
2. ✅ **Login** - Gets token (SAVES IT AUTOMATICALLY!)
3. ✅ **Update Password** - Uses saved token
4. ✅ **Update Email** - Uses saved token
5. ✅ **Update All** - Uses saved token
6. ✅ **Try Without Token** - Should fail with 401
7. ✅ **Delete User** - Uses saved token

---

## 💡 Pro Tips

1. **Check the Console:** Always check Postman Console (View → Show Postman Console) to see token save confirmations
2. **Token Expiry:** Your tokens expire after 24 hours. Just login again to get a new one
3. **Test Scripts:** Each request has pre-configured test scripts that validate responses
4. **Environment Variables:** The collection uses variables ({{token}}, {{userId}}) that are automatically managed
5. **Authorization Tab:** Protected requests have "Bearer Token" auth pre-configured

---

## 🔐 Security Notes

- Your JWT secret is stored securely in your `.env` file on the server
- Postman **never needs to know your JWT secret**
- The server generates signed tokens using the secret
- Postman just receives and uses these pre-signed tokens
- Tokens are valid for 24 hours, then you need to login again

---

## ✅ Success Checklist

After testing, you should have verified:

- [ ] User registration works (signup)
- [ ] Login returns a valid JWT token
- [ ] Token is automatically saved in Postman
- [ ] Protected endpoints accept the token
- [ ] Password updates work with authentication
- [ ] Email updates work with authentication
- [ ] Requests without tokens are rejected (401)
- [ ] User deletion works with authentication

---

## 📞 Need Help?

If tests are failing:
1. Check application logs for errors
2. Verify database is running
3. Ensure correct port is being used
4. Check Postman Console for detailed error messages
5. Review the `JWT_TESTING_GUIDE.md` for more details

---

**Happy Testing! 🎉**

Your JWT authentication is fully configured and ready to use.

