package com.classhub.notificacao;

import com.classhub.notificacao.dto.NotificacaoResponse;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-04T17:21:16-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4.1 (Oracle Corporation)"
)
@Component
public class NotificacaoMapperImpl implements NotificacaoMapper {

    @Override
    public NotificacaoResponse toResponse(Notificacao notificacao) {
        if ( notificacao == null ) {
            return null;
        }

        Long id = null;
        TipoNotificacao tipo = null;
        String mensagem = null;
        Instant criadoEm = null;

        id = notificacao.getId();
        tipo = notificacao.getTipo();
        mensagem = notificacao.getMensagem();
        criadoEm = notificacao.getCriadoEm();

        NotificacaoResponse notificacaoResponse = new NotificacaoResponse( id, tipo, mensagem, criadoEm );

        return notificacaoResponse;
    }
}
