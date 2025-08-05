package com.finbot.Beta.service;

import com.finbot.Beta.Dto.LoginDto;
import com.finbot.Beta.Dto.SignupDto;
import com.finbot.Beta.Dto.UpdatedUserDto;
import com.finbot.Beta.Dto.UserResponseDto;

import java.util.UUID;

public interface UserService {
    UserResponseDto registerUser(SignupDto signupDto);
    UserResponseDto loginUser(LoginDto loginDto);
    UserResponseDto updateAll(UUID userId, UpdatedUserDto updatedUserDto);
    UserResponseDto updateUserPassword(UUID userId, String newPassword);
    UserResponseDto updateUserEmail(UUID userId, String newEmail);
    UserResponseDto deleteUserById(UUID userId);

}
