package com.iotcitybackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceStatsDTO {
    private long totalDevices;
    private long activeDevices;
    private long offlineDevices;
    
    public DeviceStatsDTO() {}

    public DeviceStatsDTO(long totalDevices, long activeDevices, long offlineDevices) {
        this.totalDevices = totalDevices;
        this.activeDevices = activeDevices;
        this.offlineDevices = offlineDevices;
    }

} 