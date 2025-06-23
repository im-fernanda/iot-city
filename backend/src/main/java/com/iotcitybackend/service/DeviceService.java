package com.iotcitybackend.service;

import com.iotcitybackend.model.Device;
import com.iotcitybackend.repository.DeviceRepository;
import com.iotcitybackend.dto.DeviceDTO;
import com.iotcitybackend.dto.UpdateDeviceDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeviceService {

    // Configuração para timeout de dispositivos offline (em minutos)
    @Setter
    @Getter
    @Value("${device.offline.timeout.minutes:30}")
    private int offlineTimeoutMinutes;
    
    private final DeviceRepository deviceRepository;


    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Device createDevice(Device device) {
        device.setActive(true);
        device.setLastSeen(LocalDateTime.now());
        return deviceRepository.save(device);
    }
    
    // Buscar todos os dispositivos
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
    
    // Buscar dispositivo por ID
    public Optional<Device> getDeviceById(Long id) {
        return deviceRepository.findById(id);
    }
    
    // Atualizar dispositivo
    public Device updateDevice(Long id, Map<String, Object> updates) {
        Optional<Device> deviceOpt = deviceRepository.findById(id);
        if (deviceOpt.isPresent()) {
            Device device = deviceOpt.get();
            
            // Atualiza apenas os campos fornecidos no Map
            if (updates.containsKey("name")) {
                String name = (String) updates.get("name");
                if (name != null && !name.trim().isEmpty()) {
                    device.setName(name.trim());
                }
            }
            
            if (updates.containsKey("location")) {
                String location = (String) updates.get("location");
                if (location != null && !location.trim().isEmpty()) {
                    device.setLocation(location.trim());
                }
            }

            return deviceRepository.save(device);
        }
        return null;
    }
    
    // Heartbeat do dispositivo - para dispositivos IoT reportarem status
    public Device deviceHeartbeat(Long deviceId, Integer batteryLevel, Integer signalStrength) {
        Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
        if (deviceOpt.isPresent()) {
            Device device = deviceOpt.get();

            device.setLastSeen(LocalDateTime.now());
            // Atualiza dados de status se fornecidos
            if (batteryLevel != null) device.setBatteryLevel(batteryLevel);
            if (signalStrength != null) device.setSignalStrength(signalStrength);
            
            return deviceRepository.save(device);
        }
        return null;
    }
    
    // Deletar dispositivo
    public boolean deleteDevice(Long id) {
        if (deviceRepository.existsById(id)) {
            deviceRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Buscar dispositivos por tipo
    public List<Device> getDevicesByType(String type) {
        return deviceRepository.findByType(type);
    }
    
    // Buscar dispositivos ativos
    public List<Device> getActiveDevices() {
        return deviceRepository.findByActiveTrue();
    }
    
    // Buscar dispositivos por localização
    public List<Device> getDevicesByLocation(String location) {
        return deviceRepository.findByLocationContainingIgnoreCase(location);
    }
    
    // Buscar dispositivos offline (não vistos há mais de X minutos)
    public List<Device> getOfflineDevices() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(offlineTimeoutMinutes);
        return deviceRepository.findDevicesNotSeenSince(threshold);
    }
    
    // Buscar dispositivos com bateria baixa
    public List<Device> getDevicesWithLowBattery() {
        return deviceRepository.findDevicesWithLowBattery();
    }
    
    // Ativar/desativar dispositivo
    public Device toggleDeviceStatus(Long id) {
        Optional<Device> deviceOpt = deviceRepository.findById(id);
        if (deviceOpt.isPresent()) {
            Device device = deviceOpt.get();
            device.setActive(!device.isActive());
            return deviceRepository.save(device);
        }
        return null;
    }
    
    // Estatísticas dos dispositivos
    public long getTotalDevices() {
        return deviceRepository.count();
    }
    
    public long getActiveDevicesCount() {
        return deviceRepository.findByActiveTrue().size();
    }
    
    public long getOfflineDevicesCount() {
        return getOfflineDevices().size();
    }

}