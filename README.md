# IoT City - Sistema de Gerenciamento de Dispositivos IoT

Este projeto demonstra um sistema completo de gerenciamento de dispositivos IoT com backend Spring Boot e frontend React, tudo containerizado com Docker. O sistema oferece uma API REST completa para monitoramento, controle e análise de dados simulados de sensores distribuídos pela cidade de Natal/RN e região.
![image](https://github.com/user-attachments/assets/fe012be9-dba8-48f6-b3c6-6130f490e85c)
![image](https://github.com/user-attachments/assets/bcb3854b-6d3c-43d6-a882-ffd5d940bfe8)
![image](https://github.com/user-attachments/assets/901ddd57-60e6-4f75-ad62-131a53948762)
![image](https://github.com/user-attachments/assets/4bec5991-3d41-46fe-b334-9e08d083a2a0)


## 🐳 Execução com Docker

### Pré-requisitos
- **Docker** 20.10 ou superior
- **Docker Compose** 2.0 ou superior

### 🚀 Como Executar

1. **Clone o repositório**
   ```bash
   git clone https://github.com/im-fernanda/iot-city/
   cd iot-city
   ```

2. **Build e execução**
   ```bash
   docker-compose up --build
   ```

3. **Acesse a aplicação**
   - **Frontend**: http://localhost:3000
   - **Backend API**: http://localhost:8080
   - **Documentação Swagger**: http://localhost:8080/swagger-ui.html

### 📁 Estrutura do Projeto

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

## 🛠️ Tecnologias Utilizadas

### Backend
- **Spring Boot 3.3.12** - Framework Java
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

## 📊 Funcionalidades

### Dashboard
- Visão geral dos dispositivos IoT
- Estatísticas em tempo real
- Dispositivos por tipo
- Dispositivos recentes

### Gerenciamento de Dispositivos
- Lista completa de dispositivos
- Filtros por tipo, status e busca
- Ações: editar, ativar/desativar, excluir
- Ordenação por diferentes critérios

### Sensores e Gráficos
- Visualização de dados históricos
- Filtros por tipo de sensor e dispositivo
- Gráficos interativos com Recharts

### APIs REST
- **Dispositivos**: `/api/devices`
- **Dados de Sensores**: `/api/sensor-data`

## 📝 Tipos de Dispositivos Suportados

- **SEMÁFORO** - Semáforos inteligentes
- **QUALIDADE_AR** - Sensores de qualidade do ar
- **ILUMINACAO_PUBLICA** - Iluminação pública
- **NIVEL_AGUA** - Sensores de nível de água
- **RUÍDO** - Sensores de ruído
- **METEOROLÓGICO** - Sensores meteorológicos
- **CÂMERA_SEGURANÇA** - Câmeras de segurança
- **ESTACIONAMENTO** - Sensores de estacionamento
- **LIXEIRA** - Sensores de lixeiras
- **PAINEL_SOLAR** - Painéis solares
  
## 🔧 Comandos Docker Úteis

```bash
# Executar em background
docker-compose up -d

# Ver logs
docker-compose logs -f app

# Parar containers
docker-compose down

# Rebuild após mudanças
docker-compose up --build

# Limpar tudo
docker-compose down -v
docker system prune -a
```

## 🚨 Troubleshooting

### Porta já em uso
```bash
# Verificar processos usando as portas
netstat -ano | findstr :3000
netstat -ano | findstr :8080

# Parar processos se necessário
taskkill /PID <PID> /F
```

### Problemas com Docker
```bash
# Limpar containers e imagens
docker-compose down -v
docker system prune -a

# Rebuild completo
docker-compose up --build
```

### Banco de dados
O PostgreSQL é configurado automaticamente com dados de teste. Se precisar resetar:
```bash
docker-compose down -v
docker-compose up --build
```

## 📖 Documentação Adicional

- **[Backend](backend/README.md)** - Documentação detalhada do backend
- **[Big Data](backend/README-BIGDATA-DEVSECOPS.md)** - Funcionalidades avançadas

