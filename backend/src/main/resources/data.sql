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
        'LUZ',
        CASE
            WHEN (EXTRACT(HOUR FROM (NOW() - (i * interval '10 minute')))) BETWEEN 6 AND 18 
            THEN 800 + random() * 200 -- Dia
            ELSE 50 + random() * 50 -- Noite
        END,
        'LUX',
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
('Semaforo_Petropolis_01', 'SEMÁFORO', 'Avenida Hermes da Fonseca, Petrópolis, Natal/RN', true, NOW(), 85, 92, NOW(), NOW()),
('Semaforo_Tirol_02', 'SEMÁFORO', 'Avenida Salgado Filho, Tirol, Natal/RN', true, NOW(), 78, 88, NOW(), NOW()),
('Semaforo_CidadeAlta_03', 'SEMÁFORO', 'Rua Jundiaí, Cidade Alta, Natal/RN', true, NOW(), 92, 95, NOW(), NOW()),
('Semaforo_Parnamirim_01', 'SEMÁFORO', 'BR-101, Parnamirim/RN', true, NOW(), 80, 90, NOW(), NOW()),
('Semaforo_SaoGoncalo_01', 'SEMÁFORO', 'RN-160, São Gonçalo do Amarante/RN', true, NOW(), 75, 85, NOW(), NOW()),

-- Sensores de Qualidade do Ar
('Ar_PraiaMeio_01', 'QUALIDADE_AR', 'Praia de Meio, Natal/RN', true, NOW(), 88, 94, NOW(), NOW()),
('Ar_PontaNegra_02', 'QUALIDADE_AR', 'Praia de Ponta Negra, Natal/RN', true, NOW(), 90, 96, NOW(), NOW()),
('Ar_Centro_03', 'QUALIDADE_AR', 'Centro Histórico, Natal/RN', true, NOW(), 82, 89, NOW(), NOW()),
('Ar_Parnamirim_02', 'QUALIDADE_AR', 'Centro de Parnamirim/RN', true, NOW(), 85, 91, NOW(), NOW()),
('Ar_CearaMirim_01', 'QUALIDADE_AR', 'Centro de Ceará-Mirim/RN', true, NOW(), 79, 87, NOW(), NOW()),

-- Luminárias
('Luz_Petropolis_01', 'ILUMINACAO_PUBLICA', 'Rua Dr. Manoel Dantas, Petrópolis, Natal/RN', true, NOW(), 95, 98, NOW(), NOW()),
('Luz_Tirol_02', 'ILUMINACAO_PUBLICA', 'Rua Potengi, Tirol, Natal/RN', true, NOW(), 92, 96, NOW(), NOW()),
('Luz_CidadeAlta_03', 'ILUMINACAO_PUBLICA', 'Rua Apodi, Cidade Alta, Natal/RN', true, NOW(), 88, 93, NOW(), NOW()),
('Luz_Rocas_04', 'ILUMINACAO_PUBLICA', 'Rua Chile, Rocas, Natal/RN', true, NOW(), 90, 95, NOW(), NOW()),
('Luz_Parnamirim_03', 'ILUMINACAO_PUBLICA', 'Rua Augusto Severo, Parnamirim/RN', true, NOW(), 87, 92, NOW(), NOW()),

-- Sensores de Nível de Água
('Agua_CapimMacio_01', 'NIVEL_AGUA', 'Rua do Sol, Capim Macio, Natal/RN', true, NOW(), 85, 90, NOW(), NOW()),
('Agua_Petropolis_02', 'NIVEL_AGUA', 'Avenida Campos Sales, Petrópolis, Natal/RN', true, NOW(), 82, 88, NOW(), NOW()),
('Agua_Tirol_03', 'NIVEL_AGUA', 'Rua Mossoró, Tirol, Natal/RN', true, NOW(), 88, 93, NOW(), NOW()),
('Agua_Parnamirim_04', 'NIVEL_AGUA', 'Rua Francisco Barbosa, Parnamirim/RN', true, NOW(), 80, 86, NOW(), NOW()),
('Agua_SaoGoncalo_02', 'NIVEL_AGUA', 'RN-160, São Gonçalo do Amarante/RN', true, NOW(), 75, 82, NOW(), NOW()),

