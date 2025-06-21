package com.iotcitybackend.exception;

/**
 * Códigos de erro padronizados para a API IoT City
 */
public final class ErrorCodes {
    
    // Códigos de erro para Dispositivos (DEVICE_XXX)
    public static final String DEVICE_NOT_FOUND = "DEVICE_001";
    public static final String DEVICE_INVALID_DATA = "DEVICE_002";
    public static final String DEVICE_NAME_REQUIRED = "DEVICE_003";
    public static final String DEVICE_TYPE_REQUIRED = "DEVICE_004";
    public static final String DEVICE_LOCATION_REQUIRED = "DEVICE_005";
    public static final String DEVICE_INVALID_TYPE = "DEVICE_006";
    public static final String DEVICE_BATTERY_INVALID = "DEVICE_007";
    public static final String DEVICE_SIGNAL_INVALID = "DEVICE_008";
    public static final String DEVICE_ALREADY_EXISTS = "DEVICE_009";
    public static final String DEVICE_OFFLINE_TIMEOUT_INVALID = "DEVICE_010";
    
    // Códigos de erro para Dados de Sensor (SENSOR_XXX)
    public static final String SENSOR_DATA_NOT_FOUND = "SENSOR_001";
    public static final String SENSOR_INVALID_DATA = "SENSOR_002";
    public static final String SENSOR_DEVICE_ID_REQUIRED = "SENSOR_003";
    public static final String SENSOR_TYPE_REQUIRED = "SENSOR_004";
    public static final String SENSOR_VALUE_REQUIRED = "SENSOR_005";
    public static final String SENSOR_INVALID_TYPE = "SENSOR_006";
    public static final String SENSOR_INVALID_VALUE = "SENSOR_007";
    public static final String SENSOR_INVALID_UNIT = "SENSOR_008";
    public static final String SENSOR_INVALID_COORDINATES = "SENSOR_009";
    public static final String SENSOR_INVALID_TIMESTAMP = "SENSOR_010";
    public static final String SENSOR_DEVICE_NOT_FOUND = "SENSOR_011";
    public static final String SENSOR_NO_DATA_FOR_PERIOD = "SENSOR_012";
    public static final String SENSOR_INVALID_PERIOD = "SENSOR_013";
    
    // Códigos de erro para Big Data (BIGDATA_XXX)
    public static final String BIGDATA_SPARK_NOT_ENABLED = "BIGDATA_001";
    public static final String BIGDATA_PROCESSING_ERROR = "BIGDATA_002";
    public static final String BIGDATA_NO_DATA_AVAILABLE = "BIGDATA_003";
    
    // Códigos de erro gerais (GENERAL_XXX)
    public static final String GENERAL_VALIDATION_ERROR = "GENERAL_001";
    public static final String GENERAL_INTERNAL_ERROR = "GENERAL_002";
    public static final String GENERAL_METHOD_NOT_ALLOWED = "GENERAL_003";
    public static final String GENERAL_RESOURCE_NOT_FOUND = "GENERAL_004";
    
    private ErrorCodes() {
        // Construtor privado para evitar instanciação
    }
} 