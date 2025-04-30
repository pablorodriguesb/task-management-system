import axios from 'axios';

const API_URL = 'http://localhost:8080';

// Cria a instância principal do Axios
export const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Define UM interceptor global - sempre envia o JWT se existir
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export async function loginWithEmail(email, password) {
  try {
    // Faz login via JWT e salva o token
    const response = await api.post('/auth/login', { email, senha: password });
    const token = response.data.token;
    localStorage.setItem('token', token);

    // Busca o usuário autenticado
    const userResponse = await api.get('/usuarios/atual');
    localStorage.setItem('usuario', JSON.stringify(userResponse.data));

    return userResponse.data;
  } catch (error) {
    throw new Error(
      error?.response?.data?.mensagem ||
      error?.response?.data?.message ||
      error?.response?.data?.error ||
      'Credenciais inválidas'
    );
  }
}

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

export async function logout() {
  // Para JWT basta limpar os dados do localStorage
  localStorage.removeItem('token');
  localStorage.removeItem('usuario');
}

export function isAuthenticated() {
  return !!localStorage.getItem('token');
}
