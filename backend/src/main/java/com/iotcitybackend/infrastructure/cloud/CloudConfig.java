package com.iotcitybackend.infrastructure.cloud;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cloud")
public class CloudConfig {

    @Value("${cloud.provider:aws}")
    private String cloudProvider;

    @Value("${cloud.region:us-east-1}")
    private String region;

    @Value("${cloud.instance.type:t3.medium}")
    private String instanceType;

    @Value("${cloud.auto-scaling.enabled:true}")
    private boolean autoScalingEnabled;

    @Value("${cloud.load-balancer.enabled:true}")
    private boolean loadBalancerEnabled;

    @Bean
    public CloudProperties cloudProperties() {
        return CloudProperties.builder()
                .provider(cloudProvider)
                .region(region)
                .instanceType(instanceType)
                .autoScalingEnabled(autoScalingEnabled)
                .loadBalancerEnabled(loadBalancerEnabled)
                .build();
    }
} 