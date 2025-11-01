package com.finbot.Beta.controller;

import com.finbot.Beta.Dto.*;
import com.finbot.Beta.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
//@CrossOrigin(origins = {
//        "http://localhost:5500",
//        "http://127.0.0.1:5500",
//        "http://10.30.22.61:5500",
//        "http://172.20.240.1:5500"
//})

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody SignupDto signupDto) {
        UserResponseDto savedUser = userService.registerUser(signupDto);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@RequestBody LoginDto loginDto) {
        AuthResponseDto authResponse = userService.loginUser(loginDto);
        return ResponseEntity.ok(authResponse);
    }

    @PutMapping("/update/password/{userId}")
    public ResponseEntity<UserResponseDto> updateUserPassword(
            @PathVariable UUID userId,
            @RequestBody UpdatePasswordDto dto) {

        UserResponseDto updatedUser = userService.updateUserPassword(userId, dto.getNewPassword());
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/update/email/{userId}")
    public ResponseEntity<UserResponseDto> updateUserEmail(
            @PathVariable UUID userId,
            @RequestBody UpdateEmailDto dto) {

        UserResponseDto updatedUser = userService.updateUserEmail(userId, dto.getNewEmail());
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/updateAll/{userId}")
    public ResponseEntity<UserResponseDto> updateAll(
            @PathVariable UUID userId,
            @RequestBody UpdatedUserDto updatedUserDto) {

        return ResponseEntity.ok(userService.updateAll(userId, updatedUserDto));
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.ok("User deleted successfully");
    }
}
