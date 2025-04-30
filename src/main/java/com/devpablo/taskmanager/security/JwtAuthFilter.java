package com.devpablo.taskmanager.security;

import com.devpablo.taskmanager.model.Usuario;
import com.devpablo.taskmanager.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthFilter(JwtUtils jwtUtils, UsuarioRepository usuarioRepository) {
        this.jwtUtils = jwtUtils;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }

        if (token != null && jwtUtils.validateToken(token)) {
            // 1. Extrai o email do token JWT
            String email = jwtUtils.getEmailFromToken(token);
            System.out.println("Email extraído do token: " + email);

            // 2. Busca o usuário no banco
            Usuario usuario = usuarioRepository.buscarPorEmail(email).orElse(null);
            System.out.println("Usuário encontrado: " + (usuario != null ? "SIM" : "NÃO"));

            if (usuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 3. Cria o objeto Authentication
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                usuario.getEmail(), // Usar o email aqui
                                null,
                                Collections.emptyList()
                        );

                // 4. Define detalhes adicionais
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 5. Armazena no contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Agora sim podemos usar authentication.getName()
                System.out.println("Email da autenticação: " + authentication.getName());
            } else {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Usuário inválido ou não encontrado");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

}
