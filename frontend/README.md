# Frontend IoT City - React

Frontend da aplicação IoT City desenvolvido com React e TypeScript, responsável pela interface de usuário para gerenciamento de dispositivos IoT.

## 🛠️ Tecnologias

- **React 19** - Biblioteca JavaScript para construção de interfaces
- **TypeScript 4.9** - Tipagem estática
- **React Router DOM 6** - Navegação SPA
- **TailwindCSS 3.4** - Framework CSS utility-first
- **Framer Motion 11** - Biblioteca de animações
- **Recharts 2.15** - Gráficos interativos
- **Axios 1.7** - Cliente HTTP para API
- **Lucide React** - Ícones modernos
- **Node.js 20** - Runtime JavaScript
- **Nginx** - Servidor web para produção

## 📱 Funcionalidades

### Dashboard
- Visão geral dos dispositivos IoT
- Estatísticas em tempo real
- Dispositivos por tipo e recentes

### Gerenciamento de Dispositivos
- Lista completa com filtros (tipo, status, busca)
- Ações: editar, ativar/desativar, excluir
- Ordenação por diferentes critérios

### Sensores e Gráficos
- Visualização de dados históricos
- Filtros por tipo de sensor e dispositivo
- Gráficos interativos com Recharts

## 🎨 Interface e Design

O projeto utiliza um sistema de design customizado baseado em TailwindCSS com:
- **Cores**: Primary (Azul), Accent (Roxo), Dark (Escuro)
- **Efeitos**: Glassmorphism, gradientes animados, sombras neon
- **Animações**: Transições suaves com Framer Motion
- **Responsividade**: Layout adaptável para desktop, tablet e mobile (breakpoints: mobile < 768px, tablet 768-1024px, desktop > 1024px)

### Componentes Principais
- **Navbar** - Navegação principal
- **Dashboard** - Página inicial com estatísticas
- **Devices** - Gerenciamento de dispositivos
- **Sensors** - Visualização de dados de sensores
- **UI Components**: Button, Card, Badge, Input, Loading, Modal

## 🔧 Estrutura de Arquivos

```
frontend/
├── src/
│   ├── components/     # Componentes React reutilizáveis
│   │   ├── ui/        # Componentes de interface
│   │   └── Navbar.tsx
│   ├── pages/         # Páginas da aplicação
│   │   ├── Dashboard.tsx
│   │   ├── Devices.tsx
│   │   └── Sensors.tsx
│   ├── services/      # Serviços de API
│   │   └── api.ts     # Configuração do Axios
│   ├── App.tsx        # Componente raiz
│   └── index.tsx      # Ponto de entrada
├── public/            # Arquivos estáticos
├── package.json       # Dependências e scripts
├── tsconfig.json      # Configuração TypeScript
└── tailwind.config.js # Configuração do TailwindCSS
```

## 📊 Integração com Backend

### Configuração da API
O serviço de API está configurado em `src/services/api.ts`:
- Base URL: `/api` (proxy para `http://localhost:8080` em desenvolvimento via `package.json`)
- Timeout: 15 segundos
- Interceptors para tratamento global de erros e logging

### APIs Consumidas
- `GET /api/devices` - Listar dispositivos
- `PATCH /api/devices/{id}/toggle` - Alternar status ativo/inativo
- `DELETE /api/devices/{id}` - Excluir dispositivo
- `GET /api/sensor-data` - Dados históricos de sensores

### Tratamento de Erros
- Interceptor global no Axios captura erros automaticamente
- Log detalhado no console do navegador
- Feedback visual implementado nos componentes
- Mensagens de erro amigáveis para o usuário

## 🎯 Performance e Otimizações

- **Code Splitting**: Lazy loading de rotas com React Router
- **Memoização**: `React.memo` e `useMemo` para componentes pesados
- **Debounce**: Implementado em filtros de busca para reduzir requisições
- **Compressão**: Assets comprimidos com gzip no Nginx
- **Tree Shaking**: Remoção automática de código não utilizado
- **Minificação**: Código minificado em produção
- **Métricas Alvo**: FCP < 2s, TTI < 3s, LCP < 2.5s, CLS < 0.1

## 🔧 Configurações Importantes

### Proxy de Desenvolvimento
O `package.json` contém `"proxy": "http://localhost:8080"` para redirecionar requisições `/api/*` ao backend durante desenvolvimento local.

### Hot Reload
- Mudanças em arquivos `.tsx` atualizam automaticamente
- Mudanças em CSS/TailwindCSS atualizam sem reload completo
- Fast Refresh mantém o estado dos componentes

### TailwindCSS
Configuração customizada em `tailwind.config.js` com:
- Paleta de cores personalizada (primary, accent, dark)
- Animações customizadas (fade-in, slide-up, scale-in, gradient)
- Sombras especiais (glass, neon)
- Gradientes customizados

## 🚀 Build e Deploy

O frontend é construído usando multi-stage Docker build:
1. **Stage 1 - Build**: Node.js 20 Alpine
   - Instala dependências (`npm install`)
   - Executa build de produção (`npm run build`)
   - Gera arquivos otimizados na pasta `build/`

2. **Stage 2 - Runtime**: Eclipse Temurin 21 JRE Alpine
   - Copia arquivos build para `/usr/share/nginx/html`
   - Configura Nginx como servidor web estático
   - Inicia Nginx junto com o backend Spring Boot

### Configurações de Produção
- Build otimizado e minificado
- Nginx como servidor estático na porta 80
- Compressão gzip habilitada
- Cache de assets configurado
- Proxy reverso para `/api/*` redirecionando ao backend

## 🚨 Troubleshooting

### Problemas de Build

**Docker:**
```bash
docker-compose down
docker system prune -a
docker-compose up --build
```

**Local:**
```bash
rm -rf node_modules package-lock.json
npm install
npm cache clean --force
```

### Problemas de Conexão com API

```bash
# Verificar se o backend está rodando
docker-compose ps
docker-compose logs -f app

# Testar conexão
curl http://localhost:8080/api/devices
```

**Em desenvolvimento local:**
- Certifique-se de que o backend está rodando na porta 8080
- Verifique a configuração do proxy no `package.json`
- Confirme que a URL base da API está correta

### Problemas com TailwindCSS

```bash
# Rebuild do CSS
npm run build

# Verificar configuração
cat tailwind.config.js

# Limpar cache do PostCSS
rm -rf .cache
```

### Problemas com TypeScript

```bash
# Verificar erros de tipo
npm run build

# Verificar configuração
cat tsconfig.json
```
