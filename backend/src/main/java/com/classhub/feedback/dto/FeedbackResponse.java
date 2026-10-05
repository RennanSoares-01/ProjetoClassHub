package com.classhub.feedback.dto;

import com.classhub.entrega.StatusEntrega;
import com.classhub.usuario.dto.UsuarioResponse;

import java.math.BigDecimal;
import java.time.Instant;

public record FeedbackResponse(
        Long id,
        Long entregaId,
        BigDecimal nota,
        String comentario,
        StatusEntrega status,
        UsuarioResponse professor,
        Instant criadoEm
) {
}