-- Sensores de Ruído
('Ruido_PontaNegra_01', 'RUÍDO', 'Via Costeira, Ponta Negra, Natal/RN', true, NOW(), 90, 95, NOW(), NOW()),
('Ruido_Centro_02', 'RUÍDO', 'Rua Chile, Centro, Natal/RN', true, NOW(), 85, 90, NOW(), NOW()),
('Ruido_Petropolis_03', 'RUÍDO', 'Avenida Hermes da Fonseca, Petrópolis, Natal/RN', true, NOW(), 88, 93, NOW(), NOW()),
('Ruido_Parnamirim_05', 'RUÍDO', 'BR-101, Parnamirim/RN', true, NOW(), 82, 88, NOW(), NOW()),
('Ruido_CearaMirim_02', 'RUÍDO', 'RN-160, Ceará-Mirim/RN', true, NOW(), 78, 85, NOW(), NOW()),

-- Sensores Meteorológicos
('Temp_PontaNegra_01', 'METEOROLÓGICO', 'Morro do Careca, Ponta Negra, Natal/RN', true, NOW(), 92, 97, NOW(), NOW()),
('Temp_Centro_02', 'METEOROLÓGICO', 'Forte dos Reis Magos, Natal/RN', true, NOW(), 88, 94, NOW(), NOW()),
('Temp_Petropolis_03', 'METEOROLÓGICO', 'Parque das Dunas, Natal/RN', true, NOW(), 90, 95, NOW(), NOW()),
('Temp_Parnamirim_06', 'METEOROLÓGICO', 'Aeroporto Internacional de Natal, Parnamirim/RN', true, NOW(), 85, 91, NOW(), NOW()),
('Temp_SaoGoncalo_03', 'METEOROLÓGICO', 'Porto de São Gonçalo do Amarante/RN', true, NOW(), 80, 87, NOW(), NOW());

-- =============================================================================
-- DISPOSITIVOS INATIVOS E COM BATERIA BAIXA PARA TESTE
-- =============================================================================

-- Dispositivos inativos
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Semaforo_Offline_01', 'SEMÁFORO', 'Rua Teste Offline, Natal/RN', false, NOW() - INTERVAL '2 hours', 45, 60, NOW(), NOW()),
('Ar_Offline_01', 'QUALIDADE_AR', 'Avenida Teste Offline, Parnamirim/RN', false, NOW() - INTERVAL '3 hours', 30, 50, NOW(), NOW()),
('Luz_Offline_01', 'ILUMINACAO_PUBLICA', 'Rua Luz Offline, Natal/RN', false, NOW() - INTERVAL '1 hour', 25, 40, NOW(), NOW()),
('Agua_Offline_01', 'NIVEL_AGUA', 'Avenida Água Offline, São Gonçalo/RN', false, NOW() - INTERVAL '4 hours', 20, 35, NOW(), NOW()),
('Ruido_Offline_01', 'RUÍDO', 'Rua Ruído Offline, Ceará-Mirim/RN', false, NOW() - INTERVAL '5 hours', 15, 30, NOW(), NOW()),
('Temp_Offline_01', 'METEOROLÓGICO', 'Avenida Temp Offline, Natal/RN', false, NOW() - INTERVAL '6 hours', 10, 25, NOW(), NOW());

-- Dispositivos com bateria baixa (ainda ativos)
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Semaforo_BateriaBaixa_01', 'SEMÁFORO', 'Rua Bateria Baixa, Natal/RN', true, NOW(), 18, 70, NOW(), NOW()),
('Ar_BateriaBaixa_01', 'QUALIDADE_AR', 'Avenida Bateria Baixa, Parnamirim/RN', true, NOW(), 15, 65, NOW(), NOW()),
('Luz_BateriaBaixa_01', 'ILUMINACAO_PUBLICA', 'Rua Luz Bateria Baixa, Natal/RN', true, NOW(), 12, 60, NOW(), NOW()),
('Agua_BateriaBaixa_01', 'NIVEL_AGUA', 'Avenida Água Bateria Baixa, São Gonçalo/RN', true, NOW(), 8, 55, NOW(), NOW()),
('Ruido_BateriaBaixa_01', 'RUÍDO', 'Rua Ruído Bateria Baixa, Ceará-Mirim/RN', true, NOW(), 5, 50, NOW(), NOW()),
('Temp_BateriaBaixa_01', 'METEOROLÓGICO', 'Avenida Temp Bateria Baixa, Natal/RN', true, NOW(), 3, 45, NOW(), NOW());

