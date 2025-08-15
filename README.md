# IoT City – IoT Device Management System

This project demonstrates a complete IoT device management system with a Spring Boot backend and a React frontend, all containerized with Docker. The system provides a full REST API for monitoring, controlling, and analyzing simulated sensor data distributed across the city of Natal/RN and surrounding areas.

![image](https://github.com/user-attachments/assets/fe012be9-dba8-48f6-b3c6-6130f490e85c)
![image](https://github.com/user-attachments/assets/bcb3854b-6d3c-43d6-a882-ffd5d940bfe8)
![image](https://github.com/user-attachments/assets/901ddd57-60e6-4f75-ad62-131a53948762)
![image](https://github.com/user-attachments/assets/4bec5991-3d41-46fe-b334-9e08d083a2a0)


## 🐳 Running with Docker

### Prerequisites

- **Docker** 20.10 our higher
- **Docker Compose** 2.0 or higher

### 🚀 How to Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/im-fernanda/iot-city/
   cd iot-city
   ```

2. **Build and run:**
   ```bash
   docker-compose up --build
   ```

3. **Access the application:**
   - **Frontend**: http://localhost:3000
   - **Backend API**: http://localhost:8080
   - **Swagger documentation:**: http://localhost:8080/swagger-ui.html

### 📁 Project Structure

```
iot-city-backend/
├── backend/          # Backend Spring Boot (Java)
│   ├── src/         # Código fonte Java
│   ├── pom.xml      # Dependências Maven
│   └── README.md    # Documentação do backend
├── frontend/        # Frontend React (TypeScript)
│   ├── src/         # Código fonte React
│   ├── package.json # Dependências Node.js
│   └── README.md    # Documentação do frontend
├── docker-compose.yml # Configuração Docker
├── Dockerfile       # Build da aplicação
└── README.md        # Este arquivo
```

## 🛠️ Technologies Used

### Backend
- **Spring Boot 3.3.12** - Java Framework
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança
- **PostgreSQL** - Banco de dados
- **OpenAPI/Swagger** - Documentação da API

### Frontend
- **React 18** - Biblioteca JavaScript
- **TypeScript** - Tipagem estática
- **React Router** - Navegação
- **Recharts** - Gráficos
- **CSS3** - Estilização moderna

### DevOps
- **Docker** - Containerização
- **Docker Compose** - Orquestração
- **Nginx** - Servidor web para frontend

## 📊 Features

### Dashboard
- IoT device overview
- Real-time statistics
- Devices by type
- ecent devices

### Device Management
- Full device list
- Filters by type, status, and search
- Actions: edit, enable/disable, delete
- Sorting by different criteria

### Sensors and Charts
- Historical data visualization
- Filters by sensor type and device
- Interactive charts with Recharts

### APIs REST
- **Devicees**: `/api/devices`
- **Sensor Data**: `/api/sensor-data`

## 📝Supported Device Types

- **SEMÁFORO** - Smart traffic lights
- **QUALIDADE_AR** - Air quality sensors
- **ILUMINACAO_PUBLICA** - Public lighting
- **NIVEL_AGUA** - Water level sensors
- **RUÍDO** - Noise sensors
- **METEOROLÓGICO** - Weather sensors
- **CÂMERA_SEGURANÇA** - Security cameras
- **ESTACIONAMENTO** - Parking sensors
- **LIXEIRA** - Smart waste bin sensors
- **PAINEL_SOLAR** - Solar panels
  
## 🔧 Useful Docker Commands

```bash
# Run in background
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop containers
docker-compose down

# Rebuild after changes
docker-compose up --build

# Clean everything
docker-compose down -v
docker system prune -a
```

## 🚨 Troubleshooting

### Port Already in Use

Check processes using the ports:
```bash
# 
netstat -ano | findstr :3000
netstat -ano | findstr :8080

# Stop processes if needed:
taskkill /PID <PID> /F
```

### Docker Issues

```bash
# Clean containers and images
docker-compose down -v
docker system prune -a

# Full rebuild
docker-compose up --build
```

### Database
PostgreSQL is automatically configured with test data. To reset:
```bash
docker-compose down -v
docker-compose up --build
```

## 📖 Additional Documentation

- **[Backend](backend/README.md)** - Detailed backend documentation
- **[Big Data](backend/README-BIGDATA-DEVSECOPS.md)** - Advanced features

