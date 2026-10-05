package com.classhub.tarefa;

import com.classhub.common.exception.ForbiddenException;
import com.classhub.common.exception.NotFoundException;
import com.classhub.common.security.CurrentUser;
import com.classhub.common.security.UsuarioAutenticado;
import com.classhub.tarefa.dto.AtualizarTarefaRequest;
import com.classhub.tarefa.dto.CriarTarefaRequest;
import com.classhub.tarefa.dto.TarefaResponse;
import com.classhub.turma.Turma;
import com.classhub.turma.TurmaRepository;
import com.classhub.usuario.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final TurmaRepository turmaRepository;
    private final TarefaMapper tarefaMapper;

    public TarefaService(TarefaRepository tarefaRepository, TurmaRepository turmaRepository, TarefaMapper tarefaMapper) {
        this.tarefaRepository = tarefaRepository;
        this.turmaRepository = turmaRepository;
        this.tarefaMapper = tarefaMapper;
    }

    @Transactional
    public TarefaResponse criar(CriarTarefaRequest request) {
        CurrentUser.exigirRole(Role.PROFESSOR);
        Turma turma = buscarTurma(request.turmaId());
        exigirDonoDaTurma(turma);
        Tarefa tarefa = new Tarefa(turma, request.titulo(), request.descricao(), request.tipo(), request.dataLimite());
        return tarefaMapper.toResponse(tarefaRepository.save(tarefa));
    }

    @Transactional(readOnly = true)
    public List<TarefaResponse> listar(Long turmaIdFiltro) {
        UsuarioAutenticado atual = CurrentUser.get();
        List<Tarefa> tarefas = switch (atual.role()) {
            case ADMIN -> turmaIdFiltro != null ? tarefaRepository.findByTurma_Id(turmaIdFiltro) : tarefaRepository.findAll();
            case PROFESSOR -> {
                List<Tarefa> doProfessor = tarefaRepository.findByTurma_Professor_Id(atual.id());
                yield turmaIdFiltro != null
                        ? doProfessor.stream().filter(t -> t.getTurma().getId().equals(turmaIdFiltro)).toList()
                        : doProfessor;
            }
            case ALUNO -> {
                List<Long> turmaIds = turmaRepository.findByAlunos_Id(atual.id()).stream().map(Turma::getId).toList();
                List<Tarefa> doAluno = tarefaRepository.findByTurma_IdIn(turmaIds);
                yield turmaIdFiltro != null
                        ? doAluno.stream().filter(t -> t.getTurma().getId().equals(turmaIdFiltro)).toList()
                        : doAluno;
            }
        };
        return tarefas.stream().map(tarefaMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TarefaResponse buscarPorId(Long id) {
        return tarefaMapper.toResponse(buscarComAcesso(id));
    }

    @Transactional
    public TarefaResponse atualizar(Long id, AtualizarTarefaRequest request) {
        Tarefa tarefa = buscarEntidade(id);
        exigirDonoDaTurma(tarefa.getTurma());
        if (request.titulo() != null && !request.titulo().isBlank()) {
            tarefa.setTitulo(request.titulo());
        }
        if (request.descricao() != null) {
            tarefa.setDescricao(request.descricao());
        }
        if (request.tipo() != null) {
            tarefa.setTipo(request.tipo());
        }
        if (request.dataLimite() != null) {
            tarefa.setDataLimite(request.dataLimite());
        }
        return tarefaMapper.toResponse(tarefa);
    }

    @Transactional
    public void excluir(Long id) {
        Tarefa tarefa = buscarEntidade(id);
        exigirDonoDaTurma(tarefa.getTurma());
        tarefaRepository.delete(tarefa);
    }

    Tarefa buscarEntidade(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tarefa não encontrada."));
    }

    /** Valida que o usuário atual pode visualizar a tarefa (aluno matriculado, professor dono ou admin). */
    Tarefa buscarComAcesso(Long id) {
        Tarefa tarefa = buscarEntidade(id);
        UsuarioAutenticado atual = CurrentUser.get();
        Turma turma = tarefa.getTurma();
        boolean podeVer = switch (atual.role()) {
            case ADMIN -> true;
            case PROFESSOR -> turma.getProfessor().getId().equals(atual.id());
            case ALUNO -> turma.getAlunos().stream().anyMatch(a -> a.getId().equals(atual.id()));
        };
        if (!podeVer) {
            throw new ForbiddenException("Usuário não tem acesso a esta tarefa.");
        }
        return tarefa;
    }

    void exigirAcessoProfessorDaTarefa(Tarefa tarefa) {
        exigirDonoDaTurma(tarefa.getTurma());
    }

    private Turma buscarTurma(Long turmaId) {
        return turmaRepository.findById(turmaId)
                .orElseThrow(() -> new NotFoundException("Turma não encontrada."));
    }

    private void exigirDonoDaTurma(Turma turma) {
        UsuarioAutenticado atual = CurrentUser.get();
        boolean autorizado = atual.role() == Role.ADMIN
                || (atual.role() == Role.PROFESSOR && turma.getProfessor().getId().equals(atual.id()));
        if (!autorizado) {
            throw new ForbiddenException("Somente o professor dono da turma ou um administrador podem realizar esta ação.");
        }
    }
}
