import { api } from './auth';

export interface Tarefa {
  id: number;
  titulo: string;
  descricao: string;
  status: string;
  prioridade: string;
}

// Buscar todas as tarefas do usuário autenticado
export async function getTarefas(): Promise<Tarefa[]> {
  try {
    const resp = await api.get('/tarefas');
    console.log('Resposta da API:', resp.data); // Log para debug
    
    if (Array.isArray(resp.data)) return resp.data;
    if (resp.data && Array.isArray(resp.data.content)) return resp.data.content;
    return [];
  } catch (error) {
    console.error('Erro ao buscar tarefas:', error);
    return [];
  }
}

export async function createTarefa(
  titulo: string,
  descricao: string,
  prioridade: string = 'MEDIA'
): Promise<Tarefa> {
  try {
    // Pegue o usuário do localStorage
    const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');
    const responsavelId = usuario?.id;
    
    const payload = {
      titulo,
      descricao,
      status: 'PENDENTE',
      prioridade,
      responsavelId
    };
    
    console.log('Criando tarefa:', payload); // Log para debug
    const resp = await api.post('/tarefas', payload);
    return resp.data;
  } catch (error) {
    console.error('Erro ao criar tarefa:', error);
    throw error;
  }
}

export async function updateTarefa(
  id: number,
  dados: Partial<Omit<Tarefa, 'id'>>
): Promise<Tarefa> {
  try {
    const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');
    const responsavelId = usuario?.id;
    
    const payload = {
      ...dados,
      responsavelId,
      status: dados.status || 'PENDENTE'
    };
    
    console.log(`Atualizando tarefa ${id}:`, payload); // Log para debug
    const resp = await api.put(`/tarefas/${id}`, payload);
    return resp.data;
  } catch (error) {
    console.error(`Erro ao atualizar tarefa ${id}:`, error);
    throw error;
  }
}

// Deletar tarefa
export async function deleteTarefa(id: number): Promise<void> {
  try {
    console.log(`Deletando tarefa ${id}`); // Log para debug
    await api.delete(`/tarefas/${id}`);
  } catch (error) {
    console.error(`Erro ao deletar tarefa ${id}:`, error);
    throw error;
  }
}
