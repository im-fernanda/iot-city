package com.iotcitybackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfflineTimeoutConfigDTO {
    private int timeoutMinutes;   
    public OfflineTimeoutConfigDTO() {}

    public OfflineTimeoutConfigDTO(int timeoutMinutes) {
        this.timeoutMinutes = timeoutMinutes;
    }

} 