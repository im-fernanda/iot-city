package com.iotcitybackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Schema(description = "Representa dados coletados por um sensor IoT")
public class SensorData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único do registro de dados do sensor", example = "1")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    @Schema(description = "Dispositivo que coletou os dados")
    private Device device;
    
    @Column(nullable = false)
    @Schema(description = "Tipo do sensor", example = "TEMPERATURE", allowableValues = {"TEMPERATURE", "HUMIDITY", "AIR_QUALITY"})
    private String sensorType;
    
    @Column(name = "sensor_value", nullable = false)
    @Schema(description = "Valor medido pelo sensor", example = "25.5")
    private Double value;
    
    @Column(name = "unit")
    @Schema(description = "Unidade de medida", example = "CELSIUS", allowableValues = {"CELSIUS", "PERCENTAGE", "PPM"})
    private String unit;

    @Column(name = "timestamp", nullable = false)
    @Schema(description = "Data e hora da medição", example = "2024-03-15T10:30:00")
    private LocalDateTime timestamp;
    
    @Column(name = "latitude")
    @Schema(description = "Latitude da localização do sensor", example = "-5.7793")
    private Double latitude;
    
    @Column(name = "longitude")
    @Schema(description = "Longitude da localização do sensor", example = "-35.2009")
    private Double longitude;

    @Column(name = "created_at")
    @Schema(description = "Data e hora do registro no sistema", example = "2024-03-15T10:30:01")
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
} 