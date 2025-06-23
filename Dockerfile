FROM node:18-alpine AS frontend-build

WORKDIR /app

COPY frontend/package*.json ./

RUN npm install

COPY frontend/src ./src
COPY frontend/public ./public
COPY frontend/tsconfig.json ./

# Build do frontend
RUN npm run build

# Build do backend
FROM maven:3.9.6-eclipse-temurin-17 AS backend-build

WORKDIR /app

COPY backend/pom.xml .
COPY backend/.mvn .mvn
COPY backend/mvnw .
COPY backend/mvnw.cmd .

# Baixar dependências
RUN mvn dependency:go-offline -B

COPY backend/src ./src

# Compilar e criar JAR
RUN mvn clean package -DskipTests

# Imagem final com Nginx + Backend
FROM eclipse-temurin:17-jre-alpine

# Instalar Nginx e curl
RUN apk add --no-cache nginx curl

# Criar usuário não-root para segurança
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Configurar Nginx e criar diretórios necessários
COPY nginx.conf /etc/nginx/nginx.conf
RUN mkdir -p /var/log/nginx /var/lib/nginx /run/nginx && \
    chown -R appuser:appgroup /var/log/nginx && \
    chown -R appuser:appgroup /var/lib/nginx && \
    chown -R appuser:appgroup /run/nginx

WORKDIR /app

COPY --from=frontend-build /app/build /usr/share/nginx/html
COPY --from=backend-build /app/target/*.jar app.jar

# Mudar propriedade dos arquivos para o usuário não-root
RUN chown -R appuser:appgroup /app && \
    chown -R appuser:appgroup /usr/share/nginx/html

# Mudar para usuário não-root
USER appuser

EXPOSE 80 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando para executar a aplicação (nginx em background + java)
CMD nginx -g "daemon off;" & sleep 2 && java -jar app.jar 