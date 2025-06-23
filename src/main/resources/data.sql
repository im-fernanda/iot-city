CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =============================================================================
-- CRIAÇÃO DE ÍNDICES OTIMIZADOS
-- =============================================================================

-- Índices principais para sensor_data
CREATE INDEX IF NOT EXISTS idx_sensor_data_device_id ON sensor_data(device_id);
CREATE INDEX IF NOT EXISTS idx_sensor_data_timestamp ON sensor_data(timestamp);
CREATE INDEX IF NOT EXISTS idx_sensor_data_sensor_type ON sensor_data(sensor_type);
CREATE INDEX IF NOT EXISTS idx_sensor_data_device_timestamp ON sensor_data(device_id, timestamp);
CREATE INDEX IF NOT EXISTS idx_sensor_data_type_timestamp ON sensor_data(sensor_type, timestamp);
CREATE INDEX IF NOT EXISTS idx_sensor_data_location ON sensor_data(latitude, longitude);

-- Índices para devices
CREATE INDEX IF NOT EXISTS idx_devices_active ON devices(active);
CREATE INDEX IF NOT EXISTS idx_devices_type ON devices(type);
CREATE INDEX IF NOT EXISTS idx_devices_name ON devices(name);
CREATE INDEX IF NOT EXISTS idx_devices_type_active ON devices(type, active);

-- =============================================================================
-- FUNÇÕES AUXILIARES PARA GERAÇÃO DE DADOS
-- =============================================================================

-- Função para gerar dados históricos de forma otimizada
CREATE OR REPLACE FUNCTION generate_historical_data(
    p_device_name TEXT,
    p_sensor_type TEXT,
    p_base_value NUMERIC,
    p_random_factor NUMERIC,
    p_unit TEXT,
    p_latitude NUMERIC,
    p_longitude NUMERIC,
    p_hours_back INTEGER DEFAULT 24,
    p_interval_minutes INTEGER DEFAULT 30
) RETURNS VOID AS $$
BEGIN
    INSERT INTO sensor_data (device_id, sensor_type, sensor_value, unit, timestamp, latitude, longitude)
    SELECT
        d.id,
        p_sensor_type,
        p_base_value + random() * p_random_factor,
        p_unit,
        NOW() - (i * interval '1 minute' * p_interval_minutes),
        p_latitude,
        p_longitude
    FROM devices d, generate_series(1, (p_hours_back * 60 / p_interval_minutes)) i
    WHERE d.name = p_device_name;
END;
$$ LANGUAGE plpgsql;

-- Função para gerar dados de luminosidade com variação dia/noite
CREATE OR REPLACE FUNCTION generate_light_data(
    p_device_name TEXT,
    p_latitude NUMERIC,
    p_longitude NUMERIC,
    p_hours_back INTEGER DEFAULT 24
) RETURNS VOID AS $$
BEGIN
    INSERT INTO sensor_data (device_id, sensor_type, sensor_value, unit, timestamp, latitude, longitude)
    SELECT
        d.id,
        'LIGHT_INTENSITY',
        CASE
            WHEN (EXTRACT(HOUR FROM (NOW() - (i * interval '10 minute')))) BETWEEN 6 AND 18 
            THEN 800 + random() * 200 -- Dia
            ELSE 50 + random() * 50 -- Noite
        END,
        'lux',
        NOW() - (i * interval '10 minute'),
        p_latitude,
        p_longitude
    FROM devices d, generate_series(1, p_hours_back * 6) i
    WHERE d.name = p_device_name;
END;
$$ LANGUAGE plpgsql;

-- =============================================================================
-- INSERÇÃO DE DISPOSITIVOS 
-- =============================================================================

-- Inserir Dispositivos em lotes por tipo
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
-- Semáforos
('Semaforo_Petropolis_01', 'TRAFFIC_LIGHT', 'Avenida Hermes da Fonseca, Petrópolis, Natal/RN', true, NOW(), 85, 92, NOW(), NOW()),
('Semaforo_Tirol_02', 'TRAFFIC_LIGHT', 'Avenida Salgado Filho, Tirol, Natal/RN', true, NOW(), 78, 88, NOW(), NOW()),
('Semaforo_CidadeAlta_03', 'TRAFFIC_LIGHT', 'Rua Jundiaí, Cidade Alta, Natal/RN', true, NOW(), 92, 95, NOW(), NOW()),
('Semaforo_Parnamirim_01', 'TRAFFIC_LIGHT', 'BR-101, Parnamirim/RN', true, NOW(), 80, 90, NOW(), NOW()),
('Semaforo_SaoGoncalo_01', 'TRAFFIC_LIGHT', 'RN-160, São Gonçalo do Amarante/RN', true, NOW(), 75, 85, NOW(), NOW()),

