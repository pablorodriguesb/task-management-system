import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { loginWithEmail } from "../services/auth";

// Removemos os props pois agora usamos navegação direta
export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [erro, setErro] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  async function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    setErro("");

    if (!email || !password) {
      setErro("Preencha todos os campos");
      return;
    }

    try {
      setIsLoading(true);
      await loginWithEmail(email, password);
      // Navegação programática após login bem-sucedido
      navigate("/tarefas");
    } catch (error: any) {
      setErro(error.message || "Erro ao fazer login");
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <div style={{
      minHeight: '100vh',
      width: '100vw',
      background: '#f8fafc',
      display: 'flex',
      justifyContent: 'center',
      alignItems: 'center',
      padding: 16
    }}>
      <div className="card shadow" style={{ width: '100%', maxWidth: 400, padding: 32 }}>
        <div className="mb-3 text-center">
          <span style={{ fontWeight: 'bold', fontSize: 32 }}>Task</span>
          <span style={{ fontWeight: 'lighter', fontSize: 32 }}>Manager</span>
        </div>
        <div className="mb-3 text-center" style={{ fontSize: 17 }}>Faça login para começar</div>
        <form onSubmit={handleSubmit}>
          <input 
            type="email" 
            className="form-control mb-2" 
            placeholder="Email" 
            value={email} 
            onChange={e => setEmail(e.target.value)} 
            required 
          />
          <input 
            type="password" 
            className="form-control mb-3" 
            placeholder="Senha" 
            value={password} 
            onChange={e => setPassword(e.target.value)} 
            required 
          />
          <button 
            type="submit" 
            className="btn btn-primary w-100"
            disabled={isLoading}
          >
            {isLoading ? "Carregando..." : "Entrar"}
          </button>
          {erro && <div className="alert alert-danger mt-2">{erro}</div>}
        </form>
        <div className="text-center mt-3">
          <button
            type="button"
            className="btn btn-link"
            onClick={() => navigate("/register")}
            style={{ textDecoration: "none", color: "#1976d2" }}
          >
            Criar Conta
          </button>
        </div>
      </div>
    </div>
  );
}
