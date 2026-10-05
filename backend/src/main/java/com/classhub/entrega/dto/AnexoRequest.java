package com.classhub.entrega.dto;

import jakarta.validation.constraints.NotBlank;

public record AnexoRequest(
        @NotBlank(message = "nomeArquivo é obrigatório") String nomeArquivo,
        @NotBlank(message = "url é obrigatória") String url
) {
}
