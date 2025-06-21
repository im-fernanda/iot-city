import React, { useState, useEffect } from 'react';
import './App.css';

interface Device {
  id: number;
  name: string;
  type: string;
  location: string;
  active: boolean;
  batteryLevel?: number;
  signalStrength?: number;
  lastSeen: string;
}

function App() {
  const [devices, setDevices] = useState<Device[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchDevices();
  }, []);

  const fetchDevices = async () => {
    try {
      setLoading(true);
      const response = await fetch('http://localhost:8080/api/devices');
      if (!response.ok) {
        throw new Error('Erro ao carregar dispositivos');
      }
      const data = await response.json();
      setDevices(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro desconhecido');
    } finally {
      setLoading(false);
    }
  };

  const getDeviceTypeIcon = (type: string) => {
    const icons: { [key: string]: string } = {
      'TRAFFIC_LIGHT': '🚦',
      'AIR_QUALITY': '🌬️',
      'STREET_LIGHT': '💡',
      'WATER_LEVEL': '💧',
      'NOISE_SENSOR': '🔊',
      'WEATHER_SENSOR': '🌤️',
      'SECURITY_CAMERA': '📹',
      'PARKING_SENSOR': '🅿️',
      'WASTE_SENSOR': '🗑️',
      'SOLAR_PANEL': '☀️'
    };
    return icons[type] || '📱';
  };

  const getStatusColor = (active: boolean) => {
    return active ? '#4CAF50' : '#f44336';
  };

  if (loading) {
    return (
      <div className="App">
        <div className="loading">
          <h2>Carregando dispositivos IoT...</h2>
          <div className="spinner"></div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="App">
        <div className="error">
          <h2>Erro ao carregar dados</h2>
          <p>{error}</p>
          <button onClick={fetchDevices}>Tentar novamente</button>
        </div>
      </div>
    );
  }

  return (
    <div className="App">
      <header className="App-header">
        <h1>🌆 IoT City - Sistema de Gerenciamento</h1>
        <p>Monitoramento de Dispositivos IoT em Tempo Real</p>
      </header>
      
      <main className="App-main">
        <div className="stats">
          <div className="stat-card">
            <h3>Total de Dispositivos</h3>
            <p className="stat-number">{devices.length}</p>
          </div>
          <div className="stat-card">
            <h3>Dispositivos Ativos</h3>
            <p className="stat-number">{devices.filter(d => d.active).length}</p>
          </div>
          <div className="stat-card">
            <h3>Dispositivos Offline</h3>
            <p className="stat-number">{devices.filter(d => !d.active).length}</p>
          </div>
        </div>

        <div className="devices-grid">
          <h2>Dispositivos IoT</h2>
          {devices.length === 0 ? (
            <p className="no-devices">Nenhum dispositivo encontrado</p>
          ) : (
            <div className="devices-list">
              {devices.map(device => (
                <div key={device.id} className="device-card">
                  <div className="device-header">
                    <span className="device-icon">{getDeviceTypeIcon(device.type)}</span>
                    <h3>{device.name}</h3>
                    <span 
                      className="status-indicator" 
                      style={{ backgroundColor: getStatusColor(device.active) }}
                    >
                      {device.active ? 'Online' : 'Offline'}
                    </span>
                  </div>
                  
                  <div className="device-info">
                    <p><strong>Tipo:</strong> {device.type}</p>
                    <p><strong>Localização:</strong> {device.location}</p>
                    {device.batteryLevel && (
                      <p><strong>Bateria:</strong> {device.batteryLevel}%</p>
                    )}
                    {device.signalStrength && (
                      <p><strong>Sinal:</strong> {device.signalStrength}%</p>
                    )}
                    <p><strong>Última atividade:</strong> {new Date(device.lastSeen).toLocaleString()}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </main>
    </div>
  );
}

export default App;
