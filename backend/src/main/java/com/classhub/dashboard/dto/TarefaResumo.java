package com.classhub.dashboard.dto;

import java.time.LocalDateTime;

public record TarefaResumo(Long id, String titulo, String turmaNome, LocalDateTime dataLimite) {
}
