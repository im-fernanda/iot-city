# 🐳 Docker Setup - IoT City Backend

Este documento explica como executar o projeto IoT City Backend usando Docker.

## 📋 Pré-requisitos

- Docker Desktop instalado
- Docker Compose instalado
- Pelo menos 4GB de RAM disponível

## 🚀 Execução Rápida

### 1. Build e Execução Completa
```bash
# Build e start de todos os serviços
docker-compose up --build

# Executar em background
docker-compose up -d --build
```

### 2. Apenas a Aplicação (com H2 em memória)
```bash
# Build da imagem
docker build -t iot-city-backend .

# Executar container
docker run -p 8080:8080 iot-city-backend
```

## 🔧 Configurações

### Variáveis de Ambiente
O projeto usa as seguintes variáveis de ambiente no Docker:

- `SPRING_PROFILES_ACTIVE=docker` - Perfil Spring Boot
- `SPRING_DATASOURCE_URL` - URL do banco PostgreSQL
- `SPRING_DATASOURCE_USERNAME` - Usuário do banco
- `SPRING_DATASOURCE_PASSWORD` - Senha do banco

### Portas
- **8080** - Aplicação Spring Boot
- **5432** - PostgreSQL
- **6379** - Redis (opcional)

## 📊 Serviços Incluídos

### 1. Aplicação Spring Boot (`app`)
- **Imagem:** Customizada (multi-stage build)
- **Porta:** 8080
- **Health Check:** `/actuator/health`

### 2. PostgreSQL (`db`)
- **Imagem:** postgres:15-alpine
- **Porta:** 5432
- **Database:** iotcity
- **Usuário:** iotcity_user
- **Senha:** iotcity_pass

### 3. Redis (`redis`) - Opcional
- **Imagem:** redis:7-alpine
- **Porta:** 6379
- **Persistência:** Habilitada

## 🛠️ Comandos Úteis

### Gerenciamento de Containers
```bash
# Ver logs da aplicação
docker-compose logs app

# Ver logs de todos os serviços
docker-compose logs

# Parar todos os serviços
docker-compose down

# Parar e remover volumes
docker-compose down -v

# Rebuild apenas a aplicação
docker-compose build app

# Executar apenas o banco
docker-compose up db
```

### Debug e Troubleshooting
```bash
# Entrar no container da aplicação
docker-compose exec app sh

# Verificar logs em tempo real
docker-compose logs -f app

# Verificar status dos containers
docker-compose ps

# Verificar uso de recursos
docker stats
```

## 🌐 Acessos

Após executar `docker-compose up`, você pode acessar:

- **API:** http://localhost:8080/api/
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Health Check:** http://localhost:8080/actuator/health
- **PostgreSQL:** localhost:5432
- **Redis:** localhost:6379

## 📝 Dados Iniciais

O banco PostgreSQL é inicializado automaticamente com:
- 6 dispositivos de exemplo
- 8 registros de dados de sensor
- Índices otimizados para performance

## 🔒 Segurança

- Containers executam com usuário não-root
- Health checks configurados
- Volumes persistentes para dados
- Rede isolada entre containers

## 🚨 Troubleshooting

### Problema: Porta 8080 já em uso
```bash
# Verificar o que está usando a porta
netstat -ano | findstr :8080

# Parar o processo ou usar outra porta
docker-compose up -p 8081:8080
```

### Problema: Erro de conexão com banco
```bash
# Verificar se o PostgreSQL está rodando
docker-compose ps db

# Ver logs do banco
docker-compose logs db

# Aguardar inicialização completa
docker-compose up db
# Aguardar 30 segundos
docker-compose up app
```

### Problema: Build falha
```bash
# Limpar cache do Docker
docker system prune -a

# Rebuild sem cache
docker-compose build --no-cache
```

## 📈 Monitoramento

### Health Checks
- **Aplicação:** Verifica endpoint `/actuator/health`
- **PostgreSQL:** Verifica conectividade com `pg_isready`

### Logs
- Logs estruturados em JSON
- Níveis configuráveis por ambiente
- Rotação automática de logs

## 🔄 CI/CD

Para integração com CI/CD, use:

```bash
# Build da imagem
docker build -t iot-city-backend:latest .

# Push para registry
docker tag iot-city-backend:latest your-registry/iot-city-backend:latest
docker push your-registry/iot-city-backend:latest
``` 