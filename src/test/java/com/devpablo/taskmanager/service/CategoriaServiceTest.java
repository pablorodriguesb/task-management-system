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
    void deveBuscarTodasCategorias() {

        // arrange
        List<Categoria> categoriasMock = List.of(new Categoria(), new Categoria());

    Mockito.when(categoriaRepository.findAll()).thenReturn(categoriasMock);

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

    Mockito.when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoriaMock));

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

    Mockito.when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

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

    Mockito.when(categoriaRepository.findById(id)).thenReturn(Optional.of(categoriaMock));

    Mockito.when(tarefaRepository.countByCategoria_Id(id)).thenReturn(0L);

        // act
        categoriaService.excluir(id);

        // assert
        Mockito.verify(categoriaRepository).delete(categoriaMock);
    }

    @Test
    void deveLancarExcecaoAoExcluirCategoriaInexistente() {
        // arrange
        Long id = 999L;

    Mockito.when(categoriaRepository.findById(id)).thenReturn(Optional.empty());

        // act e assert
        assertThrows(RuntimeException.class, () -> categoriaService.excluir(id));
    }

    @Test
    void deveBuscarCategoriasPorNome() {
        // arrange
        String nome = "Estudo";
        List<Categoria> categoriasMock = List.of(new Categoria());

    Mockito.when(categoriaRepository.buscarPorNome(nome)).thenReturn(categoriasMock);

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

    Mockito.when(categoriaRepository.buscarPorNome("Nova")).thenReturn(List.of());

    Mockito.when(categoriaRepository.save(Mockito.any(Categoria.class))).thenReturn(novaCategoria);

    // act
    Categoria resultado = categoriaService.salvar(novaCategoria);

    // assert
    Assertions.assertNotNull(resultado.getDataCriacao());
    Assertions.assertNull(resultado.getDataAtualizacao());
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