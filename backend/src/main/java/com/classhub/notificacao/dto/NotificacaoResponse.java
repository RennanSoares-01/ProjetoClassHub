package com.classhub.notificacao.dto;

import com.classhub.notificacao.TipoNotificacao;

import java.time.Instant;

public record NotificacaoResponse(Long id, TipoNotificacao tipo, String mensagem, Instant criadoEm) {
}
