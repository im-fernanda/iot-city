# IoT City - Sistema de Gerenciamento de Dispositivos IoT

Sistema completo de gerenciamento de dispositivos IoT com backend Spring Boot e frontend React moderno, containerizado com Docker.

<img width="1893" height="944" alt="1" src="https://github.com/user-attachments/assets/5110358f-1780-4663-bfd5-b22441074689" />
<img width="1886" height="941" alt="2" src="https://github.com/user-attachments/assets/982722c5-2042-42d7-87f0-7be257bae0cd" />
<img width="1891" height="942" alt="3" src="https://github.com/user-attachments/assets/e5a3693f-2da2-4cf4-b18e-15db3ca82bb3" />
<img width="1906" height="938" alt="4" src="https://github.com/user-attachments/assets/c32fa4c1-646a-49c3-a6f7-42e3f10cbb51" />

## 💡 Conceitos e Aprendizados
Este projeto consolidou meus conhecimentos em arquitetura de software e desenvolvimento full-stack. No backend, utilizei Clean Architecture, organizando o código em camadas bem definidas para garantir separação de responsabilidades e facilitar a manutenção. Apliquei os princípios SOLID, com uso adequado de interfaces, abstrações e inversão de dependências.

Trabalhei com Spring Boot 3, explorando IoC e Injeção de Dependências, além de Spring Data JPA para persistência, com repositories customizados e queries otimizadas. Implementei tratamento centralizado de exceções, validações com Bean Validation, controle transacional, documentação de APIs com OpenAPI/Swagger e configuração de CORS.

No frontend, desenvolvi uma interface moderna baseada em componentes reutilizáveis, utilizando React com TypeScript, React Hooks e custom hooks para gerenciamento de estado. A estilização foi feita com TailwindCSS, animações com Framer Motion, seguindo Mobile-First Design, além da integração com APIs REST via Axios.

Na infraestrutura, utilizei Docker com multi-stage builds e Docker Compose para orquestração de serviços. Configurei Nginx como proxy reverso e trabalhei com PostgreSQL, aplicando modelagem relacional, migrations versionadas e consultas otimizadas com JPQL e Criteria API.

## 🚀 Como Rodar

### Opção 1: Docker (Recomendado)

**Pré-requisitos:** Docker e Docker Compose instalados

```bash
# 1. Clone o repositório
git clone https://github.com/im-fernanda/iot-city-backend.git
cd iot-city-backend

# 2. Execute com Docker Compose
docker-compose up --build

# 3. Acesse no navegador
# Frontend: http://localhost
# Backend API: http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

### Opção 2: Execução Local

**Pré-requisitos:**
- Java 21+
- Node.js 20+
- PostgreSQL 16+
- Maven 3.9+

#### Backend
```bash
cd backend

# Configurar banco de dados no application.properties
# spring.datasource.url=jdbc:postgresql://localhost:5432/iot_city
# spring.datasource.username=postgres
# spring.datasource.password=postgres

# Executar
mvn spring-boot:run

# API disponível em http://localhost:8080
```

#### Frontend
```bash
cd frontend

# Instalar dependências
npm install

# Executar em desenvolvimento
npm start

# Aplicação disponível em http://localhost:3000
```

## 📊 Features

- **Dashboard** - Visão geral com estatísticas e métricas
- **Gerenciamento de Dispositivos** - CRUD completo com filtros e busca
- **Visualização de Sensores** - Gráficos históricos de dados
- **API REST** - Endpoints documentados com Swagger
- **Interface Moderna** - Design glassmorphism com animações

## 🛠️ Tecnologias

**Backend:** Spring Boot 3, Spring Data JPA, PostgreSQL, OpenAPI/Swagger  
**Frontend:** React 19, TypeScript, TailwindCSS, Framer Motion, Recharts  
**DevOps:** Docker, Docker Compose, Nginx

## 📝 Tipos de Dispositivos

SEMÁFORO • QUALIDADE_AR • ILUMINACAO_PUBLICA • NIVEL_AGUA • RUÍDO • METEOROLÓGICO • CÂMERA_SEGURANÇA • ESTACIONAMENTO • LIXEIRA • PAINEL_SOLAR

## 🔧 Comandos Úteis

```bash
# Run in background
docker-compose up -d

# Ver logs
docker-compose logs -f

# Parar
docker-compose down

# Resetar tudo
docker-compose down -v && docker-compose up --build
```
