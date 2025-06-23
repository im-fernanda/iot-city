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
  deviceId: number;
  sensorType: string;
  sensorValue: number;
  timestamp: string;
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
        const [devicesRes, sensorDataRes] = await Promise.all([
          api.get('/devices'),
          api.get('/sensor-data') 
        ]);
        setAllDevices(devicesRes.data);
        setDevices(devicesRes.data);
        setAllSensorData(sensorDataRes.data);
        
        // Filtra apenas os tipos de sensores que realmente têm dados
        const sensorData = sensorDataRes.data;
        const typesWithData = Array.from(new Set(sensorData.map((d: any) => d.sensorType))) as string[];
        setSensorTypes(typesWithData);
      } catch {
        setError("Erro ao carregar filtros");
      }
    };
    fetchFilterData();
  }, []);

  // Filtra dispositivos quando o tipo de sensor muda
  useEffect(() => {
    if (selectedType) {
      // Filtra dispositivos que têm dados do tipo selecionado
      const devicesWithTypeData = allSensorData
        .filter(data => data.sensorType === selectedType)
        .map(data => data.deviceId);
      
      const filteredDevices = allDevices.filter(device => 
        devicesWithTypeData.includes(device.id)
      );
      setDevices(filteredDevices);
      
      // Limpa a seleção de dispositivo se o dispositivo atual não tem dados do tipo selecionado
      if (selectedDevice && !devicesWithTypeData.includes(Number(selectedDevice))) {
        setSelectedDevice('');
      }
    } else {
      // Se nenhum tipo está selecionado, mostra todos os dispositivos
      setDevices(allDevices);
    }
  }, [selectedType, allDevices, allSensorData, selectedDevice]);

  // Busca dados do gráfico quando os filtros mudam
  useEffect(() => {
    if (!selectedType && !selectedDevice) {
      setSensorData([]); // Limpa o gráfico se nenhum filtro estiver selecionado
      return;
    }
    fetchChartData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedType, selectedDevice]);

  const fetchChartData = async () => {
    setLoading(true);
    setError(null);
    try {
      let url = '/sensor-data';
      if (selectedDevice) {
        url = `/sensor-data/device/${selectedDevice}`;
      } else if (selectedType) {
        url = `/sensor-data/type/${selectedType}`;
      }
      
      const response = await api.get(url);
      let data = response.data;

      // Filtra adicionalmente por tipo se ambos estiverem selecionados
      if (selectedDevice && selectedType) {
        data = data.filter((d: SensorData) => d.sensorType === selectedType);
      }
      
      setSensorData(data);
    } catch (err) {
      setError('Erro ao buscar dados do sensor');
      setSensorData([]);
    } finally {
      setLoading(false);
    }
  };

  // Prepara os dados para o gráfico
  const chartData = (
    Object.values(
      sensorData.reduce((acc, { timestamp, sensorType, sensorValue }) => {
        const time = new Date(timestamp).toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
        
        if (!acc[time]) {
          acc[time] = { time };
        }
        
        acc[time][sensorType] = sensorValue;
        return acc;
      }, {} as Record<string, any>)
    )
    .sort((a, b) => {
      // Extrai a hora e o minuto para uma comparação robusta
      const [aHour, aMinute] = a.time.split(':').map(Number);
      const [bHour, bMinute] = b.time.split(':').map(Number);
      return aHour * 60 + aMinute - (bHour * 60 + bMinute);
    })
  );

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
        ) : chartData.length === 0 ? (
          <div className="no-data">
            {selectedType || selectedDevice 
              ? "Nenhum dado encontrado para o filtro selecionado."
              : "Por favor, selecione um filtro para exibir o gráfico."
            }
          </div>
        ) : (
          <ResponsiveContainer width="100%" height={400}>
            <LineChart data={chartData} margin={{ top: 20, right: 30, left: 0, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="time" />
              <YAxis />
              <Tooltip />
              <Legend />
              {dataSensorTypes.map((type, index) => (
                <Line 
                  key={type} 
                  type="monotone" 
                  dataKey={type} 
                  name={type} 
                  stroke={COLORS[index % COLORS.length]} 
                  dot={false}
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