package org.example.backend.exception;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final MessageSource messageSource;
    private final Locale locale = Locale.getDefault();

    public GlobalExceptionHandler(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex){
        String translatedMessage = messageSource.getMessage(
                ex.getMessage(),
                ex.getArgs(),
                ex.getMessage(),
                this.locale
        );

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", translatedMessage);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }
}
