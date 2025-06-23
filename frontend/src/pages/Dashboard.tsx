import React, { useState, useEffect } from 'react';
import './Dashboard.css';

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

const Dashboard: React.FC = () => {
  const [devices, setDevices] = useState<Device[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchDevices();
  }, []);

  const fetchDevices = async () => {
    try {
      setLoading(true);
      const response = await fetch('/api/devices');
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
      'SEMÁFORO': '🚦',
      'QUALIDADE_AR': '🌬️',
      'ILUMINACAO_PUBLICA': '💡',
      'NIVEL_AGUA': '💧',
      'RUÍDO': '🔊',
      'METEOROLÓGICO': '🌤️',
      'CÂMERA_SEGURANÇA': '📹',
      'ESTACIONAMENTO': '🅿️',
      'LIXEIRA': '🗑️',
      'PAINEL_SOLAR': '☀️'
    };
    return icons[type] || '📱';
  };

  const getStatusColor = (active: boolean) => {
    return active ? '#4CAF50' : '#f44336';
  };

  // Estatísticas
  const totalDevices = devices.length;
  const activeDevices = devices.filter(d => d.active).length;
  const offlineDevices = devices.filter(d => !d.active).length;
  const lowBatteryDevices = devices.filter(d => d.batteryLevel && d.batteryLevel < 20).length;

  // Dispositivos por tipo
  const devicesByType = devices.reduce((acc, device) => {
    acc[device.type] = (acc[device.type] || 0) + 1;
    return acc;
  }, {} as { [key: string]: number });

  if (loading) {
    return (
      <div className="dashboard">
        <div className="loading">
          <h2>Carregando dashboard...</h2>
          <div className="spinner"></div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="dashboard">
        <div className="error">
          <h2>Erro ao carregar dados</h2>
          <p>{error}</p>
          <button onClick={fetchDevices}>Tentar novamente</button>
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <h1>📊 Dashboard IoT City</h1>
        <p>Visão geral do sistema de dispositivos IoT</p>
      </header>

      {/* Cards de Estatísticas */}
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-icon">📱</div>
          <div className="stat-content">
            <h3>Total de Dispositivos</h3>
            <p className="stat-number">{totalDevices}</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">🟢</div>
          <div className="stat-content">
            <h3>Dispositivos Ativos</h3>
            <p className="stat-number">{activeDevices}</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">🔴</div>
          <div className="stat-content">
            <h3>Dispositivos Offline</h3>
            <p className="stat-number">{offlineDevices}</p>
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-icon">🔋</div>
          <div className="stat-content">
            <h3>Bateria Baixa</h3>
            <p className="stat-number">{lowBatteryDevices}</p>
          </div>
        </div>
      </div>

      {/* Dispositivos por Tipo */}
      <div className="dashboard-section">
        <h2>Dispositivos por Tipo</h2>
        <div className="type-stats">
          {Object.entries(devicesByType).map(([type, count]) => (
            <div key={type} className="type-card">
              <span className="type-icon">{getDeviceTypeIcon(type)}</span>
              <div className="type-info">
                <h4>{type.replace('_', ' ')}</h4>
                <p>{count} dispositivos</p>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Dispositivos Recentes */}
      <div className="dashboard-section">
        <h2>Dispositivos Recentes</h2>
        <div className="recent-devices">
          {devices.slice(0, 6).map(device => (
            <div key={device.id} className="recent-device-card">
              <div className="device-header">
                <span className="device-icon">{getDeviceTypeIcon(device.type)}</span>
                <h4>{device.name}</h4>
                <span 
                  className="status-indicator" 
                  style={{ backgroundColor: getStatusColor(device.active) }}
                >
                  {device.active ? 'Online' : 'Offline'}
                </span>
              </div>
              <p className="device-location">{device.location}</p>
              {device.batteryLevel && (
                <div className="battery-info">
                  <span>🔋 {device.batteryLevel}%</span>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Dashboard; 