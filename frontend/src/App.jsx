import React, { useState } from "react";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";
import { isAuthenticated, logout } from "./services/auth";

export default function App() {
  
  const [view, setView] = useState(isAuthenticated() ? "dashboard" : "login");

  const handleLogin = () => setView("dashboard");
  const handleLogout = () => {
    logout();
    setView("login");
  };
  const handleRegister = () => setView("login");

  return (
    <>
      {view === "login" && <Login onLogin={handleLogin} />}
      {view === "register" && <Register onRegister={handleRegister} />}
      {view === "dashboard" && (
        <div>
          <button className="btn btn-danger m-2 float-right" onClick={handleLogout}>
            Sair
          </button>
          <Dashboard />
        </div>
      )}
      {view === "login" && (
        <p className="text-center mt-2">
          <button className="btn btn-link" onClick={() => setView("register")}>
            Criar Conta
          </button>
        </p>
      )}
      {view === "register" && (
        <p className="text-center mt-2">
          <button className="btn btn-link" onClick={() => setView("login")}>
            Já tenho conta
          </button>
        </p>
      )}
    </>
  );
}
