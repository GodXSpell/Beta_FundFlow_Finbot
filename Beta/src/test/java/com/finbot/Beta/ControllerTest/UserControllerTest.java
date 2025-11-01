package com.finbot.Beta.ControllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finbot.Beta.Dto.*;
import com.finbot.Beta.controller.UserController;
import com.finbot.Beta.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;
    private UUID userId;
    private String uuidString;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
        userId = UUID.fromString("11111111-1111-1111-1111-111111111111");
        uuidString = userId.toString();
    }

    @Test
    void testSignupEndpoint() throws Exception {
        SignupDto signupDto = SignupDto.builder()
                .name("Example Name")
                .email("test@example.com")
                .password("password123")
                .confirmPassword("password123")
                .build();

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(uuidString)
                .name("Example Name")
                .email("test@example.com")
                .build();

        when(userService.registerUser(any(SignupDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(uuidString))
                .andExpect(jsonPath("$.name").value("Example Name"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testLoginEndpoint() throws Exception {
        LoginDto loginDto = LoginDto.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        UserResponseDto userDto = UserResponseDto.builder()
                .id(uuidString)
                .name("Example Name")
                .email("test@example.com")
                .build();

        AuthResponseDto responseDto = AuthResponseDto.builder()
                .token("eyJhbGciOiJIUzI1NiJ9.test.token")
                .tokenType("Bearer")
                .user(userDto)
                .build();

        when(userService.loginUser(any(LoginDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.user.id").value(uuidString))
                .andExpect(jsonPath("$.user.name").value("Example Name"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
    }

    @Test
    void testUpdatePasswordEndpoint() throws Exception {
        UpdatePasswordDto passwordDto = new UpdatePasswordDto();
        passwordDto.setNewPassword("newPassword123");

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(uuidString)
                .name("Example Name")
                .email("test@example.com")
                .build();

        when(userService.updateUserPassword(eq(userId), any(String.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/users/update/password/" + uuidString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(uuidString));
    }

    @Test
    void testUpdateEmailEndpoint() throws Exception {
        UpdateEmailDto emailDto = new UpdateEmailDto();
        emailDto.setNewEmail("updated@example.com");

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(uuidString)
                .name("Example Name")
                .email("updated@example.com")
                .build();

        when(userService.updateUserEmail(eq(userId), any(String.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/users/update/email/" + uuidString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void testUpdateAllEndpoint() throws Exception {
        UpdatedUserDto updatedUserDto = new UpdatedUserDto();
        updatedUserDto.setName("Updated Name");
        updatedUserDto.setEmail("updated@example.com");
        updatedUserDto.setPassword("updatedPass");

        UserResponseDto responseDto = UserResponseDto.builder()
                .id(uuidString)
                .name("Updated Name")
                .email("updated@example.com")
                .build();

        when(userService.updateAll(eq(userId), any(UpdatedUserDto.class)))
                .thenReturn(responseDto);

        mockMvc.perform(put("/api/users/updateAll/" + uuidString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void testDeleteUserEndpoint() throws Exception {
        when(userService.deleteUserById(userId)).thenReturn(null); // or the actual return type

        mockMvc.perform(delete("/api/users/delete/" + uuidString)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("User deleted successfully"));
    }
}