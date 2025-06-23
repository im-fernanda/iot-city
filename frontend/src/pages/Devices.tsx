import React, { useState, useEffect, FormEvent } from 'react';
import api from '../services/api';
import './Devices.css';

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

const Devices: React.FC = () => {
  const [devices, setDevices] = useState<Device[]>([]);
  const [filteredDevices, setFilteredDevices] = useState<Device[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [actionLoading, setActionLoading] = useState<number | null>(null);
  
  // Edit Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingDevice, setEditingDevice] = useState<Device | null>(null);

  // Filtros
  const [searchTerm, setSearchTerm] = useState('');
  const [typeFilter, setTypeFilter] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [sortBy, setSortBy] = useState('name');

  useEffect(() => {
    fetchDevices();
  }, []);

  useEffect(() => {
    filterAndSortDevices();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [devices, searchTerm, typeFilter, statusFilter, sortBy]);

  const fetchDevices = async () => {
    try {
      setLoading(true);
      const response = await api.get('/devices');
      setDevices(response.data);
    } catch (err) {
      setError('Erro ao carregar dispositivos');
    } finally {
      setLoading(false);
    }
  };

  const filterAndSortDevices = () => {
    let filtered = [...devices];

    if (searchTerm) {
      filtered = filtered.filter(d =>
        d.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        d.location.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (typeFilter) {
      filtered = filtered.filter(d => d.type === typeFilter);
    }

    if (statusFilter) {
      filtered = filtered.filter(d => (statusFilter === 'active' ? d.active : !d.active));
    }

    filtered.sort((a, b) => {
      switch (sortBy) {
        case 'name': return a.name.localeCompare(b.name);
        case 'type': return a.type.localeCompare(b.type);
        case 'location': return a.location.localeCompare(b.location);
        case 'battery': return (b.batteryLevel || 0) - (a.batteryLevel || 0);
        case 'lastSeen': return new Date(b.lastSeen).getTime() - new Date(a.lastSeen).getTime();
        default: return 0;
      }
    });

    setFilteredDevices(filtered);
  };
  
  // --- CRUD Handlers ---

  const handleToggleStatus = async (deviceId: number) => {
    setActionLoading(deviceId);
    try {
      const response = await api.patch(`/devices/${deviceId}/toggle`);
      setDevices(devices.map(d => (d.id === deviceId ? response.data : d)));
    } catch (err) {
      alert('Falha ao alternar o status do dispositivo.');
    } finally {
      setActionLoading(null);
    }
  };

  const handleDelete = async (deviceId: number) => {
    const deviceToDelete = devices.find(d => d.id === deviceId);
    if (!window.confirm(`Tem certeza que deseja excluir o dispositivo "${deviceToDelete?.name}"?`)) return;
    
    setActionLoading(deviceId);
    try {
      await api.delete(`/devices/${deviceId}`);
      setDevices(devices.filter(d => d.id !== deviceId));
      // Feedback de sucesso
      alert(`Dispositivo "${deviceToDelete?.name}" excluído com sucesso!`);
    } catch (err: any) {
      // Tratamento de erro mais específico
      let errorMessage = 'Falha ao excluir o dispositivo.';
      
      if (err.response?.data?.message) {
        errorMessage = err.response.data.message;
      } else if (err.response?.status === 404) {
        errorMessage = 'Dispositivo não encontrado.';
      } else if (err.response?.status === 500) {
        errorMessage = 'Erro interno do servidor. Tente novamente.';
      } else if (err.message) {
        errorMessage = `Erro: ${err.message}`;
      }
      
      alert(`❌ ${errorMessage}`);
    } finally {
      setActionLoading(null);
    }
  };

  const handleOpenEditModal = (device: Device) => {
    setEditingDevice({ ...device });
    setIsModalOpen(true);
  };
  
  const handleUpdateDevice = async (e: FormEvent) => {
    e.preventDefault();
    if (!editingDevice) return;

    setActionLoading(editingDevice.id);
    try {
      const response = await api.put(`/devices/${editingDevice.id}`, {
        name: editingDevice.name,
        location: editingDevice.location,
      });
      setDevices(devices.map(d => d.id === editingDevice.id ? response.data : d));
      setIsModalOpen(false);
      setEditingDevice(null);
    } catch (err) {
      alert('Falha ao atualizar o dispositivo.');
    } finally {
      setActionLoading(null);
    }
  };

  // --- UI Helpers ---

  const getDeviceTypeIcon = (type: string) => ({
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
  }[type] || '📱');

  const getStatusStyle = (active: boolean) => ({
    backgroundColor: active ? '#4CAF50' : '#f44336',
  });

  const getBatteryStyle = (level?: number) => {
    if (!level) return { width: '0%', backgroundColor: '#666' };
    const color = level > 60 ? '#4CAF50' : level > 20 ? '#FF9800' : '#f44336';
    return { width: `${level}%`, backgroundColor: color };
  };

  const deviceTypes = Array.from(new Set(devices.map(d => d.type)));
  
  if (loading) return <div className="devices-page"><div className="loading"><div className="spinner"></div></div></div>;
  if (error) return <div className="devices-page"><div className="error"><h2>{error}</h2><button onClick={fetchDevices}>Tentar Novamente</button></div></div>;

  return (
    <div className="devices-page">
      <header className="devices-header">
        <h1>📱 Dispositivos IoT</h1>
        <p>Gerencie e monitore todos os dispositivos do sistema</p>
      </header>

      {/* Filtros */}
      <div className="filters-section">
        <div className="filters-grid">
          <div className="filter-group">
            <label>🔍 Buscar</label>
            <input type="text" placeholder="Nome ou localização..." value={searchTerm} onChange={(e) => setSearchTerm(e.target.value)} className="filter-input"/>
          </div>
          <div className="filter-group">
            <label>📋 Tipo</label>
            <select value={typeFilter} onChange={(e) => setTypeFilter(e.target.value)} className="filter-select">
              <option value="">Todos os tipos</option>
              {deviceTypes.map(type => <option key={type} value={type}>{getDeviceTypeIcon(type)} {type.replace('_', ' ')}</option>)}
            </select>
          </div>
          <div className="filter-group">
            <label>🔄 Status</label>
            <select value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)} className="filter-select">
              <option value="">Todos</option>
              <option value="active">🟢 Ativos</option>
              <option value="inactive">🔴 Inativos</option>
            </select>
          </div>
          <div className="filter-group">
            <label>📊 Ordenar por</label>
            <select value={sortBy} onChange={(e) => setSortBy(e.target.value)} className="filter-select">
              <option value="name">Nome</option>
              <option value="type">Tipo</option>
              <option value="location">Localização</option>
              <option value="battery">Bateria</option>
              <option value="lastSeen">Última atividade</option>
            </select>
          </div>
        </div>
        <div className="filters-summary">
          <span>📊 {filteredDevices.length} de {devices.length} dispositivos</span>
          {(searchTerm || typeFilter || statusFilter) && (
            <button onClick={() => { setSearchTerm(''); setTypeFilter(''); setStatusFilter(''); }} className="clear-filters">
              🗑️ Limpar filtros
            </button>
          )}
        </div>
      </div>

      {/* Grid de Dispositivos */}
      <div className="devices-grid">
        {filteredDevices.length === 0 ? (
          <div className="no-devices"><h3>Nenhum dispositivo encontrado</h3><p>Tente ajustar os filtros de busca</p></div>
        ) : (
          filteredDevices.map(device => (
            <div key={device.id} className="device-card">
              <div className="device-header">
                <span className="device-icon">{getDeviceTypeIcon(device.type)}</span>
                <h3>{device.name}</h3>
                <span className="status-indicator" style={getStatusStyle(device.active)}>
                  {device.active ? 'Online' : 'Offline'}
                </span>
              </div>
              <div className="device-info">
                <p><strong>Tipo:</strong> {device.type.replace('_', ' ')}</p>
                <p><strong>Localização:</strong> {device.location}</p>
                <p><strong>Última atividade:</strong> {new Date(device.lastSeen).toLocaleString()}</p>
              </div>
              <div className="device-metrics">
                {device.batteryLevel != null && (
                  <div className="metric">
                    <span>🔋 Bateria</span>
                    <div className="battery-bar"><div className="battery-fill" style={getBatteryStyle(device.batteryLevel)}></div></div>
                    <span className="battery-text">{device.batteryLevel}%</span>
                  </div>
                )}
                {device.signalStrength != null && (
                  <div className="metric">
                    <span>📶 Sinal</span>
                    <div className="signal-bar"><div className="signal-fill" style={{ width: `${device.signalStrength}%` }}></div></div>
                    <span className="signal-text">{device.signalStrength}%</span>
                  </div>
                )}
              </div>
              <div className="device-actions">
                <button className="action-btn edit" onClick={() => handleOpenEditModal(device)} disabled={!!actionLoading}>
                  ✏️ Editar
                </button>
                <button className="action-btn toggle" onClick={() => handleToggleStatus(device.id)} disabled={!!actionLoading}>
                  {actionLoading === device.id ? '...' : (device.active ? '⏸️ Desativar' : '▶️ Ativar')}
                </button>
                <button className="action-btn delete" onClick={() => handleDelete(device.id)} disabled={!!actionLoading}>
                  {actionLoading === device.id ? '...' : '🗑️ Excluir'}
                </button>
              </div>
            </div>
          ))
        )}
      </div>

      {/* Modal de Edição */}
      {isModalOpen && editingDevice && (
        <div className="modal-overlay">
          <div className="modal-content">
            <form onSubmit={handleUpdateDevice}>
              <h2>Editar Dispositivo</h2>
              <div className="form-group">
                <label htmlFor="deviceName">Nome</label>
                <input
                  id="deviceName"
                  type="text"
                  value={editingDevice.name}
                  onChange={(e) => setEditingDevice({ ...editingDevice, name: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="deviceLocation">Localização</label>
                <input
                  id="deviceLocation"
                  type="text"
                  value={editingDevice.location}
                  onChange={(e) => setEditingDevice({ ...editingDevice, location: e.target.value })}
                  required
                />
              </div>
              <div className="modal-actions">
                <button type="button" className="btn-cancel" onClick={() => setIsModalOpen(false)} disabled={!!actionLoading}>
                  Cancelar
                </button>
                <button type="submit" className="btn-save" disabled={!!actionLoading}>
                  {actionLoading === editingDevice?.id ? 'Salvando...' : 'Salvar Alterações'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default Devices; 