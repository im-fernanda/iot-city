# Frontend IoT City - React

Frontend da aplicação IoT City desenvolvido com React e TypeScript, responsável pela interface de usuário para gerenciamento de dispositivos IoT.

## 🐳 Execução com Docker

### Pré-requisitos
- **Docker** 20.10 ou superior
- **Docker Compose** 2.0 ou superior

### 🚀 Como Executar

1. **Na raiz do projeto**
   ```bash
   docker-compose up --build
   ```

2. **Acesse a aplicação**
   - **Frontend**: http://localhost:3000
   - **Backend API**: http://localhost:8080

## 🛠️ Tecnologias

- **React 18** - Biblioteca JavaScript
- **TypeScript** - Tipagem estática
- **React Router** - Navegação SPA
- **Recharts** - Gráficos interativos
- **CSS3** - Estilização moderna
- **Node.js** - Runtime JavaScript
- **Nginx** - Servidor web

## 📱 Funcionalidades

### Dashboard
- Visão geral dos dispositivos IoT
- Estatísticas em tempo real
- Dispositivos por tipo
- Dispositivos recentes

### Gerenciamento de Dispositivos
- Lista completa de dispositivos
- Filtros por tipo, status e busca
- Ações: editar, ativar/desativar, excluir
- Ordenação por diferentes critérios

### Sensores e Gráficos
- Visualização de dados históricos
- Filtros por tipo de sensor e dispositivo
- Gráficos interativos com Recharts

## 🎨 Interface

### Design Responsivo
- Layout adaptável para desktop e mobile
- Componentes reutilizáveis
- Navegação intuitiva
- Feedback visual para ações

### Componentes Principais
- `Navbar` - Navegação principal
- `DeviceCard` - Card de dispositivo
- `SensorChart` - Gráfico de sensores
- `FilterPanel` - Painel de filtros

## 🔧 Configuração

### Estrutura de Arquivos
```
frontend/
├── src/
│   ├── components/     # Componentes React
│   ├── pages/         # Páginas da aplicação
│   ├── services/      # Serviços de API
│   ├── types/         # Tipos TypeScript
│   └── utils/         # Utilitários
├── public/            # Arquivos estáticos
├── package.json       # Dependências
└── tsconfig.json      # Configuração TypeScript
```

## 📊 Integração com Backend

### APIs Consumidas
- `GET /api/devices` - Listar dispositivos
- `PATCH /api/devices/{id}/toggle` - Toggle status
- `DELETE /api/devices/{id}` - Excluir dispositivo
- `GET /api/sensor-data` - Dados de sensores

### Tratamento de Erros
- Feedback visual para erros de API
- Retry automático para falhas temporárias
- Mensagens de erro amigáveis

## 🚨 Troubleshooting

### Problemas de Build
```bash
# Limpar cache do Docker
docker-compose down
docker system prune -a
docker-compose up --build
```

### Problemas de Conexão com API
```bash
# Verificar se o backend está rodando
docker-compose ps

# Ver logs do frontend
docker-compose logs -f frontend

# Ver logs do backend
docker-compose logs -f app
```

### Problemas de Porta
```bash
# Verificar processos usando a porta 3000
netstat -ano | findstr :3000

# Parar processo se necessário
taskkill /PID <PID> /F
```

## 🔍 Desenvolvimento

### Hot Reload
O React está configurado com hot reload para desenvolvimento:
- Mudanças em arquivos `.tsx` atualizam automaticamente
- Mudanças em CSS atualizam sem reload
- Console do navegador mostra erros em tempo real

### Debug
```bash
# Ver logs do frontend
docker-compose logs -f frontend

# Acessar container
docker-compose exec frontend sh
```

## 📱 Responsividade

### Breakpoints
- **Desktop**: > 1024px
- **Tablet**: 768px - 1024px
- **Mobile**: < 768px

### Componentes Adaptáveis
- Grid responsivo para dispositivos
- Cards que se ajustam ao tamanho da tela
- Navegação mobile-friendly
- Gráficos que se redimensionam

## 🎯 Performance

### Otimizações
- Lazy loading de componentes
- Memoização de componentes pesados
- Debounce em filtros de busca
- Compressão de assets

### Métricas
- First Contentful Paint < 2s
- Time to Interactive < 3s
- Bundle size otimizado

## 🔐 Segurança

### Headers de Segurança
- Content Security Policy
- X-Frame-Options
- X-Content-Type-Options

### Validação de Dados
- Sanitização de inputs
- Validação de tipos TypeScript
- Tratamento seguro de dados da API

## 🚀 Deploy

O frontend está configurado para produção com:
- Build otimizado para produção
- Servidor Nginx configurado
- Compressão gzip habilitada
- Cache de assets configurado
