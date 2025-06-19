-- Schema para dispositivos IoT
CREATE TABLE IF NOT EXISTS devices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    location VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    last_seen TIMESTAMP,
    battery_level INTEGER,
    signal_strength INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Schema para dados de sensores
CREATE TABLE IF NOT EXISTS sensor_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    device_id BIGINT NOT NULL,
    sensor_type VARCHAR(50) NOT NULL,
    sensor_value DOUBLE NOT NULL,
    unit VARCHAR(20),
    timestamp TIMESTAMP NOT NULL,
    latitude DOUBLE,
    longitude DOUBLE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (device_id) REFERENCES devices(id)
);

-- Índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_devices_type ON devices(type);
CREATE INDEX IF NOT EXISTS idx_devices_location ON devices(location);
CREATE INDEX IF NOT EXISTS idx_devices_active ON devices(active);
CREATE INDEX IF NOT EXISTS idx_sensor_data_device_id ON sensor_data(device_id);
CREATE INDEX IF NOT EXISTS idx_sensor_data_timestamp ON sensor_data(timestamp);
CREATE INDEX IF NOT EXISTS idx_sensor_data_sensor_type ON sensor_data(sensor_type); 