package com.classhub.usuario;

import com.classhub.usuario.dto.UsuarioResponse;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-04T17:21:16-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4.1 (Oracle Corporation)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioResponse toResponse(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        Long id = null;
        String matricula = null;
        String nome = null;
        String email = null;
        Role role = null;

        id = usuario.getId();
        matricula = usuario.getMatricula();
        nome = usuario.getNome();
        email = usuario.getEmail();
        role = usuario.getRole();

        UsuarioResponse usuarioResponse = new UsuarioResponse( id, matricula, nome, email, role );

        return usuarioResponse;
    }
}
