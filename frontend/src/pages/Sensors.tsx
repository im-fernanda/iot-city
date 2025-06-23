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

  // Busca dados do gráfico quando um dispositivo ou tipo de sensor muda
  useEffect(() => {
    if (selectedDevice) {
      fetchSensorData();
    } else {
      setSensorData([]); // Limpa o gráfico se nenhum dispositivo estiver selecionado
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedDevice, selectedType]);

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
    // A busca agora só precisa de um dispositivo selecionado
    if (!selectedDevice) return;

    try {
      const startDate = new Date();
      startDate.setHours(startDate.getHours() - 24);
      
      const params: any = {
        deviceId: selectedDevice,
        start: startDate.toISOString(),
        end: new Date().toISOString()
      };

      // Adiciona o tipo de sensor apenas se ele estiver selecionado
      if (selectedType) {
        params.sensorType = selectedType;
      }
      
      const response = await api.get('/sensor-data/search', { params });

      const data = response.data.map((item: any) => ({
        ...item,
        value: item.sensorValue,
        deviceId: item.device.id,
        deviceName: item.device.name
      }));

      setSensorData(data);
    } catch (error) {
      console.error('Erro ao buscar dados do sensor:', error);
    }
  };

  // Prepara os dados para o gráfico, agrupando por timestamp
  const chartData = sensorData.reduce((acc, { timestamp, sensorType, value }) => {
    const time = new Date(timestamp).toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' });
    
    let entry = acc.find(item => item.time === time);

    if (!entry) {
      entry = { time };
      acc.push(entry);
    }
    
    entry[sensorType] = value;
    return acc;
  }, [] as any[]);

  // Ordena os dados por tempo para que a linha do gráfico seja desenhada corretamente
  chartData.sort((a, b) => new Date(a.time).getTime() - new Date(b.time).getTime());

  // Agrupa os tipos de sensor presentes nos dados para renderizar múltiplas linhas
  const dataSensorTypes = Array.from(new Set(sensorData.map(d => d.sensorType)));
  
  return (
    <div className="sensors-page">
      <header className="sensors-header">
        <h1>📈 Sensores IoT</h1>
        <p>Visualize o histórico de dados dos sensores</p>
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
            {selectedDevice 
              ? "Nenhum dado encontrado para o filtro selecionado."
              : "Por favor, selecione um dispositivo para exibir o gráfico."
            }
          </div>
        ) : (
          <ResponsiveContainer width="100%" height={400}>
            <LineChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" stroke="#ffffff30" />
              <XAxis 
                dataKey="time" 
                stroke="#ffffff90"
                tick={{ fill: '#ffffff90' }}
                angle={-20}
                textAnchor="end"
                height={50}
              />
              <YAxis stroke="#ffffff90" tick={{ fill: '#ffffff90' }} />
              <Tooltip 
                contentStyle={{ 
                  backgroundColor: 'rgba(30, 41, 59, 0.8)',
                  borderColor: '#4A5568',
                  color: '#E2E8F0'
                }}
                labelFormatter={(value: string) => `Horário: ${value}`}
              />
              <Legend wrapperStyle={{ color: '#E2E8F0', paddingTop: '20px' }} />
              {dataSensorTypes.map((type, index) => (
                <Line 
                  key={type}
                  type="monotone" 
                  dataKey={type} 
                  name={type}
                  stroke={COLORS[index % COLORS.length]}
                  strokeWidth={2}
                  dot={false}
                  activeDot={{ r: 6 }}
                />
              ))}
            </LineChart>
          </ResponsiveContainer>
        )}
      </div>
    </div>
  );
};

export default Sensors; 