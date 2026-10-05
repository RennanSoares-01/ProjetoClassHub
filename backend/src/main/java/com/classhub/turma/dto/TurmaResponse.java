package com.classhub.turma.dto;

import com.classhub.usuario.dto.UsuarioResponse;

import java.time.Instant;
import java.util.List;

public record TurmaResponse(Long id, String nome, UsuarioResponse professor, List<UsuarioResponse> alunos, Instant criadoEm) {
}
