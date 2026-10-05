package com.classhub.common.security;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sessões de autenticação em memória (ADR-008 em .ai/architecture.md).
 * Suficiente para o MVP; não sobrevive a um restart do backend.
 */
@Component
public class TokenStore {

    private final Map<String, SessaoToken> sessoes = new ConcurrentHashMap<>();

    public String criar(UsuarioAutenticado usuario) {
        String token = UUID.randomUUID().toString();
        sessoes.put(token, new SessaoToken(token, usuario, Instant.now()));
        return token;
    }

    public Optional<UsuarioAutenticado> validar(String token) {
        return Optional.ofNullable(sessoes.get(token)).map(SessaoToken::usuario);
    }

    public void invalidar(String token) {
        sessoes.remove(token);
    }
}
