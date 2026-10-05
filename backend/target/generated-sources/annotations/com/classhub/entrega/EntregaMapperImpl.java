package com.classhub.entrega;

import com.classhub.entrega.dto.AnexoResponse;
import com.classhub.entrega.dto.EntregaResponse;
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
public class EntregaMapperImpl implements EntregaMapper {

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Override
    public EntregaResponse toResponse(Entrega entrega) {
        if ( entrega == null ) {
            return null;
        }

        Long id = null;
        UsuarioResponse autor = null;
        List<UsuarioResponse> participantes = null;
        String conteudo = null;
        String comentario = null;
        List<AnexoResponse> anexos = null;
        StatusEntrega status = null;
        Instant dataCriacao = null;
        Instant dataAtualizacao = null;

        id = entrega.getId();
        autor = usuarioMapper.toResponse( entrega.getAutor() );
        participantes = usuarioSetToUsuarioResponseList( entrega.getParticipantes() );
        conteudo = entrega.getConteudo();
        comentario = entrega.getComentario();
        anexos = entregaAnexoListToAnexoResponseList( entrega.getAnexos() );
        status = entrega.getStatus();
        dataCriacao = entrega.getDataCriacao();
        dataAtualizacao = entrega.getDataAtualizacao();

        Long tarefaId = entrega.getTarefa().getId();
        String tarefaTitulo = entrega.getTarefa().getTitulo();

        EntregaResponse entregaResponse = new EntregaResponse( id, tarefaId, tarefaTitulo, autor, participantes, conteudo, comentario, anexos, status, dataCriacao, dataAtualizacao );

        return entregaResponse;
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

    protected AnexoResponse entregaAnexoToAnexoResponse(EntregaAnexo entregaAnexo) {
        if ( entregaAnexo == null ) {
            return null;
        }

        Long id = null;
        String nomeArquivo = null;
        String url = null;

        id = entregaAnexo.getId();
        nomeArquivo = entregaAnexo.getNomeArquivo();
        url = entregaAnexo.getUrl();

        AnexoResponse anexoResponse = new AnexoResponse( id, nomeArquivo, url );

        return anexoResponse;
    }

    protected List<AnexoResponse> entregaAnexoListToAnexoResponseList(List<EntregaAnexo> list) {
        if ( list == null ) {
            return null;
        }

        List<AnexoResponse> list1 = new ArrayList<AnexoResponse>( list.size() );
        for ( EntregaAnexo entregaAnexo : list ) {
            list1.add( entregaAnexoToAnexoResponse( entregaAnexo ) );
        }

        return list1;
    }
}
