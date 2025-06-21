package com.iotcitybackend.repository;

import com.iotcitybackend.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface do repositório de dispositivos no domínio
 * Define contratos sem dependências de implementação
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    
    List<Device> findByType(String type);

    List<Device> findByActiveTrue();

    List<Device> findByLocationContainingIgnoreCase(String location);

    @Query("SELECT d FROM Device d WHERE d.lastSeen < :threshold")
    List<Device> findDevicesNotSeenSince(@Param("threshold") LocalDateTime threshold);

    @Query("SELECT d FROM Device d WHERE d.batteryLevel < 20")
    List<Device> findDevicesWithLowBattery();

}

