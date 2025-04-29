import React, { useState } from "react";
import { loginWithEmail } from "../services/auth";

interface LoginProps {
  onLogin: () => void;
  onNavigateToRegister: () => void;
}

export default function Login({ onLogin, onNavigateToRegister }: LoginProps) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [erro, setErro] = useState("");

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setErro("");
    try {
      await loginWithEmail(email, password);
      onLogin();
    } catch (error: any) {
      setErro(error.message);
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
          <input type="email" className="form-control mb-2" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} required />
          <input type="password" className="form-control mb-3" placeholder="Senha" value={password} onChange={e => setPassword(e.target.value)} required />
          <button type="submit" className="btn btn-primary w-100">Entrar</button>
          {erro && <div className="alert alert-danger mt-2">{erro}</div>}
        </form>
        <div className="text-center mt-3">
          <button
            type="button"
            className="btn btn-link"
            onClick={onNavigateToRegister}
            style={{ textDecoration: "none", color: "#1976d2" }}
          >
            Criar Conta
          </button>
        </div>
      </div>
    </div>
  );
}
