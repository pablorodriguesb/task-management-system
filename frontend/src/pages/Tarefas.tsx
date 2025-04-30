import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getTarefas, createTarefa, updateTarefa, deleteTarefa, Tarefa } from "../services/tarefas";
import { logout } from "../services/auth";

const PRIORIDADES = [
  { label: "Baixa", value: "BAIXA" },
  { label: "Média", value: "MEDIA" },
  { label: "Alta", value: "ALTA" },
  { label: "Urgente", value: "URGENTE" },
];

const Tarefas: React.FC = () => {
  const [tarefas, setTarefas] = useState<Tarefa[]>([]);
  const [titulo, setTitulo] = useState("");
  const [descricao, setDescricao] = useState("");
  const [prioridade, setPrioridade] = useState("MEDIA");
  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState("");
  const [editando, setEditando] = useState<Tarefa | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    buscarTarefas();
  }, []);

  async function buscarTarefas() {
    setErro(""); setSucesso("");
    try {
      const lista = await getTarefas();
      console.log("Tarefas obtidas:", lista);
      setTarefas(lista);
    } catch (error) {
      console.error("Erro ao buscar:", error);
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
  async function handleStatusChange(id, novoStatus) {
    try {
      const tarefaAtual = tarefas.find(t => t.id === id);
      if (!tarefaAtual) {
        setErro("Tarefa não encontrada!");
        return;
      }
      await updateTarefa(id, {
        titulo: tarefaAtual.titulo,
        descricao: tarefaAtual.descricao,
        prioridade: tarefaAtual.prioridade,
        status: novoStatus,
        usuarioId: tarefaAtual.usuarioId // <-- use usuarioId!
      });
      setSucesso("Status atualizado com sucesso!");
      buscarTarefas();
    } catch (err) {
      setErro(err.message || "Erro ao atualizar status");
    }
  }
  


  const handleLogout = () => {
    logout();
    navigate("/login");
  };

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
        {/* Header e botões */}
        <div className="d-flex justify-content-between align-items-center mb-4">
          <div>
            <span style={{ fontWeight: 'bold', fontSize: 30 }}>Minhas</span>
            <span style={{ fontWeight: 'lighter', fontSize: 30 }}> Tarefas</span>
          </div>
          <div>
            <button className="btn btn-info me-2" onClick={() => navigate("/painel")}>
              Ver Estatísticas
            </button>
            <button className="btn btn-danger" onClick={handleLogout}>
              Sair
            </button>
          </div>
        </div>

        {/* FORMULÁRIO DE NOVA TAREFA */}
        <form className="row g-2 mb-3" style={{ flexWrap: "wrap" }} onSubmit={handleSubmit}>
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
              {/* Opções */}
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

        {/* MENSAGENS DE FEEDBACK */}
        {erro && <div className="alert alert-danger">{erro}</div>}
        {sucesso && <div className="alert alert-success">{sucesso}</div>}

        {/* TABELA DE TAREFAS */}
        <div className="table-responsive">
          {tarefas.length === 0 ? (
            <div className="alert alert-info text-center mt-3">
              Você ainda não tem tarefas cadastradas. Adicione sua primeira tarefa usando o formulário acima!
            </div>
          ) : (
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
                    <td>
                      <select
                        className="form-select form-select-sm"
                        value={tarefa.status}
                        onChange={(e) => handleStatusChange(tarefa.id, e.target.value)}
                      >
                        <option value="PENDENTE">Pendente</option>
                        <option value="EM_ANDAMENTO">Em Andamento</option>
                        <option value="CONCLUIDA">Concluída</option>
                        <option value="CANCELADA">Cancelada</option>
                      </select>
                    </td>

                    <td>{tarefa.prioridade}</td>
                    <td>
                      <button className="btn btn-sm btn-info me-2" onClick={() => handleEdit(tarefa)}>Editar</button>
                      <button className="btn btn-sm btn-danger" onClick={() => handleDelete(tarefa.id)}>Remover</button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  );
};

export default Tarefas;
