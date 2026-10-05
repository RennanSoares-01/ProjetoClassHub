package com.classhub.dashboard;

import com.classhub.common.security.CurrentUser;
import com.classhub.common.security.UsuarioAutenticado;
import com.classhub.dashboard.dto.*;
import com.classhub.entrega.Entrega;
import com.classhub.entrega.EntregaRepository;
import com.classhub.entrega.StatusEntrega;
import com.classhub.feedback.Feedback;
import com.classhub.feedback.FeedbackRepository;
import com.classhub.tarefa.Tarefa;
import com.classhub.tarefa.TarefaRepository;
import com.classhub.turma.Turma;
import com.classhub.turma.TurmaRepository;
import com.classhub.usuario.Role;
import com.classhub.usuario.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class DashboardService {

    private final TurmaRepository turmaRepository;
    private final TarefaRepository tarefaRepository;
    private final EntregaRepository entregaRepository;
    private final FeedbackRepository feedbackRepository;
    private final UsuarioRepository usuarioRepository;

    public DashboardService(TurmaRepository turmaRepository, TarefaRepository tarefaRepository,
                             EntregaRepository entregaRepository, FeedbackRepository feedbackRepository,
                             UsuarioRepository usuarioRepository) {
        this.turmaRepository = turmaRepository;
        this.tarefaRepository = tarefaRepository;
        this.entregaRepository = entregaRepository;
        this.feedbackRepository = feedbackRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public DashboardAlunoResponse aluno() {
        CurrentUser.exigirRole(Role.ALUNO);
        UsuarioAutenticado atual = CurrentUser.get();

        List<Turma> turmas = turmaRepository.findByAlunos_Id(atual.id());
        List<Long> turmaIds = turmas.stream().map(Turma::getId).toList();
        List<Tarefa> tarefas = turmaIds.isEmpty() ? List.of() : tarefaRepository.findByTurma_IdIn(turmaIds);

        List<Entrega> entregas = entregaRepository.findByAutor_Id(atual.id());
        Map<Long, Entrega> entregaPorTarefa = entregas.stream()
                .collect(java.util.stream.Collectors.toMap(e -> e.getTarefa().getId(), Function.identity()));

        List<TarefaResumo> proximas = tarefas.stream()
                .filter(t -> t.estaAberta() && !entregaPorTarefa.containsKey(t.getId()))
                .map(this::toTarefaResumo)
                .toList();

        List<TarefaResumo> entregues = tarefas.stream()
                .filter(t -> entregaPorTarefa.containsKey(t.getId()))
                .map(this::toTarefaResumo)
                .toList();

        List<TarefaResumo> atrasadas = tarefas.stream()
                .filter(t -> !t.estaAberta() && !entregaPorTarefa.containsKey(t.getId()))
                .map(this::toTarefaResumo)
                .toList();

        List<NotaResumo> notas = entregas.stream()
                .filter(e -> e.getStatus() != StatusEntrega.PENDENTE)
                .map(e -> {
                    java.math.BigDecimal nota = feedbackRepository.findByEntrega_Id(e.getId()).map(Feedback::getNota).orElse(null);
                    return new NotaResumo(e.getTarefa().getId(), e.getTarefa().getTitulo(), nota, e.getStatus().name());
                })
                .toList();

        return new DashboardAlunoResponse(
                turmas.stream().map(t -> new TurmaResumo(t.getId(), t.getNome())).toList(),
                proximas, entregues, atrasadas, notas
        );
    }

    @Transactional(readOnly = true)
    public DashboardProfessorResponse professor() {
        CurrentUser.exigirRole(Role.PROFESSOR);
        UsuarioAutenticado atual = CurrentUser.get();
        LocalDateTime agora = LocalDateTime.now();

        List<TurmaProfessorResumo> turmas = turmaRepository.findByProfessor_Id(atual.id()).stream()
                .map(t -> new TurmaProfessorResumo(t.getId(), t.getNome(), t.getAlunos().size()))
                .toList();

        long abertas = tarefaRepository.countByTurma_Professor_IdAndDataLimiteAfter(atual.id(), agora);
        long encerradas = tarefaRepository.countByTurma_Professor_IdAndDataLimiteBefore(atual.id(), agora);
        long pendentes = entregaRepository.countByTarefa_Turma_Professor_IdAndStatus(atual.id(), StatusEntrega.PENDENTE);

        return new DashboardProfessorResponse(turmas, abertas, encerradas, pendentes);
    }

    @Transactional(readOnly = true)
    public DashboardAdminResponse admin() {
        CurrentUser.exigirRole(Role.ADMIN);
        return new DashboardAdminResponse(usuarioRepository.count(), turmaRepository.count(), entregaRepository.count());
    }

    private TarefaResumo toTarefaResumo(Tarefa tarefa) {
        return new TarefaResumo(tarefa.getId(), tarefa.getTitulo(), tarefa.getTurma().getNome(), tarefa.getDataLimite());
    }
}
