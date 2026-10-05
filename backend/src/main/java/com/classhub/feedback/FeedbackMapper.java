package com.classhub.feedback;

import com.classhub.feedback.dto.FeedbackResponse;
import com.classhub.usuario.UsuarioMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UsuarioMapper.class)
public interface FeedbackMapper {

    @Mapping(target = "entregaId", expression = "java(feedback.getEntrega().getId())")
    FeedbackResponse toResponse(Feedback feedback);
}
