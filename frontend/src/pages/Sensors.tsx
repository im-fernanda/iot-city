import React, { useEffect, useState } from 'react';
import {
  LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer
} from 'recharts';
import './Sensors.css';

interface Device {
  id: number;
  name: string;
  type: string;
}

interface SensorData {
  id: number;
  deviceId: number;
  sensorType: string;
  sensorValue: number;
  unit: string;
  timestamp: string;
}

const Sensors: React.FC = () => {
  const [devices, setDevices] = useState<Device[]>([]);
  const [sensorTypes, setSensorTypes] = useState<string[]>([]);
  const [selectedType, setSelectedType] = useState('');
  const [selectedDevice, setSelectedDevice] = useState('');
  const [sensorData, setSensorData] = useState<SensorData[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Buscar dispositivos e tipos de sensor ao carregar
  useEffect(() => {
    fetchDevices();
    fetchSensorTypes();
  }, []);

  // Buscar dados ao mudar filtros
  useEffect(() => {
    if (selectedType || selectedDevice) {
      fetchSensorData();
    } else {
      setSensorData([]);
    }
    // eslint-disable-next-line
  }, [selectedType, selectedDevice]);

  const fetchDevices = async () => {
    try {
      const res = await fetch('/api/devices');
      const data = await res.json();
      setDevices(data);
    } catch {
      setDevices([]);
    }
  };

  const fetchSensorTypes = async () => {
    try {
      const res = await fetch('/api/sensor-data');
      const data = await res.json();
      const types = Array.from(new Set(data.map((d: any) => d.sensorType))) as string[];
      setSensorTypes(types);
    } catch {
      setSensorTypes([]);
    }
  };

  const fetchSensorData = async () => {
    setLoading(true);
    setError(null);
    try {
      let url = '/api/sensor-data';
      if (selectedType && selectedDevice) {
        url = `/api/sensor-data/device/${selectedDevice}`;
      } else if (selectedType) {
        url = `/api/sensor-data/type/${selectedType}`;
      } else if (selectedDevice) {
        url = `/api/sensor-data/device/${selectedDevice}`;
      }
      const res = await fetch(url);
      let data = await res.json();
      if (selectedType && selectedDevice) {
        data = data.filter((d: any) => d.sensorType === selectedType);
      }
      setSensorData(data);
    } catch (err) {
      setError('Erro ao buscar dados do sensor');
      setSensorData([]);
    } finally {
      setLoading(false);
    }
  };

  // Opções de dispositivos filtradas por tipo de sensor
  const filteredDevices = selectedType
    ? devices.filter(d =>
        sensorData.some(sd => sd.deviceId === d.id && sd.sensorType === selectedType)
      )
    : devices;

  // Dados para o gráfico
  const chartData = sensorData
    .filter(d =>
      (!selectedType || d.sensorType === selectedType) &&
      (!selectedDevice || d.deviceId === Number(selectedDevice))
    )
    .map(d => ({
      ...d,
      time: new Date(d.timestamp).toLocaleString(),
    }))
    .sort((a, b) => new Date(a.timestamp).getTime() - new Date(b.timestamp).getTime());

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
            <option value="">Todos</option>
            {sensorTypes.map(type => (
              <option key={type} value={type}>{type}</option>
            ))}
          </select>
        </div>
        <div className="filter-group">
          <label>Dispositivo</label>
          <select value={selectedDevice} onChange={e => setSelectedDevice(e.target.value)}>
            <option value="">Todos</option>
            {filteredDevices.map(device => (
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
          <div className="no-data">Nenhum dado encontrado para o filtro selecionado.</div>
        ) : (
          <ResponsiveContainer width="100%" height={400}>
            <LineChart data={chartData} margin={{ top: 20, right: 30, left: 0, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="time" minTickGap={40} />
              <YAxis domain={['auto', 'auto']} />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="sensorValue" name="Valor" stroke="#4CAF50" dot={false} />
            </LineChart>
          </ResponsiveContainer>
        )}
      </div>
    </div>
  );
};

export default Sensors; 