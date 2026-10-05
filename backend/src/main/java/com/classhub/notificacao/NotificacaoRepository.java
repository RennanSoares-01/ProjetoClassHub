package com.classhub.notificacao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    List<Notificacao> findByDestinatario_IdOrderByCriadoEmDesc(Long destinatarioId);

    boolean existsByDestinatario_IdAndTipoAndReferenciaId(Long destinatarioId, TipoNotificacao tipo, Long referenciaId);
}
