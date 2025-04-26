package com.devpablo.taskmanager.service;

import com.devpablo.taskmanager.enums.StatusTarefa;
import com.devpablo.taskmanager.model.Tarefa;
import com.devpablo.taskmanager.repository.TarefaRepository;
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
public class TarefaServiceTest {
    // mock do repositorio, simulando o banco de dados
    @Mock
    private TarefaRepository tarefaRepository;

    // injeta o mock no service, para testar so a logica do service
    @InjectMocks
    private TarefaService tarefaService;

    // TESTES PARA SALVAR A TAREFA <<

    @Test
    void deveSalvarTarefaValidaComDataCriacao() {
        // seguindo o padrao AAA, comecamos no arrange (preparando dados e mocks)
        Tarefa novaTarefa = new Tarefa();

        // configura o mock para retornar a tarefa com ID que foi simulado
        Mockito.when(tarefaRepository.save(Mockito.any(Tarefa.class)))
                .thenAnswer(inv -> {
                    Tarefa t = inv.getArgument(0);
                    t.setId(1L);
                    t.setDataCriacao(LocalDateTime.now());
                    return t;
                });

        // act : chamando o metodo real de salvar
        Tarefa resultado = tarefaService.salvar(novaTarefa);

        // assert: verificando os dados
        Assertions.assertEquals(1L, resultado.getId());
        Assertions.assertNotNull(resultado.getDataCriacao());
    }

    @Test
    void deveAtualizarTarefaExistenteComDataAtualizacao() {
        // arrange: tarefa ja existe (tem ID)
        Tarefa tarefaExistente = new Tarefa();
        tarefaExistente.setId(2L);

        tarefaExistente.setDataCriacao(LocalDateTime.now().minusDays(1)); // data antiga

        // configura o mock para retornar a tarefa atualizada
        Mockito.when(tarefaRepository.save(tarefaExistente)).thenReturn(tarefaExistente);

        // act
        Tarefa resultado = tarefaService.salvar(tarefaExistente);

        // assert: data da atualizacao tem que ser preenchida
        Assertions.assertNotNull(resultado.getDataAtualizacao());
    }
        // TESTES PARA CONCLUIR TAREFA <<
        @Test
        void deveConcluirTarefaComStatusEDatasCorretas() {
            // arrange: tarefa pendente
            Tarefa tarefa = new Tarefa();
            tarefa.setId(3L);
            tarefa.setStatus(StatusTarefa.PENDENTE);

            // configurando o mock para encontrar a tarefa

            Mockito.when(tarefaRepository.findById(3L)).thenReturn(Optional.of(tarefa));

            Mockito.when(tarefaRepository.save(Mockito.any(Tarefa.class))).thenReturn(tarefa);

            // act
            Tarefa resultado = tarefaService.concluir(3L);

            // assert: verifica status e datas
            Assertions.assertEquals(StatusTarefa.CONCLUIDA,
                    resultado.getStatus());
            Assertions.assertNotNull(resultado.getDataConclusao());
            Assertions.assertNotNull(resultado.getDataAtualizacao());
        }

        @Test
        void naoDeveConcluirTarefaInexistente() {

            // arrange: id nao existe
            Mockito.when(tarefaRepository.findById(999L)).thenReturn(Optional.empty());

            // act e assert: deve lançar exceção
            Assertions.assertThrows(IllegalArgumentException.class, () ->
                    tarefaService.concluir(999L)
            );
        }

        // TESTES PARA BUSCAR AS TAREFAS <<
        @Test
        void deveBuscarTodasTarefas() {

            // arrange: lista mockada
            List<Tarefa> tarefasMock = List.of(new Tarefa(), new Tarefa());

            Mockito.when(tarefaRepository.findAll()).thenReturn(tarefasMock);

            // act
            List<Tarefa> resultado = tarefaService.buscarTodasTarefas();

            // assert: verifica tamanho da lista
            Assertions.assertEquals(2, resultado.size());

        }

        @Test
        void deveBuscarTarefaPorIdExistente() {

            // arrange: id valido
            Tarefa tarefaMock = new Tarefa();
            tarefaMock.setId(4L);

        Mockito.when(tarefaRepository.findById(4L)).thenReturn(Optional.of(tarefaMock));

            // act
            Optional<Tarefa> resultado = tarefaService.buscarPorId(4L);

            // assert
            Assertions.assertTrue(resultado.isPresent());
    }

        @Test
        void deveBuscarTarefasPorUsuario() {

            // arrange: simula as tarefas que seriam retornadas
            Tarefa tarefa1 = new Tarefa();
            Tarefa tarefa2 = new Tarefa();
            List<Tarefa> tarefasMock = List.of(tarefa1, tarefa2);

            // configurando o mock para retornar a lista quando o metodo for chamado
        Mockito.when(tarefaRepository.buscarPorUsuarioId(1L)).thenReturn(tarefasMock);

            // act: chama o metodo do service
            List<Tarefa> resultado = tarefaService.buscarPorUsuario(1L);

            // assert: verifica se o resultado é o esperado
            Assertions.assertEquals(2, resultado.size());
        }

        @Test
        void deveBuscarTarefasPorStatus() {
            // arrange
            List<Tarefa> tarefasMock = List.of(new Tarefa(), new Tarefa());

        Mockito.when(tarefaRepository.buscarPorStatus(StatusTarefa.PENDENTE)).thenReturn(tarefasMock);

            // act
            List<Tarefa> resultado = tarefaService.buscarPorStatus(StatusTarefa.PENDENTE);

            // assert
            Assertions.assertEquals(2, resultado.size());
        }

        @Test
        void deveBuscarTarefasPorUsuarioEStatus() {
        // arrange
            Tarefa tarefa = new Tarefa();
            List<Tarefa> tarefasMock = List.of(tarefa);

        // configura o mock
        Mockito.when(tarefaRepository.buscarPorUsuarioIdEStatus(1L, StatusTarefa.PENDENTE))
                .thenReturn(tarefasMock);

        // act
            List<Tarefa> resultado = tarefaService.buscarPorUsuarioEStatus(1L, StatusTarefa.PENDENTE);

        // assert
        Assertions.assertEquals(1, resultado.size());
        }
}