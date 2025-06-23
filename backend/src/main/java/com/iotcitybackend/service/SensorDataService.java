package com.iotcitybackend.service;

import com.iotcitybackend.model.SensorData;
import com.iotcitybackend.repository.SensorDataRepository;
import com.iotcitybackend.model.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SensorDataService {
    
    @Autowired
    private SensorDataRepository sensorDataRepository;

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

    public List<SensorData> findSensorData(String sensorType, Long deviceId, LocalDateTime start, LocalDateTime end) {
        return sensorDataRepository.findWithFilters(sensorType, deviceId, start, end);
    }

    public List<String> getSensorTypes() {
        return sensorDataRepository.findDistinctSensorTypes();
    }
    
    public Optional<SensorData> getLatestDataByDevice(Long deviceId) {
        List<SensorData> results = sensorDataRepository.findLatestByDeviceId(deviceId);
        return results.stream().findFirst();
    }
    
    public Double getAverageValueBySensorType(String sensorType, LocalDateTime startDate, LocalDateTime endDate) {
        if (sensorType == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException("SensorType, startDate e endDate são obrigatórios para calcular a média.");
        }
        return sensorDataRepository.findAverageValueBySensorTypeAndPeriod(sensorType, startDate, endDate);
    }
    
    public Optional<SensorData> getSensorDataById(Long id) {
        return sensorDataRepository.findById(id);
    }
} 