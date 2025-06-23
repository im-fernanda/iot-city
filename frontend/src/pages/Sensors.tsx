import React, { useEffect, useState } from 'react';
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer
} from 'recharts';
import api from '../services/api';
import './Sensors.css';

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
  value: number;
  unit: string;
  timestamp: string;
  latitude?: number;
  longitude?: number;
}

// Cores para as linhas do gráfico
const COLORS = ['#8884d8', '#82ca9d', '#ffc658', '#ff8042', '#0088FE', '#00C49F'];

const Sensors: React.FC = () => {
  const [devices, setDevices] = useState<Device[]>([]);
  const [allDevices, setAllDevices] = useState<Device[]>([]);
  const [sensorTypes, setSensorTypes] = useState<string[]>([]);
  const [selectedType, setSelectedType] = useState('');
  const [selectedDevice, setSelectedDevice] = useState('');
  const [sensorData, setSensorData] = useState<SensorData[]>([]);
  const [allSensorData, setAllSensorData] = useState<SensorData[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Busca dados para os filtros na montagem inicial
  useEffect(() => {
    const fetchFilterData = async () => {
      try {
        // Busca os tipos de sensores e todos os dispositivos
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

  // Filtra dispositivos quando o tipo de sensor muda
  useEffect(() => {
    const fetchDevicesForType = async () => {
      if (selectedType) {
        try {
          console.log(`Buscando dispositivos para o tipo: ${selectedType}`);
          const response = await api.get(`/sensor-data/devices-by-type/${selectedType}`);
          console.log("Dispositivos recebidos da API:", response.data);
          
          setDevices(response.data);

          // Limpa a seleção de dispositivo se o dispositivo atual não está na nova lista
          if (selectedDevice && !response.data.some((d: Device) => d.id === Number(selectedDevice))) {
            setSelectedDevice('');
          }
        } catch (error) {
          console.error("Erro ao buscar dispositivos para o tipo:", error);
          setDevices([]); // Limpa a lista em caso de erro
        }
      } else {
        // Se nenhum tipo está selecionado, redefina para todos os dispositivos
        console.log("Nenhum tipo selecionado, mostrando todos os dispositivos.");
        setDevices(allDevices);
      }
    };

    fetchDevicesForType();
  }, [selectedType, allDevices]);

  // Busca dados do gráfico quando um dispositivo é selecionado
  useEffect(() => {
    if (selectedDevice) {
      fetchSensorData();
    } else {
      setSensorData([]); // Limpa o gráfico se nenhum dispositivo estiver selecionado
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedDevice]);

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

    try {
      const startDate = new Date();
      startDate.setHours(startDate.getHours() - 24);
      
      const response = await api.get('/sensor-data', {
        params: {
          sensorType: selectedType,
          deviceId: selectedDevice,
          start: startDate.toISOString(),
          end: new Date().toISOString()
        }
      });

      const data = response.data.map((item: any) => ({
        ...item,
        deviceId: item.device.id,
        deviceName: item.device.name
      }));

      setSensorData(data);
    } catch (error) {
      console.error('Erro ao buscar dados do sensor:', error);
    }
  };

  // Prepara os dados para o gráfico
  const chartData = sensorData.reduce((acc, { timestamp, sensorType, value }) => {
    const time = new Date(timestamp).toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
    
    if (!acc[time]) {
      acc[time] = { time };
    }
    
    acc[time][sensorType] = value;
    return acc;
  }, {} as Record<string, any>);

  // Agrupa os tipos de sensor presentes nos dados para renderizar múltiplas linhas
  const dataSensorTypes = Array.from(new Set(sensorData.map(d => d.sensorType)));
  
  return (
    <div className="sensors-page">
      <header className="sensors-header">
        <h1>📈 Sensores IoT</h1>
        <p>Visualize dados históricos dos sensores</p>
      </header>

      <div className="sensors-filters">
        <div className="filter-group">
          <label>Tipo de Sensor</label>
          <select value={selectedType} onChange={e => setSelectedType(e.target.value)}>
            <option value="">Selecione um tipo</option>
            {sensorTypes.map(type => (
              <option key={type} value={type}>{type}</option>
            ))}
          </select>
        </div>
        <div className="filter-group">
          <label>Dispositivo</label>
          <select value={selectedDevice} onChange={e => setSelectedDevice(e.target.value)}>
            <option value="">Selecione um dispositivo</option>
            {devices.map(device => (
              <option key={device.id} value={device.id}>{device.name}</option>
            ))}
          </select>
        </div>
      </div>

      <div className="sensors-graph-section">
        {loading ? (
          <div className="loading"><div className="spinner"></div>Carregando dados...</div>
        ) : error ? (
          <div className="error">{error}</div>
        ) : sensorData.length === 0 ? (
          <div className="no-data">
            {selectedType || selectedDevice 
              ? "Nenhum dado encontrado para o filtro selecionado."
              : "Por favor, selecione um filtro para exibir o gráfico."
            }
          </div>
        ) : (
          <ResponsiveContainer width="100%" height={400}>
            <LineChart data={sensorData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#ffffff30" />
              <XAxis 
                dataKey="timestamp" 
                tickFormatter={(value: string) => new Date(value).toLocaleTimeString()}
                stroke="#ffffff90"
                tick={{ fill: '#ffffff90' }}
              />
              <YAxis stroke="#ffffff90" tick={{ fill: '#ffffff90' }} />
              <Tooltip 
                contentStyle={{ 
                  backgroundColor: 'rgba(30, 41, 59, 0.8)',
                  borderColor: '#4A5568',
                  color: '#E2E8F0'
                }}
                labelFormatter={(value: string) => new Date(value).toLocaleString()}
                formatter={(value: any, name: string) => [`${value} ${getUnit(selectedType)}`, selectedType]}
              />
              <Legend wrapperStyle={{ color: '#E2E8F0' }} />
              <Line 
                type="monotone" 
                dataKey="value" 
                name={selectedType}
                stroke="#38bdf8" 
                strokeWidth={2}
                dot={{ r: 4, fill: '#38bdf8' }}
                activeDot={{ r: 8 }}
              />
            </LineChart>
          </ResponsiveContainer>
        )}
      </div>
    </div>
  );
};

export default Sensors; 