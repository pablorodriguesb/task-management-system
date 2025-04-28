import axios from 'axios';

const API_URL = 'http://localhost:8080';

export const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Interceptor para adicionar token JWT em todas as requisições (se usar JWT)
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

/**
 * Login com Spring Security padrão
 * Em dev, use user / senha que aparece no console do Spring Boot
 */
export async function loginWithEmail(email, password) {
  try {
    const loginResponse = await axios.post(`${API_URL}/login`,
      `username=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`,
      { headers: { 'Content-Type': 'application/x-www-form-urlencoded' } }
    );

    // Se usar JWT, ajuste aqui para pegar o token
    const token = loginResponse.headers.authorization;
    if (token) {
      localStorage.setItem('token', token.replace('Bearer ', ''));
    }

    // Busca dados do usuário (opcional, se existir endpoint)
    try {
      const userResponse = await api.get('/usuarios/atual');
      localStorage.setItem('usuario', JSON.stringify(userResponse.data));
      return userResponse.data;
    } catch {
      return { email, authenticated: true };
    }
  } catch (error) {
    throw new Error(error?.response?.data?.message || 'Credenciais inválidas');
  }
}

/**
 * Cadastro de novo usuário
 */
export async function registerWithEmail(email, password, nome) {
  try {
    const response = await api.post('/usuarios', { nome, email, senha: password });
    return response.data;
  } catch (error) {
    throw new Error(
      error?.response?.data?.mensagem ||
      error?.response?.data?.message ||
      'Erro ao cadastrar usuário'
    );
  }
}

/**
 * Logout
 */
export function logout() {
  localStorage.removeItem('token');
  localStorage.removeItem('usuario');
}

/**
 * Verifica se está autenticado
 */
export function isAuthenticated() {
  const token = localStorage.getItem('token');
  return !!token;
}