-- Dispositivos com sinal fraco
INSERT INTO devices (name, type, location, active, last_seen, battery_level, signal_strength, created_at, updated_at) VALUES
('Semaforo_SinalFraco_01', 'SEMÁFORO', 'Rua Sinal Fraco, Natal/RN', true, NOW(), 85, 25, NOW(), NOW()),
('Ar_SinalFraco_01', 'QUALIDADE_AR', 'Avenida Sinal Fraco, Parnamirim/RN', true, NOW(), 80, 20, NOW(), NOW()),
('Luz_SinalFraco_01', 'ILUMINACAO_PUBLICA', 'Rua Luz Sinal Fraco, Natal/RN', true, NOW(), 75, 15, NOW(), NOW());

-- =============================================================================
-- GERAÇÃO DE DADOS HISTÓRICOS 
-- =============================================================================

-- Dados meteorológicos para Ponta Negra 
SELECT generate_historical_data('Temp_PontaNegra_01', 'TEMPERATURA', 24, 8, 'CELSIUS', -5.7945, -35.2090, 24, 30);
SELECT generate_historical_data('Temp_PontaNegra_01', 'UMIDADE', 70, 25, 'PERCENTAGE', -5.7945, -35.2090, 24, 30);

-- Dados meteorológicos 
SELECT generate_historical_data('Temp_Centro_02', 'TEMPERATURA', 26, 6, 'CELSIUS', -5.7967, -35.2078, 24, 30);
SELECT generate_historical_data('Temp_Centro_02', 'UMIDADE', 60, 40, 'PERCENTAGE', -5.7967, -35.2078, 24, 30);
SELECT generate_historical_data('Temp_Petropolis_03', 'TEMPERATURA', 25, 7, 'CELSIUS', -5.7900, -35.2075, 24, 30);
SELECT generate_historical_data('Temp_Parnamirim_06', 'TEMPERATURA', 27, 5, 'CELSIUS', -5.9200, -35.2600, 24, 30);
SELECT generate_historical_data('Temp_SaoGoncalo_03', 'TEMPERATURA', 28, 6, 'CELSIUS', -5.7900, -35.2075, 24, 30);

-- Dados de qualidade do ar
SELECT generate_historical_data('Ar_PraiaMeio_01', 'QUALIDADE_AR', 10, 50, 'PPM', -5.7945, -35.2090, 24, 30);
SELECT generate_historical_data('Ar_PontaNegra_02', 'QUALIDADE_AR', 15, 40, 'PPM', -5.8810, -35.1650, 24, 30);
SELECT generate_historical_data('Ar_Centro_03', 'QUALIDADE_AR', 20, 60, 'PPM', -5.7967, -35.2078, 24, 30);
SELECT generate_historical_data('Ar_Parnamirim_02', 'QUALIDADE_AR', 400, 200, 'PPM', -5.9200, -35.2600, 24, 30);

-- Dados de movimento para semáforos (detecção de tráfego)
SELECT generate_historical_data('Semaforo_Tirol_02', 'MOVIMENTO', 20, 80, 'BOOLEAN', -5.8119, -35.2067, 24, 15);
SELECT generate_historical_data('Semaforo_Petropolis_01', 'MOVIMENTO', 15, 60, 'BOOLEAN', -5.7900, -35.2075, 24, 15);
SELECT generate_historical_data('Semaforo_CidadeAlta_03', 'MOVIMENTO', 10, 40, 'BOOLEAN', -5.7967, -35.2078, 24, 15);

-- Dados de ruído
SELECT generate_historical_data('Ruido_PontaNegra_01', 'RUÍDO', 50, 50, 'DB', -5.8825, -35.1633, 24, 20);
SELECT generate_historical_data('Ruido_Centro_02', 'RUÍDO', 60, 40, 'DB', -5.7967, -35.2078, 24, 20);
SELECT generate_historical_data('Ruido_Petropolis_03', 'RUÍDO', 55, 45, 'DB', -5.7900, -35.2075, 24, 20);

-- Dados de luminosidade (com variação dia/noite)
SELECT generate_light_data('Luz_Petropolis_01', -5.7900, -35.2075, 24);
SELECT generate_light_data('Luz_Tirol_02', -5.8119, -35.2067, 24);
SELECT generate_light_data('Luz_CidadeAlta_03', -5.7967, -35.2078, 24);

-- Dados de água (usando tipos válidos - removidos tipos inválidos)
-- Nota: PRESSAO_AGUA, NIVEL_AGUA e VAZAO_AGUA não são tipos de sensores válidos no backend
-- Se necessário, adicione novos tipos de sensores no backend ou use tipos existentes

