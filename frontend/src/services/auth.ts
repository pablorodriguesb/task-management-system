import axios from 'axios';

const API_URL = 'http://localhost:8080';

export const api = axios.create({
  baseURL: API_URL,
  withCredentials: true, // Essencial para enviar JSESSIONID
  headers: {
    'Content-Type': 'application/json'
  }
});

export async function loginWithEmail(email: string, password: string) {
  try {
    // Spring Security espera username/password em form-urlencoded
    await axios.post(
      `${API_URL}/login`,
      `username=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`,
      {
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        withCredentials: true // ESSENCIAL para manter sessão!
      }
    );

    // Após login bem-sucedido, já tem JSESSIONID nos cookies (session)
    // Agora busca dados do usuário logado:
    const userResponse = await api.get('/usuarios/atual');
    localStorage.setItem('usuario', JSON.stringify(userResponse.data));
    return userResponse.data;
  } catch (error: any) {
    throw new Error(
      error?.response?.data?.message ||
      error?.response?.data?.error ||
      'Credenciais inválidas'
    );
  }
}
/**
 * Cadastro de novo usuário
 */
export async function registerWithEmail(email: string, password: string, nome: string) {
  try {
    const response = await api.post('/usuarios', { 
      nome, 
      email, 
      senha: password 
    });
    return response.data;
  } catch (error: any) {
    throw new Error(
      error?.response?.data?.mensagem || 
      error?.response?.data?.message ||
      'Erro ao cadastrar usuário'
    );
  }
}


export async function logout() {
  try {
    // Chama endpoint de logout do Spring Security
    await api.post('/logout');
  } finally {
    // Limpa dados locais
    localStorage.removeItem('usuario');
  }
}

export function isAuthenticated() {
  // Verifica se existe usuário no localStorage
  return !!localStorage.getItem('usuario');
}
