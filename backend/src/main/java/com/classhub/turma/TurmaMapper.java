package com.classhub.turma;

import com.classhub.turma.dto.TurmaResponse;
import com.classhub.usuario.UsuarioMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UsuarioMapper.class)
public interface TurmaMapper {

    TurmaResponse toResponse(Turma turma);
}
