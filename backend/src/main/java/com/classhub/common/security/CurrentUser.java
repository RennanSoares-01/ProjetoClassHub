package com.classhub.common.security;

import com.classhub.common.exception.ForbiddenException;
import com.classhub.common.exception.UnauthorizedException;
import com.classhub.usuario.Role;
import org.springframework.stereotype.Component;

/** Contexto do usuário autenticado na requisição atual (preenchido pelo AuthenticationFilter). */
@Component
public final class CurrentUser {

    private final ThreadLocal<UsuarioAutenticado> contexto = new ThreadLocal<>();

    public void set(UsuarioAutenticado usuario) {
        contexto.set(usuario);
    }

    public void clear() {
        contexto.remove();
    }

    public UsuarioAutenticado get() {
        UsuarioAutenticado usuario = contexto.get();
        if (usuario == null) {
            throw new UnauthorizedException("Usuário não autenticado.");
        }
        return usuario;
    }

    public void exigirRole(Role... permitidos) {
        UsuarioAutenticado atual = get();
        for (Role role : permitidos) {
            if (role == atual.role()) {
                return;
            }
        }
        throw new ForbiddenException("Usuário não tem permissão para executar esta ação.");
    }
}
