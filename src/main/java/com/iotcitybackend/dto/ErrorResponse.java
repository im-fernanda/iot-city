package com.iotcitybackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Resposta de erro padronizada")
public class ErrorResponse {
    
    @Schema(description = "Código do erro", example = "DEVICE_001")
    private String errorCode;
    
    @Schema(description = "Mensagem de erro", example = "Dispositivo não encontrado")
    private String message;
    
    @Schema(description = "Descrição detalhada do erro", example = "O dispositivo com ID 999 não foi encontrado no sistema")
    private String details;
    
    @Schema(description = "Timestamp do erro", example = "2024-03-19T11:15:30")
    private LocalDateTime timestamp;
    
    @Schema(description = "Caminho da requisição", example = "/api/devices/999")
    private String path;
    
    public static ErrorResponse of(String errorCode, String message, String details, String path) {
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .message(message)
                .details(details)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
    }
} 