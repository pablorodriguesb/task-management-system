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
        Categoria categoria = converterParaEntidade(dto);
        categoria.setId(id);
        Categoria atualizada = categoriaService.salvar(categoria);
        return converterParaDTO(atualizada);
    }

    @GetMapping
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaService.buscarTodasCategorias()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

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
