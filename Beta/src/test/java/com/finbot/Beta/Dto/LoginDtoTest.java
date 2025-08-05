package com.finbot.Beta.Dto;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginDtoTest {

    @Test
    void testLoginDtoBuilder() {
        LoginDto loginDto = LoginDto.builder()
                .email("TestEmail@gmail.com")
                .password("TestPassword123")
                .build();

        assertEquals("TestEmail@gmail.com", loginDto.getEmail());
        assertEquals("TestPassword123", loginDto.getPassword());
    }

    @Test
    void testNoArgsConstructorAndSetters() {
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("user@example.com");
        loginDto.setPassword("securePassword");

        assertEquals("user@example.com", loginDto.getEmail());
        assertEquals("securePassword", loginDto.getPassword());
    }

    @Test
    void testAllArgsConstructor() {
        LoginDto loginDto = new LoginDto("admin@example.com", "adminPass");

        assertEquals("admin@example.com", loginDto.getEmail());
        assertEquals("adminPass", loginDto.getPassword());
    }

    @Test
    void testEqualsAndHashCode() {
        LoginDto dto1 = LoginDto.builder()
                .email("same@example.com")
                .password("samePassword")
                .build();

        LoginDto dto2 = LoginDto.builder()
                .email("same@example.com")
                .password("samePassword")
                .build();

        LoginDto differentDto = LoginDto.builder()
                .email("different@example.com")
                .password("differentPassword")
                .build();

        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        assertNotEquals(dto1, differentDto);
        assertNotEquals(dto1.hashCode(), differentDto.hashCode());
    }

    @Test
    void testToString() {
        LoginDto loginDto = LoginDto.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        String toStringResult = loginDto.toString();

        assertTrue(toStringResult.contains("test@example.com"));
        assertTrue(toStringResult.contains("password123"));
    }
}