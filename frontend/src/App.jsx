import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Tarefas from "./pages/Tarefas"; // Novo componente (era Dashboard anteriormente)
import { isAuthenticated } from "./services/auth";

// Componente para rotas protegidas
const ProtectedRoute = ({ children }) => {
  return isAuthenticated() ? children : <Navigate to="/login" replace />;
};


export default function App() {
  return (
    <Router>
      <Routes>
        {/* Redireciona para login ou tarefas dependendo da autenticação */}
        <Route 
          path="/" 
          element={
            isAuthenticated() ? 
              <Navigate to="/tarefas" replace /> : 
              <Navigate to="/login" replace />
          } 
        />

        {/* Rotas públicas */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Rota protegida para tarefas */}
        <Route 
          path="/tarefas" 
          element={
            <ProtectedRoute>
              <Tarefas />
            </ProtectedRoute>
          } 
        />

        {/* Redireciona rotas não encontradas */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Router>
  );
}
