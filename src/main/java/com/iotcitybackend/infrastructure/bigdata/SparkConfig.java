package com.iotcitybackend.infrastructure.bigdata;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spark.enabled", havingValue = "true", matchIfMissing = false)
public class SparkConfig {

    @Value("${spark.app.name:iot-city-spark}")
    private String appName;

    @Value("${spark.master:local[*]}")
    private String master;

    @Bean
    public SparkConf sparkConf() {
        return new SparkConf()
                .setAppName(appName)
                .setMaster(master)
                .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
                .set("spark.sql.adaptive.enabled", "true")
                .set("spark.sql.adaptive.coalescePartitions.enabled", "true");
    }

    @Bean
    public JavaSparkContext javaSparkContext() {
        return new JavaSparkContext(sparkConf());
    }

    @Bean
    public SparkSession sparkSession() {
        return SparkSession.builder()
                .config(sparkConf())
                .getOrCreate();
    }
} 