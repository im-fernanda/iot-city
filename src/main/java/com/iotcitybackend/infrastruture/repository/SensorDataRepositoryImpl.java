package com.iotcitybackend.infrastruture.repository;

import com.iotcitybackend.domain.model.SensorData;
import com.iotcitybackend.domain.repository.SensorDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SensorDataRepositoryImpl extends JpaRepository<SensorData, Long>, SensorDataRepository {

    // Buscar dados por dispositivo
    @Override
    List<SensorData> findByDeviceIdOrderByTimestampDesc(Long deviceId);
    
    // Buscar dados por tipo de sensor
    @Override
    List<SensorData> findBySensorTypeOrderByTimestampDesc(String sensorType);
    
    // Buscar dados por dispositivo e tipo de sensor
    @Override
    List<SensorData> findByDeviceIdAndSensorTypeOrderByTimestampDesc(Long deviceId, String sensorType);
    
    // Buscar dados em um período específico
    @Override
    @Query("SELECT sd FROM SensorData sd WHERE sd.timestamp BETWEEN :startDate AND :endDate ORDER BY sd.timestamp DESC")
    List<SensorData> findByTimestampBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Buscar dados por dispositivo em um período específico
    @Override
    @Query("SELECT sd FROM SensorData sd WHERE sd.device.id = :deviceId AND sd.timestamp BETWEEN :startDate AND :endDate ORDER BY sd.timestamp DESC")
    List<SensorData> findByDeviceIdAndTimestampBetween(@Param("deviceId") Long deviceId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Buscar média de valores por tipo de sensor em um período
    @Override
    @Query("SELECT AVG(sd.value) FROM SensorData sd WHERE sd.sensorType = :sensorType AND sd.timestamp BETWEEN :startDate AND :endDate")
    Double findAverageValueBySensorTypeAndPeriod(@Param("sensorType") String sensorType, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Buscar dados mais recentes por dispositivo
    @Override
    @Query("SELECT sd FROM SensorData sd WHERE sd.device.id = :deviceId ORDER BY sd.timestamp DESC LIMIT 1")
    SensorData findLatestByDeviceId(@Param("deviceId") Long deviceId);
    
    // Buscar dados por localização geográfica (aproximada)
    @Override
    @Query("SELECT sd FROM SensorData sd WHERE sd.latitude BETWEEN :minLat AND :maxLat AND sd.longitude BETWEEN :minLng AND :maxLng")
    List<SensorData> findByLocationRange(@Param("minLat") Double minLat, @Param("maxLat") Double maxLat, 
                                       @Param("minLng") Double minLng, @Param("maxLng") Double maxLng);
   
} 