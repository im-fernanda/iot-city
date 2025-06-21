package com.iotcitybackend.controller;

import com.iotcitybackend.service.BigDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bigdata")
@CrossOrigin(origins = "*")
@Tag(name = "Big Data", description = "APIs para processamento de Big Data com Spark")
@ConditionalOnProperty(name = "spark.enabled", havingValue = "true", matchIfMissing = false)
public class BigDataController {

    @Autowired
    private BigDataService bigDataService;

    @GetMapping("/analyze")
    @Operation(summary = "Análise de dados IoT usando Spark", 
               description = "Processa dados de sensores usando Apache Spark para análise estatística")
    public ResponseEntity<Map<String, Object>> analyzeSensorData() {
        Map<String, Object> analysis = bigDataService.analyzeSensorDataWithSpark();
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/temporal-patterns")
    @Operation(summary = "Análise de padrões temporais", 
               description = "Analisa padrões temporais nos dados usando Spark DataFrame")
    public ResponseEntity<Map<String, Object>> analyzeTemporalPatterns() {
        Map<String, Object> patterns = bigDataService.analyzeTemporalPatterns();
        return ResponseEntity.ok(patterns);
    }

    @GetMapping("/geographic")
    @Operation(summary = "Análise de distribuição geográfica", 
               description = "Analisa a distribuição geográfica dos sensores")
    public ResponseEntity<Map<String, Object>> analyzeGeographicDistribution() {
        Map<String, Object> distribution = bigDataService.analyzeGeographicDistribution();
        return ResponseEntity.ok(distribution);
    }

    @GetMapping("/performance")
    @Operation(summary = "Métricas de performance", 
               description = "Analisa performance e throughput do processamento de Big Data")
    public ResponseEntity<Map<String, Object>> analyzePerformanceMetrics() {
        Map<String, Object> metrics = bigDataService.analyzePerformanceMetrics();
        return ResponseEntity.ok(metrics);
    }
} 