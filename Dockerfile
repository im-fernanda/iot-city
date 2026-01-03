FROM node:20-alpine AS frontend-build

WORKDIR /app


RUN npm install -g npm@latest

COPY frontend/package*.json ./
RUN npm install && npm cache clean --force

COPY frontend/src ./src
COPY frontend/public ./public
COPY frontend/tsconfig.json ./
COPY frontend/tailwind.config.js ./
COPY frontend/postcss.config.js ./
RUN npm run build

# Build do backend
FROM maven:3.9.9-eclipse-temurin-21 AS backend-build

WORKDIR /app

# Copiar pom.xml e baixar dependências
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fonte e compilar
COPY backend/src ./src
RUN mvn package -DskipTests -B

# Imagem final com Nginx + Backend
FROM eclipse-temurin:21-jre-alpine

# Instalar Nginx e curl
RUN apk add --no-cache nginx curl

# Criar usuário não-root
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Copiar arquivos
WORKDIR /app
COPY --from=frontend-build /app/build /usr/share/nginx/html
COPY --from=backend-build /app/target/*.jar app.jar
COPY nginx.conf /etc/nginx/nginx.conf
COPY start.sh /app/start.sh

# Configurar permissões
RUN mkdir -p /var/log/nginx /var/lib/nginx /run/nginx /var/cache/nginx && \
    chown -R appuser:appgroup /app /usr/share/nginx/html /var/log/nginx /var/lib/nginx /run/nginx /var/cache/nginx && \
    chmod +x /app/start.sh

# Nginx precisa rodar como root
EXPOSE 80 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

CMD ["/app/start.sh"] 