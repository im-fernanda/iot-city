package com.iotcitybackend.infrastructure.cloud;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CloudProperties {
    private String provider;
    private String region;
    private String instanceType;
    private boolean autoScalingEnabled;
    private boolean loadBalancerEnabled;
} 