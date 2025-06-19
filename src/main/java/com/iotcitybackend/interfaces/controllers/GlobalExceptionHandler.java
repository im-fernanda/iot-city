package com.iotcitybackend.interfaces.controllers;

import com.iotcitybackend.interfaces.constants.ErrorCodes;
import com.iotcitybackend.interfaces.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
        ErrorResponse error = ErrorResponse.of(
            ErrorCodes.GENERAL_RESOURCE_NOT_FOUND,
            "Recurso não encontrado",
            ex.getMessage(),
            request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorResponse error = ErrorResponse.of(
            ErrorCodes.GENERAL_VALIDATION_ERROR,
            "Dados inválidos",
            ex.getMessage(),
            request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce("", (a, b) -> a + "; " + b);
        
        ErrorResponse error = ErrorResponse.of(
            ErrorCodes.GENERAL_VALIDATION_ERROR,
            "Erro de validação",
            "Campos inválidos: " + fieldErrors,
            request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        ErrorResponse error = ErrorResponse.of(
            ErrorCodes.GENERAL_VALIDATION_ERROR,
            "Tipo de parâmetro inválido",
            "O parâmetro '" + ex.getName() + "' deve ser do tipo " + ex.getRequiredType().getSimpleName(),
            request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        ErrorResponse error = ErrorResponse.of(
            ErrorCodes.GENERAL_INTERNAL_ERROR,
            "Erro interno do servidor",
            "Ocorreu um erro inesperado: " + ex.getMessage(),
            request.getDescription(false)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
} 