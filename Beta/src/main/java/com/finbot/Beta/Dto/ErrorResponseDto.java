package com.finbot.Beta.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ErrorResponseDto {
    private String message;
    private String details;
    private int status;
    private LocalDateTime timestamp;

}
