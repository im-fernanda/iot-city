-- Configurações do PostgreSQL
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Criar índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_sensor_data_device_id ON sensor_data(device_id);
CREATE INDEX IF NOT EXISTS idx_sensor_data_timestamp ON sensor_data(timestamp);
CREATE INDEX IF NOT EXISTS idx_sensor_data_sensor_type ON sensor_data(sensor_type);
CREATE INDEX IF NOT EXISTS idx_devices_active ON devices(active);
CREATE INDEX IF NOT EXISTS idx_devices_type ON devices(type);

-- Inserir Dispositivos
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Semaforo_Petropolis_01', 'TRAFFIC_LIGHT', 'Avenida Hermes da Fonseca, Petrópolis, Natal/RN', true, NOW(), 85, 92, NOW(), NOW()),
('Semaforo_Tirol_02', 'TRAFFIC_LIGHT', 'Avenida Salgado Filho, Tirol, Natal/RN', true, NOW(), 78, 88, NOW(), NOW()),
('Semaforo_CidadeAlta_03', 'TRAFFIC_LIGHT', 'Rua Jundiaí, Cidade Alta, Natal/RN', true, NOW(), 92, 95, NOW(), NOW()),
('Semaforo_Parnamirim_01', 'TRAFFIC_LIGHT', 'BR-101, Parnamirim/RN', true, NOW(), 80, 90, NOW(), NOW()),
('Semaforo_SaoGoncalo_01', 'TRAFFIC_LIGHT', 'RN-160, São Gonçalo do Amarante/RN', true, NOW(), 75, 85, NOW(), NOW()),
('Ar_PraiaMeio_01', 'AIR_QUALITY', 'Praia de Meio, Natal/RN', true, NOW(), 88, 94, NOW(), NOW()),
('Ar_PontaNegra_02', 'AIR_QUALITY', 'Praia de Ponta Negra, Natal/RN', true, NOW(), 90, 96, NOW(), NOW()),
('Ar_Centro_03', 'AIR_QUALITY', 'Centro Histórico, Natal/RN', true, NOW(), 82, 89, NOW(), NOW()),
('Ar_Parnamirim_02', 'AIR_QUALITY', 'Centro de Parnamirim/RN', true, NOW(), 85, 91, NOW(), NOW()),
('Ar_CearaMirim_01', 'AIR_QUALITY', 'Centro de Ceará-Mirim/RN', true, NOW(), 79, 87, NOW(), NOW()),
('Luz_Petropolis_01', 'STREET_LIGHT', 'Rua Dr. Manoel Dantas, Petrópolis, Natal/RN', true, NOW(), 95, 98, NOW(), NOW()),
('Luz_Tirol_02', 'STREET_LIGHT', 'Rua Potengi, Tirol, Natal/RN', true, NOW(), 92, 96, NOW(), NOW()),
('Luz_CidadeAlta_03', 'STREET_LIGHT', 'Rua Apodi, Cidade Alta, Natal/RN', true, NOW(), 88, 93, NOW(), NOW()),
('Luz_Rocas_04', 'STREET_LIGHT', 'Rua Chile, Rocas, Natal/RN', true, NOW(), 90, 95, NOW(), NOW()),
('Luz_Parnamirim_03', 'STREET_LIGHT', 'Rua Augusto Severo, Parnamirim/RN', true, NOW(), 87, 92, NOW(), NOW()),
('Agua_CapimMacio_01', 'WATER_LEVEL', 'Rua do Sol, Capim Macio, Natal/RN', true, NOW(), 85, 90, NOW(), NOW()),
('Agua_Petropolis_02', 'WATER_LEVEL', 'Avenida Campos Sales, Petrópolis, Natal/RN', true, NOW(), 82, 88, NOW(), NOW()),
('Agua_Tirol_03', 'WATER_LEVEL', 'Rua Mossoró, Tirol, Natal/RN', true, NOW(), 88, 93, NOW(), NOW()),
('Agua_Parnamirim_04', 'WATER_LEVEL', 'Rua Francisco Barbosa, Parnamirim/RN', true, NOW(), 80, 86, NOW(), NOW()),
('Agua_SaoGoncalo_02', 'WATER_LEVEL', 'RN-160, São Gonçalo do Amarante/RN', true, NOW(), 75, 82, NOW(), NOW()),
('Ruido_PontaNegra_01', 'NOISE_SENSOR', 'Via Costeira, Ponta Negra, Natal/RN', true, NOW(), 90, 95, NOW(), NOW()),
('Ruido_Centro_02', 'NOISE_SENSOR', 'Rua Chile, Centro, Natal/RN', true, NOW(), 85, 90, NOW(), NOW()),
('Ruido_Petropolis_03', 'NOISE_SENSOR', 'Avenida Hermes da Fonseca, Petrópolis, Natal/RN', true, NOW(), 88, 93, NOW(), NOW()),
('Ruido_Parnamirim_05', 'NOISE_SENSOR', 'BR-101, Parnamirim/RN', true, NOW(), 82, 88, NOW(), NOW()),
('Ruido_CearaMirim_02', 'NOISE_SENSOR', 'RN-160, Ceará-Mirim/RN', true, NOW(), 78, 85, NOW(), NOW()),
('Temp_PontaNegra_01', 'WEATHER_SENSOR', 'Morro do Careca, Ponta Negra, Natal/RN', true, NOW(), 92, 97, NOW(), NOW()),
('Temp_Centro_02', 'WEATHER_SENSOR', 'Forte dos Reis Magos, Natal/RN', true, NOW(), 88, 94, NOW(), NOW()),
('Temp_Petropolis_03', 'WEATHER_SENSOR', 'Parque das Dunas, Natal/RN', true, NOW(), 90, 95, NOW(), NOW()),
('Temp_Parnamirim_06', 'WEATHER_SENSOR', 'Aeroporto Internacional de Natal, Parnamirim/RN', true, NOW(), 85, 91, NOW(), NOW()),
('Temp_SaoGoncalo_03', 'WEATHER_SENSOR', 'Porto de São Gonçalo do Amarante/RN', true, NOW(), 80, 87, NOW(), NOW());

-- Inserir Dados de Sensores Históricos
INSERT INTO sensor_data (device_id, sensor_type, sensor_value, unit, timestamp, latitude, longitude)
SELECT
    d.id,
    'TEMPERATURE',
    15 + random() * 15,
    '°C',
    NOW() - (i * interval '30 minute'),
    -5.7945,
    -35.2090
FROM devices d, generate_series(1, 48) i
WHERE d.name = 'Temp_PontaNegra_01';

INSERT INTO sensor_data (device_id, sensor_type, sensor_value, unit, timestamp, latitude, longitude)
SELECT
    d.id,
    'HUMIDITY',
    60 + random() * 40,
    '%',
    NOW() - (i * interval '30 minute'),
    -5.7967,
    -35.2078
FROM devices d, generate_series(1, 48) i
WHERE d.name = 'Temp_Centro_02';

INSERT INTO sensor_data (device_id, sensor_type, sensor_value, unit, timestamp, latitude, longitude)
SELECT
    d.id,
    'PM25',
    10 + random() * 50,
    'μg/m³',
    NOW() - (i * interval '30 minute'),
    -5.7945,
    -35.2090
FROM devices d, generate_series(1, 48) i
WHERE d.name = 'Ar_PraiaMeio_01'; 