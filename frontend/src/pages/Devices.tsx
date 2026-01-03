import React, { useState, useEffect, FormEvent } from 'react';
import { motion } from 'framer-motion';
import {
  Search,
  Filter,
  Edit2,
  Trash2,
  Power,
  PowerOff,
  Battery,
  Signal,
  MapPin,
  Clock
} from 'lucide-react';
import api from '../services/api';
import { Button } from '../components/ui/Button';
import { Badge } from '../components/ui/Badge';
import { Input, Select } from '../components/ui/Input';
import { Modal } from '../components/ui/Modal';
import { Loading } from '../components/ui/Loading';

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
  
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingDevice, setEditingDevice] = useState<Device | null>(null);

  const [searchTerm, setSearchTerm] = useState('');
  const [typeFilter, setTypeFilter] = useState('');
  const [statusFilter, setStatusFilter] = useState('');
  const [sortBy, setSortBy] = useState('name');

  useEffect(() => {
    fetchDevices();
    // eslint-disable-next-line react-hooks/exhaustive-deps
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
      setError(null);
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
    } catch (err: any) {
      const errorMessage = err.response?.data?.message || 'Falha ao excluir o dispositivo.';
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

  const getDeviceTypeEmoji = (type: string) => ({
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

  const deviceTypes = Array.from(new Set(devices.map(d => d.type)));
  
  if (loading && devices.length === 0) {
    return <Loading message="Carregando dispositivos..." />;
  }

  if (error && devices.length === 0) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <motion.div
          initial={{ opacity: 0, scale: 0.9 }}
          animate={{ opacity: 1, scale: 1 }}
          className="glass-card p-8 text-center max-w-md"
        >
          <h2 className="text-2xl font-bold text-white mb-2">Erro</h2>
          <p className="text-white/70 mb-6">{error}</p>
          <Button onClick={fetchDevices} variant="primary">Tentar Novamente</Button>
        </motion.div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <motion.header
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        className="text-center"
      >
        <h1 className="text-4xl md:text-5xl font-bold mb-3 gradient-text">
          Dispositivos IoT
        </h1>
        <p className="text-white/70 text-lg">
          Gerencie e monitore todos os dispositivos do sistema
        </p>
      </motion.header>

      {/* Filters */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="glass-card p-6"
      >
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-4">
          <Input
            placeholder="Buscar por nome ou localização..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            icon={<Search className="w-4 h-4" />}
          />
          <Select
            value={typeFilter}
            onChange={(e) => setTypeFilter(e.target.value)}
            options={[
              { value: '', label: 'Todos os tipos' },
              ...deviceTypes.map(type => ({ value: type, label: type.replace(/_/g, ' ') }))
            ]}
          />
          <Select
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
            options={[
              { value: '', label: 'Todos os status' },
              { value: 'active', label: 'Ativos' },
              { value: 'inactive', label: 'Inativos' }
            ]}
          />
          <Select
            value={sortBy}
            onChange={(e) => setSortBy(e.target.value)}
            options={[
              { value: 'name', label: 'Nome' },
              { value: 'type', label: 'Tipo' },
              { value: 'location', label: 'Localização' },
              { value: 'battery', label: 'Bateria' },
              { value: 'lastSeen', label: 'Última atividade' }
            ]}
          />
        </div>
        <div className="flex items-center justify-between text-sm">
          <span className="text-white/70">
            {filteredDevices.length} de {devices.length} dispositivos
          </span>
          {(searchTerm || typeFilter || statusFilter) && (
            <Button
              variant="ghost"
              size="sm"
              onClick={() => { setSearchTerm(''); setTypeFilter(''); setStatusFilter(''); }}
            >
              Limpar filtros
            </Button>
          )}
        </div>
      </motion.div>

      {/* Devices Grid */}
      <div className="grid grid-cols-1 lg:grid-cols-2 xl:grid-cols-3 gap-6">
        {filteredDevices.length === 0 ? (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            className="col-span-full glass-card p-12 text-center"
          >
            <Filter className="w-16 h-16 text-white/30 mx-auto mb-4" />
            <h3 className="text-xl font-bold text-white mb-2">Nenhum dispositivo encontrado</h3>
            <p className="text-white/60">Tente ajustar os filtros de busca</p>
          </motion.div>
        ) : (
          filteredDevices.map((device, index) => (
              <motion.div
                key={device.id}
                layout
                initial={{ opacity: 0, scale: 0.9 }}
                animate={{ opacity: 1, scale: 1 }}
                exit={{ opacity: 0, scale: 0.9 }}
                transition={{ delay: index * 0.03 }}
                className="glass-card-hover p-6"
              >
                {/* Device Header */}
                <div className="flex items-start justify-between mb-4">
                  <div className="flex items-center gap-3 flex-1 min-w-0">
                    <div className="text-4xl flex-shrink-0">{getDeviceTypeEmoji(device.type)}</div>
                    <div className="min-w-0 flex-1">
                      <h3 className="text-lg font-bold text-white truncate">{device.name}</h3>
                      <p className="text-sm text-white/60">{device.type.replace(/_/g, ' ')}</p>
                    </div>
                  </div>
                  <Badge variant={device.active ? 'success' : 'danger'}>
                    {device.active ? 'Online' : 'Offline'}
                  </Badge>
                </div>

                {/* Device Info */}
                <div className="space-y-3 mb-4">
                  <div className="flex items-center gap-2 text-sm text-white/70">
                    <MapPin className="w-4 h-4 flex-shrink-0" />
                    <span className="truncate">{device.location}</span>
                  </div>
                  <div className="flex items-center gap-2 text-sm text-white/70">
                    <Clock className="w-4 h-4 flex-shrink-0" />
                    <span>{new Date(device.lastSeen).toLocaleString('pt-BR')}</span>
                  </div>
                </div>

                {/* Metrics */}
                <div className="space-y-3 mb-4">
                  {device.batteryLevel != null && (
                    <div>
                      <div className="flex items-center justify-between text-xs text-white/70 mb-1">
                        <span className="flex items-center gap-1">
                          <Battery className="w-3 h-3" />
                          Bateria
                        </span>
                        <span className="font-semibold">{device.batteryLevel}%</span>
                      </div>
                      <div className="h-2 bg-white/10 rounded-full overflow-hidden">
                        <motion.div
                          initial={{ width: 0 }}
                          animate={{ width: `${device.batteryLevel}%` }}
                          transition={{ duration: 1, delay: index * 0.05 }}
                          className={`h-full ${
                            device.batteryLevel > 60 ? 'bg-green-400' :
                            device.batteryLevel > 20 ? 'bg-yellow-400' : 'bg-red-400'
                          }`}
                        />
                      </div>
                    </div>
                  )}
                  {device.signalStrength != null && (
                    <div>
                      <div className="flex items-center justify-between text-xs text-white/70 mb-1">
                        <span className="flex items-center gap-1">
                          <Signal className="w-3 h-3" />
                          Sinal
                        </span>
                        <span className="font-semibold">{device.signalStrength}%</span>
                      </div>
                      <div className="h-2 bg-white/10 rounded-full overflow-hidden">
                        <motion.div
                          initial={{ width: 0 }}
                          animate={{ width: `${device.signalStrength}%` }}
                          transition={{ duration: 1, delay: index * 0.05 + 0.2 }}
                          className="h-full bg-primary-400"
                        />
                      </div>
                    </div>
                  )}
                </div>

                {/* Actions */}
                <div className="flex gap-2">
                  <Button
                    variant="secondary"
                    size="sm"
                    className="flex-1"
                    onClick={() => handleOpenEditModal(device)}
                    disabled={!!actionLoading}
                    icon={<Edit2 className="w-4 h-4" />}
                  >
                    Editar
                  </Button>
                  <Button
                    variant={device.active ? 'ghost' : 'success'}
                    size="sm"
                    onClick={() => handleToggleStatus(device.id)}
                    loading={actionLoading === device.id}
                    disabled={!!actionLoading && actionLoading !== device.id}
                    icon={device.active ? <PowerOff className="w-4 h-4" /> : <Power className="w-4 h-4" />}
                  >
                    {device.active ? 'Desativar' : 'Ativar'}
                  </Button>
                  <Button
                    variant="danger"
                    size="sm"
                    onClick={() => handleDelete(device.id)}
                    loading={actionLoading === device.id}
                    disabled={!!actionLoading && actionLoading !== device.id}
                    icon={<Trash2 className="w-4 h-4" />}
                  >
                  </Button>
                </div>
              </motion.div>
            ))
        )}
      </div>

      {/* Edit Modal */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Editar Dispositivo"
      >
        <form onSubmit={handleUpdateDevice} className="space-y-4">
          <Input
            label="Nome do Dispositivo"
            value={editingDevice?.name || ''}
            onChange={(e) => setEditingDevice(editingDevice ? { ...editingDevice, name: e.target.value } : null)}
            required
          />
          <Input
            label="Localização"
            value={editingDevice?.location || ''}
            onChange={(e) => setEditingDevice(editingDevice ? { ...editingDevice, location: e.target.value } : null)}
            required
          />
          <div className="flex gap-3 justify-end pt-4">
            <Button
              type="button"
              variant="ghost"
              onClick={() => setIsModalOpen(false)}
              disabled={!!actionLoading}
            >
              Cancelar
            </Button>
            <Button
              type="submit"
              variant="primary"
              loading={!!actionLoading}
            >
              Salvar Alterações
            </Button>
          </div>
        </form>
      </Modal>
    </div>
  );
};

export default Devices;
