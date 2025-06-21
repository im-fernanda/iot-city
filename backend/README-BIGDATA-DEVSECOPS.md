# 🚀 IoT City Backend - Big Data, DevSecOps & Cloud

Este projeto é um estudo prático em **Big Data**, **DevSecOps** e **Cloud Computing** através de uma aplicação IoT para cidades inteligentes.

## 🎯 **Tecnologias Demonstradas**

### **📊 Big Data**
- ✅ **Apache Spark 3.5** - Processamento distribuído de dados
- ✅ **Apache Kafka** - Stream processing e mensageria
- ✅ **Spark SQL** - Análise de dados com SQL
- ✅ **RDDs e DataFrames** - Manipulação de dados distribuídos
- ✅ **Spark Streaming** - Processamento de dados em tempo real

### **🔒 DevSecOps**
- ✅ **Spring Security** - Autenticação e autorização
- ✅ **Security Headers** - HSTS, X-Frame-Options, CSP
- ✅ **CORS Configuration** - Cross-origin resource sharing
- ✅ **Docker Security** - Containers seguros e multi-stage builds
- ✅ **Input Validation** - Validação de entrada e sanitização

### **☁️ Cloud Computing**
- ✅ **Multi-cloud Support** - AWS, Azure, GCP
- ✅ **Auto-scaling** - Escalabilidade automática
- ✅ **Load Balancing** - Distribuição de carga
- ✅ **Environment Profiles** - Configurações por ambiente
- ✅ **Cloud-native Architecture** - Arquitetura nativa para nuvem

## 🏗️ **Arquitetura**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   API Gateway   │    │   Load Balancer │
│   (React/Vue)   │    │   (Kong/Nginx)  │    │   (HAProxy)     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │  Spring Boot    │
                    │   Application   │
                    │  (IoT Backend)  │
                    └─────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   PostgreSQL    │    │   Apache Spark  │    │   Apache Kafka  │
│   Database      │    │   (Big Data)    │    │   (Streaming)   │
│   (Primary DB)  │    │   (Analytics)   │    │   (Real-time)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🚀 **Como Executar**

### **1. Stack Completo (Big Data + DevSecOps)**
```bash
# Executar com Kafka, Spark e todas as funcionalidades
docker-compose -f docker-compose-bigdata.yml up --build

# Verificar status dos serviços
docker-compose -f docker-compose-bigdata.yml ps

# Logs específicos
docker-compose -f docker-compose-bigdata.yml logs -f spark
docker-compose -f docker-compose-bigdata.yml logs -f kafka
```

### **2. Stack Básico (Apenas IoT)**
```bash
# Executar apenas aplicação + banco
docker-compose up --build

# Executar em background
docker-compose up -d
```

