package com.classhub.notificacao;

import com.classhub.common.security.CurrentUser;
import com.classhub.common.security.UsuarioAutenticado;
import com.classhub.entrega.Entrega;
import com.classhub.notificacao.dto.NotificacaoResponse;
import com.classhub.tarefa.Tarefa;
import com.classhub.usuario.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final NotificacaoMapper notificacaoMapper;
    private final EmailSimuladoService emailSimuladoService;

    public NotificacaoService(NotificacaoRepository notificacaoRepository, NotificacaoMapper notificacaoMapper,
                               EmailSimuladoService emailSimuladoService) {
        this.notificacaoRepository = notificacaoRepository;
        this.notificacaoMapper = notificacaoMapper;
        this.emailSimuladoService = emailSimuladoService;
    }

    public List<NotificacaoResponse> listarMinhas() {
        UsuarioAutenticado atual = CurrentUser.get();
        return notificacaoRepository.findByDestinatario_IdOrderByCriadoEmDesc(atual.id()).stream()
                .map(notificacaoMapper::toResponse)
                .toList();
    }

    @Transactional
    public void notificarEntregaAvaliada(Entrega entrega) {
        Set<Usuario> destinatarios = new LinkedHashSet<>(entrega.getParticipantes());
        destinatarios.add(entrega.getAutor());

        String mensagem = "Sua entrega da tarefa '" + entrega.getTarefa().getTitulo() + "' foi avaliada: " + entrega.getStatus() + ".";
        for (Usuario destinatario : destinatarios) {
            notificar(destinatario, TipoNotificacao.ENTREGA_AVALIADA, entrega.getId(), mensagem);
        }
    }

    @Transactional
    public void notificarPrazoProximo(Tarefa tarefa, Usuario aluno) {
        boolean jaNotificado = notificacaoRepository.existsByDestinatario_IdAndTipoAndReferenciaId(
                aluno.getId(), TipoNotificacao.PRAZO_PROXIMO, tarefa.getId());
        if (jaNotificado) {
            return;
        }
        String mensagem = "O prazo da tarefa '" + tarefa.getTitulo() + "' está próximo do fim (" + tarefa.getDataLimite() + ").";
        notificar(aluno, TipoNotificacao.PRAZO_PROXIMO, tarefa.getId(), mensagem);
    }

    private void notificar(Usuario destinatario, TipoNotificacao tipo, Long referenciaId, String mensagem) {
        notificacaoRepository.save(new Notificacao(destinatario, tipo, referenciaId, mensagem));
        emailSimuladoService.enviar(destinatario.getEmail(), "Class Hub - " + tipo, mensagem);
    }
}
