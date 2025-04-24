package com.devpablo.taskmanager.repository;

import com.devpablo.taskmanager.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> buscarPorNome(String nome);
}
