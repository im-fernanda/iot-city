# 🚀 IoT City Backend - Big Data, DevSecOps & Cloud

Este projeto é um estudo prático em **Big Data**, **DevSecOps** e **Cloud Computing** através de uma aplicação IoT para cidades inteligentes.

## 🎯 **Tecnologias Demonstradas**

### **📊 Big Data**
- ✅ **Apache Spark** - Processamento distribuído de dados
- ✅ **Apache Kafka** - Stream processing
- ✅ **Spark SQL** - Análise de dados com SQL
- ✅ **RDDs e DataFrames** - Manipulação de dados

### **🔒 DevSecOps**
- ✅ **Spring Security** - Autenticação e autorização
- ✅ **Security Headers** - HSTS, X-Frame-Options
- ✅ **CORS Configuration** - Cross-origin resource sharing
- ✅ **Docker Security** - Containers seguros

### **☁️ Cloud Computing**
- ✅ **Multi-cloud Support** - AWS, Azure, GCP
- ✅ **Auto-scaling** - Escalabilidade automática
- ✅ **Load Balancing** - Distribuição de carga
- ✅ **Environment Profiles** - Configurações por ambiente

## 🏗️ **Arquitetura**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   API Gateway   │    │   Load Balancer │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │  Spring Boot    │
                    │   Application   │
                    └─────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   PostgreSQL    │    │   Apache Spark  │    │   Apache Kafka  │
│   Database      │    │   (Big Data)    │    │   (Streaming)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🚀 **Como Executar**

### **1. Stack Completo (Big Data)**
```bash
# Executar com Kafka e Spark
docker-compose -f docker-compose-bigdata.yml up --build
```

### **2. Stack Básico**
```bash
# Executar apenas aplicação + banco
docker-compose up --build
```

### **3. Apenas Aplicação**
```bash
# Build e execução local
docker build -t iot-city-backend .
docker run -p 8080:8080 iot-city-backend
```

## 📊 **APIs Big Data**

### **Análise Estatística**
```bash
GET /api/bigdata/analyze
```
**Resposta:**
```json
{
  "averageValue": 25.5,
  "maxValue": 45.2,
  "minValue": 15.8,
  "totalRecords": 1000,
  "sensorTypeDistribution": {
    "TEMPERATURE": 400,
    "HUMIDITY": 300,
    "AIR_QUALITY": 300
  }
}
```

### **Análise Temporal**
```bash
GET /api/bigdata/temporal-patterns
```
Analisa padrões temporais usando Spark SQL.

### **Análise Geográfica**
```bash
GET /api/bigdata/geographic
```
Analisa distribuição geográfica dos sensores.

### **Métricas de Performance**
```bash
GET /api/bigdata/performance
```
**Resposta:**
```json
{
  "totalRecords": 1000,
  "averageValue": 25.5,
  "processingTimeMs": 150,
  "throughput": 6666.67
}
```

## 🔒 **Segurança (DevSecOps)**

### **Security Headers Implementados**
- ✅ **HSTS** - HTTP Strict Transport Security
- ✅ **X-Frame-Options** - Prevenção de clickjacking
- ✅ **X-Content-Type-Options** - Prevenção de MIME sniffing
- ✅ **CORS** - Cross-origin resource sharing configurado

### **Configurações de Segurança**
```java
// Headers de segurança
.frameOptions().deny()
.contentTypeOptions().and()
.httpStrictTransportSecurity(hstsConfig -> hstsConfig
    .maxAgeInSeconds(31536000)
    .includeSubdomains(true)
)
```

## ☁️ **Cloud Computing**

### **Configurações Multi-Cloud**
```yaml
cloud:
  provider: aws  # aws, azure, gcp
  region: us-east-1
  instance-type: t3.medium
  auto-scaling:
    enabled: true
  load-balancer:
    enabled: true
```

### **Perfis de Ambiente**
- **`default`** - Desenvolvimento local
- **`cloud`** - Produção na nuvem
- **`bigdata`** - Stack completo com Kafka/Spark

## 📈 **Monitoramento**

### **Health Checks**
- **Aplicação:** `GET /actuator/health`
- **Spark:** `http://localhost:8081` (Spark UI)
- **Kafka:** `localhost:9092`

### **Métricas Disponíveis**
- Throughput de processamento
- Tempo de resposta
- Distribuição de dados
- Performance de Big Data

## 🛠️ **Stack Tecnológica**

### **Backend**
- **Spring Boot 3.5** - Framework principal
- **Spring Security** - Segurança
- **Spring Data JPA** - Persistência
- **PostgreSQL** - Banco de dados

### **Big Data**
- **Apache Spark 3.5** - Processamento distribuído
- **Apache Kafka** - Stream processing
- **Spark SQL** - Análise de dados

### **DevOps**
- **Docker** - Containerização
- **Docker Compose** - Orquestração
- **Multi-stage builds** - Otimização de imagens

### **Cloud**
- **Multi-cloud support** - AWS/Azure/GCP
- **Auto-scaling** - Escalabilidade
- **Load balancing** - Distribuição de carga

## 🎯 **Demonstra Conhecimento em**

### **Big Data**
✅ **Spark RDDs** - Resilient Distributed Datasets  
✅ **Spark DataFrames** - Manipulação estruturada  
✅ **Spark SQL** - Análise com SQL  
✅ **Kafka** - Stream processing  
✅ **Performance Analysis** - Métricas de throughput  

### **DevSecOps**
✅ **Security Headers** - HSTS, X-Frame-Options  
✅ **CORS Configuration** - Cross-origin security  
✅ **Docker Security** - Containers seguros  
✅ **Environment Management** - Perfis de ambiente  

### **Cloud Computing**
✅ **Multi-cloud Support** - AWS, Azure, GCP  
✅ **Auto-scaling** - Escalabilidade automática  
✅ **Load Balancing** - Distribuição de carga  
✅ **Configuration Management** - Propriedades por ambiente  

## 📊 **Acessos**

### **APIs**
- **Base URL:** `http://localhost:8080/api/`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **Health Check:** `http://localhost:8080/actuator/health`

### **Big Data**
- **Spark UI:** `http://localhost:8081`
- **Kafka:** `localhost:9092`
- **PostgreSQL:** `localhost:5432`

