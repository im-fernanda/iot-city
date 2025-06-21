package com.iotcitybackend.repository;

import com.iotcitybackend.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    
    @Query("SELECT s FROM SensorData s WHERE s.device.id = :deviceId ORDER BY s.timestamp DESC")
    List<SensorData> findByDeviceIdOrderByTimestampDesc(@Param("deviceId") Long deviceId);

    @Query("SELECT s FROM SensorData s WHERE s.sensorType = :sensorType ORDER BY s.timestamp DESC")
    List<SensorData> findBySensorTypeOrderByTimestampDesc(@Param("sensorType") String sensorType);

    @Query("SELECT s FROM SensorData s WHERE s.device.id = :deviceId AND s.sensorType = :sensorType ORDER BY s.timestamp DESC")
    List<SensorData> findByDeviceIdAndSensorTypeOrderByTimestampDesc(@Param("deviceId") Long deviceId, @Param("sensorType") String sensorType);

    @Query("SELECT s FROM SensorData s WHERE s.timestamp BETWEEN :startDate AND :endDate")
    List<SensorData> findByTimestampBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT s FROM SensorData s WHERE s.device.id = :deviceId AND s.timestamp BETWEEN :startDate AND :endDate")
    List<SensorData> findByDeviceIdAndTimestampBetween(@Param("deviceId") Long deviceId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT AVG(s.value) FROM SensorData s WHERE s.sensorType = :sensorType AND s.timestamp BETWEEN :startDate AND :endDate")
    Double findAverageValueBySensorTypeAndPeriod(@Param("sensorType") String sensorType, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT s FROM SensorData s WHERE s.device.id = :deviceId ORDER BY s.timestamp DESC LIMIT 1")
    SensorData findLatestByDeviceId(@Param("deviceId") Long deviceId);

    @Query("SELECT s FROM SensorData s WHERE s.latitude BETWEEN :minLat AND :maxLat AND s.longitude BETWEEN :minLng AND :maxLng")
    List<SensorData> findByLocationRange(@Param("minLat") Double minLat, @Param("maxLat") Double maxLat, @Param("minLng") Double minLng, @Param("maxLng") Double maxLng);
} 