package com.finbot.Beta.Dto;

import com.finbot.Beta.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SignupDtoTest {

    @Test
    void testSignupDto() {
        SignupDto signupDto = SignupDto.builder()
                .name("TestUser")
                .email("test@example.com")
                .password("secret123")
                .confirmPassword("secret123")
                .registeredOn("2025-07-31")
                .build();

        // Assertions can be added here to validate the properties of signupDto
        assertEquals("TestUser", signupDto.getName());
        assertEquals("test@example.com", signupDto.getEmail());
        assertEquals("secret123", signupDto.getPassword());
        assertEquals("2025-07-31", signupDto.getRegisteredOn());
    }

    @Test
    void testThatSignupDtoIsStoredToUserEntity() {
        SignupDto signupDto = SignupDto.builder()
                .name("TestUser")
                .email("testuser@example.com")
                .password("testpass123")
                .confirmPassword("testpass123")
                .registeredOn("2025-08-01")
                .build();

        User user = User.builder()
                .name(signupDto.getName())
                .email(signupDto.getEmail())
                .password(signupDto.getPassword())
                .registeredOn(LocalDate.parse(signupDto.getRegisteredOn()))
                .build();
        // Assertions to verify that the User entity is correctly populated from SignupDto
        assertEquals(signupDto.getName(), user.getName());
        assertEquals(signupDto.getEmail(), user.getEmail());
        assertEquals(signupDto.getPassword(), user.getPassword());
        assertEquals(LocalDate.parse(signupDto.getRegisteredOn()), user.getRegisteredOn());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        SignupDto signupDto = new SignupDto();
        signupDto.setName("John Doe");
        signupDto.setEmail("john@example.com");
        signupDto.setPassword("password123");
        signupDto.setConfirmPassword("password123");
        signupDto.setRegisteredOn("2025-01-01");

        assertEquals("John Doe", signupDto.getName());
        assertEquals("john@example.com", signupDto.getEmail());
        assertEquals("password123", signupDto.getPassword());
        assertEquals("password123", signupDto.getConfirmPassword());
        assertEquals("2025-01-01", signupDto.getRegisteredOn());
    }

    @Test
    void testEqualsAndHashCode() {
        SignupDto dto1 = SignupDto.builder()
                .name("TestUser")
                .email("test@example.com")
                .password("secret123")
                .confirmPassword("secret123")
                .registeredOn("2025-07-31")
                .build();

        SignupDto dto2 = SignupDto.builder()
                .name("TestUser")
                .email("test@example.com")
                .password("secret123")
                .confirmPassword("secret123")
                .registeredOn("2025-07-31")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SignupDto dto = SignupDto.builder()
                .name("TestUser")
                .email("test@example.com")
                .password("secret123")
                .confirmPassword("secret123")
                .registeredOn("2025-07-31")
                .build();

        String toStringResult = dto.toString();

        assertTrue(toStringResult.contains("TestUser"));
        assertTrue(toStringResult.contains("test@example.com"));
        assertTrue(toStringResult.contains("secret123"));
        assertTrue(toStringResult.contains("2025-07-31"));
    }
}
