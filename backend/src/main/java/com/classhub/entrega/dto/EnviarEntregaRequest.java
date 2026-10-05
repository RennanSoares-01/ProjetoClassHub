package com.classhub.entrega.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record EnviarEntregaRequest(
        @NotNull(message = "tarefaId é obrigatório") Long tarefaId,
        @NotBlank(message = "conteúdo é obrigatório") String conteudo,
        String comentario,
        List<AnexoRequest> anexos,
        List<String> participantesMatriculas
) {
}
