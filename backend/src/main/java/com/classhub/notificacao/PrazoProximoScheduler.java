package com.classhub.notificacao;

import com.classhub.entrega.EntregaRepository;
import com.classhub.tarefa.Tarefa;
import com.classhub.tarefa.TarefaRepository;
import com.classhub.usuario.Usuario;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Gera a notificação de "prazo próximo" (ver .ai/business-rules.md, seção Notificações).
 * Roda periodicamente e verifica tarefas cuja data limite está nas próximas 48 horas.
 */
@Component
public class PrazoProximoScheduler {

    private static final long JANELA_HORAS = 48;

    private final TarefaRepository tarefaRepository;
    private final EntregaRepository entregaRepository;
    private final NotificacaoService notificacaoService;

    public PrazoProximoScheduler(TarefaRepository tarefaRepository, EntregaRepository entregaRepository,
                                  NotificacaoService notificacaoService) {
        this.tarefaRepository = tarefaRepository;
        this.entregaRepository = entregaRepository;
        this.notificacaoService = notificacaoService;
    }

    @Scheduled(fixedDelayString = "PT10M", initialDelayString = "PT1M")
    public void verificarPrazosProximos() {
        LocalDateTime agora = LocalDateTime.now();
        List<Tarefa> tarefasComPrazoProximo = tarefaRepository.findByDataLimiteBetween(agora, agora.plusHours(JANELA_HORAS));

        for (Tarefa tarefa : tarefasComPrazoProximo) {
            for (Usuario aluno : tarefa.getTurma().getAlunos()) {
                boolean jaEnviou = entregaRepository.findByTarefa_IdAndAutor_Id(tarefa.getId(), aluno.getId()).isPresent();
                if (!jaEnviou) {
                    notificacaoService.notificarPrazoProximo(tarefa, aluno);
                }
            }
        }
    }
}
