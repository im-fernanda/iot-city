package com.iotcitybackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Dados formatados de sensor IoT")
public class SensorDataDTO {
    @Schema(description = "ID único do registro", example = "1")
    private Long id;
    
    @Schema(description = "Dispositivo que coletou os dados")
    private DeviceDTO device;
    
    @Schema(description = "Tipo do sensor", example = "TEMPERATURA")
    private String sensorType;
    
    @Schema(description = "Valor medido pelo sensor (formatado com 2 casas decimais)", example = "25.50")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String value;
    
    @Schema(description = "Unidade de medida", example = "CELSIUS")
    private String unit;
    
    @Schema(description = "Data e hora da medição", example = "2024-03-15T10:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
    
    @Schema(description = "Latitude da localização", example = "-5.7793")
    private Double latitude;
    
    @Schema(description = "Longitude da localização", example = "-35.2009")
    private Double longitude;
}

