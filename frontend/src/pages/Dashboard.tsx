import React, { useEffect, useState } from "react";
import { getTarefas, createTarefa, updateTarefa, deleteTarefa, Tarefa } from "../services/tarefas";

const PRIORIDADES = [
  { label: "Baixa", value: "BAIXA" },
  { label: "Média", value: "MEDIA" },
  { label: "Alta", value: "ALTA" },
  { label: "Urgente", value: "URGENTE" },
];

interface DashboardProps {
  onLogout: () => void;
}

const Dashboard: React.FC<DashboardProps> = ({ onLogout }) => {
  const [tarefas, setTarefas] = useState<Tarefa[]>([]);
  const [titulo, setTitulo] = useState("");
  const [descricao, setDescricao] = useState("");
  const [prioridade, setPrioridade] = useState("MEDIA");
  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState("");
  const [editando, setEditando] = useState<Tarefa | null>(null);

  useEffect(() => {
    buscarTarefas();
  }, []);

  async function buscarTarefas() {
    setErro(""); setSucesso("");
    try {
      const lista = await getTarefas();
      setTarefas(lista);
    } catch {
      setErro("Erro ao buscar tarefas");
    }
  }

  function limparForm() {
    setTitulo(""); setDescricao(""); setPrioridade("MEDIA");
    setEditando(null); setErro(""); setSucesso("");
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setErro(""); setSucesso("");
    if (!titulo.trim()) {
      setErro("Título é obrigatório");
      return;
    }
    try {
      if (editando) {
        await updateTarefa(editando.id, { titulo, descricao, prioridade });
        setSucesso("Tarefa atualizada!");
      } else {
        await createTarefa(titulo, descricao, prioridade);
        setSucesso("Tarefa adicionada!");
      }
      limparForm();
      buscarTarefas();
    } catch (err: any) {
      setErro(err.message || "Erro ao salvar tarefa");
    }
  }

  function handleEdit(tarefa: Tarefa) {
    setEditando(tarefa);
    setTitulo(tarefa.titulo);
    setDescricao(tarefa.descricao || "");
    setPrioridade(PRIORIDADES.some(p => p.value === tarefa.prioridade) ? tarefa.prioridade : "MEDIA");
    setSucesso(""); setErro("");
  }

  async function handleDelete(id: number) {
    if (!window.confirm("Deseja realmente excluir a tarefa?")) return;
    try {
      await deleteTarefa(id);
      setSucesso("Tarefa removida!");
      buscarTarefas();
    } catch (err: any) {
      setErro(err?.message || "Erro ao deletar tarefa!");
    }
  }

  return (
    <div style={{
      minHeight: '100vh',
      width: '100vw',
      background: '#f8fafc',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'flex-start',
      padding: 24
    }}>
      <div className="card shadow p-4" style={{ maxWidth: 1000, width: "100%", marginTop: 32, position: "relative" }}>
        {/* BOTÃO SAIR NO TOPO DIREITA DENTRO DO CARD */}
        <button
          className="btn btn-danger"
          style={{ position: "absolute", right: 24, top: 24 }}
          onClick={onLogout}
        >
          Sair
        </button>
        <div className="mb-4 text-center">
          <span style={{ fontWeight: 'bold', fontSize: 30 }}>Minhas</span>
          <span style={{ fontWeight: 'lighter', fontSize: 30 }}> Tarefas</span>
        </div>
        <form onSubmit={handleSubmit} className="row g-2 mb-3" style={{ flexWrap:"wrap" }}>
          <div className="col-sm-12 col-md-3">
            <input className="form-control" type="text" placeholder="Título" value={titulo} onChange={e => setTitulo(e.target.value)} required />
          </div>
          <div className="col-sm-12 col-md-4">
            <input className="form-control" type="text" placeholder="Descrição" value={descricao} onChange={e => setDescricao(e.target.value)} />
          </div>
          <div className="col-sm-12 col-md-2">
            <select className="form-control" value={prioridade} onChange={e => setPrioridade(e.target.value)}>
              {PRIORIDADES.map(({ label, value }) => (
                <option key={value} value={value}>{label}</option>
              ))}
            </select>
          </div>
          <div className="col-sm-12 col-md-3 d-flex">
            <button type="submit" className="btn btn-primary me-2 flex-shrink-1">
              {editando ? "Atualizar" : "Adicionar"}
            </button>
            {editando && (
              <button type="button" className="btn btn-secondary" onClick={limparForm}>
                Cancelar
              </button>
            )}
          </div>
        </form>
        {erro && <div className="alert alert-danger">{erro}</div>}
        {sucesso && <div className="alert alert-success">{sucesso}</div>}
        <div className="table-responsive">
          <table className="table table-striped mt-4">
            <thead>
              <tr>
                <th>ID</th>
                <th>Título</th>
                <th>Descrição</th>
                <th>Status</th>
                <th>Prioridade</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {tarefas.map(tarefa => (
                <tr key={tarefa.id}>
                  <td>{tarefa.id}</td>
                  <td>{tarefa.titulo}</td>
                  <td>{tarefa.descricao}</td>
                  <td>{tarefa.status}</td>
                  <td>{tarefa.prioridade}</td>
                  <td>
                    <button className="btn btn-sm btn-info me-2" onClick={() => handleEdit(tarefa)}>Editar</button>
                    <button className="btn btn-sm btn-danger" onClick={() => handleDelete(tarefa.id)}>Remover</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
