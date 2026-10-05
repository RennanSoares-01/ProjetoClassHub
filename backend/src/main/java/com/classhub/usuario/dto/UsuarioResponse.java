package com.classhub.usuario.dto;

import com.classhub.usuario.Role;

public record UsuarioResponse(Long id, String matricula, String nome, String email, Role role) {
}
