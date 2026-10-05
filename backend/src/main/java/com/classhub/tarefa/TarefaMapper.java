package com.classhub.tarefa;

import com.classhub.tarefa.dto.TarefaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TarefaMapper {

    @Mapping(target = "turmaId", expression = "java(tarefa.getTurma().getId())")
    @Mapping(target = "turmaNome", expression = "java(tarefa.getTurma().getNome())")
    @Mapping(target = "aberta", expression = "java(tarefa.estaAberta())")
    TarefaResponse toResponse(Tarefa tarefa);
}
