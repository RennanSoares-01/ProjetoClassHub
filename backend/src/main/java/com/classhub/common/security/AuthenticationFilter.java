package com.classhub.common.security;

import com.classhub.common.exception.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Autenticação simplificada do MVP (ADR-008): valida o token Bearer emitido no login
 * para as rotas protegidas e expõe o usuário atual via {@link CurrentUser}.
 */
@Component
@Order(1)
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> PREFIXOS_PROTEGIDOS = List.of(
            "/usuarios", "/turmas", "/tarefas", "/entregas", "/feedbacks", "/notificacoes", "/dashboard"
    );

    private final TokenStore tokenStore;
    private final ObjectMapper objectMapper;

    public AuthenticationFilter(TokenStore tokenStore, ObjectMapper objectMapper) {
        this.tokenStore = tokenStore;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        boolean precisaAutenticar = PREFIXOS_PROTEGIDOS.stream().anyMatch(path::startsWith)
                || "/auth/usuarios".equals(path);

        if (!precisaAutenticar) {
            chain.doFilter(request, response);
            return;
        }

        Optional<UsuarioAutenticado> usuario = extrairToken(request).flatMap(tokenStore::validar);
        if (usuario.isEmpty()) {
            escreverErro(response, request, "Token ausente ou inválido. Faça login novamente.");
            return;
        }

        try {
            CurrentUser.set(usuario.get());
            chain.doFilter(request, response);
        } finally {
            CurrentUser.clear();
        }
    }

    private Optional<String> extrairToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return Optional.of(header.substring("Bearer ".length()).trim());
        }
        return Optional.empty();
    }

    private void escreverErro(HttpServletResponse response, HttpServletRequest request, String mensagem) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        ApiError erro = new ApiError(Instant.now(), 401, "Unauthorized", mensagem, request.getRequestURI());
        response.getWriter().write(objectMapper.writeValueAsString(erro));
    }
}
