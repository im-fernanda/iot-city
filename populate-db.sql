-- Script para popular o banco de dados do Docker
-- Execute: docker exec -i iot-city-db psql -U iotcity_user -d iotcity < populate-db.sql

-- Dispositivos IoT para Cidade Inteligente - Natal/RN e Região

-- SENSORES DE TRÁFEGO
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Semaforo_Petropolis_01', 'TRAFFIC_LIGHT', 'Avenida Hermes da Fonseca, Petrópolis, Natal/RN', true, NOW(), 85, 92, NOW(), NOW()),
('Semaforo_Tirol_02', 'TRAFFIC_LIGHT', 'Avenida Salgado Filho, Tirol, Natal/RN', true, NOW(), 78, 88, NOW(), NOW()),
('Semaforo_CidadeAlta_03', 'TRAFFIC_LIGHT', 'Rua Jundiaí, Cidade Alta, Natal/RN', true, NOW(), 92, 95, NOW(), NOW());

-- SENSORES DE QUALIDADE DO AR
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Ar_PraiaMeio_01', 'AIR_QUALITY', 'Praia de Meio, Natal/RN', true, NOW(), 88, 94, NOW(), NOW()),
('Ar_PontaNegra_02', 'AIR_QUALITY', 'Praia de Ponta Negra, Natal/RN', true, NOW(), 90, 96, NOW(), NOW());

-- SENSORES DE TEMPERATURA
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Temp_PontaNegra_01', 'TEMPERATURE', 'Morro do Careca, Ponta Negra, Natal/RN', true, NOW(), 92, 97, NOW(), NOW()),
('Temp_Centro_02', 'TEMPERATURE', 'Forte dos Reis Magos, Natal/RN', true, NOW(), 88, 94, NOW(), NOW());

-- Dados de sensores (exemplos)
INSERT INTO sensor_data (device_id, sensor_type, sensor_value, unit, timestamp, latitude, longitude, created_at) VALUES
(1, 'TRAFFIC_FLOW', 45.5, 'vehicles/min', NOW(), -5.7945, -35.2090, NOW()),
(2, 'TRAFFIC_FLOW', 32.8, 'vehicles/min', NOW(), -5.7967, -35.2078, NOW()),
(4, 'PM25', 12.3, 'μg/m³', NOW(), -5.7945, -35.2090, NOW()),
(5, 'PM25', 8.7, 'μg/m³', NOW(), -5.7967, -35.2078, NOW()),
(6, 'TEMPERATURE', 28.5, '°C', NOW(), -5.7945, -35.2090, NOW()),
(7, 'TEMPERATURE', 29.2, '°C', NOW(), -5.7967, -35.2078, NOW()); 