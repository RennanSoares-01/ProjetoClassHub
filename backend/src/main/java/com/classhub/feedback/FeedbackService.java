package com.classhub.feedback;

import com.classhub.common.exception.BadRequestException;
import com.classhub.common.exception.ForbiddenException;
import com.classhub.common.exception.NotFoundException;
import com.classhub.common.security.CurrentUser;
import com.classhub.common.security.UsuarioAutenticado;
import com.classhub.entrega.Entrega;
import com.classhub.entrega.EntregaRepository;
import com.classhub.entrega.StatusEntrega;
import com.classhub.feedback.dto.CriarFeedbackRequest;
import com.classhub.feedback.dto.FeedbackResponse;
import com.classhub.notificacao.NotificacaoService;
import com.classhub.usuario.Role;
import com.classhub.usuario.Usuario;
import com.classhub.usuario.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final EntregaRepository entregaRepository;
    private final UsuarioService usuarioService;
    private final NotificacaoService notificacaoService;
    private final FeedbackMapper feedbackMapper;

    public FeedbackService(FeedbackRepository feedbackRepository, EntregaRepository entregaRepository,
                            UsuarioService usuarioService, NotificacaoService notificacaoService,
                            FeedbackMapper feedbackMapper) {
        this.feedbackRepository = feedbackRepository;
        this.entregaRepository = entregaRepository;
        this.usuarioService = usuarioService;
        this.notificacaoService = notificacaoService;
        this.feedbackMapper = feedbackMapper;
    }

    @Transactional
    public FeedbackResponse criar(CriarFeedbackRequest request) {
        CurrentUser.exigirRole(Role.PROFESSOR, Role.ADMIN);
        if (request.status() == StatusEntrega.PENDENTE) {
            throw new BadRequestException("O status do feedback deve ser APROVADA ou REPROVADA.");
        }

        Entrega entrega = entregaRepository.findById(request.entregaId())
                .orElseThrow(() -> new NotFoundException("Entrega não encontrada."));
        exigirProfessorDonoOuAdmin(entrega);

        UsuarioAutenticado atual = CurrentUser.get();
        Usuario professor = usuarioService.buscarEntidade(atual.id());

        Feedback feedback = feedbackRepository.findByEntrega_Id(entrega.getId())
                .map(existente -> {
                    existente.atualizar(professor, request.nota(), request.comentario(), request.status());
                    return existente;
                })
                .orElseGet(() -> new Feedback(entrega, professor, request.nota(), request.comentario(), request.status()));

        feedback = feedbackRepository.save(feedback);
        entrega.aplicarResultadoAvaliacao(request.status());

        notificacaoService.notificarEntregaAvaliada(entrega);

        return feedbackMapper.toResponse(feedback);
    }

    @Transactional(readOnly = true)
    public FeedbackResponse buscarPorEntrega(Long entregaId) {
        Entrega entrega = entregaRepository.findById(entregaId)
                .orElseThrow(() -> new NotFoundException("Entrega não encontrada."));
        exigirAcessoEntrega(entrega);

        Feedback feedback = feedbackRepository.findByEntrega_Id(entregaId)
                .orElseThrow(() -> new NotFoundException("Esta entrega ainda não possui feedback."));
        return feedbackMapper.toResponse(feedback);
    }

    private void exigirProfessorDonoOuAdmin(Entrega entrega) {
        UsuarioAutenticado atual = CurrentUser.get();
        boolean autorizado = atual.role() == Role.ADMIN
                || entrega.getTarefa().getTurma().getProfessor().getId().equals(atual.id());
        if (!autorizado) {
            throw new ForbiddenException("Somente o professor dono da turma ou um administrador podem avaliar esta entrega.");
        }
    }

    private void exigirAcessoEntrega(Entrega entrega) {
        UsuarioAutenticado atual = CurrentUser.get();
        boolean podeVer = atual.role() == Role.ADMIN
                || entrega.getAutor().getId().equals(atual.id())
                || entrega.getParticipantes().stream().anyMatch(p -> p.getId().equals(atual.id()))
                || entrega.getTarefa().getTurma().getProfessor().getId().equals(atual.id());
        if (!podeVer) {
            throw new ForbiddenException("Usuário não tem acesso a este feedback.");
        }
    }
}
