package com.devpablo.taskmanager.service;

import com.devpablo.taskmanager.enums.StatusTarefa;
import com.devpablo.taskmanager.model.Tarefa;
import com.devpablo.taskmanager.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    // injetando as dependencias do repositorio
    private final TarefaRepository tarefaRepository;
    @Autowired
    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }
        // Metodo para buscar especificos
        public List<Tarefa> buscarPorStatus(StatusTarefa status) {
            return tarefaRepository.buscarPorStatus(status);
        }

        public List<Tarefa> buscarPorUsuario(Long usuarioId) {
            return tarefaRepository.buscarPorUsuarioId(usuarioId);
        }

        public List<Tarefa> buscarPorUsuarioEStatus(Long usuarioId, StatusTarefa status) {
            return tarefaRepository.buscarPorUsuarioIdEStatus(usuarioId, status);
        }

        // Metodo para buscar todas as tarefas
        public List<Tarefa> buscarTodasTarefas() {
            return tarefaRepository.findAll();
    }

        // Metodo ordenado por id
        public List<Tarefa> buscarTodasOrdenadasPorId() {
        return tarefaRepository.findAllByOrderByIdAsc();
        }

        // Metodo para buscar uma tarefa pelo ID
        public Optional<Tarefa> buscarPorId(Long id) {
            return tarefaRepository.findById(id);
    }

        // Metodo para salvar uma nova tarefa ou atualizar existente
        public Tarefa salvar(Tarefa tarefa) {
            if (tarefa.getResponsavel() == null) {
                throw new RuntimeException("Tarefa precisa ter um responsável");
            }
        LocalDateTime agora = LocalDateTime.now();

            if (tarefa.getId() == null) {
                tarefa.setDataCriacao(agora);
            } else {
                tarefa.setDataAtualizacao(agora);
            }
            return tarefaRepository.save(tarefa);
        }

        @Transactional
        public Tarefa atualizar(Long id, Tarefa tarefaAtualizada) {
            Tarefa tarefaExistente = tarefaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

            // atualiza apenas os campos permitidos
            tarefaExistente.setTitulo(tarefaAtualizada.getTitulo());
            tarefaExistente.setDescricao(tarefaAtualizada.getDescricao());
            tarefaExistente.setStatus(tarefaAtualizada.getStatus());
            tarefaExistente.setPrioridade(tarefaAtualizada.getPrioridade());

            tarefaExistente.setDataVencimento(tarefaAtualizada.getDataVencimento());
            tarefaExistente.setDataAtualizacao(LocalDateTime.now());

            // mantem os relacionamentos existentes se nao houver fornecidos no DTO
            if (tarefaAtualizada.getResponsavel() != null) {
                tarefaExistente.setResponsavel(tarefaAtualizada.getResponsavel());
            }
            if (tarefaAtualizada.getCategoria() != null) {
                tarefaExistente.setCategoria(tarefaAtualizada.getCategoria());
            }
            return tarefaRepository.save(tarefaExistente);
        }

        @Transactional
        public void excluir(Long id) {
            Tarefa tarefa = tarefaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
            tarefaRepository.delete(tarefa);
    }

        @Transactional
        public Tarefa concluir(Long id) {

            // buscando tarefa (com tratamento de erros)
            Tarefa tarefa = tarefaRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Tarefa não" +
                            "encontrada com Id: " + id));

            // atualizando os campos
            LocalDateTime agora = LocalDateTime.now();
            tarefa.setStatus(StatusTarefa.CONCLUIDA);
            tarefa.setDataConclusao(agora);
            tarefa.setDataAtualizacao(agora); // atualiza a data de modificacao

            return tarefaRepository.save(tarefa);
        }
    }

