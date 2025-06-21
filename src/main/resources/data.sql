-- Dispositivos IoT para Cidade Inteligente - Natal/RN e Região

-- SENSORES DE TRÁFEGO
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Semaforo_Petropolis_01', 'TRAFFIC_LIGHT', 'Avenida Hermes da Fonseca, Petrópolis, Natal/RN', true, NOW(), 85, 92, NOW(), NOW()),
('Semaforo_Tirol_02', 'TRAFFIC_LIGHT', 'Avenida Salgado Filho, Tirol, Natal/RN', true, NOW(), 78, 88, NOW(), NOW()),
('Semaforo_CidadeAlta_03', 'TRAFFIC_LIGHT', 'Rua Jundiaí, Cidade Alta, Natal/RN', true, NOW(), 92, 95, NOW(), NOW()),
('Semaforo_Parnamirim_01', 'TRAFFIC_LIGHT', 'BR-101, Parnamirim/RN', true, NOW(), 80, 90, NOW(), NOW()),
('Semaforo_SaoGoncalo_01', 'TRAFFIC_LIGHT', 'RN-160, São Gonçalo do Amarante/RN', true, NOW(), 75, 85, NOW(), NOW());

-- SENSORES DE QUALIDADE DO AR
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Ar_PraiaMeio_01', 'AIR_QUALITY', 'Praia de Meio, Natal/RN', true, NOW(), 88, 94, NOW(), NOW()),
('Ar_PontaNegra_02', 'AIR_QUALITY', 'Praia de Ponta Negra, Natal/RN', true, NOW(), 90, 96, NOW(), NOW()),
('Ar_Centro_03', 'AIR_QUALITY', 'Centro Histórico, Natal/RN', true, NOW(), 82, 89, NOW(), NOW()),
('Ar_Parnamirim_02', 'AIR_QUALITY', 'Centro de Parnamirim/RN', true, NOW(), 85, 91, NOW(), NOW()),
('Ar_CearaMirim_01', 'AIR_QUALITY', 'Centro de Ceará-Mirim/RN', true, NOW(), 79, 87, NOW(), NOW());

-- SENSORES DE ILUMINAÇÃO PÚBLICA
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Luz_Petropolis_01', 'STREET_LIGHT', 'Rua Dr. Manoel Dantas, Petrópolis, Natal/RN', true, NOW(), 95, 98, NOW(), NOW()),
('Luz_Tirol_02', 'STREET_LIGHT', 'Rua Potengi, Tirol, Natal/RN', true, NOW(), 92, 96, NOW(), NOW()),
('Luz_CidadeAlta_03', 'STREET_LIGHT', 'Rua Apodi, Cidade Alta, Natal/RN', true, NOW(), 88, 93, NOW(), NOW()),
('Luz_Rocas_04', 'STREET_LIGHT', 'Rua Chile, Rocas, Natal/RN', true, NOW(), 90, 95, NOW(), NOW()),
('Luz_Parnamirim_03', 'STREET_LIGHT', 'Rua Augusto Severo, Parnamirim/RN', true, NOW(), 87, 92, NOW(), NOW());

-- SENSORES DE NÍVEL DE ÁGUA (ENCHENTES)
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Agua_CapimMacio_01', 'WATER_LEVEL', 'Rua do Sol, Capim Macio, Natal/RN', true, NOW(), 85, 90, NOW(), NOW()),
('Agua_Petropolis_02', 'WATER_LEVEL', 'Avenida Campos Sales, Petrópolis, Natal/RN', true, NOW(), 82, 88, NOW(), NOW()),
('Agua_Tirol_03', 'WATER_LEVEL', 'Rua Mossoró, Tirol, Natal/RN', true, NOW(), 88, 93, NOW(), NOW()),
('Agua_Parnamirim_04', 'WATER_LEVEL', 'Rua Francisco Barbosa, Parnamirim/RN', true, NOW(), 80, 86, NOW(), NOW()),
('Agua_SaoGoncalo_02', 'WATER_LEVEL', 'RN-160, São Gonçalo do Amarante/RN', true, NOW(), 75, 82, NOW(), NOW());

-- SENSORES DE RUÍDO
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Ruido_PontaNegra_01', 'NOISE_SENSOR', 'Via Costeira, Ponta Negra, Natal/RN', true, NOW(), 90, 95, NOW(), NOW()),
('Ruido_Centro_02', 'NOISE_SENSOR', 'Rua Chile, Centro, Natal/RN', true, NOW(), 85, 90, NOW(), NOW()),
('Ruido_Petropolis_03', 'NOISE_SENSOR', 'Avenida Hermes da Fonseca, Petrópolis, Natal/RN', true, NOW(), 88, 93, NOW(), NOW()),
('Ruido_Parnamirim_05', 'NOISE_SENSOR', 'BR-101, Parnamirim/RN', true, NOW(), 82, 88, NOW(), NOW()),
('Ruido_CearaMirim_02', 'NOISE_SENSOR', 'RN-160, Ceará-Mirim/RN', true, NOW(), 78, 85, NOW(), NOW());

