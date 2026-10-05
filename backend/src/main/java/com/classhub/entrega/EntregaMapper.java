package com.classhub.entrega;

import com.classhub.entrega.dto.EntregaResponse;
import com.classhub.usuario.UsuarioMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UsuarioMapper.class)
public interface EntregaMapper {

    @Mapping(target = "tarefaId", expression = "java(entrega.getTarefa().getId())")
    @Mapping(target = "tarefaTitulo", expression = "java(entrega.getTarefa().getTitulo())")
    EntregaResponse toResponse(Entrega entrega);
}
