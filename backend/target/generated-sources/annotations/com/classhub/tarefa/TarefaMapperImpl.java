package com.classhub.tarefa;

import com.classhub.tarefa.dto.TarefaResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-04T17:21:16-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4.1 (Oracle Corporation)"
)
@Component
public class TarefaMapperImpl implements TarefaMapper {

    @Override
    public TarefaResponse toResponse(Tarefa tarefa) {
        if ( tarefa == null ) {
            return null;
        }

        Long id = null;
        String titulo = null;
        String descricao = null;
        TipoTarefa tipo = null;
        LocalDateTime dataLimite = null;
        Instant criadoEm = null;

        id = tarefa.getId();
        titulo = tarefa.getTitulo();
        descricao = tarefa.getDescricao();
        tipo = tarefa.getTipo();
        dataLimite = tarefa.getDataLimite();
        criadoEm = tarefa.getCriadoEm();

        Long turmaId = tarefa.getTurma().getId();
        String turmaNome = tarefa.getTurma().getNome();
        boolean aberta = tarefa.estaAberta();

        TarefaResponse tarefaResponse = new TarefaResponse( id, turmaId, turmaNome, titulo, descricao, tipo, dataLimite, aberta, criadoEm );

        return tarefaResponse;
    }
}
