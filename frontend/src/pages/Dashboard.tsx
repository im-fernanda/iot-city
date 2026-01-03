import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { 
  Cpu, 
  Activity, 
  AlertTriangle, 
  Battery, 
  TrendingUp,
  MapPin,
  Clock,
  Zap
} from 'lucide-react';
import { StatCard } from '../components/ui/Card';
import { Loading } from '../components/ui/Loading';
import { Badge } from '../components/ui/Badge';
import { Button } from '../components/ui/Button';

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
    const interval = setInterval(fetchDevices, 30000); // Refresh every 30s
    return () => clearInterval(interval);
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
      setError(null);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Erro desconhecido');
    } finally {
      setLoading(false);
    }
  };

  const getDeviceTypeEmoji = (type: string) => {
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

  // Estatísticas
  const totalDevices = devices.length;
  const activeDevices = devices.filter(d => d.active).length;
  const offlineDevices = devices.filter(d => !d.active).length;
  const lowBatteryDevices = devices.filter(d => d.batteryLevel && d.batteryLevel < 20).length;
  const activePercentage = totalDevices > 0 ? ((activeDevices / totalDevices) * 100).toFixed(1) : 0;

  // Dispositivos por tipo
  const devicesByType = devices.reduce((acc, device) => {
    acc[device.type] = (acc[device.type] || 0) + 1;
    return acc;
  }, {} as { [key: string]: number });

  if (loading && devices.length === 0) {
    return <Loading message="Carregando dashboard..." />;
  }

  if (error && devices.length === 0) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <motion.div
          initial={{ opacity: 0, scale: 0.9 }}
          animate={{ opacity: 1, scale: 1 }}
          className="glass-card p-8 text-center max-w-md"
        >
          <AlertTriangle className="w-16 h-16 text-red-400 mx-auto mb-4" />
          <h2 className="text-2xl font-bold text-white mb-2">Erro ao carregar dados</h2>
          <p className="text-white/70 mb-6">{error}</p>
          <Button onClick={fetchDevices} variant="primary">
            Tentar novamente
          </Button>
        </motion.div>
      </div>
    );
  }

  return (
    <div className="space-y-8">
      {/* Header */}
      <motion.header
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        className="text-center mb-8"
      >
        <h1 className="text-4xl md:text-5xl font-bold mb-3 text-white">
          Dashboard IoT City
        </h1>
        <p className="text-white/70 text-lg">
          Visão geral do sistema de dispositivos IoT em tempo real
        </p>
      </motion.header>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard
          icon={<Cpu className="w-8 h-8 text-primary-400" />}
          title="Total de Dispositivos"
          value={totalDevices}
        />
        <StatCard
          icon={<Activity className="w-8 h-8 text-green-400" />}
          title="Dispositivos Ativos"
          value={activeDevices}
          trend={{ value: Number(activePercentage), isPositive: activeDevices > offlineDevices }}
        />
        <StatCard
          icon={<AlertTriangle className="w-8 h-8 text-red-400" />}
          title="Dispositivos Offline"
          value={offlineDevices}
        />
        <StatCard
          icon={<Battery className="w-8 h-8 text-yellow-400" />}
          title="Bateria Baixa"
          value={lowBatteryDevices}
        />
      </div>

      {/* Dispositivos por Tipo */}
      <motion.section
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.2 }}
        className="glass-card p-6"
      >
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold text-white flex items-center gap-2">
            <TrendingUp className="w-6 h-6 text-primary-400" />
            Dispositivos por Tipo
          </h2>
        </div>
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-4">
          {Object.entries(devicesByType).map(([type, count], index) => (
            <motion.div
              key={type}
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              transition={{ delay: index * 0.05 }}
              whileHover={{ scale: 1.05 }}
              className="glass-card-hover p-4 text-center"
            >
              <div className="text-4xl mb-2">{getDeviceTypeEmoji(type)}</div>
              <h4 className="text-white font-semibold mb-1 text-sm">
                {type.replace(/_/g, ' ')}
              </h4>
              <p className="text-primary-400 font-bold text-xl">{count}</p>
              <p className="text-white/60 text-xs">dispositivos</p>
            </motion.div>
          ))}
        </div>
      </motion.section>

      {/* Dispositivos Recentes */}
      <motion.section
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.3 }}
        className="glass-card p-6"
      >
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold text-white flex items-center gap-2">
            <Clock className="w-6 h-6 text-primary-400" />
            Dispositivos Recentes
          </h2>
          <Button 
            variant="ghost" 
            size="sm"
            onClick={fetchDevices}
            loading={loading}
          >
            Atualizar
          </Button>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {devices.slice(0, 6).map((device, index) => (
            <motion.div
              key={device.id}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.05 }}
              whileHover={{ y: -5 }}
              className="glass-card-hover p-5"
            >
              <div className="flex items-start justify-between mb-3">
                <div className="flex items-center gap-3">
                  <div className="text-3xl">{getDeviceTypeEmoji(device.type)}</div>
                  <div>
                    <h4 className="text-white font-semibold">{device.name}</h4>
                    <p className="text-white/60 text-sm flex items-center gap-1">
                      <MapPin className="w-3 h-3" />
                      {device.location}
                    </p>
                  </div>
                </div>
                <Badge variant={device.active ? 'success' : 'danger'}>
                  {device.active ? 'Online' : 'Offline'}
                </Badge>
              </div>
              
              {device.batteryLevel != null && (
                <div className="mt-3">
                  <div className="flex items-center justify-between text-xs text-white/70 mb-1">
                    <span className="flex items-center gap-1">
                      <Battery className="w-3 h-3" />
                      Bateria
                    </span>
                    <span>{device.batteryLevel}%</span>
                  </div>
                  <div className="h-2 bg-white/10 rounded-full overflow-hidden">
                    <motion.div
                      initial={{ width: 0 }}
                      animate={{ width: `${device.batteryLevel}%` }}
                      transition={{ duration: 1, delay: index * 0.1 }}
                      className={`h-full rounded-full ${
                        device.batteryLevel > 60
                          ? 'bg-green-400'
                          : device.batteryLevel > 20
                          ? 'bg-yellow-400'
                          : 'bg-red-400'
                      }`}
                    />
                  </div>
                </div>
              )}

              {device.signalStrength != null && (
                <div className="mt-2">
                  <div className="flex items-center justify-between text-xs text-white/70 mb-1">
                    <span className="flex items-center gap-1">
                      <Zap className="w-3 h-3" />
                      Sinal
                    </span>
                    <span>{device.signalStrength}%</span>
                  </div>
                  <div className="h-2 bg-white/10 rounded-full overflow-hidden">
                    <motion.div
                      initial={{ width: 0 }}
                      animate={{ width: `${device.signalStrength}%` }}
                      transition={{ duration: 1, delay: index * 0.1 + 0.2 }}
                      className="h-full rounded-full bg-primary-400"
                    />
                  </div>
                </div>
              )}
            </motion.div>
          ))}
        </div>
      </motion.section>
    </div>
  );
};

export default Dashboard; 