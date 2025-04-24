package com.devpablo.taskmanager.repository;

import com.devpablo.taskmanager.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> buscarPorEmail(String email);
    boolean existePorEmail(String email);
}
