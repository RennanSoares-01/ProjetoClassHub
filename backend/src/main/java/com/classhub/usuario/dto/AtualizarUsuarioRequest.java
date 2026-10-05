package com.classhub.usuario.dto;

import com.classhub.usuario.Role;

public record AtualizarUsuarioRequest(String nome, Role role) {
}