### **3. Desenvolvimento Local**
```bash
# Build e execução local
mvn clean install
mvn spring-boot:run -Dspring.profiles.active=bigdata

# Com debug
mvn spring-boot:run -Dspring.profiles.active=bigdata -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

## 📊 **APIs Big Data**

### **Análise Estatística Avançada**
```bash
GET /api/bigdata/analyze
```
**Resposta:**
```json
{
  "summary": {
    "averageValue": 25.5,
    "maxValue": 45.2,
    "minValue": 15.8,
    "totalRecords": 1000,
    "standardDeviation": 8.3,
    "median": 24.1
  },
  "sensorTypeDistribution": {
    "TEMPERATURE": 400,
    "HUMIDITY": 300,
    "AIR_QUALITY": 300
  },
  "geographicDistribution": {
    "Natal": 600,
    "Parnamirim": 250,
    "São Gonçalo": 150
  },
  "performance": {
    "processingTimeMs": 150,
    "throughput": 6666.67,
    "memoryUsage": "512MB"
  }
}
```

### **Análise Temporal com Spark SQL**
```bash
GET /api/bigdata/temporal-patterns
```
**Resposta:**
```json
{
  "hourlyPatterns": {
    "peakHours": ["08:00", "12:00", "18:00"],
    "lowActivityHours": ["02:00", "03:00", "04:00"],
    "averageReadingsPerHour": 41.67
  },
  "dailyPatterns": {
    "weekdayAverage": 28.5,
    "weekendAverage": 26.2,
    "busiestDay": "Wednesday"
  },
  "seasonalTrends": {
    "summer": 32.1,
    "winter": 18.9,
    "spring": 25.3,
    "autumn": 27.8
  }
}
```

### **Análise Geográfica Distribuída**
```bash
GET /api/bigdata/geographic
```
**Resposta:**
```json
{
  "cityCoverage": {
    "Natal": {
      "totalDevices": 150,
      "activeDevices": 142,
      "coveragePercentage": 94.7,
      "averageSignalStrength": 85.2
    },
    "Parnamirim": {
      "totalDevices": 75,
      "activeDevices": 68,
      "coveragePercentage": 90.7,
      "averageSignalStrength": 78.9
    }
  },
  "hotspots": [
    {
      "location": "Centro de Natal",
      "deviceDensity": "high",
      "averageReadings": 45.2
    }
  ],
  "coverageGaps": [
    {
      "area": "Zona Norte",
      "reason": "Low population density",
      "recommendation": "Add 3 more devices"
    }
  ]
}
```

### **Métricas de Performance em Tempo Real**
```bash
GET /api/bigdata/performance
```
**Resposta:**
```json
{
  "systemMetrics": {
    "totalRecords": 1000,
    "averageValue": 25.5,
    "processingTimeMs": 150,
    "throughput": 6666.67,
    "memoryUsage": "512MB",
    "cpuUsage": "23%"
  },
  "sparkMetrics": {
    "activeJobs": 2,
    "completedJobs": 45,
    "failedJobs": 0,
    "executorMemory": "2GB",
    "driverMemory": "1GB"
  },
  "kafkaMetrics": {
    "messagesPerSecond": 1250,
    "lag": 0,
    "consumerGroups": 3,
    "partitions": 8
  }
}
```

### **Streaming Analytics**
```bash
POST /api/bigdata/stream/start
```
**Inicia processamento de streaming:**
```json
{
  "streamConfig": {
    "windowSize": "5 minutes",
    "slideInterval": "1 minute",
    "topics": ["sensor-data", "device-status"],
    "outputFormat": "json"
  }
}
```

## 🔒 **Segurança (DevSecOps)**

### **Security Headers Implementados**
```java
// Configuração completa de segurança
http
    .headers()
        .frameOptions().deny()                    // Prevenção de clickjacking
        .contentTypeOptions().and()               // Prevenção de MIME sniffing
        .httpStrictTransportSecurity(hstsConfig -> hstsConfig
            .maxAgeInSeconds(31536000)            // 1 ano
            .includeSubdomains(true)
            .preload(true)
        ).and()
        .contentSecurityPolicy(csp -> csp
            .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'")
        ).and()
        .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
    .and()
    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
    .csrf(csrf -> csrf.disable())                 // Para APIs REST
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/public/**").permitAll()
        .requestMatchers("/actuator/health").permitAll()
        .anyRequest().authenticated()
    );
```

### **CORS Configuration**
```java
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:3000", "https://*.iotcity.com"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    configuration.setMaxAge(3600L);
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```

### **Docker Security Best Practices**
```dockerfile
# Multi-stage build para segurança
FROM openjdk:17-jdk-slim AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jre-slim AS runtime
# Usuário não-root
RUN groupadd -r iotcity && useradd -r -g iotcity iotcity
USER iotcity

# Diretório de trabalho
WORKDIR /app

# Copiar apenas o JAR necessário
COPY --from=builder /app/target/*.jar app.jar

# Configurações de segurança
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -XX:+UseContainerSupport"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### **Input Validation e Sanitização**
```java
@RestController
@Validated
public class SensorDataController {
    
    @PostMapping("/api/sensor-data")
    public ResponseEntity<SensorData> createSensorData(
            @Valid @RequestBody SensorDataDTO sensorData) {
        // Validação automática via Bean Validation
        return ResponseEntity.ok(sensorDataService.save(sensorData));
    }
}

public class SensorDataDTO {
    @NotNull
    @Min(0)
    @Max(100)
    private Double value;
    
    @NotNull
    @Pattern(regexp = "^[A-Z_]+$")
    private String sensorType;
    
    @NotNull
    @Size(min = 1, max = 100)
    private String location;
    
    // Getters e setters
}
```

## ☁️ **Cloud Computing**

### **Configurações Multi-Cloud**
```yaml
# application-cloud.yml
cloud:
  provider: aws  # aws, azure, gcp
  region: us-east-1
  instance-type: t3.medium
  auto-scaling:
    enabled: true
    min-instances: 2
    max-instances: 10
    target-cpu-utilization: 70
  load-balancer:
    enabled: true
    health-check-path: /actuator/health
    sticky-sessions: true
  database:
    rds:
      instance-class: db.t3.micro
      multi-az: true
      backup-retention: 7
  monitoring:
    cloudwatch:
      enabled: true
      metrics-interval: 60
```

### **Perfis de Ambiente**
```properties
# application-bigdata.properties
spring.profiles.active=bigdata
spark.master=spark://spark-master:7077
kafka.bootstrap-servers=kafka:9092
spring.jpa.hibernate.ddl-auto=update
logging.level.org.apache.spark=INFO
logging.level.org.apache.kafka=INFO

# application-cloud.properties
spring.profiles.active=cloud
spring.datasource.url=${RDS_URL}
spring.datasource.username=${RDS_USERNAME}
spring.datasource.password=${RDS_PASSWORD}
management.endpoints.web.exposure.include=health,info,metrics
```

### **Auto-scaling Configuration**
```java
@Configuration
@Profile("cloud")
public class CloudConfig {
    
    @Bean
    @ConditionalOnProperty(name = "cloud.auto-scaling.enabled", havingValue = "true")
    public AutoScalingService autoScalingService() {
        return new AutoScalingService();
    }
    
    @Bean
    public LoadBalancer loadBalancer() {
        return new LoadBalancer();
    }
}
```

## 📈 **Monitoramento e Observabilidade**

### **Health Checks**
```bash
# Aplicação principal
curl http://localhost:8080/actuator/health

# Spark cluster
curl http://localhost:8081/api/v1/applications

# Kafka cluster
curl http://localhost:9092/topics

# PostgreSQL
pg_isready -h localhost -p 5432
```

### **Métricas Disponíveis**
- **Application Metrics**: Request count, response time, error rate
- **Big Data Metrics**: Spark job duration, memory usage, throughput
- **Infrastructure Metrics**: CPU, memory, disk usage
- **Business Metrics**: Active devices, data volume, coverage percentage

### **Logging Configuration**
```yaml
logging:
  level:
    com.iotcitybackend: INFO
    org.apache.spark: WARN
    org.apache.kafka: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/iot-city-backend.log
    max-size: 100MB
    max-history: 30
```

## 🛠️ **Stack Tecnológica Detalhada**

### **Backend Core**
- **Spring Boot 3.5** - Framework principal com suporte a GraalVM
- **Spring Security 6** - Segurança moderna com OAuth2/JWT
- **Spring Data JPA** - Persistência com Hibernate
- **Spring Boot Actuator** - Monitoramento e métricas
- **Spring Cloud** - Microservices e configuração distribuída

### **Big Data Stack**
- **Apache Spark 3.5** - Processamento distribuído com Scala/Java
- **Apache Kafka 3.5** - Stream processing e mensageria
- **Spark SQL** - Análise de dados com SQL
- **Spark Streaming** - Processamento em tempo real
- **Delta Lake** - Lakehouse para dados estruturados

### **DevOps & Security**
- **Docker 24** - Containerização com multi-stage builds
- **Docker Compose** - Orquestração local
- **Kubernetes** - Orquestração em produção
- **Helm Charts** - Gerenciamento de deployments
- **SonarQube** - Análise de qualidade de código

### **Cloud & Infrastructure**
- **AWS ECS/EKS** - Container orchestration
- **Azure AKS** - Kubernetes managed
- **Google GKE** - Kubernetes no Google Cloud
- **Terraform** - Infrastructure as Code
- **Prometheus + Grafana** - Monitoring stack

## 🎯 **Demonstra Conhecimento em**

### **Big Data Engineering**
✅ **Spark RDDs** - Resilient Distributed Datasets  
✅ **Spark DataFrames** - Manipulação estruturada de dados  
✅ **Spark SQL** - Análise com linguagem SQL  
✅ **Spark Streaming** - Processamento em tempo real  
✅ **Kafka Integration** - Stream processing  
✅ **Performance Optimization** - Tuning de jobs Spark  
✅ **Data Pipeline Design** - ETL/ELT pipelines  

### **DevSecOps Practices**
✅ **Security Headers** - HSTS, CSP, X-Frame-Options  
✅ **CORS Configuration** - Cross-origin security  
✅ **Input Validation** - Bean Validation e sanitização  
✅ **Docker Security** - Multi-stage builds, non-root users  
✅ **Environment Management** - Perfis e configurações  
✅ **CI/CD Pipeline** - Automated testing e deployment  
✅ **Security Scanning** - Vulnerability assessment  

### **Cloud Computing**
✅ **Multi-cloud Support** - AWS, Azure, GCP  
✅ **Auto-scaling** - Escalabilidade automática  
✅ **Load Balancing** - Distribuição de carga  
✅ **Configuration Management** - Externalized configuration  
✅ **Cloud-native Design** - 12-factor app principles  
✅ **Infrastructure as Code** - Terraform/CloudFormation  
✅ **Monitoring & Alerting** - Observability stack  

## 📊 **Acessos e URLs**

### **APIs e Documentação**
- **Base URL:** `http://localhost:8080/api/`
- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/api-docs`
- **Health Check:** `http://localhost:8080/actuator/health`
- **Metrics:** `http://localhost:8080/actuator/metrics`

### **Big Data Tools**
- **Spark UI:** `http://localhost:8081` (Master)
- **Spark History Server:** `http://localhost:18080`
- **Kafka UI:** `http://localhost:9000`
- **Kafka Broker:** `localhost:9092`
- **Zookeeper:** `localhost:2181`

### **Infrastructure**
- **PostgreSQL:** `localhost:5432`
- **Redis (Cache):** `localhost:6379`
- **Elasticsearch:** `localhost:9200`
- **Kibana:** `http://localhost:5601`

## 🚀 **Próximos Passos**

### **Melhorias Planejadas**
- [ ] **Machine Learning** - Integração com MLlib para predições
- [ ] **Real-time Analytics** - Dashboards em tempo real
- [ ] **Edge Computing** - Processamento na borda
- [ ] **Blockchain** - Integridade de dados com blockchain
- [ ] **5G Integration** - Suporte a redes 5G
- [ ] **AI/ML Pipeline** - Pipeline completo de ML

### **Escalabilidade**
- [ ] **Microservices** - Decomposição em microserviços
- [ ] **Event Sourcing** - Arquitetura baseada em eventos
- [ ] **CQRS** - Command Query Responsibility Segregation
- [ ] **Distributed Tracing** - Jaeger/Zipkin integration

