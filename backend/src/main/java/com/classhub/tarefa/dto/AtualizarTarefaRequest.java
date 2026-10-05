package com.classhub.tarefa.dto;

import com.classhub.tarefa.TipoTarefa;

import java.time.LocalDateTime;

public record AtualizarTarefaRequest(String titulo, String descricao, TipoTarefa tipo, LocalDateTime dataLimite) {
}
