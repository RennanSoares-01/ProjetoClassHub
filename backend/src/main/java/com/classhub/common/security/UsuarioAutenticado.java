package com.classhub.common.security;

import com.classhub.usuario.Role;

public record UsuarioAutenticado(Long id, String matricula, String nome, Role role) {
}
