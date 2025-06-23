# 🗄️ Gerenciamento de Dados - Docker

Este guia explica como gerenciar os dados do banco PostgreSQL no ambiente Docker.

## 📊 Persistência de Dados

### ✅ Dados Persistem Entre Execuções
- Os dados são salvos no volume `postgres_data`
- Não são perdidos ao parar/restartar containers
- Só são resetados se você remover o volume manualmente

### 🔄 Comandos Úteis

#### **Verificar Status dos Containers**
```bash
docker-compose ps
```

#### **Ver Logs do Banco**
```bash
docker-compose logs db
```

#### **Acessar Banco de Dados**
```bash
# Conectar via psql
docker-compose exec db psql -U iotcity_user -d iotcity

# Ver tabelas
\dt

# Ver dados
SELECT * FROM devices;
SELECT * FROM sensor_data;
```

#### **Backup dos Dados**
```bash
# Criar backup
docker-compose exec db pg_dump -U iotcity_user iotcity > backup.sql

# Restaurar backup
docker-compose exec -T db psql -U iotcity_user -d iotcity < backup.sql
```

## 🚨 Reset de Dados

### **Reset Completo (CUIDADO!)**
```bash
# Para containers
docker-compose down

# Remove volume (APAGA TODOS OS DADOS!)
docker volume rm iot-city-backend_postgres_data

# Recria tudo
docker-compose up --build
```

### **Reset Apenas Dados (Mantém Estrutura)**
```bash
# Conectar no banco
docker-compose exec db psql -U iotcity_user -d iotcity

# Limpar dados
TRUNCATE devices, sensor_data CASCADE;

# Sair
\q

# Recarregar dados iniciais
docker-compose restart app
```

## 🔧 Solução de Problemas

### **Dados Duplicados**
Se você vir dados duplicados:

1. **Verifique se o volume está sendo usado:**
   ```bash
   docker volume ls | grep postgres_data
   ```

2. **Verifique os dados atuais:**
   ```bash
   docker-compose exec db psql -U iotcity_user -d iotcity -c "SELECT COUNT(*) FROM devices;"
   ```

3. **Se necessário, faça reset completo:**
   ```bash
   docker-compose down -v
   docker-compose up --build
   ```

### **Banco Não Inicializa**
```bash
# Ver logs detalhados
docker-compose logs db

# Verificar se arquivos SQL existem
ls -la backend/schema.sql backend/data.sql

# Rebuild se necessário
docker-compose down
docker-compose up --build
```

### **Problemas de Conexão**
```bash
# Verificar se banco está rodando
docker-compose exec db pg_isready -U iotcity_user -d iotcity

# Verificar conectividade
docker-compose exec app ping db
```

## 📈 Monitoramento

### **Verificar Uso de Disco**
```bash
# Tamanho do volume
docker system df -v

# Tamanho do banco
docker-compose exec db psql -U iotcity_user -d iotcity -c "SELECT pg_size_pretty(pg_database_size('iotcity'));"
```

### **Verificar Performance**
```bash
# Estatísticas do banco
docker-compose exec db psql -U iotcity_user -d iotcity -c "SELECT schemaname, tablename, n_tup_ins, n_tup_upd, n_tup_del FROM pg_stat_user_tables;"
```

## 🎯 Boas Práticas

### ✅ **Recomendado**
- Use `docker-compose down` para parar (não `docker-compose down -v`)
- Faça backups regulares dos dados importantes
- Monitore o tamanho do volume
- Use health checks para verificar status

### ❌ **Evite**
- Não use `docker-compose down -v` a menos que queira apagar tudo
- Não modifique dados diretamente no volume
- Não pare containers abruptamente
- Não execute múltiplas instâncias simultaneamente

## 🔍 Verificação Rápida

Para verificar se tudo está funcionando:

```bash
# 1. Status dos containers
docker-compose ps

# 2. Verificar se banco tem dados
docker-compose exec db psql -U iotcity_user -d iotcity -c "SELECT COUNT(*) FROM devices;"

# 3. Verificar se API responde
curl http://localhost:8080/api/devices

# 4. Verificar se frontend carrega
curl http://localhost:3000
```

Se todos os comandos retornarem sucesso, seus dados estão sendo persistidos corretamente! 🎉 