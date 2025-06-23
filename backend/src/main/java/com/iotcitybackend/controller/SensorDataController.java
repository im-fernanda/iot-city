package com.iotcitybackend.controller;

import com.iotcitybackend.service.SensorDataService;
import com.iotcitybackend.model.SensorData;
import com.iotcitybackend.service.DeviceService;
import com.iotcitybackend.model.Device;
import com.iotcitybackend.exception.ErrorCodes;
import com.iotcitybackend.dto.ErrorResponse;
import com.iotcitybackend.dto.DeviceDTO;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sensor-data")
@CrossOrigin(origins = "*")
@Tag(name = "Sensor Data", description = "APIs para gerenciamento de dados de sensores")
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
            @Parameter(description = "Longitude da localização") @RequestParam(required = false) Double longitude) {
        
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
        return sensorDataService.getSensorDataById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    @Operation(
        summary = "Busca avançada de dados de sensor", 
        description = "Busca dados de sensor com filtros opcionais por tipo, dispositivo e período. Se nenhum filtro for fornecido, retorna todos os dados."
    )
    public ResponseEntity<List<SensorData>> findSensorData(
            @Parameter(description = "Tipo do sensor (ex: TEMPERATURE)") @RequestParam(required = false) String sensorType,
            @Parameter(description = "ID do dispositivo") @RequestParam(required = false) Long deviceId,
            @Parameter(description = "Data de início do período (ISO 8601)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "Data de fim do período (ISO 8601)") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        
        List<SensorData> data = sensorDataService.findSensorData(sensorType, deviceId, start, end);
        return ResponseEntity.ok(data);
    }
    
    @GetMapping("/types")
    @Operation(summary = "Listar tipos de sensor únicos", description = "Retorna todos os tipos de sensor distintos disponíveis nos dados")
    public ResponseEntity<List<String>> getSensorTypes() {
        List<String> types = sensorDataService.getSensorTypes();
        return ResponseEntity.ok(types);
    }
    
    @GetMapping("/devices-by-type/{sensorType}")
    @Operation(
        summary = "Buscar dispositivos por tipo de sensor", 
        description = "Retorna uma lista de dispositivos que possuem dados para o tipo de sensor especificado."
    )
    public ResponseEntity<List<DeviceDTO>> getDevicesBySensorType(@PathVariable String sensorType) {
        List<SensorData> data = sensorDataService.findSensorData(sensorType, null, null, null);
        List<DeviceDTO> devices = data.stream()
            .map(d -> {
                Device device = d.getDevice();
                return DeviceDTO.builder()
                        .id(device.getId())
                        .name(device.getName())
                        .type(device.getType())
                        .location(device.getLocation())
                        .build();
            })
            .distinct()
            .collect(Collectors.toList());
        return ResponseEntity.ok(devices);
    }
    
    @GetMapping("/latest/device/{deviceId}")
    @Operation(summary = "Dados mais recentes do dispositivo", description = "Retorna os dados mais recentes de um dispositivo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dados encontrados"),
        @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado ou sem dados")
    })
    public ResponseEntity<SensorData> getLatestDataByDevice(
            @Parameter(description = "ID do dispositivo") @PathVariable Long deviceId) {
        return sensorDataService.getLatestDataByDevice(deviceId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/average")
    @Operation(summary = "Média de valores por tipo de sensor", description = "Calcula a média dos valores de um tipo de sensor em um período específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Média calculada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Filtros insuficientes ou período inválido"),
        @ApiResponse(responseCode = "404", description = "Nenhum dado encontrado para os filtros especificados")
    })
    public ResponseEntity<Double> getAverageValue(
            @Parameter(description = "Tipo do sensor") @RequestParam String sensorType,
            @Parameter(description = "Data de início") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Data de fim") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        Double average = sensorDataService.getAverageValueBySensorType(sensorType, startDate, endDate);
        if (average != null) {
            return ResponseEntity.ok(average);
        }
        return ResponseEntity.notFound().build();
    }
} 