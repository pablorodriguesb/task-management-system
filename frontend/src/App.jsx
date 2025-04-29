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
      {view === "login" && (
        <Login
          onLogin={handleLogin}
          onNavigateToRegister={() => setView("register")}
        />
      )}
      {view === "register" && (
        <Register
          onRegister={handleRegister}
          onNavigateToLogin={() => setView("login")}
        />
      )}
      {view === "dashboard" && (
        <Dashboard onLogout={handleLogout} />
      )}
    </>
  );
}
