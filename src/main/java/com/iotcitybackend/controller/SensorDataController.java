package com.iotcitybackend.controller;

import com.iotcitybackend.service.SensorDataService;
import com.iotcitybackend.model.SensorData;
import com.iotcitybackend.service.DeviceService;
import com.iotcitybackend.model.Device;
import com.iotcitybackend.exception.ErrorCodes;
import com.iotcitybackend.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/sensor-data")
@CrossOrigin(origins = "*")
@Tag(name = "Dados de Sensor", description = "API para gerenciamento de dados de sensores IoT")
public class SensorDataController {
    
    private static final Set<String> VALID_SENSOR_TYPES = Set.of(
        "TEMPERATURE", "HUMIDITY", "AIR_QUALITY", "NOISE", "LIGHT", "MOTION"
    );
    
    private static final Set<String> VALID_UNITS = Set.of(
        "CELSIUS", "FAHRENHEIT", "PERCENTAGE", "PPM", "DB", "LUX", "BOOLEAN"
    );
    
    private final SensorDataService sensorDataService;
    private final DeviceService deviceService;
    
    public SensorDataController(SensorDataService sensorDataService, DeviceService deviceService) {
        this.sensorDataService = sensorDataService;
        this.deviceService = deviceService;
    }
    
    @PostMapping
    @Operation(
        summary = "Receber dados de sensor",
        description = "Recebe e armazena dados de um sensor IoT"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Dados recebidos com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = SensorData.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public ResponseEntity<?> receiveSensorData(
            @Parameter(description = "ID do dispositivo") @RequestParam Long deviceId,
            @Parameter(description = "Tipo do sensor") @RequestParam String sensorType,
            @Parameter(description = "Valor medido pelo sensor") @RequestParam Double value,
            @Parameter(description = "Unidade de medida") @RequestParam(required = false) String unit,
            @Parameter(description = "Latitude da localização") @RequestParam(required = false) Double latitude,
            @Parameter(description = "Longitude da localização") @RequestParam(required = false) Double longitude,
            @Parameter(description = "Data/hora da medição") @RequestParam(required = false) LocalDateTime timestamp) {
        
        // Validações
        if (deviceId == null || deviceId <= 0) {
            ErrorResponse error = ErrorResponse.of(
                ErrorCodes.SENSOR_DEVICE_ID_REQUIRED,
                "ID do dispositivo é obrigatório",
                "O campo 'deviceId' deve ser um número positivo",
                "/api/sensor-data"
            );
            return ResponseEntity.badRequest().body(error);
        }
        
        if (sensorType == null || sensorType.trim().isEmpty()) {
            ErrorResponse error = ErrorResponse.of(
                ErrorCodes.SENSOR_TYPE_REQUIRED,
                "Tipo do sensor é obrigatório",
                "O campo 'sensorType' não pode estar vazio",
                "/api/sensor-data"
            );
            return ResponseEntity.badRequest().body(error);
        }
        
        if (!VALID_SENSOR_TYPES.contains(sensorType)) {
            ErrorResponse error = ErrorResponse.of(
                ErrorCodes.SENSOR_INVALID_TYPE,
                "Tipo de sensor inválido",
                "O tipo '" + sensorType + "' não é válido. Tipos válidos: " + String.join(", ", VALID_SENSOR_TYPES),
                "/api/sensor-data"
            );
            return ResponseEntity.badRequest().body(error);
        }
        
        if (value == null) {
            ErrorResponse error = ErrorResponse.of(
                ErrorCodes.SENSOR_VALUE_REQUIRED,
                "Valor do sensor é obrigatório",
                "O campo 'value' não pode estar vazio",
                "/api/sensor-data"
            );
            return ResponseEntity.badRequest().body(error);
        }
        
        if (unit != null && !VALID_UNITS.contains(unit)) {
            ErrorResponse error = ErrorResponse.of(
                ErrorCodes.SENSOR_INVALID_UNIT,
                "Unidade de medida inválida",
                "A unidade '" + unit + "' não é válida. Unidades válidas: " + String.join(", ", VALID_UNITS),
                "/api/sensor-data"
            );
            return ResponseEntity.badRequest().body(error);
        }
        
        if (latitude != null && (latitude < -90 || latitude > 90)) {
            ErrorResponse error = ErrorResponse.of(
                ErrorCodes.SENSOR_INVALID_COORDINATES,
                "Latitude inválida",
                "A latitude deve estar entre -90 e 90",
                "/api/sensor-data"
            );
            return ResponseEntity.badRequest().body(error);
        }
        
        if (longitude != null && (longitude < -180 || longitude > 180)) {
            ErrorResponse error = ErrorResponse.of(
                ErrorCodes.SENSOR_INVALID_COORDINATES,
                "Longitude inválida",
                "A longitude deve estar entre -180 e 180",
                "/api/sensor-data"
            );
            return ResponseEntity.badRequest().body(error);
        }
        
        // Buscar o dispositivo
        Optional<Device> deviceOpt = deviceService.getDeviceById(deviceId);
        if (deviceOpt.isEmpty()) {
            ErrorResponse error = ErrorResponse.of(
                ErrorCodes.SENSOR_DEVICE_NOT_FOUND,
                "Dispositivo não encontrado",
                "O dispositivo com ID " + deviceId + " não foi encontrado no sistema",
                "/api/sensor-data"
            );
            return ResponseEntity.badRequest().body(error);
        }
        
        SensorData sensorData = new SensorData();
        sensorData.setDevice(deviceOpt.get());
        sensorData.setSensorType(sensorType.trim());
        sensorData.setValue(value);
        sensorData.setUnit(unit);
        sensorData.setLatitude(latitude);
        sensorData.setLongitude(longitude);
        sensorData.setTimestamp(timestamp);
        
        SensorData savedData = sensorDataService.saveSensorData(sensorData);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedData);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar dados de sensor por ID", description = "Retorna dados específicos de um sensor pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados encontrados"),
        @ApiResponse(responseCode = "404", description = "Dados não encontrados")
    })
    public ResponseEntity<SensorData> getSensorDataById(
            @Parameter(description = "ID dos dados de sensor") @PathVariable Long id) {
        SensorData data = sensorDataService.getSensorDataById(id);
        if (data != null) {
            return ResponseEntity.ok(data);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping
    @Operation(summary = "Listar todos os dados de sensor", description = "Retorna todos os dados de sensores cadastrados no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de dados retornada com sucesso")
    })
    public ResponseEntity<List<SensorData>> getAllSensorData() {
        List<SensorData> data = sensorDataService.getAllSensorData();
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("/device/{deviceId}")
    @Operation(summary = "Buscar dados por dispositivo", description = "Retorna todos os dados de um dispositivo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados encontrados"),
        @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    })
    public ResponseEntity<List<SensorData>> getDataByDevice(
            @Parameter(description = "ID do dispositivo") @PathVariable Long deviceId) {
        List<SensorData> data = sensorDataService.getDataByDevice(deviceId);
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("/type/{sensorType}")
    @Operation(summary = "Buscar dados por tipo de sensor", description = "Retorna dados de um tipo específico de sensor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados encontrados"),
        @ApiResponse(responseCode = "404", description = "Nenhum dado encontrado para o tipo especificado")
    })
    public ResponseEntity<List<SensorData>> getDataBySensorType(
            @Parameter(description = "Tipo do sensor") @PathVariable String sensorType) {
        List<SensorData> data = sensorDataService.getDataBySensorType(sensorType);
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("/device/{deviceId}/type/{sensorType}")
    @Operation(summary = "Buscar dados por dispositivo e tipo", description = "Retorna dados de um dispositivo específico e tipo de sensor")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados encontrados"),
        @ApiResponse(responseCode = "404", description = "Nenhum dado encontrado para os critérios especificados")
    })
    public ResponseEntity<List<SensorData>> getDataByDeviceAndType(
            @Parameter(description = "ID do dispositivo") @PathVariable Long deviceId, 
            @Parameter(description = "Tipo do sensor") @PathVariable String sensorType) {
        List<SensorData> data = sensorDataService.getDataByDeviceAndType(deviceId, sensorType);
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("/period")
    @Operation(summary = "Buscar dados em período específico", description = "Retorna dados de sensores em um período de tempo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados encontrados"),
        @ApiResponse(responseCode = "400", description = "Período inválido")
    })
    public ResponseEntity<List<SensorData>> getDataByPeriod(
            @Parameter(description = "Data de início") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Data de fim") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<SensorData> data = sensorDataService.getDataByPeriod(startDate, endDate);
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("/device/{deviceId}/period")
    @Operation(summary = "Buscar dados de dispositivo em período", description = "Retorna dados de um dispositivo específico em um período de tempo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados encontrados"),
        @ApiResponse(responseCode = "400", description = "Período inválido"),
        @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    })
    public ResponseEntity<List<SensorData>> getDataByDeviceAndPeriod(
            @Parameter(description = "ID do dispositivo") @PathVariable Long deviceId,
            @Parameter(description = "Data de início") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Data de fim") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<SensorData> data = sensorDataService.getDataByDeviceAndPeriod(deviceId, startDate, endDate);
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("/average/{sensorType}")
    @Operation(summary = "Média de valores por tipo de sensor", description = "Calcula a média dos valores de um tipo de sensor em um período específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Média calculada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Período inválido"),
        @ApiResponse(responseCode = "404", description = "Nenhum dado encontrado para o tipo especificado")
    })
    public ResponseEntity<Double> getAverageValueBySensorType(
            @Parameter(description = "Tipo do sensor") @PathVariable String sensorType,
            @Parameter(description = "Data de início") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Data de fim") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        Double average = sensorDataService.getAverageValueBySensorTypeAndPeriod(sensorType, startDate, endDate);
        return ResponseEntity.ok(average);
    }
    
    @GetMapping("/latest/device/{deviceId}")
    @Operation(summary = "Dados mais recentes do dispositivo", description = "Retorna os dados mais recentes de um dispositivo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados encontrados"),
        @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado ou sem dados")
    })
    public ResponseEntity<SensorData> getLatestDataByDevice(
            @Parameter(description = "ID do dispositivo") @PathVariable Long deviceId) {
        SensorData data = sensorDataService.getLatestDataByDevice(deviceId);
        if (data != null) {
            return ResponseEntity.ok(data);
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/location")
    @Operation(summary = "Buscar dados por localização geográfica", description = "Retorna dados de sensores em uma área geográfica específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados encontrados"),
        @ApiResponse(responseCode = "400", description = "Coordenadas inválidas")
    })
    public ResponseEntity<List<SensorData>> getDataByLocation(
            @Parameter(description = "Latitude mínima") @RequestParam Double minLat,
            @Parameter(description = "Latitude máxima") @RequestParam Double maxLat,
            @Parameter(description = "Longitude mínima") @RequestParam Double minLng,
            @Parameter(description = "Longitude máxima") @RequestParam Double maxLng) {
        List<SensorData> data = sensorDataService.getDataByLocationRange(minLat, maxLat, minLng, maxLng);
        return ResponseEntity.ok(data);
    }
} 