-- Sensores de Qualidade do Ar
('Ar_PraiaMeio_01', 'AIR_QUALITY', 'Praia de Meio, Natal/RN', true, NOW(), 88, 94, NOW(), NOW()),
('Ar_PontaNegra_02', 'AIR_QUALITY', 'Praia de Ponta Negra, Natal/RN', true, NOW(), 90, 96, NOW(), NOW()),
('Ar_Centro_03', 'AIR_QUALITY', 'Centro Histórico, Natal/RN', true, NOW(), 82, 89, NOW(), NOW()),
('Ar_Parnamirim_02', 'AIR_QUALITY', 'Centro de Parnamirim/RN', true, NOW(), 85, 91, NOW(), NOW()),
('Ar_CearaMirim_01', 'AIR_QUALITY', 'Centro de Ceará-Mirim/RN', true, NOW(), 79, 87, NOW(), NOW()),

-- Luminárias
('Luz_Petropolis_01', 'STREET_LIGHT', 'Rua Dr. Manoel Dantas, Petrópolis, Natal/RN', true, NOW(), 95, 98, NOW(), NOW()),
('Luz_Tirol_02', 'STREET_LIGHT', 'Rua Potengi, Tirol, Natal/RN', true, NOW(), 92, 96, NOW(), NOW()),
('Luz_CidadeAlta_03', 'STREET_LIGHT', 'Rua Apodi, Cidade Alta, Natal/RN', true, NOW(), 88, 93, NOW(), NOW()),
('Luz_Rocas_04', 'STREET_LIGHT', 'Rua Chile, Rocas, Natal/RN', true, NOW(), 90, 95, NOW(), NOW()),
('Luz_Parnamirim_03', 'STREET_LIGHT', 'Rua Augusto Severo, Parnamirim/RN', true, NOW(), 87, 92, NOW(), NOW()),

-- Sensores de Nível de Água
('Agua_CapimMacio_01', 'WATER_LEVEL', 'Rua do Sol, Capim Macio, Natal/RN', true, NOW(), 85, 90, NOW(), NOW()),
('Agua_Petropolis_02', 'WATER_LEVEL', 'Avenida Campos Sales, Petrópolis, Natal/RN', true, NOW(), 82, 88, NOW(), NOW()),
('Agua_Tirol_03', 'WATER_LEVEL', 'Rua Mossoró, Tirol, Natal/RN', true, NOW(), 88, 93, NOW(), NOW()),
('Agua_Parnamirim_04', 'WATER_LEVEL', 'Rua Francisco Barbosa, Parnamirim/RN', true, NOW(), 80, 86, NOW(), NOW()),
('Agua_SaoGoncalo_02', 'WATER_LEVEL', 'RN-160, São Gonçalo do Amarante/RN', true, NOW(), 75, 82, NOW(), NOW()),

-- Sensores de Ruído
('Ruido_PontaNegra_01', 'NOISE_SENSOR', 'Via Costeira, Ponta Negra, Natal/RN', true, NOW(), 90, 95, NOW(), NOW()),
('Ruido_Centro_02', 'NOISE_SENSOR', 'Rua Chile, Centro, Natal/RN', true, NOW(), 85, 90, NOW(), NOW()),
('Ruido_Petropolis_03', 'NOISE_SENSOR', 'Avenida Hermes da Fonseca, Petrópolis, Natal/RN', true, NOW(), 88, 93, NOW(), NOW()),
('Ruido_Parnamirim_05', 'NOISE_SENSOR', 'BR-101, Parnamirim/RN', true, NOW(), 82, 88, NOW(), NOW()),
('Ruido_CearaMirim_02', 'NOISE_SENSOR', 'RN-160, Ceará-Mirim/RN', true, NOW(), 78, 85, NOW(), NOW()),

-- Sensores Meteorológicos
('Temp_PontaNegra_01', 'WEATHER_SENSOR', 'Morro do Careca, Ponta Negra, Natal/RN', true, NOW(), 92, 97, NOW(), NOW()),
('Temp_Centro_02', 'WEATHER_SENSOR', 'Forte dos Reis Magos, Natal/RN', true, NOW(), 88, 94, NOW(), NOW()),
('Temp_Petropolis_03', 'WEATHER_SENSOR', 'Parque das Dunas, Natal/RN', true, NOW(), 90, 95, NOW(), NOW()),
('Temp_Parnamirim_06', 'WEATHER_SENSOR', 'Aeroporto Internacional de Natal, Parnamirim/RN', true, NOW(), 85, 91, NOW(), NOW()),
('Temp_SaoGoncalo_03', 'WEATHER_SENSOR', 'Porto de São Gonçalo do Amarante/RN', true, NOW(), 80, 87, NOW(), NOW());

