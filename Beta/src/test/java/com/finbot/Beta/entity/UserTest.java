package com.finbot.Beta.entity;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserTest {

    @Test
    void testSampleUserCreation() {
        User sampleUser = User.builder()
                .id(java.util.UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .name("Johnny Sins")
                .email("johnnysins@gmail.com")
                .password("password123")
                .registeredOn(LocalDate.of(2025, 1, 1))
                .build();

        assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000001"), sampleUser.getId());
        assertEquals("Johnny Sins", sampleUser.getName());
        assertEquals("johnnysins@gmail.com", sampleUser.getEmail());
        assertEquals("password123", sampleUser.getPassword());
        assertEquals(LocalDate.of(2025, 1, 1), sampleUser.getRegisteredOn());
    }

    @Test
    void testDefaultValues() {
        User user = new User();
        assertTrue(user.getIsActive(), "IsActive should default to true");
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getRegisteredOn());
        assertNull(user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    void testUserEquality() {
        UUID userId = UUID.randomUUID();

        User user1 = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .registeredOn(LocalDate.now())
                .build();

        User user2 = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .registeredOn(LocalDate.now())
                .build();

        assertEquals(user1, user2, "Users with same data should be equal");
        assertEquals(user1.hashCode(), user2.hashCode(), "Equal users should have same hashCode");
    }

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertNotNull(user, "No-args constructor should create a non-null User");
    }
}