package com.classhub.entrega.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record AtualizarEntregaRequest(
        @NotBlank(message = "conteúdo é obrigatório") String conteudo,
        String comentario,
        List<AnexoRequest> anexos,
        List<String> participantesMatriculas
) {
}
