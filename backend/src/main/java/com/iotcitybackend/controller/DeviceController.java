package com.iotcitybackend.controller;

import com.iotcitybackend.model.Device;
import com.iotcitybackend.service.DeviceService;
import com.iotcitybackend.dto.DeviceDTO;
import com.iotcitybackend.dto.DeviceStatsDTO;
import com.iotcitybackend.dto.OfflineTimeoutConfigDTO;
import com.iotcitybackend.dto.ErrorResponse;
import com.iotcitybackend.exception.ErrorCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/devices")
@CrossOrigin(origins = "*") // Para permitir CORS com frontend
@Tag(name = "Dispositivos", description = "API para gerenciamento de dispositivos IoT")
public class DeviceController {
    
    private static final Set<String> VALID_DEVICE_TYPES = Set.of(
        "TRAFFIC_LIGHT", "AIR_QUALITY", "STREET_LIGHT", "WATER_LEVEL", 
        "NOISE_SENSOR", "WEATHER_SENSOR", "SECURITY_CAMERA", "PARKING_SENSOR", 
        "WASTE_SENSOR", "SOLAR_PANEL"
    );
    
    private final DeviceService deviceService;
    
    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    @Operation(
        summary = "Criar novo dispositivo", 
        description = """
            Cria um novo dispositivo no sistema.
            
            **Campos obrigatórios:**
            - `name`: Nome do dispositivo
            - `type`: Tipo do dispositivo (TRAFFIC_LIGHT, AIR_QUALITY, etc.)
            - `location`: Localização do dispositivo
            
            **Campos opcionais:**
            - `active`: Status ativo/inativo (padrão: true)
            - `batteryLevel`: Nível de bateria (0-100)
            - `signalStrength`: Força do sinal (0-100)
            
            **Campos preenchidos automaticamente:**
            - `id`: ID único gerado automaticamente
            - `lastSeen`: Data/hora atual
            - `createdAt`: Data/hora de criação
            - `updatedAt`: Data/hora da última atualização
            """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "Dispositivo criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DeviceDTO.class)
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
    public ResponseEntity<?> createDevice(
            @Parameter(description = "Nome do dispositivo", example = "Semaforo_Teste_01") @RequestParam String name,
            @Parameter(description = "Tipo do dispositivo", example = "TRAFFIC_LIGHT") @RequestParam String type,
            @Parameter(description = "Localização do dispositivo", example = "Rua Teste, Natal/RN") @RequestParam String location,
            @Parameter(description = "Status ativo/inativo", example = "true") @RequestParam(defaultValue = "true") boolean active,
            @Parameter(description = "Nível de bateria (0-100)", example = "85") @RequestParam(required = false) Integer batteryLevel,
            @Parameter(description = "Força do sinal (0-100)", example = "90") @RequestParam(required = false) Integer signalStrength) {
        
        // Validações
        if (name == null || name.trim().isEmpty()) {
            ErrorResponse error = ErrorResponse.of(
                ErrorCodes.DEVICE_NAME_REQUIRED,
                "Nome do dispositivo é obrigatório",
                "O campo 'name' não pode estar vazio",
                "/api/devices"
            );
            return ResponseEntity.badRequest().body(error);
        }
        
        if (type == null || type.trim().isEmpty()) {
            ErrorResponse error = ErrorResponse.of(
                ErrorCodes.DEVICE_TYPE_REQUIRED,
                "Tipo do dispositivo é obrigatório",
                "O campo 'type' não pode estar vazio",
                "/api/devices"
            );
            return ResponseEntity.badRequest().body(error);
        }
        
        if (!VALID_DEVICE_TYPES.contains(type)) {
            ErrorResponse error = ErrorResponse.of(
                ErrorCodes.DEVICE_INVALID_TYPE,
                "Tipo de dispositivo inválido",
                "O tipo '" + type + "' não é válido. Tipos válidos: " + String.join(", ", VALID_DEVICE_TYPES),
                "/api/devices"
            );
            return ResponseEntity.badRequest().body(error);
        }
        
        if (location == null || location.trim().isEmpty()) {
            ErrorResponse error = ErrorResponse.of(
                ErrorCodes.DEVICE_LOCATION_REQUIRED,
                "Localização do dispositivo é obrigatória",
                "O campo 'location' não pode estar vazio",
                "/api/devices"
            );
            return ResponseEntity.badRequest().body(error);
        }
        
        if (batteryLevel != null && (batteryLevel < 0 || batteryLevel > 100)) {
            ErrorResponse error = ErrorResponse.of(
                ErrorCodes.DEVICE_BATTERY_INVALID,
                "Nível de bateria inválido",
                "O nível de bateria deve estar entre 0 e 100",
                "/api/devices"
            );
            return ResponseEntity.badRequest().body(error);
        }
        
        if (signalStrength != null && (signalStrength < 0 || signalStrength > 100)) {
            ErrorResponse error = ErrorResponse.of(
                ErrorCodes.DEVICE_SIGNAL_INVALID,
                "Força do sinal inválida",
                "A força do sinal deve estar entre 0 e 100",
                "/api/devices"
            );
            return ResponseEntity.badRequest().body(error);
        }
        
        Device device = new Device();
        device.setName(name.trim());
        device.setType(type.trim());
        device.setLocation(location.trim());
        device.setActive(active);
        device.setBatteryLevel(batteryLevel);
        device.setSignalStrength(signalStrength);
        
        Device savedDevice = deviceService.createDevice(device);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(savedDevice));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar dispositivo", 
        description = """
            Atualiza um dispositivo existente pelo ID.
            
            **Campos que podem ser atualizados:**
            - `name`: Nome do dispositivo
            - `type`: Tipo do dispositivo
            - `location`: Localização do dispositivo
            - `active`: Status ativo/inativo
            - `batteryLevel`: Nível de bateria (0-100)
            - `signalStrength`: Força do sinal (0-100)
            
            **Campos preenchidos automaticamente:**
            - `updatedAt`: Data/hora da última atualização
            """
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Dispositivo atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = DeviceDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Dispositivo não encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(example = "{\"error\": \"Dispositivo com ID 999 não encontrado\"}")
            )
        )
    })
    public ResponseEntity<DeviceDTO> updateDevice(
            @Parameter(description = "ID do dispositivo a ser atualizado", example = "1") @PathVariable Long id,
            @Parameter(description = "Nome do dispositivo", example = "Semaforo_Petropolis_01_Atualizado") @RequestParam(required = false) String name,
            @Parameter(description = "Tipo do dispositivo", example = "TRAFFIC_LIGHT") @RequestParam(required = false) String type,
            @Parameter(description = "Localização do dispositivo", example = "Avenida Hermes da Fonseca, Petrópolis, Natal/RN") @RequestParam(required = false) String location,
            @Parameter(description = "Status ativo/inativo", example = "true") @RequestParam(required = false) Boolean active,
            @Parameter(description = "Nível de bateria (0-100)", example = "90") @RequestParam(required = false) Integer batteryLevel,
            @Parameter(description = "Força do sinal (0-100)", example = "95") @RequestParam(required = false) Integer signalStrength) {
        
        Device deviceDetails = new Device();
        deviceDetails.setName(name);
        deviceDetails.setType(type);
        deviceDetails.setLocation(location);
        if (active != null) {
            deviceDetails.setActive(active);
        }
        deviceDetails.setBatteryLevel(batteryLevel);
        deviceDetails.setSignalStrength(signalStrength);
        
        Device updatedDevice = deviceService.updateDevice(id, deviceDetails);
        if (updatedDevice != null) {
            return ResponseEntity.ok(convertToDTO(updatedDevice));
        }
        return ResponseEntity.notFound().build();
    }
    

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar dispositivo", description = "Remove um dispositivo do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Dispositivo deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    })
    public ResponseEntity<Void> deleteDevice(
            @Parameter(description = "ID do dispositivo") @PathVariable Long id) {
        boolean deleted = deviceService.deleteDevice(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    @Operation(summary = "Listar todos os dispositivos", description = "Retorna uma lista de todos os dispositivos cadastrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de dispositivos retornada com sucesso")
    })
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        List<Device> devices = deviceService.getAllDevices();
        List<DeviceDTO> deviceDTOs = devices.stream()
                .map(this::convertToDTO)
                .toList();
        return ResponseEntity.ok(deviceDTOs);
    }
    

    @GetMapping("/{id}")
    @Operation(summary = "Buscar dispositivo por ID", description = "Retorna um dispositivo específico pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Dispositivo encontrado"),
        @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    })
    public ResponseEntity<DeviceDTO> getDeviceById(
            @Parameter(description = "ID do dispositivo") @PathVariable Long id) {
        Optional<Device> device = deviceService.getDeviceById(id);
        if (device.isPresent()) {
            return ResponseEntity.ok(convertToDTO(device.get()));
        }
        return ResponseEntity.notFound().build();
    }
    
    
    @GetMapping("/type/{type}")
    @Operation(summary = "Buscar dispositivos por tipo", description = "Retorna uma lista de dispositivos de um tipo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de dispositivos retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum dispositivo encontrado para o tipo especificado")
    })
    public ResponseEntity<List<DeviceDTO>> getDevicesByType(
            @Parameter(description = "Tipo do dispositivo") @PathVariable String type) {
        List<Device> devices = deviceService.getDevicesByType(type);
        List<DeviceDTO> deviceDTOs = devices.stream()
                .map(this::convertToDTO)
                .toList();
        return ResponseEntity.ok(deviceDTOs);
    }
    
    @GetMapping("/active")
    @Operation(summary = "Buscar dispositivos ativos", description = "Retorna uma lista de todos os dispositivos ativos no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de dispositivos ativos retornada com sucesso")
    })
    public ResponseEntity<List<DeviceDTO>> getActiveDevices() {
        List<Device> devices = deviceService.getActiveDevices();
        List<DeviceDTO> deviceDTOs = devices.stream()
                .map(this::convertToDTO)
                .toList();
        return ResponseEntity.ok(deviceDTOs);
    }
    
    @GetMapping("/location/{location}")
    @Operation(summary = "Buscar dispositivos por localização", description = "Retorna uma lista de dispositivos em uma localização específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de dispositivos retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum dispositivo encontrado na localização especificada")
    })
    public ResponseEntity<List<DeviceDTO>> getDevicesByLocation(
            @Parameter(description = "Localização dos dispositivos") @PathVariable String location) {
        List<Device> devices = deviceService.getDevicesByLocation(location);
        List<DeviceDTO> deviceDTOs = devices.stream()
                .map(this::convertToDTO)
                .toList();
        return ResponseEntity.ok(deviceDTOs);
    }
    
    @GetMapping("/offline")
    @Operation(summary = "Buscar dispositivos offline", description = "Retorna uma lista de dispositivos que não foram vistos há mais tempo que o timeout configurado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de dispositivos offline retornada com sucesso")
    })
    public ResponseEntity<List<DeviceDTO>> getOfflineDevices() {
        List<Device> devices = deviceService.getOfflineDevices();
        List<DeviceDTO> deviceDTOs = devices.stream()
                .map(this::convertToDTO)
                .toList();
        return ResponseEntity.ok(deviceDTOs);
    }
    
    @GetMapping("/low-battery")
    @Operation(summary = "Buscar dispositivos com bateria baixa", description = "Retorna uma lista de dispositivos com nível de bateria abaixo de 20%")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de dispositivos com bateria baixa retornada com sucesso")
    })
    public ResponseEntity<List<DeviceDTO>> getDevicesWithLowBattery() {
        List<Device> devices = deviceService.getDevicesWithLowBattery();
        List<DeviceDTO> deviceDTOs = devices.stream()
                .map(this::convertToDTO)
                .toList();
        return ResponseEntity.ok(deviceDTOs);
    }
    
    @PatchMapping("/{id}/toggle")
    @Operation(summary = "Ativar/desativar dispositivo", description = "Alterna o status ativo/inativo de um dispositivo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status do dispositivo alterado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    })
    public ResponseEntity<DeviceDTO> toggleDeviceStatus(
            @Parameter(description = "ID do dispositivo") @PathVariable Long id) {
        Device device = deviceService.toggleDeviceStatus(id);
        if (device != null) {
            return ResponseEntity.ok(convertToDTO(device));
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping("/{id}/heartbeat")
    @Operation(summary = "Heartbeat do dispositivo", description = "Dispositivo IoT reporta seu status e atualiza lastSeen")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Heartbeat registrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    })
    public ResponseEntity<DeviceDTO> deviceHeartbeat(
            @Parameter(description = "ID do dispositivo") @PathVariable Long id,
            @Parameter(description = "Nível de bateria") @RequestParam(required = false) Integer batteryLevel,
            @Parameter(description = "Força do sinal") @RequestParam(required = false) Integer signalStrength) {
        Device device = deviceService.deviceHeartbeat(id, batteryLevel, signalStrength);
        if (device != null) {
            return ResponseEntity.ok(convertToDTO(device));
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/stats")
    @Operation(summary = "Obter estatísticas dos dispositivos", description = "Retorna estatísticas gerais sobre os dispositivos no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso")
    })
    public ResponseEntity<DeviceStatsDTO> getDeviceStats() {
        DeviceStatsDTO stats = new DeviceStatsDTO(
            deviceService.getTotalDevices(),
            deviceService.getActiveDevicesCount(),
            deviceService.getOfflineDevicesCount()
        );
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/config/offline-timeout")
    @Operation(summary = "Obter timeout de dispositivos offline", description = "Retorna o tempo em minutos para considerar um dispositivo offline")
    public ResponseEntity<OfflineTimeoutConfigDTO> getOfflineTimeout() {
        OfflineTimeoutConfigDTO config = new OfflineTimeoutConfigDTO(
            deviceService.getOfflineTimeoutMinutes()
        );
        return ResponseEntity.ok(config);
    }
    
    @PutMapping("/config/offline-timeout")
    @Operation(summary = "Configurar timeout de dispositivos offline", description = "Define o tempo em minutos para considerar um dispositivo offline")
    public ResponseEntity<OfflineTimeoutConfigDTO> setOfflineTimeout(
            @Parameter(description = "Timeout em minutos") @RequestParam int timeoutMinutes) {
        if (timeoutMinutes <= 0) {
            return ResponseEntity.badRequest().build();
        }
        deviceService.setOfflineTimeoutMinutes(timeoutMinutes);
        OfflineTimeoutConfigDTO config = new OfflineTimeoutConfigDTO(timeoutMinutes);
        return ResponseEntity.ok(config);
    }

    private DeviceDTO convertToDTO(Device device) {
        DeviceDTO dto = new DeviceDTO();
        dto.setId(device.getId());
        dto.setName(device.getName());
        dto.setType(device.getType());
        dto.setLocation(device.getLocation());
        dto.setActive(device.isActive());
        dto.setLastSeen(device.getLastSeen());
        dto.setBatteryLevel(device.getBatteryLevel());
        dto.setSignalStrength(device.getSignalStrength());
        dto.setCreatedAt(device.getCreatedAt());
        dto.setUpdatedAt(device.getUpdatedAt());
        return dto;
    }
    
    private Device convertToEntity(DeviceDTO dto) {
        Device device = new Device();
        device.setName(dto.getName());
        device.setType(dto.getType());
        device.setLocation(dto.getLocation());
        device.setActive(dto.isActive());
        device.setBatteryLevel(dto.getBatteryLevel());
        device.setSignalStrength(dto.getSignalStrength());
        return device;
    }
} 