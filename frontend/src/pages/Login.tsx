import React, { useState } from "react";
import { loginWithEmail } from "../services/auth";

export default function Login({ onLogin }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [erro, setErro] = useState("");

  async function handleSubmit(e) {
    e.preventDefault();
    setErro("");
    try {
      await loginWithEmail(email, password);
      onLogin();
    } catch (error) {
      setErro(error.message);
    }
  }

  return (
    <div className="login-box" style={{ margin: "0 auto", marginTop: "50px" }}>
      <div className="login-logo">
        <b>Task</b>Manager
      </div>
      <div className="card">
        <div className="card-body login-card-body">
          <p className="login-box-msg">Faça login para começar</p>
          <form onSubmit={handleSubmit}>
            <div className="input-group mb-3">
              <input type="email" className="form-control" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} required/>
            </div>
            <div className="input-group mb-3">
              <input type="password" className="form-control" placeholder="Senha" value={password} onChange={e => setPassword(e.target.value)} required/>
            </div>
            <div className="row">
              <div className="col-12">
                <button type="submit" className="btn btn-primary btn-block">
                  Entrar
                </button>
              </div>
            </div>
            {erro && <div className="alert alert-danger mt-2">{erro}</div>}
          </form>
        </div>
      </div>
    </div>
  );
}
