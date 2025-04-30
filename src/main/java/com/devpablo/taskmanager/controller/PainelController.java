package com.devpablo.taskmanager.controller;

import com.devpablo.taskmanager.enums.StatusTarefa;
import com.devpablo.taskmanager.model.Usuario;
import com.devpablo.taskmanager.repository.TarefaRepository;
import com.devpablo.taskmanager.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/painel")
public class PainelController {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    
    @GetMapping("/estatisticas")
    public Map<String, Long> obterEstatisticas(Authentication authentication) {
        // obter um usuario autenticado
        String email = authentication.getName();
        Usuario usuario = usuarioRepository.buscarPorEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    
    
        long pendentes = tarefaRepository.buscarPorUsuarioIdEStatus(usuario.getId(), StatusTarefa.PENDENTE).size();
        
        long andamento = tarefaRepository.buscarPorUsuarioIdEStatus(usuario.getId(), StatusTarefa.EM_ANDAMENTO).size();
        
        long concluidas = tarefaRepository.buscarPorUsuarioIdEStatus(usuario.getId(), StatusTarefa.CONCLUIDA).size();
        
        long canceladas = tarefaRepository.buscarPorUsuarioIdEStatus(usuario.getId(), StatusTarefa.CANCELADA).size();

        Map<String, Long> estatisticas = new HashMap<>();
        estatisticas.put("pendentes", pendentes);
        estatisticas.put("andamento", andamento);
        estatisticas.put("concluidas", concluidas);
        estatisticas.put("canceladas", canceladas);
        return estatisticas;
    }
}
