package com.classhub.dashboard.dto;

import java.math.BigDecimal;

public record NotaResumo(Long tarefaId, String tarefaTitulo, BigDecimal nota, String status) {
}
