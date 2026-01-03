import React, { useEffect, useState } from 'react';
import { motion } from 'framer-motion';
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer
} from 'recharts';
import {
  Activity,
  Thermometer,
  Droplets,
  Wind,
  TrendingUp,
  Calendar,
  RefreshCw
} from 'lucide-react';
import api from '../services/api';
import { Select } from '../components/ui/Input';
import { Button } from '../components/ui/Button';
import { Loading } from '../components/ui/Loading';

interface Device {
  id: number;
  name: string;
}

interface SensorData {
  id: number;
  device: {
    id: number;
    name: string;
  };
  sensorType: string;
  value: string; // Backend já retorna formatado com 2 casas decimais
  unit: string;
  timestamp: string;
  latitude?: number;
  longitude?: number;
}

const Sensors: React.FC = () => {
  const [devices, setDevices] = useState<Device[]>([]);
  const [allDevices, setAllDevices] = useState<Device[]>([]);
  const [sensorTypes, setSensorTypes] = useState<string[]>([]);
  const [selectedType, setSelectedType] = useState('');
  const [selectedDevice, setSelectedDevice] = useState('');
  const [sensorData, setSensorData] = useState<SensorData[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchFilterData = async () => {
      try {
        const [typesRes, devicesRes] = await Promise.all([
          api.get('/sensor-data/types'),
          api.get('/devices')
        ]);
        setSensorTypes(typesRes.data);
        setAllDevices(devicesRes.data);
        setDevices(devicesRes.data);
      } catch {
        setError("Erro ao carregar filtros");
      }
    };
    fetchFilterData();
  }, []);

  useEffect(() => {
    const fetchDevicesForType = async () => {
      if (selectedType) {
        try {
          const response = await api.get(`/sensor-data/devices-by-type/${selectedType}`);
          setDevices(response.data);
          if (selectedDevice && !response.data.some((d: Device) => d.id === Number(selectedDevice))) {
            setSelectedDevice('');
          }
        } catch (error) {
          setDevices([]);
        }
      } else {
        setDevices(allDevices);
      }
    };
    fetchDevicesForType();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedType, allDevices]);

  useEffect(() => {
    if (selectedDevice && selectedType) {
      fetchSensorData();
    } else {
      setSensorData([]);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedDevice, selectedType]);

  const getSensorIcon = (sensorType: string) => {
    switch (sensorType?.toUpperCase()) {
      case 'TEMPERATURE':
        return <Thermometer className="w-6 h-6 text-orange-400" />;
      case 'HUMIDITY':
        return <Droplets className="w-6 h-6 text-blue-400" />;
      case 'PM25':
        return <Wind className="w-6 h-6 text-purple-400" />;
      default:
        return <Activity className="w-6 h-6 text-primary-400" />;
    }
  };

  const getUnit = (sensorType: string): string => {
    switch (sensorType) {
      case 'TEMPERATURE':
        return '°C';
      case 'HUMIDITY':
        return '%';
      case 'PM25':
        return 'μg/m³';
      default:
        return '';
    }
  };

  const fetchSensorData = async () => {
    if (!selectedType || !selectedDevice) return;

    setLoading(true);
    setError(null);
    try {
      const startDate = new Date();
      startDate.setHours(startDate.getHours() - 24);
      
      const params = {
        deviceId: selectedDevice,
        sensorType: selectedType,
        start: startDate.toISOString(),
        end: new Date().toISOString()
      };
      
      const response = await api.get('/sensor-data', { params });

      const data = response.data
        .sort((a: any, b: any) => new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime())
        .map((item: any) => ({
          ...item,
          // value vem como string formatada do backend, mas precisamos do número para o gráfico
          valueForChart: parseFloat(item.value), // Para o gráfico (número)
          value: item.value, // Mantém string formatada para exibição
          timestamp: new Date(item.timestamp).toLocaleString('pt-BR', {
            day: '2-digit',
            month: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
          })
        }));

      setSensorData(data);
    } catch (error) {
      setError("Falha ao buscar dados do sensor.");
    } finally {
      setLoading(false);
    }
  };

  const selectedDeviceName = devices.find(d => d.id === Number(selectedDevice))?.name;

  // Calculate stats (valores já vêm formatados do backend como string)
  const stats = sensorData.length > 0 ? {
    current: sensorData[sensorData.length - 1]?.value || '0.00',
    average: (sensorData.reduce((acc, d) => acc + parseFloat(d.value), 0) / sensorData.length).toFixed(2),
    max: Math.max(...sensorData.map(d => parseFloat(d.value))).toFixed(2),
    min: Math.min(...sensorData.map(d => parseFloat(d.value))).toFixed(2),
  } : null;

  return (
    <div className="space-y-6">
      {/* Header */}
      <motion.header
        initial={{ opacity: 0, y: -20 }}
        animate={{ opacity: 1, y: 0 }}
        className="text-center"
      >
        <h1 className="text-4xl md:text-5xl font-bold mb-3 gradient-text">
          Sensores IoT
        </h1>
        <p className="text-white/70 text-lg">
          Visualize o histórico de dados dos sensores em tempo real
        </p>
      </motion.header>

      {/* Filters */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        className="glass-card p-6"
      >
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          <Select
            label="Tipo de Sensor"
            value={selectedType}
            onChange={e => setSelectedType(e.target.value)}
            options={[
              { value: '', label: 'Selecione um tipo' },
              ...sensorTypes.map(type => ({ value: type, label: type }))
            ]}
          />
          <Select
            label="Dispositivo"
            value={selectedDevice}
            onChange={e => setSelectedDevice(e.target.value)}
            disabled={!selectedType}
            options={[
              { value: '', label: selectedType ? "Selecione um dispositivo" : "Selecione um tipo primeiro" },
              ...devices.map(device => ({ value: String(device.id), label: device.name }))
            ]}
          />
          <div className="flex items-end">
            <Button
              variant="secondary"
              className="w-full"
              onClick={fetchSensorData}
              disabled={!selectedType || !selectedDevice || loading}
              loading={loading}
              icon={<RefreshCw className="w-4 h-4" />}
            >
              Atualizar Dados
            </Button>
          </div>
        </div>
      </motion.div>

      {/* Stats Cards */}
      {stats && selectedType && (
        <motion.div
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="grid grid-cols-2 md:grid-cols-4 gap-4"
        >
          <div className="glass-card p-4">
            <div className="flex items-center gap-2 mb-2">
              {getSensorIcon(selectedType)}
              <span className="text-sm text-white/70">Atual</span>
            </div>
            <p className="text-2xl font-bold text-white">
              {stats.current} {getUnit(selectedType)}
            </p>
          </div>
          <div className="glass-card p-4">
            <div className="flex items-center gap-2 mb-2">
              <TrendingUp className="w-6 h-6 text-green-400" />
              <span className="text-sm text-white/70">Média</span>
            </div>
            <p className="text-2xl font-bold text-white">
              {stats.average} {getUnit(selectedType)}
            </p>
          </div>
          <div className="glass-card p-4">
            <div className="flex items-center gap-2 mb-2">
              <Activity className="w-6 h-6 text-blue-400" />
              <span className="text-sm text-white/70">Máximo</span>
            </div>
            <p className="text-2xl font-bold text-white">
              {stats.max} {getUnit(selectedType)}
            </p>
          </div>
          <div className="glass-card p-4">
            <div className="flex items-center gap-2 mb-2">
              <Activity className="w-6 h-6 text-purple-400" />
              <span className="text-sm text-white/70">Mínimo</span>
            </div>
            <p className="text-2xl font-bold text-white">
              {stats.min} {getUnit(selectedType)}
            </p>
          </div>
        </motion.div>
      )}

      {/* Graph Section */}
      <motion.div
        initial={{ opacity: 0, y: 20 }}
        animate={{ opacity: 1, y: 0 }}
        transition={{ delay: 0.2 }}
        className="glass-card p-6"
      >
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold text-white flex items-center gap-2">
            <Calendar className="w-6 h-6 text-primary-400" />
            Histórico - Últimas 24 horas
          </h2>
          {selectedDeviceName && (
            <span className="text-sm text-white/70">
              Dispositivo: <span className="text-white font-semibold">{selectedDeviceName}</span>
            </span>
          )}
        </div>

        {loading ? (
          <div className="flex items-center justify-center h-[400px]">
            <Loading message="Carregando dados do sensor..." />
          </div>
        ) : error ? (
          <div className="flex items-center justify-center h-[400px]">
            <div className="text-center">
              <Activity className="w-16 h-16 text-red-400 mx-auto mb-4" />
              <p className="text-white/70">{error}</p>
            </div>
          </div>
        ) : sensorData.length === 0 ? (
          <div className="flex items-center justify-center h-[400px]">
            <div className="text-center">
              <Activity className="w-16 h-16 text-white/30 mx-auto mb-4" />
              <p className="text-white/70 mb-2">
                {selectedType && selectedDevice
                  ? "Nenhum dado encontrado para o período selecionado."
                  : "Selecione um tipo de sensor e um dispositivo para exibir o gráfico."}
              </p>
            </div>
          </div>
        ) : (
          <motion.div
            initial={{ opacity: 0, scale: 0.95 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{ duration: 0.5 }}
          >
            <ResponsiveContainer width="100%" height={400}>
              <LineChart data={sensorData}>
                <defs>
                  <linearGradient id="colorValue" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="5%" stopColor="#38bdf8" stopOpacity={0.8}/>
                    <stop offset="95%" stopColor="#38bdf8" stopOpacity={0.1}/>
                  </linearGradient>
                </defs>
                <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.1)" />
                <XAxis 
                  dataKey="timestamp" 
                  stroke="rgba(255,255,255,0.6)"
                  tick={{ fill: 'rgba(255,255,255,0.6)', fontSize: 12 }}
                  angle={-20}
                  textAnchor="end"
                  height={70}
                />
                <YAxis 
                  stroke="rgba(255,255,255,0.6)" 
                  tick={{ fill: 'rgba(255,255,255,0.6)', fontSize: 12 }}
                  tickFormatter={(value) => value.toFixed(2)}
                  label={{ 
                    value: getUnit(selectedType), 
                    angle: -90, 
                    position: 'insideLeft', 
                    fill: 'rgba(255,255,255,0.8)',
                    style: { fontSize: 14, fontWeight: 'bold' }
                  }}
                />
                <Tooltip 
                  contentStyle={{ 
                    backgroundColor: 'rgba(15, 23, 42, 0.95)',
                    borderColor: 'rgba(255, 255, 255, 0.2)',
                    borderRadius: '12px',
                    color: '#fff',
                    backdropFilter: 'blur(10px)',
                    padding: '12px'
                  }}
                  labelStyle={{ color: '#94a3b8', marginBottom: '8px' }}
                  itemStyle={{ color: '#38bdf8' }}
                  labelFormatter={(value: string) => `Horário: ${value}`}
                  formatter={(value: number, name: string, props: any) => {
                    // Usa o valor formatado do payload (que vem do backend)
                    const formattedValue = props.payload?.value || value.toFixed(2);
                    return [`${formattedValue} ${getUnit(selectedType)}`, selectedType];
                  }}
                />
                <Legend 
                  wrapperStyle={{ 
                    color: '#E2E8F0', 
                    paddingTop: '20px',
                    fontSize: '14px'
                  }} 
                />
                <Line 
                  type="monotone" 
                  dataKey="valueForChart"
                  name={selectedType}
                  stroke="#38bdf8"
                  strokeWidth={3}
                  dot={{ fill: '#38bdf8', strokeWidth: 2, r: 4 }}
                  activeDot={{ r: 8, fill: '#38bdf8', stroke: '#fff', strokeWidth: 2 }}
                  fill="url(#colorValue)"
                  animationDuration={1500}
                  animationEasing="ease-in-out"
                  connectNulls={false}
                />
              </LineChart>
            </ResponsiveContainer>
          </motion.div>
        )}
      </motion.div>
    </div>
  );
};

export default Sensors;