-- =============================================================================
-- GERAÇÃO DE DADOS HISTÓRICOS 
-- =============================================================================

-- Dados meteorológicos para Ponta Negra (múltiplos sensores)
SELECT generate_historical_data('Temp_PontaNegra_01', 'TEMPERATURE', 24, 8, '°C', -5.7945, -35.2090, 24, 30);
SELECT generate_historical_data('Temp_PontaNegra_01', 'HUMIDITY', 70, 25, '%', -5.7945, -35.2090, 24, 30);
SELECT generate_historical_data('Temp_PontaNegra_01', 'AIR_PRESSURE', 1008, 5, 'hPa', -5.7945, -35.2090, 24, 30);
SELECT generate_historical_data('Temp_PontaNegra_01', 'WIND_SPEED', 5, 20, 'km/h', -5.7945, -35.2090, 24, 30);

-- Dados meteorológicos para Centro
SELECT generate_historical_data('Temp_Centro_02', 'TEMPERATURE', 26, 6, '°C', -5.7967, -35.2078, 24, 30);
SELECT generate_historical_data('Temp_Centro_02', 'HUMIDITY', 60, 40, '%', -5.7967, -35.2078, 24, 30);
SELECT generate_historical_data('Temp_Centro_02', 'AIR_PRESSURE', 1010, 3, 'hPa', -5.7967, -35.2078, 24, 30);

-- Dados de qualidade do ar
SELECT generate_historical_data('Ar_PraiaMeio_01', 'PM25', 10, 50, 'μg/m³', -5.7945, -35.2090, 24, 30);
SELECT generate_historical_data('Ar_PontaNegra_02', 'AIR_QUALITY', 15, 40, 'AQI', -5.8810, -35.1650, 24, 30);
SELECT generate_historical_data('Ar_Centro_03', 'PM10', 20, 60, 'μg/m³', -5.7967, -35.2078, 24, 30);
SELECT generate_historical_data('Ar_Parnamirim_02', 'CO2', 400, 200, 'ppm', -5.9200, -35.2600, 24, 30);

-- Dados de tráfego para semáforos
SELECT generate_historical_data('Semaforo_Tirol_02', 'TRAFFIC_FLOW', 20, 80, 'vehicles/hour', -5.8119, -35.2067, 24, 15);
SELECT generate_historical_data('Semaforo_Petropolis_01', 'TRAFFIC_FLOW', 15, 60, 'vehicles/hour', -5.7900, -35.2075, 24, 15);
SELECT generate_historical_data('Semaforo_CidadeAlta_03', 'TRAFFIC_FLOW', 10, 40, 'vehicles/hour', -5.7967, -35.2078, 24, 15);

-- Dados de ruído
SELECT generate_historical_data('Ruido_PontaNegra_01', 'NOISE_LEVEL', 50, 50, 'dB', -5.8825, -35.1633, 24, 20);
SELECT generate_historical_data('Ruido_Centro_02', 'NOISE_LEVEL', 60, 40, 'dB', -5.7967, -35.2078, 24, 20);
SELECT generate_historical_data('Ruido_Petropolis_03', 'NOISE_LEVEL', 55, 45, 'dB', -5.7900, -35.2075, 24, 20);

-- Dados de luminosidade (com variação dia/noite)
SELECT generate_light_data('Luz_Petropolis_01', -5.7900, -35.2075, 24);
SELECT generate_light_data('Luz_Tirol_02', -5.8119, -35.2067, 24);
SELECT generate_light_data('Luz_CidadeAlta_03', -5.7967, -35.2078, 24);

-- Dados de água
SELECT generate_historical_data('Agua_CapimMacio_01', 'WATER_PRESSURE', 30, 20, 'PSI', -5.8500, -35.1950, 24, 60);
SELECT generate_historical_data('Agua_Petropolis_02', 'WATER_LEVEL', 50, 30, 'cm', -5.7900, -35.2075, 24, 60);
SELECT generate_historical_data('Agua_Tirol_03', 'WATER_FLOW', 10, 15, 'L/min', -5.8119, -35.2067, 24, 60);

-- Dados meteorológicos adicionais para outros locais
SELECT generate_historical_data('Temp_Petropolis_03', 'TEMPERATURE', 25, 7, '°C', -5.7900, -35.2075, 24, 30);
SELECT generate_historical_data('Temp_Parnamirim_06', 'TEMPERATURE', 27, 5, '°C', -5.9200, -35.2600, 24, 30);
SELECT generate_historical_data('Temp_SaoGoncalo_03', 'TEMPERATURE', 28, 6, '°C', -5.7900, -35.2075, 24, 30);

