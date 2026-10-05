package com.classhub.entrega;

import com.classhub.common.exception.BadRequestException;
import com.classhub.common.exception.ForbiddenException;
import com.classhub.common.exception.NotFoundException;
import com.classhub.common.security.CurrentUser;
import com.classhub.common.security.UsuarioAutenticado;
import com.classhub.entrega.dto.AnexoRequest;
import com.classhub.entrega.dto.AtualizarEntregaRequest;
import com.classhub.entrega.dto.EntregaResponse;
import com.classhub.entrega.dto.EnviarEntregaRequest;
import com.classhub.tarefa.Tarefa;
import com.classhub.tarefa.TarefaRepository;
import com.classhub.tarefa.TipoTarefa;
import com.classhub.turma.Turma;
import com.classhub.usuario.Role;
import com.classhub.usuario.Usuario;
import com.classhub.usuario.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class EntregaService {

    private final EntregaRepository entregaRepository;
    private final TarefaRepository tarefaRepository;
    private final UsuarioService usuarioService;
    private final EntregaMapper entregaMapper;

    public EntregaService(EntregaRepository entregaRepository, TarefaRepository tarefaRepository,
                           UsuarioService usuarioService, EntregaMapper entregaMapper) {
        this.entregaRepository = entregaRepository;
        this.tarefaRepository = tarefaRepository;
        this.usuarioService = usuarioService;
        this.entregaMapper = entregaMapper;
    }

    @Transactional
    public EntregaResponse enviar(EnviarEntregaRequest request) {
        CurrentUser.exigirRole(Role.ALUNO);
        UsuarioAutenticado atual = CurrentUser.get();
        Tarefa tarefa = buscarTarefa(request.tarefaId());
        Turma turma = tarefa.getTurma();

        exigirAlunoMatriculado(turma, atual.id());
        exigirTarefaAberta(tarefa);
        validarParticipantesPermitidos(tarefa, request.participantesMatriculas());

        Usuario autor = usuarioService.buscarEntidade(atual.id());
        Set<Usuario> participantes = resolverParticipantes(turma, autor, request.participantesMatriculas());

        Entrega entrega = entregaRepository.findByTarefa_IdAndAutor_Id(tarefa.getId(), atual.id())
                .orElseGet(() -> new Entrega(tarefa, autor, request.conteudo(), request.comentario()));

        if (entrega.getId() != null) {
            entrega.sobrescrever(request.conteudo(), request.comentario());
        }
        entrega.substituirAnexos(criarAnexos(entrega, request.anexos()));
        entrega.substituirParticipantes(participantes);

        return entregaMapper.toResponse(entregaRepository.save(entrega));
    }

    @Transactional
    public EntregaResponse atualizar(Long id, AtualizarEntregaRequest request) {
        Entrega entrega = buscarEntidade(id);
        UsuarioAutenticado atual = CurrentUser.get();
        if (!entrega.getAutor().getId().equals(atual.id())) {
            throw new ForbiddenException("Somente o autor da entrega pode atualizá-la.");
        }
        Tarefa tarefa = entrega.getTarefa();
        exigirTarefaAberta(tarefa);
        validarParticipantesPermitidos(tarefa, request.participantesMatriculas());

        Set<Usuario> participantes = resolverParticipantes(tarefa.getTurma(), entrega.getAutor(), request.participantesMatriculas());

        entrega.sobrescrever(request.conteudo(), request.comentario());
        entrega.substituirAnexos(criarAnexos(entrega, request.anexos()));
        entrega.substituirParticipantes(participantes);

        return entregaMapper.toResponse(entrega);
    }

    @Transactional(readOnly = true)
    public EntregaResponse buscarPorId(Long id) {
        return entregaMapper.toResponse(buscarComAcesso(id));
    }

    @Transactional(readOnly = true)
    public List<EntregaResponse> listarPorTarefa(Long tarefaId) {
        Tarefa tarefa = buscarTarefa(tarefaId);
        exigirProfessorDonoOuAdmin(tarefa.getTurma());
        return entregaRepository.findByTarefa_Id(tarefaId).stream().map(entregaMapper::toResponse).toList();
    }

    Entrega buscarEntidade(Long id) {
        return entregaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Entrega não encontrada."));
    }

    Entrega buscarComAcesso(Long id) {
        Entrega entrega = buscarEntidade(id);
        UsuarioAutenticado atual = CurrentUser.get();
        boolean podeVer = atual.role() == Role.ADMIN
                || entrega.getAutor().getId().equals(atual.id())
                || entrega.getParticipantes().stream().anyMatch(p -> p.getId().equals(atual.id()))
                || entrega.getTarefa().getTurma().getProfessor().getId().equals(atual.id());
        if (!podeVer) {
            throw new ForbiddenException("Usuário não tem acesso a esta entrega.");
        }
        return entrega;
    }

    private Tarefa buscarTarefa(Long tarefaId) {
        return tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new NotFoundException("Tarefa não encontrada."));
    }

    private void exigirAlunoMatriculado(Turma turma, Long alunoId) {
        boolean matriculado = turma.getAlunos().stream().anyMatch(a -> a.getId().equals(alunoId));
        if (!matriculado) {
            throw new ForbiddenException("Aluno não está matriculado na turma desta tarefa.");
        }
    }

    private void exigirTarefaAberta(Tarefa tarefa) {
        if (!tarefa.estaAberta()) {
            throw new BadRequestException("O prazo desta tarefa já foi encerrado.");
        }
    }

    private void exigirProfessorDonoOuAdmin(Turma turma) {
        UsuarioAutenticado atual = CurrentUser.get();
        boolean autorizado = atual.role() == Role.ADMIN
                || (atual.role() == Role.PROFESSOR && turma.getProfessor().getId().equals(atual.id()));
        if (!autorizado) {
            throw new ForbiddenException("Somente o professor dono da turma ou um administrador podem ver estas entregas.");
        }
    }

    private void validarParticipantesPermitidos(Tarefa tarefa, List<String> participantesMatriculas) {
        boolean temParticipantes = participantesMatriculas != null && !participantesMatriculas.isEmpty();
        if (tarefa.getTipo() == TipoTarefa.INDIVIDUAL && temParticipantes) {
            throw new BadRequestException("Tarefas individuais não aceitam participantes adicionais.");
        }
    }

    private Set<Usuario> resolverParticipantes(Turma turma, Usuario autor, List<String> participantesMatriculas) {
        Set<Usuario> participantes = new LinkedHashSet<>();
        if (participantesMatriculas == null) {
            return participantes;
        }
        for (String matricula : participantesMatriculas) {
            Usuario participante = usuarioService.buscarPorMatricula(matricula);
            if (participante.getId().equals(autor.getId())) {
                continue;
            }
            if (participante.getRole() != Role.ALUNO) {
                throw new BadRequestException("Participante '" + matricula + "' não é um aluno.");
            }
            boolean daTurma = turma.getAlunos().stream().anyMatch(a -> a.getId().equals(participante.getId()));
            if (!daTurma) {
                throw new BadRequestException("Participante '" + matricula + "' não está matriculado nesta turma.");
            }
            participantes.add(participante);
        }
        return participantes;
    }

    private List<EntregaAnexo> criarAnexos(Entrega entrega, List<AnexoRequest> anexos) {
        if (anexos == null) {
            return List.of();
        }
        return anexos.stream().map(a -> new EntregaAnexo(entrega, a.nomeArquivo(), a.url())).toList();
    }
}
