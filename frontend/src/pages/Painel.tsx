import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { api } from "../services/auth";
import { logout } from "../services/auth";

interface Estatisticas {
  pendentes: number;
  andamento: number;
  concluidas: number;
  canceladas: number;
}

export default function Painel() {
  const [estatisticas, setEstatisticas] = useState<Estatisticas>({
    pendentes: 0,
    andamento: 0,
    concluidas: 0,
    canceladas: 0
  });
  const [carregando, setCarregando] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    carregarEstatisticas();
  }, []);

  async function carregarEstatisticas() {
    try {
      const resposta = await api.get('/painel/estatisticas');
      setEstatisticas(resposta.data);
      setCarregando(false);
    } catch (erro) {
      console.error("Erro ao carregar estatísticas:", erro);
      setCarregando(false);
    }
  }

  function handleSair() {
    logout();
    navigate("/login");
  }

  function irParaTarefas() {
    navigate("/tarefas");
  }

  return (
    <div style={{
      minHeight: '100vh',
      width: '100vw',
      background: '#f8fafc',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      padding: 24
    }}>
      <div className="card shadow p-4" style={{ maxWidth: 1000, width: "100%", position: "relative" }}>
        {/* Botão Sair */}
        <button 
          className="btn btn-danger" 
          style={{ position: "absolute", right: 24, top: 24 }} 
          onClick={handleSair}
        >
          Sair
        </button>

        {/* Título */}
        <div className="mb-4 text-center">
          <span style={{ fontWeight: 'bold', fontSize: 30 }}>Painel de Estatísticas</span>
        </div>

        {carregando ? (
          <div className="text-center">
            <div className="spinner-border text-primary" role="status">
              <span className="visually-hidden">Carregando...</span>
            </div>
          </div>
        ) : (
          <div className="row text-center">
            {/* Card Pendentes */}
            <div className="col-md-3 mb-3">
              <div className="card border-primary">
                <div className="card-body">
                  <h5 className="card-title text-primary">Pendentes</h5>
                  <p className="card-text" style={{ fontSize: 24 }}>{estatisticas.pendentes}</p>
                </div>
              </div>
            </div>

            {/* Card Em Andamento */}
            <div className="col-md-3 mb-3">
              <div className="card border-warning">
                <div className="card-body">
                  <h5 className="card-title text-warning">Em Andamento</h5>
                  <p className="card-text" style={{ fontSize: 24 }}>{estatisticas.andamento}</p>
                </div>
              </div>
            </div>

            {/* Card Concluídas */}
            <div className="col-md-3 mb-3">
              <div className="card border-success">
                <div className="card-body">
                  <h5 className="card-title text-success">Concluídas</h5>
                  <p className="card-text" style={{ fontSize: 24 }}>{estatisticas.concluidas}</p>
                </div>
              </div>
            </div>

            {/* Card Canceladas */}
            <div className="col-md-3 mb-3">
              <div className="card border-danger">
                <div className="card-body">
                  <h5 className="card-title text-danger">Canceladas</h5>
                  <p className="card-text" style={{ fontSize: 24 }}>{estatisticas.canceladas}</p>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Botão para Tarefas */}
        <div className="text-center mt-4">
          <button 
            className="btn btn-primary"
            onClick={irParaTarefas}
          >
            Ver Todas as Tarefas
          </button>
        </div>
      </div>
    </div>
  );
}
