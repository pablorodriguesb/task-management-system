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
  const resp = await api.get('/tarefas');
  if (Array.isArray(resp.data)) return resp.data;
  if (resp.data && Array.isArray(resp.data.content)) return resp.data.content;
  return [];
}

export async function createTarefa(
    titulo: string,
    descricao: string,
    prioridade: string = 'MEDIA'
  ): Promise<Tarefa> {
    // Pegue o usuário do localStorage
    const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');
    const responsavelId = usuario?.id;
  
    const resp = await api.post('/tarefas', {
      titulo,
      descricao,
      status: 'PENDENTE',
      prioridade,
      responsavelId, 
    });
    return resp.data;
  }
  
  

  export async function updateTarefa(
    id: number,
    dados: Partial<Omit<Tarefa, 'id' | 'responsavelId'>> // Omitimos responsavelId para incluir manualmente, se quiser
  ) {
    const usuario = JSON.parse(localStorage.getItem('usuario') || '{}');
    const responsavelId = usuario?.id;
    // Envie todos os campos obrigatórios
    const payload = {
      ...dados,
      responsavelId, // obrigatório
      status: dados.status || 'PENDENTE', // mantenha o status da tarefa ou defina um default
    };
    const resp = await api.put(`/tarefas/${id}`, payload);
    return resp.data;
  }
  

// Deletar tarefa
export async function deleteTarefa(id: number) {
  await api.delete(`/tarefas/${id}`);
}
