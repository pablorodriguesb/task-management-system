package com.devpablo.taskmanager.service;

import com.devpablo.taskmanager.model.Usuario;
import com.devpablo.taskmanager.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("teste@devpablo.com");
        usuario.setNome("Teste");
        usuario.setSenha("senha333");
    }

    @Test
    void buscarTodosUsuarios_DeveRetornarListaDeUsuarios() {
        // arrange
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario));

        // act
        List<Usuario> resultado = usuarioService.buscarTodosUsuarios();

        // assert
        assertEquals(1, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_QuandoExistir_DeveRetornarUsuario() {
        // arrange
    when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // act
        Optional<Usuario> resultado = usuarioService.buscarPorId(1L);

        // assert
        assertTrue(resultado.isPresent());
        assertEquals("Teste", resultado.get().getNome());
    }

    @Test
    void salvar_NovoUsuario_DeveSetarDataCriacao() {
        // arrange
        usuario.setId(null);
        when(usuarioRepository.existePorEmail(any())).thenReturn(false);

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // act
        Usuario salvo = usuarioService.salvar(usuario);

        // assert
        assertNotNull(salvo.getDataCriacao());
        assertNull(salvo.getDataAtualizacao());
        verify(usuarioRepository).save(usuario);
    }
    @Test
    void salvar_UsuarioExistente_DeveSetarDataAtualizacao() {
        // arrange
        usuario.setDataCriacao(LocalDateTime.now().minusDays(1));
        when(usuarioRepository.existePorEmail(any())).thenReturn(false);

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // act
        Usuario atualizado = usuarioService.salvar(usuario);

        // assert
        assertNotNull(atualizado.getDataAtualizacao());
        assertTrue(atualizado.getDataAtualizacao().isAfter(atualizado.getDataCriacao()));
        }

    @Test
    void salvar_EmailDuplicado_DeveLancarExcecao() {
        // arrange
        when(usuarioRepository.existePorEmail(any())).thenReturn(true);

        // act e assert
        assertThrows(RuntimeException.class, () -> usuarioService.salvar(usuario));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void excluirPorId_UsuarioExistente_DeveChamarDelete() {
        // arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // act
        usuarioService.excluirPorId(1L);

        // assert
        verify(usuarioRepository).delete(usuario);
    }

    @Test
    void excluirPorId_UsuarioInexistenet_DeveLancarExcecao() {
        // arrange
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        // act e assert
        assertThrows(RuntimeException.class, () -> usuarioService.excluirPorId(2L));
        verify(usuarioRepository, never()).delete(any());
    }

    // Teste para SensitiveCase
    @Test
    void devePermitirAtualizarEmailParaMesmoEmailEmMaiusculas() {
        Usuario existente = new Usuario();
        existente.setId(1L);
        existente.setEmail("teste@dev.com");

        when(usuarioRepository.buscarPorEmail("TESTE@DEV.COM")).thenReturn(Optional.of(existente));

        Usuario atualizado = new Usuario();
        atualizado.setEmail("TESTE@DEV.COM");
        atualizado.setId(1L);

        // tem que permitir pois é o mesmo usuário
        assertDoesNotThrow(() -> usuarioService.salvar(atualizado));
    }
}

