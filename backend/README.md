# 🌆 IoT City Backend

> **Sistema de Gerenciamento de Dispositivos IoT para Cidades Inteligentes**

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.12-green.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-✓-blue.svg)](https://www.docker.com/)

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Funcionalidades](#-funcionalidades)
- [Tecnologias](#-tecnologias)
- [Endpoints e documentação Interativa](#-documentação-interativa)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação](#-instalação)
- [Configuração](#-configuração)
- [Uso](#-uso)
- [Desenvolvimento](#-desenvolvimento)
- [Troubleshooting](#-troubleshooting)

## 🎯 Sobre o Projeto

O **IoT City Backend** é uma aplicação Spring Boot desenvolvida para gerenciar dispositivos IoT em cidades inteligentes. O sistema oferece uma API REST completa para monitoramento, controle e análise de dados de sensores distribuídos pela cidade de Natal/RN e região.

### 🏙️ Casos de Uso

- **Sensores de Tráfego**: Monitoramento de semáforos e fluxo de veículos
- **Qualidade do Ar**: Sensores de poluição atmosférica
- **Iluminação Pública**: Controle de postes de luz inteligentes
- **Monitoramento de Enchentes**: Sensores de nível de água
- **Segurança**: Câmeras de vigilância e sensores de ruído
- **Estacionamento**: Sensores de vagas disponíveis
- **Gestão de Resíduos**: Lixeiras inteligentes
- **Energia Solar**: Monitoramento de painéis solares

## ✨ Funcionalidades

### 🔧 Gerenciamento de Dispositivos
- ✅ Cadastro, edição e remoção de dispositivos IoT
- ✅ Monitoramento de status (online/offline)
- ✅ Controle de bateria e força do sinal
- ✅ Geolocalização dos dispositivos
- ✅ Categorização por tipo (semáforo, sensor de ar, etc.)

### 📊 Coleta de Dados
- ✅ Recebimento de dados de sensores em tempo real
- ✅ Validação de dados recebidos
- ✅ Armazenamento histórico
- ✅ Consultas por período, localização e tipo

### 📈 Análise de Dados
- ✅ Cálculo de médias por tipo de sensor
- ✅ Análise temporal de dados
- ✅ Relatórios de performance
- ✅ Estatísticas agregadas

### 🔒 Segurança
- ✅ Autenticação básica
- ✅ CORS configurado
- ✅ Validação de entrada
- ✅ Tratamento de erros padronizado

## 🛠️ Tecnologias

### Backend
- **Java 17** - Linguagem principal
- **Spring Boot 3.3.12** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança
- **Spring Boot Actuator** - Monitoramento

### Banco de Dados
- **PostgreSQL 15** - Banco principal

### Documentação
- **OpenAPI 3** - Documentação da API
- **Swagger UI** - Interface de teste da API

### DevOps
- **Docker** - Containerização
- **Docker Compose** - Orquestração de containers
- **Maven** - Gerenciamento de dependências

## 📚 Endpoints disponíveis
Para documentação interativa, após rodar a aplicação acesse: http://localhost:8080/swagger-ui.html ou http://localhost:8080/api-docs.

#### **Dispositivos IoT** (`/api/devices`)
- **Listar todos os dispositivos** - GET `/api/devices`
- **Buscar dispositivo por ID** - GET `/api/devices/{id}`
- **Criar novo dispositivo** - POST `/api/devices`
- **Atualizar dispositivo** - PUT `/api/devices/{id}`
- **Remover dispositivo** - DELETE `/api/devices/{id}`
- **Dispositivos offline** - GET `/api/devices/offline`
- **Dispositivos com bateria baixa** - GET `/api/devices/low-battery`
- **Estatísticas de dispositivos** - GET `/api/devices/stats`

#### **Dados de Sensores** (`/api/sensor-data`)
- **Receber dados de sensor** - POST `/api/sensor-data`
- **Listar todos os dados** - GET `/api/sensor-data`
- **Buscar dados por ID** - GET `/api/sensor-data/{id}`
- **Dados por dispositivo** - GET `/api/sensor-data/device/{deviceId}`
- **Dados por tipo de sensor** - GET `/api/sensor-data/type/{sensorType}`
- **Dados por período** - GET `/api/sensor-data/period`
- **Dados por localização** - GET `/api/sensor-data/location`
- **Média de valores** - GET `/api/sensor-data/average/{sensorType}`
- **Dados mais recentes** - GET `/api/sensor-data/latest/device/{deviceId}`

### Tipos de Dispositivos Suportados

- **TRAFFIC_LIGHT** - Semáforos inteligentes
- **AIR_QUALITY** - Sensores de qualidade do ar
- **STREET_LIGHT** - Iluminação pública
- **WATER_LEVEL** - Sensores de nível de água
- **NOISE_SENSOR** - Sensores de ruído
- **WEATHER_SENSOR** - Sensores meteorológicos
- **SECURITY_CAMERA** - Câmeras de segurança
- **PARKING_SENSOR** - Sensores de estacionamento
- **WASTE_SENSOR** - Sensores de lixeiras
- **SOLAR_PANEL** - Painéis solares

## 📋 Pré-requisitos

### Para Desenvolvimento Local
- **Java 17** ou superior
- **Maven 3.6** ou superior
- **PostgreSQL 15** ou superior
- **Git**

### Para Docker
- **Docker** 20.10 ou superior
- **Docker Compose** 2.0 ou superior

## 🚀 Instalação

### 1. Clone o Repositório
```bash
git clone https://github.com/im-fernanda/iot-city.git
cd iot-city
```

### 2. Configuração do Banco de Dados

#### Opção A: PostgreSQL Local
```bash
# Conecte no PostgreSQL
psql -U postgres

# Crie o banco de dados
CREATE DATABASE iotcity_dev;
```

#### Opção B: Docker (Recomendado)
```bash
# O banco será criado automaticamente pelo Docker Compose
```

## ⚙️ Configuração

### Configurações de Ambiente

#### Desenvolvimento Local (`application.properties`)
```properties
# Banco de dados local
spring.datasource.url=jdbc:postgresql://localhost:5432/iotcity_dev
spring.datasource.username=postgres
spring.datasource.password=sua-senha-postgres

# Modo desenvolvimento
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
logging.level.com.iotcitybackend=DEBUG
```

#### Docker (`application-docker.properties`)
```properties
# Banco de dados Docker
spring.datasource.url=jdbc:postgresql://db:5432/iotcity
spring.datasource.username=iotcity_user
spring.datasource.password=iotcity_pass

# Modo produção
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
logging.level.com.iotcitybackend=INFO
```

## 🏃‍♂️ Uso

### Desenvolvimento Local

#### 1. Iniciar a Aplicação
```bash
# Compilar e executar
mvn spring-boot:run
```

#### 2. Acessar a Aplicação
- **API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health

### Docker

#### 1. Build e Execução
```bash
# Build e start dos containers
docker-compose up --build

# Executar em background
docker-compose up -d

# Parar containers
docker-compose down
```

#### 2. Logs
```bash
# Ver logs da aplicação
docker-compose logs -f app

# Ver logs do banco
docker-compose logs -f db
```

## 🔧 Desenvolvimento

### Hot Reload
O projeto usa **Spring Boot DevTools** para restart automático:
- Modificações em arquivos `.java` reiniciam a aplicação
- Modificações em `application.properties` reiniciam a aplicação
- Arquivos estáticos não causam restart

### Logs
```bash
# Ver logs em tempo real
tail -f logs/application.log

# Logs específicos
grep "ERROR" logs/application.log
```

## 🚨 Troubleshooting

### Problemas Comuns

#### 1. Erro de Conexão com Banco
```bash
# Verificar se PostgreSQL está rodando
sudo systemctl status postgresql

# Verificar conexão
psql -U postgres -d iotcity_dev
```

#### 2. Porta 8080 Ocupada
```bash
# Encontrar processo
lsof -i :8080

# Matar processo
kill -9 <PID>
```

#### 3. Erro de Memória
```bash
# Aumentar heap
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx2g"
```

#### 4. Problemas com Docker
```bash
# Limpar containers
docker-compose down -v
docker system prune -a

# Rebuild
docker-compose up --build
```

## 📖 Documentação Adicional

- **[Big Data & DevSecOps](README-BIGDATA-DEVSECOPS.md)** - Funcionalidades avançadas de Big Data, DevSecOps e Cloud Computing
- **[Docker](README-Docker.md)** - Guia detalhado de containerização
