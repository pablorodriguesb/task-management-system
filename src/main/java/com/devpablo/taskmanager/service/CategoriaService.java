package com.devpablo.taskmanager.service;

import com.devpablo.taskmanager.enums.CategoriaTarefa;
import com.devpablo.taskmanager.model.Categoria;
import com.devpablo.taskmanager.repository.CategoriaRepository;
import com.devpablo.taskmanager.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final TarefaRepository tarefaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository, TarefaRepository tarefaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.tarefaRepository = tarefaRepository;
    }
    // metodo de buscar todas categorias
    public List<Categoria> buscarTodasCategorias() {
        return categoriaRepository.findAll();
    }
    // metodo de buscar por id
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }
    // metodo de persistencia para salvar
    public Categoria salvar(Categoria categoria) {
        // verificar se ja possui uma categoria com o mesmo nome (exceto maiusculas)
        List<Categoria> existentes =
                categoriaRepository.buscarPorNome(categoria.getNome());

        boolean nomeRepetido = existentes.stream()
                .anyMatch(cat -> cat.getNome().equalsIgnoreCase(categoria.getNome()));

        if (nomeRepetido) {
            throw new RuntimeException("Já existe uma categoria com o nome " + categoria.getNome());
        }

        if(categoria.getId() == null) {
            categoria.setDataCriacao(LocalDateTime.now());
        } else {
            categoria.setDataAtualizacao(LocalDateTime.now());
        }
        return categoriaRepository.save(categoria);
    }
    // metodo de persistencia para excluir
    public void excluir(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada " + id));

        // verificando se pode ser excluida
        if(!podeSerExcluida(id)) {
            throw new RuntimeException("Não é possivel excluir categoria com tarefas vinculadas");
        }

        // apos testar condicoes, deleta.
        categoriaRepository.delete(categoria);
    }

    // buscando categorias pelo nome (contendo o termo informado)
    public List<Categoria> buscarPorNome(String nome) {
        return categoriaRepository.buscarPorNome(nome);
    }

    // verificando se a categoria pode ser excluida (nao tendo tarefas associadas)
    @Transactional(readOnly = true)
    public boolean podeSerExcluida(Long id) {
        // busca categoria pelo id
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada: " + id));

        // verifica se tem tarefa vinculada a categoria
        Long tarefasVinculadas = tarefaRepository.countByCategoria_Id(id);

        // So pode excluir se nao houver tarefas vinculadas (contagem = 0)
        return tarefasVinculadas == 0;
    }

}
