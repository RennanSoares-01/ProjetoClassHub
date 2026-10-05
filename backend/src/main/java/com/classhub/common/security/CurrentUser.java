package com.classhub.common.security;

import com.classhub.common.exception.ForbiddenException;
import com.classhub.common.exception.UnauthorizedException;
import com.classhub.usuario.Role;

/** Contexto do usuário autenticado na requisição atual (preenchido pelo AuthenticationFilter). */
public final class CurrentUser {

    private static final ThreadLocal<UsuarioAutenticado> CONTEXTO = new ThreadLocal<>();

    private CurrentUser() {
    }

    public static void set(UsuarioAutenticado usuario) {
        CONTEXTO.set(usuario);
    }

    public static void clear() {
        CONTEXTO.remove();
    }

    public static UsuarioAutenticado get() {
        UsuarioAutenticado usuario = CONTEXTO.get();
        if (usuario == null) {
            throw new UnauthorizedException("Usuário não autenticado.");
        }
        return usuario;
    }

    public static void exigirRole(Role... permitidos) {
        UsuarioAutenticado atual = get();
        for (Role role : permitidos) {
            if (role == atual.role()) {
                return;
            }
        }
        throw new ForbiddenException("Usuário não tem permissão para executar esta ação.");
    }
}
