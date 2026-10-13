package com.classhub.common.security;

import com.classhub.usuario.Role;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenStoreTest {

    @Test
    void criarEValidarMantemSessaoValida() {
        TokenStore tokenStore = new TokenStore();
        UsuarioAutenticado usuario = new UsuarioAutenticado(10L, "aluno001", "Ana", Role.ALUNO);

        String token = tokenStore.criar(usuario);

        assertEquals(usuario, tokenStore.validar(token).orElseThrow());
    }

    @Test
    void validarRejeitaTokenExpirado() throws Exception {
        TokenStore tokenStore = new TokenStore();
        UsuarioAutenticado usuario = new UsuarioAutenticado(11L, "prof001", "Maria", Role.PROFESSOR);
        String token = tokenStore.criar(usuario);

        Field sessoesField = TokenStore.class.getDeclaredField("sessoes");
        sessoesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, SessaoToken> sessoes = (Map<String, SessaoToken>) sessoesField.get(tokenStore);
        sessoes.put(token, new SessaoToken(token, usuario, Instant.now().minusSeconds(120), Instant.now().minusSeconds(1)));

        assertTrue(tokenStore.validar(token).isEmpty());
    }
}
