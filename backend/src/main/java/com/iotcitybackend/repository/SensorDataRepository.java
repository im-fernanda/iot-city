package com.iotcitybackend.repository;

import com.iotcitybackend.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long>, SensorDataRepositoryCustom {
    
    @Query("SELECT DISTINCT s.sensorType FROM SensorData s ORDER BY s.sensorType")
    List<String> findDistinctSensorTypes();
    
    @Modifying
    @Query("UPDATE Device d SET d.lastSeen = :lastSeen WHERE d.id = :deviceId")
    void updateDeviceLastSeen(@Param("deviceId") Long deviceId, @Param("lastSeen") LocalDateTime lastSeen);
    
    @Query("SELECT AVG(s.value) FROM SensorData s WHERE s.sensorType = :sensorType AND s.timestamp BETWEEN :startDate AND :endDate")
    Double findAverageValueBySensorTypeAndPeriod(
            @Param("sensorType") String sensorType, 
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate
    );
    
    @Query("SELECT s FROM SensorData s WHERE s.device.id = :deviceId ORDER BY s.timestamp DESC")
    List<SensorData> findLatestByDeviceId(@Param("deviceId") Long deviceId);
} 