-- SENSORES DE TEMPERATURA E UMIDADE
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Temp_PontaNegra_01', 'WEATHER_SENSOR', 'Morro do Careca, Ponta Negra, Natal/RN', true, NOW(), 92, 97, NOW(), NOW()),
('Temp_Centro_02', 'WEATHER_SENSOR', 'Forte dos Reis Magos, Natal/RN', true, NOW(), 88, 94, NOW(), NOW()),
('Temp_Petropolis_03', 'WEATHER_SENSOR', 'Parque das Dunas, Natal/RN', true, NOW(), 90, 95, NOW(), NOW()),
('Temp_Parnamirim_06', 'WEATHER_SENSOR', 'Aeroporto Internacional de Natal, Parnamirim/RN', true, NOW(), 85, 91, NOW(), NOW()),
('Temp_SaoGoncalo_03', 'WEATHER_SENSOR', 'Porto de São Gonçalo do Amarante/RN', true, NOW(), 80, 87, NOW(), NOW());

-- CÂMERAS DE SEGURANÇA
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Camera_PontaNegra_01', 'SECURITY_CAMERA', 'Praça de Ponta Negra, Natal/RN', true, NOW(), 95, 98, NOW(), NOW()),
('Camera_Centro_02', 'SECURITY_CAMERA', 'Praça André de Albuquerque, Centro, Natal/RN', true, NOW(), 90, 95, NOW(), NOW()),
('Camera_Petropolis_03', 'SECURITY_CAMERA', 'Shopping Midway Mall, Petrópolis, Natal/RN', true, NOW(), 92, 96, NOW(), NOW()),
('Camera_Tirol_04', 'SECURITY_CAMERA', 'Shopping Natal Shopping, Tirol, Natal/RN', true, NOW(), 88, 93, NOW(), NOW()),
('Camera_Parnamirim_07', 'SECURITY_CAMERA', 'Centro de Parnamirim/RN', true, NOW(), 85, 90, NOW(), NOW());

-- SENSORES DE ESTACIONAMENTO
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Estac_PontaNegra_01', 'PARKING_SENSOR', 'Estacionamento Praia de Ponta Negra, Natal/RN', true, NOW(), 88, 94, NOW(), NOW()),
('Estac_Centro_02', 'PARKING_SENSOR', 'Estacionamento Shopping Midway Mall, Natal/RN', true, NOW(), 90, 95, NOW(), NOW()),
('Estac_Petropolis_03', 'PARKING_SENSOR', 'Estacionamento Natal Shopping, Natal/RN', true, NOW(), 85, 91, NOW(), NOW()),
('Estac_Parnamirim_08', 'PARKING_SENSOR', 'Estacionamento Centro de Parnamirim/RN', true, NOW(), 82, 88, NOW(), NOW()),
('Estac_CearaMirim_03', 'PARKING_SENSOR', 'Estacionamento Centro de Ceará-Mirim/RN', true, NOW(), 78, 85, NOW(), NOW());

-- SENSORES DE LIXO (LIXEIRAS INTELIGENTES)
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Lixo_PontaNegra_01', 'WASTE_SENSOR', 'Praia de Ponta Negra, Natal/RN', true, NOW(), 75, 85, NOW(), NOW()),
('Lixo_Centro_02', 'WASTE_SENSOR', 'Centro Histórico, Natal/RN', true, NOW(), 70, 80, NOW(), NOW()),
('Lixo_Petropolis_03', 'WASTE_SENSOR', 'Avenida Hermes da Fonseca, Petrópolis, Natal/RN', true, NOW(), 72, 82, NOW(), NOW()),
('Lixo_Parnamirim_09', 'WASTE_SENSOR', 'Centro de Parnamirim/RN', true, NOW(), 68, 78, NOW(), NOW()),
('Lixo_SaoGoncalo_04', 'WASTE_SENSOR', 'Centro de São Gonçalo do Amarante/RN', true, NOW(), 65, 75, NOW(), NOW());

-- SENSORES DE ENERGIA (POSTES SOLARES)
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Energia_PontaNegra_01', 'SOLAR_PANEL', 'Via Costeira, Ponta Negra, Natal/RN', true, NOW(), 95, 98, NOW(), NOW()),
('Energia_Petropolis_02', 'SOLAR_PANEL', 'Parque das Dunas, Natal/RN', true, NOW(), 92, 96, NOW(), NOW()),
('Energia_Centro_03', 'SOLAR_PANEL', 'Forte dos Reis Magos, Natal/RN', true, NOW(), 88, 93, NOW(), NOW()),
('Energia_Parnamirim_10', 'SOLAR_PANEL', 'Aeroporto Internacional de Natal, Parnamirim/RN', true, NOW(), 85, 90, NOW(), NOW()),
('Energia_SaoGoncalo_05', 'SOLAR_PANEL', 'Porto de São Gonçalo do Amarante/RN', true, NOW(), 80, 87, NOW(), NOW());

