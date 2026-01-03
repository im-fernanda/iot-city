#!/bin/sh
set -e

echo "=== Starting IoT City Application ==="

# Iniciar Nginx em background
echo "[1/2] Starting Nginx..."
nginx -t && nginx
echo "✓ Nginx started on port 80"

# Aguardar um pouco para o Nginx estabilizar
sleep 2

# Iniciar Spring Boot em foreground (para manter o container vivo)
echo "[2/2] Starting Spring Boot application..."
echo "This may take 20-30 seconds..."
java -jar /app/app.jar

