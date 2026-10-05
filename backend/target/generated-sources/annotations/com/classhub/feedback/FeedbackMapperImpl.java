package com.classhub.feedback;

import com.classhub.entrega.StatusEntrega;
import com.classhub.feedback.dto.FeedbackResponse;
import com.classhub.usuario.UsuarioMapper;
import com.classhub.usuario.dto.UsuarioResponse;
import java.math.BigDecimal;
import java.time.Instant;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-04T17:21:16-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25.0.4.1 (Oracle Corporation)"
)
@Component
public class FeedbackMapperImpl implements FeedbackMapper {

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Override
    public FeedbackResponse toResponse(Feedback feedback) {
        if ( feedback == null ) {
            return null;
        }

        Long id = null;
        BigDecimal nota = null;
        String comentario = null;
        StatusEntrega status = null;
        UsuarioResponse professor = null;
        Instant criadoEm = null;

        id = feedback.getId();
        nota = feedback.getNota();
        comentario = feedback.getComentario();
        status = feedback.getStatus();
        professor = usuarioMapper.toResponse( feedback.getProfessor() );
        criadoEm = feedback.getCriadoEm();

        Long entregaId = feedback.getEntrega().getId();

        FeedbackResponse feedbackResponse = new FeedbackResponse( id, entregaId, nota, comentario, status, professor, criadoEm );

        return feedbackResponse;
    }
}
