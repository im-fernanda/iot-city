package com.iotcitybackend.service;

import com.iotcitybackend.model.SensorData;
import com.iotcitybackend.repository.SensorDataRepository;
import com.iotcitybackend.model.Device;
import com.iotcitybackend.dto.DeviceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorDataService {
    
    @Autowired
    private SensorDataRepository sensorDataRepository;

    public SensorData addSensorData(SensorData sensorData) {
        return sensorDataRepository.save(sensorData);
    }

    @Transactional
    public SensorData saveSensorData(SensorData sensorData) {
        SensorData savedData = sensorDataRepository.save(sensorData);
        
        Device device = savedData.getDevice();
        if (device != null) {
            device.setLastSeen(savedData.getTimestamp());
 
            sensorDataRepository.updateDeviceLastSeen(device.getId(), savedData.getTimestamp());
        }
        
        return savedData;
    }

    public List<SensorData> getSensorData(String sensorType, Long deviceId, LocalDateTime start, LocalDateTime end) {
        if (deviceId != null) {
            return sensorDataRepository.findByDeviceIdAndSensorTypeAndTimestampBetween(deviceId, sensorType, start, end);
        } else {
            return sensorDataRepository.findBySensorTypeAndTimestampBetween(sensorType, start, end);
        }
    }

    public List<String> getSensorTypes() {
        return sensorDataRepository.findDistinctSensorTypes();
    }

    public List<DeviceDTO> getDevicesBySensorType(String sensorType) {
        return sensorDataRepository.findDeviceIdsBySensorType(sensorType).stream()
                .map(result -> DeviceDTO.builder()
                        .id((Long) result[0])
                        .name((String) result[1])
                        .build())
                .collect(Collectors.toList());
    }
    
    // Buscar dados por dispositivo
    public List<SensorData> getDataByDevice(Long deviceId) {
        return sensorDataRepository.findByDeviceIdOrderByTimestampDesc(deviceId);
    }
    
    // Buscar dados por tipo de sensor
    public List<SensorData> getDataBySensorType(String sensorType) {
        return sensorDataRepository.findBySensorTypeOrderByTimestampDesc(sensorType);
    }
    
    // Buscar dados por dispositivo e tipo de sensor
    public List<SensorData> getDataByDeviceAndType(Long deviceId, String sensorType) {
        return sensorDataRepository.findByDeviceIdAndSensorTypeOrderByTimestampDesc(deviceId, sensorType);
    }
    
    // Buscar dados em um período específico
    public List<SensorData> getDataByPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return sensorDataRepository.findByTimestampBetween(startDate, endDate);
    }
    
    // Buscar dados de dispositivo em um período específico
    public List<SensorData> getDataByDeviceAndPeriod(Long deviceId, LocalDateTime startDate, LocalDateTime endDate) {
        return sensorDataRepository.findByDeviceIdAndTimestampBetween(deviceId, startDate, endDate);
    }
    
    // Buscar média de valores por tipo de sensor em um período
    public Double getAverageValueBySensorTypeAndPeriod(String sensorType, LocalDateTime startDate, LocalDateTime endDate) {
        return sensorDataRepository.findAverageValueBySensorTypeAndPeriod(sensorType, startDate, endDate);
    }
    
    // Buscar dados mais recentes por dispositivo
    public SensorData getLatestDataByDevice(Long deviceId) {
        List<SensorData> results = sensorDataRepository.findLatestByDeviceId(deviceId);
        return results.isEmpty() ? null : results.get(0);
    }
    
    // Buscar dados por localização geográfica
    public List<SensorData> getDataByLocationRange(Double minLat, Double maxLat, Double minLng, Double maxLng) {
        return sensorDataRepository.findByLocationRange(minLat, maxLat, minLng, maxLng);
    }
    
    // Buscar todos os dados
    public List<SensorData> getAllSensorData() {
        return sensorDataRepository.findAll();
    }
    
    // Buscar dados por ID
    public SensorData getSensorDataById(Long id) {
        return sensorDataRepository.findById(id).orElse(null);
    }

} 