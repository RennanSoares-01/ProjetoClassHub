package com.classhub.entrega.dto;

import com.classhub.entrega.StatusEntrega;
import com.classhub.usuario.dto.UsuarioResponse;

import java.time.Instant;
import java.util.List;

public record EntregaResponse(
        Long id,
        Long tarefaId,
        String tarefaTitulo,
        UsuarioResponse autor,
        List<UsuarioResponse> participantes,
        String conteudo,
        String comentario,
        List<AnexoResponse> anexos,
        StatusEntrega status,
        Instant dataCriacao,
        Instant dataAtualizacao
) {
}
