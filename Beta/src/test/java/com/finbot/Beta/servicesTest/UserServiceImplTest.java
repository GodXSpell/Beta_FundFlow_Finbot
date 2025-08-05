package com.finbot.Beta.servicesTest;

import com.finbot.Beta.Dto.LoginDto;
import com.finbot.Beta.Dto.SignupDto;
import com.finbot.Beta.Dto.UpdatedUserDto;
import com.finbot.Beta.Dto.UserResponseDto;
import com.finbot.Beta.Exceptions.UserAlreadyExistsException;
import com.finbot.Beta.entity.User;
import com.finbot.Beta.repository.UserRepository;
import com.finbot.Beta.servicesImpl.UserServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        SignupDto signupDto = SignupDto.builder()
                .name("Test User")
                .email("test@example.com")
                .password("securepass")
                .confirmPassword("securepass")
                .registeredOn("2025-07-31")
                .build();

        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        User user = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .password("encodedPassword") // This should be the encoded value
                .registeredOn(LocalDate.parse("2025-07-31"))
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("securepass")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDto response = userService.registerUser(signupDto);

        assertEquals("Test User", response.getName());
        assertEquals("test@example.com", response.getEmail());
        verify(passwordEncoder).encode("securepass");
        verify(userRepository).save(argThat(savedUser ->
                savedUser.getPassword().equals("encodedPassword")));
    }

    @Test
    void testRegisterUserWithExistingEmail() {
        // Setup
        String existingEmail = "test@example.com";
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        // Create existing user in database (mocked)
        User existingUser = User.builder()
                .id(userId)
                .name("Existing User")
                .email(existingEmail)
                .password("password")
                .registeredOn(LocalDate.now())
                .build();

        // Setup repository mock to return existing user
        when(userRepository.findByEmail(existingEmail)).thenReturn(Optional.of(existingUser));

        // Create signup DTO with same email
        SignupDto duplicateSignupDto = SignupDto.builder()
                .name("Duplicate User")
                .email(existingEmail)
                .password("pass")
                .confirmPassword("pass")
                .build();

        // Verify exception is thrown
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser(duplicateSignupDto);
        });

        // Verify repository was called to find user
        verify(userRepository).findByEmail(existingEmail);

        // Verify save was never called
        verify(userRepository, never()).save(any());
    }

    @Test
    void testRegisterUserWithPasswordMismatch() {
        SignupDto signupDto = SignupDto.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password1")
                .confirmPassword("password2")
                .build();

        assertThrows(RuntimeException.class, () -> {
            userService.registerUser(signupDto);
        });

        verify(userRepository, never()).findByEmail(anyString());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testLoginUser() {
        LoginDto loginDto = LoginDto.builder()
                .email("test@example.com")
                .password("securepass")
                .build();
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        User user = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .password("encodedPassword") // This should be the encoded value
                .registeredOn(LocalDate.of(2025, 7, 31))
                .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("securepass", "encodedPassword")).thenReturn(true);

        UserResponseDto response = userService.loginUser(loginDto);

        assertEquals("Test User", response.getName());
        assertEquals("test@example.com", response.getEmail());
        verify(passwordEncoder).matches("securepass", "encodedPassword");
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testLoginUserWithInvalidPassword() {
        LoginDto loginDto = LoginDto.builder()
                .email("test@example.com")
                .password("wrongpassword")
                .build();
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        User user = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@example.com")
                .password("encodedPassword")
                .registeredOn(LocalDate.of(2025, 7, 31))
                .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            userService.loginUser(loginDto);
        });

        verify(passwordEncoder).matches("wrongpassword", "encodedPassword");
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testLoginUserWithNonExistentEmail() {
        LoginDto loginDto = LoginDto.builder()
                .email("nonexistent@example.com")
                .password("securepass")
                .build();

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.loginUser(loginDto);
        });

        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    void testUpdateAll() {
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        String newName = "Updated Name";
        String newEmail = "updated@example.com";
        String newPassword = "newpassword";
        String encodedPassword = "encodedNewPassword";

        User existingUser = User.builder()
                .id(userId)
                .name("Original Name")
                .email("original@example.com")
                .password("oldEncodedPassword")
                .registeredOn(LocalDate.of(2025, 7, 31))
                .build();

        UpdatedUserDto updatedUserDto = new UpdatedUserDto();
        updatedUserDto.setName(newName);
        updatedUserDto.setEmail(newEmail);
        updatedUserDto.setPassword(newPassword);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        UserResponseDto response = userService.updateAll(userId, updatedUserDto);

        assertEquals(newName, response.getName());
        assertEquals(newEmail, response.getEmail());

        verify(userRepository).findById(userId);
        verify(userRepository).findByEmail(newEmail);
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(argThat(savedUser ->
                savedUser.getName().equals(newName) &&
                        savedUser.getEmail().equals(newEmail) &&
                        savedUser.getPassword().equals(encodedPassword)));
    }

    @Test
    void testUpdateUserPassword() {
        String email = "test@test.com";
        String newPassword = "newSecure";
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        User user = User.builder()
                .id(userId)
                .name("Test User")
                .email(email)
                .password("oldEncodedPassword")
                .registeredOn(LocalDate.of(2025, 7, 31))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        UserResponseDto response = userService.updateUserPassword(userId, newPassword);

        assertEquals("Test User", response.getName());
        assertEquals(email, response.getEmail());
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(argThat(savedUser ->
                savedUser.getPassword().equals("newEncodedPassword")));
    }

    @Test
    void testUpdateUserEmail() {
        String oldEmail = "oldemail@email.com";
        String newEmail = "newEmail@gmail.com";
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        User user = User.builder()
                .id(userId)
                .name("Test User")
                .email(oldEmail)
                .password("securepass")
                .registeredOn(LocalDate.of(2025, 7, 31))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        UserResponseDto response = userService.updateUserEmail(userId, newEmail);

        assertEquals("Test User", response.getName());
        assertEquals(newEmail, response.getEmail());

        verify(userRepository).findById(userId);
        verify(userRepository).save(argThat(savedUser ->
                savedUser.getEmail().equals(newEmail)));
    }

    @Test
    void testThatUserIsDeletedById() {
        UUID userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        User user = User.builder()
                .id(userId)
                .name("Test User")
                .email("test@gmail.com")
                .password("securepass")
                .registeredOn(LocalDate.of(2025, 7, 31))
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        userService.deleteUserById(userId);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }
}
