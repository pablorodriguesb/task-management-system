import React, { useState } from "react";
import { registerWithEmail } from "../services/auth";

interface Props {
  onRegister: () => void;
}

const Register: React.FC<Props> = ({ onRegister }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [nome, setNome] = useState("");
  const [erro, setErro] = useState("");
  const [sucesso, setSucesso] = useState("");

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setErro("");
    setSucesso("");
    // Validação simples no front: senha com pelo menos 8 caracteres
    if (password.length < 8) {
      setErro("A senha deve ter pelo menos 8 caracteres.");
      return;
    }
    try {
      await registerWithEmail(email, password, nome);
      setSucesso("Cadastro realizado! Faça login.");
      onRegister();
    } catch (error: any) {
      setErro(error.message);
    }
  }

  return (
    <div className="register-box" style={{ margin: "0 auto", marginTop: "50px" }}>
      <div className="register-logo">
        <b>Task</b>Manager
      </div>
      <div className="card">
        <div className="card-body register-card-body">
          <p className="login-box-msg">Cadastre-se para começar</p>
          <form onSubmit={handleSubmit}>
            <div className="input-group mb-3">
              <input
                type="text"
                className="form-control"
                placeholder="Nome"
                value={nome}
                onChange={e => setNome(e.target.value)}
                required
              />
            </div>
            <div className="input-group mb-3">
              <input
                type="email"
                className="form-control"
                placeholder="Email"
                value={email}
                onChange={e => setEmail(e.target.value)}
                required
              />
            </div>
            <div className="input-group mb-3">
              <input
                type="password"
                className="form-control"
                placeholder="Senha"
                value={password}
                onChange={e => setPassword(e.target.value)}
                required
                minLength={8}
              />
            </div>
            <div className="row">
              <div className="col-12">
                <button type="submit" className="btn btn-primary btn-block">
                  Cadastrar
                </button>
              </div>
            </div>
            {sucesso && <div className="alert alert-success mt-2">{sucesso}</div>}
            {erro && <div className="alert alert-danger mt-2">{erro}</div>}
          </form>
        </div>
      </div>
    </div>
  );
};

export default Register;
