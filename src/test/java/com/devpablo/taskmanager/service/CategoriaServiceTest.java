package com.devpablo.taskmanager.service;

import com.devpablo.taskmanager.model.Categoria;
import com.devpablo.taskmanager.repository.CategoriaRepository;
import com.devpablo.taskmanager.repository.TarefaRepository;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void devePermitirExcluirCategoriaSemTarefas() {
        // PREPARACAO: Cria cenario controlado
        Long idCategoria = 1L;

        // cria uma categoria fake para o teste
        Categoria categoriaMock = new Categoria();
        categoriaMock.setId(idCategoria);
        categoriaMock.setNome("Teste");

        // configura o mock do repositorio de categorias
        Mockito.when(categoriaRepository.findById(idCategoria))
                .thenReturn(Optional.of(categoriaMock));
        
        Mockito.when(tarefaRepository.countByCategoria_Id(idCategoria)).thenReturn(0L);

        // execucao
        boolean podeExcluir = categoriaService.podeSerExcluida(idCategoria);

        // verificacao
        Assertions.assertTrue(podeExcluir, "Deve permitir excluir categoria sem tarefas vinculadas");
    }

    @Test
    void naoDevePermitirExcluirCategoriaComTarefas() {
        // PREPARACAO: cria cenario controlado
        Long idCategoria = 2L;

        Categoria categoriaMock = new Categoria();
        categoriaMock.setId(idCategoria);
        categoriaMock.setNome("Urgente");
        // configura o mock para retornar a categoria falsa quando buscar ID
        Mockito.when(categoriaRepository.findById(idCategoria))
                        .thenReturn(Optional.of(categoriaMock));
        // configura o mock para retornar 0(zero) tarefas vinculadas
        Mockito.when(tarefaRepository.countByCategoria_Id(idCategoria)).thenReturn(3L);

        // chama o metodo real que esta sendo testado
        boolean podeExcluir =
                categoriaService.podeSerExcluida(idCategoria);

        // confirma o resultado esperado
        Assertions.assertFalse(podeExcluir, "Nao deve permitir excluir categoria com tarefas vinculadas");
    }

    @Test
    void naoDeveSalvarCategoriaComNomeRepetido() {
        // PREPARACAO
        Categoria novaCategoria = new Categoria();
        novaCategoria.setNome("Urgente");

        // mock para simular busca por nome que retorna uma categoria existente
        Categoria existente = new Categoria();
        existente.setNome("Urgente");
        List<Categoria> categoriasExistentes = List.of(existente);

        // configura o mock para ertornar a lista de categorias existentes
        Mockito.when(categoriaRepository.buscarPorNome("Urgente")).thenReturn(categoriasExistentes);

        // executa e verifica
        assertThrows(RuntimeException.class, () -> {
            categoriaService.salvar(novaCategoria);
        });

    }
}