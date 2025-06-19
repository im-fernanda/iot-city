package com.iotcitybackend.application.service;

import com.iotcitybackend.domain.model.SensorData;
import com.iotcitybackend.domain.repository.SensorDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SensorDataService {
    
    private final SensorDataRepository sensorDataRepository;

    public SensorDataService(SensorDataRepository sensorDataRepository) {
        this.sensorDataRepository = sensorDataRepository;
    }
    
    public SensorData saveSensorData(SensorData sensorData) {
        return sensorDataRepository.save(sensorData);
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
        return sensorDataRepository.findLatestByDeviceId(deviceId);
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