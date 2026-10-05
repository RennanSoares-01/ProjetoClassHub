package com.classhub.turma;

import com.classhub.turma.dto.TurmaResponse;
import com.classhub.usuario.Usuario;
import com.classhub.usuario.UsuarioMapper;
import com.classhub.usuario.dto.UsuarioResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-04T17:21:16-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4.1 (Oracle Corporation)"
)
@Component
public class TurmaMapperImpl implements TurmaMapper {

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Override
    public TurmaResponse toResponse(Turma turma) {
        if ( turma == null ) {
            return null;
        }

        Long id = null;
        String nome = null;
        UsuarioResponse professor = null;
        List<UsuarioResponse> alunos = null;
        Instant criadoEm = null;

        id = turma.getId();
        nome = turma.getNome();
        professor = usuarioMapper.toResponse( turma.getProfessor() );
        alunos = usuarioSetToUsuarioResponseList( turma.getAlunos() );
        criadoEm = turma.getCriadoEm();

        TurmaResponse turmaResponse = new TurmaResponse( id, nome, professor, alunos, criadoEm );

        return turmaResponse;
    }

    protected List<UsuarioResponse> usuarioSetToUsuarioResponseList(Set<Usuario> set) {
        if ( set == null ) {
            return null;
        }

        List<UsuarioResponse> list = new ArrayList<UsuarioResponse>( set.size() );
        for ( Usuario usuario : set ) {
            list.add( usuarioMapper.toResponse( usuario ) );
        }

        return list;
    }
}
