package com.classhub.feedback.dto;

import com.classhub.entrega.StatusEntrega;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CriarFeedbackRequest(
        @NotNull(message = "entregaId é obrigatório") Long entregaId,
        @NotNull(message = "nota é obrigatória") @DecimalMin(value = "0", message = "nota mínima é 0") @DecimalMax(value = "10", message = "nota máxima é 10") BigDecimal nota,
        String comentario,
        @NotNull(message = "status é obrigatório") StatusEntrega status
) {
}
