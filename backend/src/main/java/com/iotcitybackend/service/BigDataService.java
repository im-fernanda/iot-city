package com.iotcitybackend.service;

import com.iotcitybackend.model.SensorData;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.List;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "spark.enabled", havingValue = "true", matchIfMissing = false)
public class BigDataService {

    @Autowired(required = false)
    private JavaSparkContext sparkContext;

    @Autowired(required = false)
    private SparkSession sparkSession;

    @Autowired
    private SensorDataService sensorDataService;

    @Value("${spark.enabled:false}")
    private boolean sparkEnabled;

    public Map<String, Object> analyzeSensorDataWithSpark() {
        if (!sparkEnabled || sparkContext == null) {
            return Map.of(
                    "error", "Spark is not available",
                    "message", "Use docker-compose -f docker-compose-bigdata.yml up --build to enable Big Data features"
            );
        }

        // Buscar dados do banco usando 
        List<SensorData> sensorDataList = sensorDataService.findSensorData(null, null, null, null);
        
        // Converter para RDD
        JavaRDD<SensorData> sensorDataRDD = sparkContext.parallelize(sensorDataList);

        double avgValue = sensorDataRDD
                .mapToDouble(data -> data.getValue())
                .mean();
        
        double maxValue = sensorDataRDD
                .mapToDouble(data -> data.getValue())
                .max();
        
        double minValue = sensorDataRDD
                .mapToDouble(data -> data.getValue())
                .min();
        
        long totalRecords = sensorDataRDD.count();
        
        // Análise por tipo de sensor
        Map<String, Long> sensorTypeCount = sensorDataRDD
                .mapToPair(data -> new Tuple2<>(data.getSensorType(), 1L))
                .reduceByKey((Function2<Long, Long, Long>) (a, b) -> a + b)
                .collectAsMap();
        
        return Map.of(
                "averageValue", avgValue,
                "maxValue", maxValue,
                "minValue", minValue,
                "totalRecords", totalRecords,
                "sensorTypeDistribution", sensorTypeCount
        );
    }

    /**
     * Análise temporal usando Spark DataFrame
     */
    public Map<String, Object> analyzeTemporalPatterns() {
        if (!sparkEnabled || sparkSession == null) {
            return Map.of(
                    "error", "Spark is not available",
                    "message", "Use docker-compose -f docker-compose-bigdata.yml up --build to enable Big Data features"
            );
        }

        List<SensorData> sensorDataList = sensorDataService.findSensorData(null, null, null, null);
        
        // Converter para DataFrame
        Dataset<Row> df = sparkSession.createDataFrame(sensorDataList, SensorData.class);
        
        // Análise temporal usando DataFrame API
        Dataset<Row> temporalAnalysis = df
                .select(
                    df.col("sensorType"),
                    df.col("value"),
                    df.col("timestamp")
                )
                .groupBy(
                    df.col("sensorType"),
                    org.apache.spark.sql.functions.date_format(df.col("timestamp"), "yyyy-MM-dd").as("date")
                )
                .agg(
                    org.apache.spark.sql.functions.avg("value").as("avg_value"),
                    org.apache.spark.sql.functions.count("*").as("readings_count"),
                    org.apache.spark.sql.functions.stddev("value").as("value_stddev")
                )
                .orderBy("sensorType", "date");
        
        // Coletar resultados
        List<Row> results = temporalAnalysis.collectAsList();
        
        // Converter para formato de resposta
        Map<String, Object> response = Map.of(
                "temporalPatterns", results,
                "totalPatterns", results.size(),
                "analysisType", "Temporal patterns by sensor type and date"
        );
        
        return response;
    }

    /**
     * Análise de distribuição geográfica dos sensores
     */
    public Map<String, Object> analyzeGeographicDistribution() {
        if (!sparkEnabled || sparkContext == null) {
            return Map.of(
                    "error", "Spark is not available",
                    "message", "Use docker-compose -f docker-compose-bigdata.yml up --build to enable Big Data features"
            );
        }

        List<SensorData> sensorDataList = sensorDataService.findSensorData(null, null, null, null);
        JavaRDD<SensorData> sensorDataRDD = sparkContext.parallelize(sensorDataList);
        
        // Análise por região geográfica
        Map<String, Double> avgByLocation = sensorDataRDD
                .mapToPair(data -> {
                    String region = getRegionFromCoordinates(data.getLatitude(), data.getLongitude());
                    return new Tuple2<>(region, data.getValue());
                })
                .groupByKey()
                .mapValues(values -> {
                    double sum = 0;
                    int count = 0;
                    for (Double value : values) {
                        sum += value;
                        count++;
                    }
                    return sum / count;
                })
                .collectAsMap();
        
        return Map.of(
                "geographicDistribution", avgByLocation,
                "totalLocations", avgByLocation.size()
        );
    }

    /**
     * Análise de performance e throughput
     */
    public Map<String, Object> analyzePerformanceMetrics() {
        if (!sparkEnabled || sparkContext == null) {
            return Map.of(
                    "error", "Spark is not available",
                    "message", "Use docker-compose -f docker-compose-bigdata.yml up --build to enable Big Data features"
            );
        }

        List<SensorData> sensorDataList = sensorDataService.findSensorData(null, null, null, null);
        JavaRDD<SensorData> sensorDataRDD = sparkContext.parallelize(sensorDataList);
        
        long startTime = System.currentTimeMillis();

        long totalRecords = sensorDataRDD.count();
        double avgValue = sensorDataRDD.mapToDouble(data -> data.getValue()).mean();
        
        long endTime = System.currentTimeMillis();
        long processingTime = endTime - startTime;
        
        return Map.of(
                "totalRecords", totalRecords,
                "averageValue", avgValue,
                "processingTimeMs", processingTime,
                "throughput", (double) totalRecords / (processingTime / 1000.0) // records per second
        );
    }

    private String getRegionFromCoordinates(Double latitude, Double longitude) {
        // Implementação simplificada de classificação por região
        if (latitude == null || longitude == null) {
            return "Unknown";
        }
        if (latitude > 0) {
            return longitude > 0 ? "Northeast" : "Northwest";
        } else {
            return longitude > 0 ? "Southeast" : "Southwest";
        }
    }
} 