-- Script de inicialização do banco de dados PostgreSQL
-- Este script é executado automaticamente quando o container PostgreSQL é criado

-- Criar extensões necessárias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Inserir dados de exemplo para dispositivos
INSERT INTO devices (name, type, location, active, battery_level, signal_strength, created_at, updated_at) VALUES
('Sensor de Temperatura - Centro', 'SENSOR', 'Centro da Cidade', true, 85, 90, NOW(), NOW()),
('Sensor de Umidade - Parque', 'SENSOR', 'Parque Central', true, 92, 88, NOW(), NOW()),
('Sensor de Qualidade do Ar - Shopping', 'SENSOR', 'Shopping Center', true, 78, 85, NOW(), NOW()),
('Câmera de Segurança - Praça', 'CAMERA', 'Praça Principal', true, 95, 92, NOW(), NOW()),
('Atuador de Iluminação - Rua A', 'ACTUATOR', 'Rua das Flores', true, 88, 87, NOW(), NOW()),
('Sensor de Tráfego - Avenida', 'SENSOR', 'Avenida Principal', false, 45, 60, NOW(), NOW())
ON CONFLICT DO NOTHING;

-- Inserir dados de exemplo para sensor_data
INSERT INTO sensor_data (device_id, sensor_type, sensor_value, unit, timestamp, latitude, longitude, created_at) VALUES
(1, 'TEMPERATURE', 25.5, 'Celsius', NOW(), -5.7945, -35.2090, NOW()),
(1, 'TEMPERATURE', 26.2, 'Celsius', NOW() - INTERVAL '1 hour', -5.7945, -35.2090, NOW()),
(2, 'HUMIDITY', 65.3, 'Percent', NOW(), -5.7945, -35.2090, NOW()),
(2, 'HUMIDITY', 68.1, 'Percent', NOW() - INTERVAL '30 minutes', -5.7945, -35.2090, NOW()),
(3, 'AIR_QUALITY', 45.2, 'AQI', NOW(), -5.7945, -35.2090, NOW()),
(4, 'MOTION_DETECTED', 1.0, 'Boolean', NOW(), -5.7945, -35.2090, NOW()),
(5, 'LIGHT_LEVEL', 0.8, 'Percent', NOW(), -5.7945, -35.2090, NOW()),
(6, 'TRAFFIC_COUNT', 150.0, 'Vehicles/Hour', NOW(), -5.7945, -35.2090, NOW())
ON CONFLICT DO NOTHING;

-- Criar índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_sensor_data_device_id ON sensor_data(device_id);
CREATE INDEX IF NOT EXISTS idx_sensor_data_timestamp ON sensor_data(timestamp);
CREATE INDEX IF NOT EXISTS idx_sensor_data_sensor_type ON sensor_data(sensor_type);
CREATE INDEX IF NOT EXISTS idx_devices_active ON devices(active);
CREATE INDEX IF NOT EXISTS idx_devices_type ON devices(type); 