package com.iotcitybackend.repository;

import com.iotcitybackend.model.SensorData;
import java.time.LocalDateTime;
import java.util.List;

public interface SensorDataRepositoryCustom {
    List<SensorData> findWithFilters(String sensorType, Long deviceId, LocalDateTime start, LocalDateTime end);
} 