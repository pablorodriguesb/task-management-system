package com.devpablo.taskmanager.repository;

import com.devpablo.taskmanager.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Query("SELECT c FROM Categoria c WHERE LOWER(c.nome) = LOWER(:nome)")
    List<Categoria> buscarPorNome(@Param("nome") String nome);
}

