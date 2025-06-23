import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  timeout: 15000, // 15 segundos de timeout
});

// Interceptor para tratar erros de forma global
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Log detalhado do erro no console
    console.error(
      'Ocorreu um erro na API:', 
      error.response?.status, 
      error.response?.data
    );
    
    // Você poderia adicionar aqui um sistema de notificação global para o usuário
    // Ex: toast.error('Ocorreu um erro. Tente novamente mais tarde.');

    return Promise.reject(error);
  }
);

export default api; 