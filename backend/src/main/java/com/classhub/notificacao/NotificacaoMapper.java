package com.classhub.notificacao;

import com.classhub.notificacao.dto.NotificacaoResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificacaoMapper {

    NotificacaoResponse toResponse(Notificacao notificacao);
}
