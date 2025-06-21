package com.iotcitybackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfflineTimeoutConfigDTO {
    private int timeoutMinutes;
    
    // Construtor padrão
    public OfflineTimeoutConfigDTO() {}
    
    // Construtor com parâmetros
    public OfflineTimeoutConfigDTO(int timeoutMinutes) {
        this.timeoutMinutes = timeoutMinutes;
    }

} 