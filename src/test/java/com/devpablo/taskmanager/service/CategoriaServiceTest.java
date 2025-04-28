package com.devpablo.taskmanager.service;

import com.devpablo.taskmanager.model.Categoria;
import com.devpablo.taskmanager.repository.CategoriaRepository;
import com.devpablo.taskmanager.repository.TarefaRepository;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
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
        when(categoriaRepository.findById(idCategoria))
                .thenReturn(Optional.of(categoriaMock));

        when(tarefaRepository.countByCategoria_Id(idCategoria)).thenReturn(0L);

        // execucao
        boolean podeExcluir = categoriaService.podeSerExcluida(idCategoria);

        // verificacao
        Assertions.assertTrue(podeExcluir, "Deve permitir excluir categoria sem tarefas vinculadas");
    }

    @Test
    void deveBuscarTodasCategorias() {

        // arrange
        List<Categoria> categoriasMock = List.of(new Categoria(), new Categoria());

    when(categoriaRepository.findAll()).thenReturn(categoriasMock);

        // act
        List<Categoria> resultado = categoriaService.buscarTodasCategorias();

        // assert
        Assertions.assertEquals(2, resultado.size());
    }

    @Test
    void deveBuscarCategoriaPorIdExistente() {
        // arrange
        Long id = 1L;
        Categoria categoriaMock = new Categoria();
        categoriaMock.setId(id);

    when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoriaMock));

        // act
        Optional<Categoria> resultado = categoriaService.buscarPorId(id);

        // assert
        Assertions.assertTrue(resultado.isPresent());
        Assertions.assertEquals(id, resultado.get().getId());
    }

    @Test
    void deveRetornarVazioQuandoBuscarPorIdInexistente() {
        // arrange
        Long id = 999L;

    when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        // act
        Optional<Categoria> resultado = categoriaService.buscarPorId(id);

        // assert
        Assertions.assertTrue(resultado.isEmpty());
    }

    @Test
    void deveExcluirCategoriaQuandoPossivel() {
        Long id = 1L;
        Categoria categoriaMock = new Categoria();
        categoriaMock.setId(id);

    when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoriaMock));

    when(tarefaRepository.countByCategoria_Id(id)).thenReturn(0L);

        // act
        categoriaService.excluir(id);

        // assert
        Mockito.verify(categoriaRepository).delete(categoriaMock);
    }

    @Test
    void deveLancarExcecaoAoExcluirCategoriaInexistente() {
        // arrange
        Long id = 999L;

    when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        // act e assert
        assertThrows(RuntimeException.class, () -> categoriaService.excluir(id));
    }

    @Test
    void deveBuscarCategoriasPorNome() {
        // arrange
        String nome = "Estudo";
        List<Categoria> categoriasMock = List.of(new Categoria());

    when(categoriaRepository.buscarPorNome(nome)).thenReturn(categoriasMock);

        // act
        List<Categoria> resultado = categoriaService.buscarPorNome(nome);

        // assert
        Assertions.assertEquals(1, resultado.size());
    }

    @Test
    void deveSalvarNovaCategoria() {
        // arrange
        Categoria novaCategoria = new Categoria();
        novaCategoria.setNome("Nova");

    when(categoriaRepository.buscarPorNome("Nova")).thenReturn(List.of());

    when(categoriaRepository.save(any(Categoria.class))).thenReturn(novaCategoria);

    // act
    Categoria resultado = categoriaService.salvar(novaCategoria);

    // assert
    Assertions.assertNotNull(resultado.getDataCriacao());
    assertNull(resultado.getDataAtualizacao());
    }

    @Test
    void naoDevePermitirExcluirCategoriaComTarefas() {
        // PREPARACAO: cria cenario controlado
        Long idCategoria = 2L;

        Categoria categoriaMock = new Categoria();
        categoriaMock.setId(idCategoria);
        categoriaMock.setNome("Urgente");
        // configura o mock para retornar a categoria falsa quando buscar ID
        when(categoriaRepository.findById(idCategoria))
                        .thenReturn(Optional.of(categoriaMock));
        // configura o mock para retornar 0(zero) tarefas vinculadas
        when(tarefaRepository.countByCategoria_Id(idCategoria)).thenReturn(3L);

        // chama o metodo real que esta sendo testado
        boolean podeExcluir =
                categoriaService.podeSerExcluida(idCategoria);

        // confirma o resultado esperado
        Assertions.assertFalse(podeExcluir, "Nao deve permitir excluir categoria com tarefas vinculadas");
    }

    @Test
    void deveAtualizarCategoriaSemAlterarNome() {
        Categoria existente = new Categoria();
        existente.setId(1L);
        existente.setNome("Estudo");
        existente.setDataCriacao(LocalDateTime.now());

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(categoriaRepository.save(any())).thenReturn(existente);

        Categoria atualizada = categoriaService.salvar(existente);
        assertNull(atualizada.getDataAtualizacao()); // deve alterar apos o ajuste no service
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
        when(categoriaRepository.buscarPorNome("Urgente")).thenReturn(categoriasExistentes);

        // executa e verifica
        assertThrows(RuntimeException.class, () -> {
            categoriaService.salvar(novaCategoria);
        });

    }
}