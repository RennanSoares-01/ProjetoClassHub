package com.classhub.common.seed;

import com.classhub.entrega.Entrega;
import com.classhub.entrega.EntregaAnexo;
import com.classhub.entrega.EntregaRepository;
import com.classhub.entrega.StatusEntrega;
import com.classhub.feedback.Feedback;
import com.classhub.feedback.FeedbackRepository;
import com.classhub.notificacao.NotificacaoService;
import com.classhub.tarefa.Tarefa;
import com.classhub.tarefa.TarefaRepository;
import com.classhub.tarefa.TipoTarefa;
import com.classhub.turma.Turma;
import com.classhub.turma.TurmaRepository;
import com.classhub.usuario.Role;
import com.classhub.usuario.Usuario;
import com.classhub.usuario.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Popula dados mockados para a demonstração do MVP (ver .ai/architecture.md, ADR-007).
 * Só roda se a base estiver vazia.
 */
@Component
public class DataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UsuarioRepository usuarioRepository;
    private final TurmaRepository turmaRepository;
    private final TarefaRepository tarefaRepository;
    private final EntregaRepository entregaRepository;
    private final FeedbackRepository feedbackRepository;
    private final NotificacaoService notificacaoService;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UsuarioRepository usuarioRepository, TurmaRepository turmaRepository,
                       TarefaRepository tarefaRepository, EntregaRepository entregaRepository,
                       FeedbackRepository feedbackRepository, NotificacaoService notificacaoService,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.turmaRepository = turmaRepository;
        this.tarefaRepository = tarefaRepository;
        this.entregaRepository = entregaRepository;
        this.feedbackRepository = feedbackRepository;
        this.notificacaoService = notificacaoService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (usuarioRepository.count() > 0) {
            log.info("Dados já existentes — seed ignorado.");
            return;
        }

        Usuario admin = usuario("admin001", "Administradora Geral", "admin@classhub.edu", "admin123", Role.ADMIN);
        Usuario professor1 = usuario("prof001", "Maria Silva", "maria.silva@classhub.edu", "prof123", Role.PROFESSOR);
        Usuario professor2 = usuario("prof002", "João Souza", "joao.souza@classhub.edu", "prof123", Role.PROFESSOR);
        Usuario aluno1 = usuario("aluno001", "Ana Pereira", "ana.pereira@classhub.edu", "aluno123", Role.ALUNO);
        Usuario aluno2 = usuario("aluno002", "Bruno Costa", "bruno.costa@classhub.edu", "aluno123", Role.ALUNO);
        Usuario aluno3 = usuario("aluno003", "Carla Dias", "carla.dias@classhub.edu", "aluno123", Role.ALUNO);
        Usuario aluno4 = usuario("aluno004", "Diego Rocha", "diego.rocha@classhub.edu", "aluno123", Role.ALUNO);

        Turma turmaEngSoftware = new Turma("Engenharia de Software - 2026/2", professor1);
        turmaEngSoftware.getAlunos().addAll(List.of(aluno1, aluno2, aluno3));
        turmaRepository.save(turmaEngSoftware);

        Turma turmaBancoDados = new Turma("Banco de Dados - 2026/2", professor2);
        turmaBancoDados.getAlunos().addAll(List.of(aluno2, aluno3, aluno4));
        turmaRepository.save(turmaBancoDados);

        LocalDateTime agora = LocalDateTime.now();

        Tarefa tarefaGrupo = new Tarefa(turmaEngSoftware, "Trabalho Final - Arquitetura de Microsserviços",
                "Desenvolver um protótipo de arquitetura de microsserviços em grupo.", TipoTarefa.GRUPO, agora.plusDays(7));
        tarefaRepository.save(tarefaGrupo);

        Tarefa tarefaIndividualProxima = new Tarefa(turmaEngSoftware, "Exercício Individual - Padrões de Projeto",
                "Implementar três padrões de projeto estudados em aula.", TipoTarefa.INDIVIDUAL, agora.plusHours(36));
        tarefaRepository.save(tarefaIndividualProxima);

        Tarefa tarefaEncerrada = new Tarefa(turmaBancoDados, "Modelagem ER",
                "Modelar o diagrama entidade-relacionamento do estudo de caso.", TipoTarefa.INDIVIDUAL, agora.minusDays(1));
        tarefaRepository.save(tarefaEncerrada);

        Entrega entregaGrupo = new Entrega(tarefaGrupo, aluno1,
                "Repositório com o protótipo e documentação da arquitetura proposta.",
                "Ficamos com dúvida sobre o particionamento do serviço de pagamentos.");
        entregaGrupo.substituirAnexos(List.of(
                new EntregaAnexo(entregaGrupo, "arquitetura.pdf", "https://mock.classhub.local/arquivos/arquitetura.pdf")
        ));
        entregaGrupo.substituirParticipantes(java.util.Set.of(aluno2));
        entregaRepository.save(entregaGrupo);

        Feedback feedback = new Feedback(entregaGrupo, professor1, new java.math.BigDecimal("9.0"),
                "Ótimo trabalho, só revisar o particionamento do serviço de pagamentos.", StatusEntrega.APROVADA);
        feedbackRepository.save(feedback);
        entregaGrupo.aplicarResultadoAvaliacao(StatusEntrega.APROVADA);
        notificacaoService.notificarEntregaAvaliada(entregaGrupo);

        log.info("Seed concluído: {} usuários, {} turmas, {} tarefas, 1 entrega avaliada.",
                usuarioRepository.count(), turmaRepository.count(), tarefaRepository.count());
    }

    private Usuario usuario(String matricula, String nome, String email, String senha, Role role) {
        return usuarioRepository.save(new Usuario(matricula, nome, email, passwordEncoder.encode(senha), role));
    }
}
