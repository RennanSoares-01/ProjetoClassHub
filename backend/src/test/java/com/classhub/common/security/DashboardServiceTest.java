package com.classhub.common.security;

import com.classhub.dashboard.DashboardService;
import com.classhub.dashboard.dto.DashboardAlunoResponse;
import com.classhub.entrega.Entrega;
import com.classhub.entrega.EntregaRepository;
import com.classhub.entrega.StatusEntrega;
import com.classhub.feedback.Feedback;
import com.classhub.feedback.FeedbackRepository;
import com.classhub.tarefa.Tarefa;
import com.classhub.tarefa.TarefaRepository;
import com.classhub.tarefa.TipoTarefa;
import com.classhub.turma.Turma;
import com.classhub.turma.TurmaRepository;
import com.classhub.usuario.Role;
import com.classhub.usuario.Usuario;
import com.classhub.usuario.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DashboardServiceTest {

    private TurmaRepository turmaRepository;
    private TarefaRepository tarefaRepository;
    private EntregaRepository entregaRepository;
    private FeedbackRepository feedbackRepository;
    private UsuarioRepository usuarioRepository;
    private CurrentUser currentUser;
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        turmaRepository = mock(TurmaRepository.class);
        tarefaRepository = mock(TarefaRepository.class);
        entregaRepository = mock(EntregaRepository.class);
        feedbackRepository = mock(FeedbackRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        currentUser = new CurrentUser();
        dashboardService = new DashboardService(turmaRepository, tarefaRepository, entregaRepository, feedbackRepository, usuarioRepository, currentUser);
    }

    @Test
    void alunoRetornaNotasRecebidasSemConsultaPorEntregaEmLoop() {
        Usuario aluno = new Usuario("aluno001", "Ana", "ana@classhub.edu", "hash", Role.ALUNO);
        ReflectionTestUtils.setField(aluno, "id", 10L);
        Usuario professor = new Usuario("prof001", "Maria", "maria@classhub.edu", "hash", Role.PROFESSOR);
        ReflectionTestUtils.setField(professor, "id", 20L);
        currentUser.set(new UsuarioAutenticado(aluno.getId(), aluno.getMatricula(), aluno.getNome(), aluno.getRole()));

        Turma turma = new Turma("Engenharia de Software", professor);
        ReflectionTestUtils.setField(turma, "id", 1L);

        Tarefa tarefa = new Tarefa(turma, "Atividade final", "Descreva a solução", TipoTarefa.INDIVIDUAL, LocalDateTime.now().plusDays(1));
        ReflectionTestUtils.setField(tarefa, "id", 7L);

        Entrega entrega = new Entrega(tarefa, aluno, "Conteúdo enviado", "Comentário");
        ReflectionTestUtils.setField(entrega, "id", 15L);
        ReflectionTestUtils.setField(entrega, "status", StatusEntrega.APROVADA);

        Feedback feedback = new Feedback(entrega, professor, new BigDecimal("8.50"), "Bom trabalho", StatusEntrega.APROVADA);

        when(turmaRepository.findByAlunos_Id(aluno.getId())).thenReturn(List.of(turma));
        when(tarefaRepository.findByTurma_IdIn(List.of(1L))).thenReturn(List.of(tarefa));
        when(entregaRepository.findByAutor_Id(aluno.getId())).thenReturn(List.of(entrega));
        when(feedbackRepository.findByEntrega_IdIn(List.of(15L))).thenReturn(List.of(feedback));

        DashboardAlunoResponse response = dashboardService.aluno();

        assertEquals(1, response.notasRecebidas().size());
        assertEquals(8.50, response.notasRecebidas().getFirst().nota().doubleValue());
        assertEquals("Atividade final", response.notasRecebidas().getFirst().tarefaTitulo());
    }
}
