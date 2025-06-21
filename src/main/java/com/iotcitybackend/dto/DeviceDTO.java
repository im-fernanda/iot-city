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
@Schema(description = "Dados do dispositivo IoT")
public class DeviceDTO {
    @Schema(description = "ID único do dispositivo (gerado automaticamente)", example = "1")
    private Long id;
    
    @Schema(description = "Nome do dispositivo", example = "Semaforo_Petropolis_01", required = true)
    private String name;
    
    @Schema(description = "Tipo do dispositivo", example = "TRAFFIC_LIGHT", required = true, 
            allowableValues = {"TRAFFIC_LIGHT", "AIR_QUALITY", "STREET_LIGHT", "WATER_LEVEL", "NOISE_SENSOR", "WEATHER_SENSOR", "SECURITY_CAMERA", "PARKING_SENSOR", "WASTE_SENSOR", "SOLAR_PANEL"})
    private String type;
    
    @Schema(description = "Localização do dispositivo", example = "Avenida Hermes da Fonseca, Petrópolis, Natal/RN", required = true)
    private String location;
    
    @Schema(description = "Status ativo/inativo do dispositivo", example = "true", defaultValue = "true")
    private boolean active;
    
    @Schema(description = "Última vez que o dispositivo foi visto (preenchido automaticamente)", example = "2024-03-19T10:30:00")
    private LocalDateTime lastSeen;
    
    @Schema(description = "Nível de bateria (0-100)", example = "85", minimum = "0", maximum = "100")
    private Integer batteryLevel;
    
    @Schema(description = "Força do sinal (0-100)", example = "92", minimum = "0", maximum = "100")
    private Integer signalStrength;
    
    @Schema(description = "Data de criação (preenchido automaticamente)", example = "2024-03-19T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "Data da última atualização (preenchido automaticamente)", example = "2024-03-19T10:30:00")
    private LocalDateTime updatedAt;
} 