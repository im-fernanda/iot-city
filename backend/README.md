# Backend IoT City - Spring Boot

Backend da aplicação IoT City desenvolvido com Spring Boot, responsável por gerenciar dispositivos IoT e dados de sensores.

## 🐳 Execução com Docker

### Pré-requisitos
- **Docker** 20.10 ou superior
- **Docker Compose** 2.0 ou superior

### 🚀 Como Executar

1. **Na raiz do projeto**
   ```bash
   docker-compose up --build
   ```

2. **Acesse a aplicação**
   - **API**: http://localhost:8080
   - **Swagger UI**: http://localhost:8080/swagger-ui.html
   - **Health Check**: http://localhost:8080/actuator/health

## 🛠️ Tecnologias

- **Spring Boot 3.3.12** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança da aplicação
- **PostgreSQL** - Banco de dados
- **OpenAPI 3** - Documentação da API
- **Maven** - Gerenciamento de dependências

## 📊 APIs Disponíveis

### Dispositivos IoT
- `GET /api/devices` - Listar dispositivos
- `GET /api/devices/{id}` - Buscar dispositivo por ID
- `POST /api/devices` - Criar dispositivo
- `PUT /api/devices/{id}` - Atualizar dispositivo
- `PATCH /api/devices/{id}/toggle` - Ativar/desativar dispositivo
- `DELETE /api/devices/{id}` - Excluir dispositivo

### Dados de Sensores
- `GET /api/sensor-data` - Listar dados de sensores
- `GET /api/sensor-data/device/{deviceId}` - Dados por dispositivo
- `POST /api/sensor-data` - Inserir dados de sensor

## 🗄️ Banco de Dados

### Configuração PostgreSQL
- **Host**: `localhost:5432`
- **Database**: `iotcity`
- **Username**: `postgres`
- **Password**: `password`

### Tabelas Principais
- `devices` - Dispositivos IoT
- `sensor_data` - Dados de sensores

### Dados de Teste
O banco é inicializado automaticamente com dados de teste via `schema.sql` e `data.sql`.

## 🔧 Configurações

### Profiles Disponíveis
- `default` - Configuração padrão
- `docker` - Configuração para containers

### Propriedades Principais
```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/iotcity
spring.datasource.username=postgres
spring.datasource.password=password

# JPA
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# Security
spring.security.user.name=admin
spring.security.user.password=admin

# Server
server.port=8080
```

## 📝 Modelos de Dados

### Device
```java
- id: Long
- name: String
- type: String (SEMÁFORO, QUALIDADE_AR, etc.)
- location: String
- active: boolean
- lastSeen: LocalDateTime
- createdAt: LocalDateTime
```

### SensorData
```java
- id: Long
- deviceId: Long
- sensorType: String
- value: Double
- unit: String
- timestamp: LocalDateTime
```

## 🔍 Endpoints de Monitoramento

- `/actuator/health` - Status da aplicação
- `/actuator/info` - Informações da aplicação
- `/actuator/metrics` - Métricas do sistema

## 🚨 Troubleshooting

### Problemas de Conexão com Banco
```bash
# Verificar se o PostgreSQL está rodando
docker-compose ps

# Ver logs do banco
docker-compose logs postgres

# Resetar banco
docker-compose down -v
docker-compose up --build
```

### Problemas de Build
```bash
# Limpar cache do Maven
docker-compose down
docker system prune -a
docker-compose up --build
```

### Logs da Aplicação
```bash
# Ver logs em tempo real
docker-compose logs -f app

# Ver logs específicos
docker-compose logs app | grep ERROR
```

## 📖 Documentação da API

Acesse http://localhost:8080/swagger-ui.html para documentação interativa da API.

## 🔐 Segurança

- Autenticação básica habilitada
- Credenciais padrão: `admin/admin`
- CORS configurado para frontend
- Headers de segurança habilitados

## 📊 Monitoramento

- Health checks automáticos
- Métricas do Spring Boot Actuator
- Logs estruturados
- Tratamento de exceções global

## 🚀 Deploy

O backend está configurado para rodar em containers Docker com:
- Multi-stage build para otimização
- Configuração de produção
- Health checks
- Graceful shutdown
