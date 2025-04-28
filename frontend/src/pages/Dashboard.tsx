import React, { useEffect, useState } from "react";
import { api } from "../services/auth";

interface Tarefa {
  id: number;
  titulo: string;
  status: string;
  prioridade: string;
}

const Dashboard: React.FC = () => {
  const [tarefas, setTarefas] = useState<Tarefa[]>([]);
  const [erro, setErro] = useState("");

  useEffect(() => {
    api.get("/tarefas")
      .then(resp => setTarefas(resp.data))
      .catch(() => setErro("Erro ao buscar tarefas"));
  }, []);

  return (
    <div className="content-wrapper" style={{ minHeight: '100vh', padding: '2rem' }}>
      <h1>Minhas Tarefas</h1>
      {erro && <div className="alert alert-danger">{erro}</div>}
      <table className="table table-striped">
        <thead>
          <tr>
            <th>ID</th>
            <th>Título</th>
            <th>Status</th>
            <th>Prioridade</th>
          </tr>
        </thead>
        <tbody>
          {tarefas.map(tarefa => (
            <tr key={tarefa.id}>
              <td>{tarefa.id}</td>
              <td>{tarefa.titulo}</td>
              <td>{tarefa.status}</td>
              <td>{tarefa.prioridade}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default Dashboard;
