#!/bin/sh

# Iniciar Nginx em background
echo "Starting Nginx..."
nginx -g "daemon off;" &

# Aguardar um pouco para o Nginx inicializar
sleep 2

# Iniciar aplicação Spring Boot
echo "Starting Spring Boot application..."
java -jar app.jar 