import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { registerWithEmail } from "../services/auth";

// Removemos props e usamos navegação direta
const Register: React.FC = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [nome, setNome] = useState("");
  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setErro("");
    setSucesso("");
    if (password.length < 8) {
      setErro("A senha deve ter pelo menos 8 caracteres.");
      return;
    }
    try {
      setIsLoading(true);
      await registerWithEmail(email, password, nome);
      setSucesso("Cadastro realizado! Redirecionando para login...");
      // Navegação programática após cadastro bem-sucedido
      setTimeout(() => navigate("/login"), 1200);
    } catch (error: any) {
      setErro(error.message || "Erro ao registrar. Tente novamente.");
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
      <div className="card p-4 shadow" style={{ maxWidth: 400, width: "100%" }}>
        <div className="mb-3 text-center">
          <span style={{ fontWeight: 'bold', fontSize: 32 }}>Task</span>
          <span style={{ fontWeight: 'lighter', fontSize: 32 }}>Manager</span>
        </div>
        <div className="mb-3 text-center" style={{ fontSize: 17 }}>Cadastre-se para começar</div>
        <form onSubmit={handleSubmit}>
          <input type="text" className="form-control mb-2" placeholder="Nome" value={nome} onChange={e => setNome(e.target.value)} required />
          <input type="email" className="form-control mb-2" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} required />
          <input type="password" className="form-control mb-3" placeholder="Senha" value={password} onChange={e => setPassword(e.target.value)} required minLength={8} />
          <button 
            type="submit" 
            className="btn btn-primary w-100"
            disabled={isLoading}
          >
            {isLoading ? "Carregando..." : "Cadastrar"}
          </button>
          {sucesso && <div className="alert alert-success mt-2">{sucesso}</div>}
          {erro && <div className="alert alert-danger mt-2">{erro}</div>}
        </form>
        <div className="text-center mt-3">
          <button
            type="button"
            className="btn btn-link"
            onClick={() => navigate("/login")}
            style={{ textDecoration: "none", color: "#1976d2" }}
          >
            Já tenho conta
          </button>
        </div>
      </div>
    </div>
  );
};

export default Register;