-- Dispositivos offline (para teste) - usando INTERVAL para PostgreSQL
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Teste_Offline_01', 'TRAFFIC_LIGHT', 'Rua Teste, Natal/RN', false, NOW() - INTERVAL '10 minutes', 45, 60, NOW(), NOW()),
('Teste_Offline_02', 'AIR_QUALITY', 'Avenida Teste, Parnamirim/RN', false, NOW() - INTERVAL '15 minutes', 30, 50, NOW(), NOW()),
('Teste_BateriaBaixa_01', 'STREET_LIGHT', 'Rua Bateria Baixa, Natal/RN', true, NOW(), 15, 70, NOW(), NOW()),
('Teste_BateriaBaixa_02', 'NOISE_SENSOR', 'Avenida Bateria Baixa, Parnamirim/RN', true, NOW(), 18, 65, NOW(), NOW());

-- Dados de sensores (exemplos)
INSERT INTO sensor_data (device_id, sensor_type, sensor_value, unit, timestamp, latitude, longitude, created_at) VALUES
(1, 'TRAFFIC_FLOW', 45.5, 'vehicles/min', NOW(), -5.7945, -35.2090, NOW()),
(2, 'TRAFFIC_FLOW', 32.8, 'vehicles/min', NOW(), -5.7967, -35.2078, NOW()),
(6, 'PM25', 12.3, 'μg/m³', NOW(), -5.7945, -35.2090, NOW()),
(7, 'PM25', 8.7, 'μg/m³', NOW(), -5.7967, -35.2078, NOW()),
(11, 'WATER_LEVEL', 15.2, 'cm', NOW(), -5.7945, -35.2090, NOW()),
(16, 'NOISE_LEVEL', 65.4, 'dB', NOW(), -5.7967, -35.2078, NOW()),
(21, 'TEMPERATURE', 28.5, '°C', NOW(), -5.7945, -35.2090, NOW()),
(22, 'HUMIDITY', 78.2, '%', NOW(), -5.7967, -35.2078, NOW()),
(26, 'PARKING_OCCUPANCY', 85.0, '%', NOW(), -5.7945, -35.2090, NOW()),
(31, 'WASTE_LEVEL', 67.3, '%', NOW(), -5.7967, -35.2078, NOW()),
(36, 'SOLAR_POWER', 1250.8, 'W', NOW(), -5.7945, -35.2090, NOW());

-- Dados de sensores com timestamps variados (usando INTERVAL do PostgreSQL)
INSERT INTO sensor_data (device_id, sensor_type, sensor_value, unit, timestamp, latitude, longitude, created_at) VALUES
(1, 'TRAFFIC_FLOW', 52.1, 'vehicles/min', NOW() - INTERVAL '5 minutes', -5.7945, -35.2090, NOW()),
(2, 'TRAFFIC_FLOW', 38.9, 'vehicles/min', NOW() - INTERVAL '3 minutes', -5.7967, -35.2078, NOW()),
(6, 'PM25', 15.7, 'μg/m³', NOW() - INTERVAL '2 minutes', -5.7945, -35.2090, NOW()),
(7, 'PM25', 11.2, 'μg/m³', NOW() - INTERVAL '1 minute', -5.7967, -35.2078, NOW()),
(11, 'WATER_LEVEL', 18.5, 'cm', NOW() - INTERVAL '10 minutes', -5.7945, -35.2090, NOW()),
(16, 'NOISE_LEVEL', 72.1, 'dB', NOW() - INTERVAL '8 minutes', -5.7967, -35.2078, NOW()),
(21, 'TEMPERATURE', 29.8, '°C', NOW() - INTERVAL '15 minutes', -5.7945, -35.2090, NOW()),
(22, 'HUMIDITY', 82.5, '%', NOW() - INTERVAL '12 minutes', -5.7967, -35.2078, NOW()),
(26, 'PARKING_OCCUPANCY', 92.3, '%', NOW() - INTERVAL '7 minutes', -5.7945, -35.2090, NOW()),
(31, 'WASTE_LEVEL', 78.9, '%', NOW() - INTERVAL '20 minutes', -5.7967, -35.2078, NOW()),
(36, 'SOLAR_POWER', 1380.2, 'W', NOW() - INTERVAL '30 minutes', -5.7945, -35.2090, NOW()); 