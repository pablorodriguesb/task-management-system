package com.devpablo.taskmanager.controller;

import com.devpablo.taskmanager.dto.CategoriaRequestDTO;
import com.devpablo.taskmanager.dto.CategoriaResponseDTO;
import com.devpablo.taskmanager.model.Categoria;
import com.devpablo.taskmanager.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponseDTO criar(@Valid @RequestBody CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());

        Categoria salva = categoriaService.salvar(categoria);
        return converterParaDTO(salva);
    }

    @PutMapping("/{id}")
    public CategoriaResponseDTO atualizar(@PathVariable Long id, @Valid
                                          @RequestBody CategoriaRequestDTO dto) {
        Categoria existente = categoriaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        existente.setNome(dto.nome());
        existente.setDescricao(dto.descricao());

        return converterParaDTO(categoriaService.salvar(existente));
    }

    @GetMapping
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaService.buscarTodasCategorias()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public CategoriaResponseDTO buscarPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
        return converterParaDTO(categoria);
    }


    // metodos auxiliares
    private Categoria converterParaEntidade(CategoriaRequestDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());
        return categoria;
    }

    private CategoriaResponseDTO converterParaDTO(Categoria categoria) {
        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao(),
                categoria.getDataCriacao()
        );
    }
}
