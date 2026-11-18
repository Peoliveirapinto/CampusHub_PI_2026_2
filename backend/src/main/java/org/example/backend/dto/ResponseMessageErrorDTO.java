package org.example.backend.dto;

import java.time.LocalDateTime;

public record ResponseMessageErrorDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message
) {}
