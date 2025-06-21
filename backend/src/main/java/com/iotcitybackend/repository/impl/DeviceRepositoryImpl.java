package com.iotcitybackend.repository.impl;

import com.iotcitybackend.model.Device;
import com.iotcitybackend.repository.DeviceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DeviceRepositoryImpl extends JpaRepository<Device, Long>, DeviceRepository {
    

    @Override
    List<Device> findByType(String type);
    
    @Override
    List<Device> findByActiveTrue();
    

    @Override
    List<Device> findByLocationContainingIgnoreCase(String location);
    
  
    @Override
    @Query("SELECT d FROM Device d WHERE d.lastSeen < :threshold")
    List<Device> findDevicesNotSeenSince(@Param("threshold") LocalDateTime threshold);
    

    @Override
    @Query("SELECT d FROM Device d WHERE d.batteryLevel < 20")
    List<Device> findDevicesWithLowBattery();
} 