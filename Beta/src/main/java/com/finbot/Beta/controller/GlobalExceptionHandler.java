package com.finbot.Beta.controller;

import com.finbot.Beta.Dto.ErrorResponseDto;
import com.finbot.Beta.Exceptions.UserAlreadyExistsException;
import com.finbot.Beta.Exceptions.ResourceNotFoundException;
import com.finbot.Beta.Exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserExists(UserAlreadyExistsException ex) {
        ErrorResponseDto userAlreadyExistsError = ErrorResponseDto.builder()
                .message("USER_ALREADY_EXISTS")
                .details(ex.getMessage())
                .status(HttpStatus.CONFLICT.value()) // 409
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(userAlreadyExistsError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponseDto userNotFoundError = ErrorResponseDto.builder()
                .message("USER_NOT_FOUND")
                .details(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value()) // 404
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(userNotFoundError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponseDto resourceNotFoundError = ErrorResponseDto.builder()
                .message("RESOURCE_NOT_FOUND")
                .details(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value()) // 404
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(resourceNotFoundError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneric(Exception ex) {
        ex.printStackTrace(); // ✅ Print to logs during dev

        ErrorResponseDto genericError = ErrorResponseDto.builder()
                .message("INTERNAL_SERVER_ERROR")
                .details(ex.getMessage()) // This will now be visible in Postman
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(genericError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
