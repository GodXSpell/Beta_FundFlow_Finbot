package com.finbot.Beta.service.impl;

import com.finbot.Beta.Dto.AuthResponseDto;
import com.finbot.Beta.Dto.LoginDto;
import com.finbot.Beta.Dto.SignupDto;
import com.finbot.Beta.Dto.UpdatedUserDto;
import com.finbot.Beta.Dto.UserResponseDto;
import com.finbot.Beta.Exceptions.UserAlreadyExistsException;
import com.finbot.Beta.Exceptions.UserNotFoundException;
import com.finbot.Beta.entity.User;
import com.finbot.Beta.repository.UserRepository;
import com.finbot.Beta.security.JWTtokenizer;
import com.finbot.Beta.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTtokenizer jwtTokenizer;

    @Override
    public UserResponseDto registerUser(SignupDto signupDto) {
        if (!signupDto.getPassword().equals(signupDto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        if (userRepository.findByEmail(signupDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists, try login");
        }

        User user = User.builder()
                .name(signupDto.getName())
                .email(signupDto.getEmail())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .registeredOn(LocalDate.now())
                .build();

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    @Override
    public AuthResponseDto loginUser(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Generate JWT token
        String token = jwtTokenizer.generateToken(user.getId().toString());

        // Return authentication response with token
        return AuthResponseDto.builder()
                .token(token)
                .tokenType("Bearer")
                .user(toResponse(user))
                .build();
    }

    @Override
    public UserResponseDto updateAll(UUID userId, UpdatedUserDto updatedUserDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (updatedUserDto.getName() != null && !updatedUserDto.getName().isEmpty()) {
            user.setName(updatedUserDto.getName());
        }

        if (updatedUserDto.getPassword() != null && !updatedUserDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
        }

        if (updatedUserDto.getEmail() != null && !updatedUserDto.getEmail().isEmpty()) {
            if (userRepository.findByEmail(updatedUserDto.getEmail()).isPresent()) {
                throw new UserAlreadyExistsException("Email already exists, try a different one");
            }
            user.setEmail(updatedUserDto.getEmail());
        }

        User updatedUser = userRepository.save(user);
        return toResponse(updatedUser);
    }

    @Override
    public UserResponseDto updateUserPassword(UUID userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        User updatedUser = userRepository.save(user);

        return toResponse(updatedUser);
    }

    @Override
    public UserResponseDto updateUserEmail(UUID userId, String newEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (userRepository.findByEmail(newEmail).isPresent()) {
            throw new UserAlreadyExistsException("Email already in use");
        }

        user.setEmail(newEmail);
        User updatedUser = userRepository.save(user);

        return toResponse(updatedUser);
    }

    @Override
    public UserResponseDto deleteUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.delete(user);
        return toResponse(user);
    }

    private UserResponseDto toResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId().toString())
                .name(user.getName())
                .email(user.getEmail())
                .registeredOn(user.getRegisteredOn())
                .build();
    }
}
