package com.classhub.turma;

import com.classhub.common.exception.BadRequestException;
import com.classhub.common.exception.ForbiddenException;
import com.classhub.common.exception.NotFoundException;
import com.classhub.common.security.CurrentUser;
import com.classhub.common.security.UsuarioAutenticado;
import com.classhub.turma.dto.AtualizarTurmaRequest;
import com.classhub.turma.dto.CriarTurmaRequest;
import com.classhub.turma.dto.TurmaResponse;
import com.classhub.usuario.Role;
import com.classhub.usuario.Usuario;
import com.classhub.usuario.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final TurmaMapper turmaMapper;
    private final UsuarioService usuarioService;

    public TurmaService(TurmaRepository turmaRepository, TurmaMapper turmaMapper, UsuarioService usuarioService) {
        this.turmaRepository = turmaRepository;
        this.turmaMapper = turmaMapper;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public TurmaResponse criar(CriarTurmaRequest request) {
        CurrentUser.exigirRole(Role.PROFESSOR);
        UsuarioAutenticado atual = CurrentUser.get();
        Usuario professor = usuarioService.buscarEntidade(atual.id());
        Turma turma = new Turma(request.nome(), professor);
        return turmaMapper.toResponse(turmaRepository.save(turma));
    }

    @Transactional(readOnly = true)
    public List<TurmaResponse> listar() {
        UsuarioAutenticado atual = CurrentUser.get();
        List<Turma> turmas = switch (atual.role()) {
            case ADMIN -> turmaRepository.findAll();
            case PROFESSOR -> turmaRepository.findByProfessor_Id(atual.id());
            case ALUNO -> turmaRepository.findByAlunos_Id(atual.id());
        };
        return turmas.stream().map(turmaMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TurmaResponse buscarPorId(Long id) {
        return turmaMapper.toResponse(buscarComAcesso(id));
    }

    @Transactional
    public TurmaResponse atualizar(Long id, AtualizarTurmaRequest request) {
        Turma turma = buscarEntidade(id);
        exigirDonoOuAdmin(turma);
        turma.setNome(request.nome());
        return turmaMapper.toResponse(turma);
    }

    @Transactional
    public void excluir(Long id) {
        Turma turma = buscarEntidade(id);
        exigirDonoOuAdmin(turma);
        turmaRepository.delete(turma);
    }

    @Transactional
    public TurmaResponse matricularAluno(Long turmaId, String matricula) {
        CurrentUser.exigirRole(Role.ADMIN);
        Turma turma = buscarEntidade(turmaId);
        Usuario aluno = usuarioService.buscarPorMatricula(matricula);
        if (aluno.getRole() != Role.ALUNO) {
            throw new BadRequestException("Somente usuários com role ALUNO podem ser matriculados em uma turma.");
        }
        turma.getAlunos().add(aluno);
        return turmaMapper.toResponse(turma);
    }

    @Transactional
    public TurmaResponse removerAluno(Long turmaId, String matricula) {
        CurrentUser.exigirRole(Role.ADMIN);
        Turma turma = buscarEntidade(turmaId);
        Usuario aluno = usuarioService.buscarPorMatricula(matricula);
        turma.getAlunos().remove(aluno);
        return turmaMapper.toResponse(turma);
    }

    Turma buscarEntidade(Long id) {
        return turmaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Turma não encontrada."));
    }

    /** Valida que o usuário atual pode visualizar a turma (dono, aluno matriculado ou admin). */
    Turma buscarComAcesso(Long id) {
        Turma turma = buscarEntidade(id);
        UsuarioAutenticado atual = CurrentUser.get();
        boolean podeVer = switch (atual.role()) {
            case ADMIN -> true;
            case PROFESSOR -> turma.getProfessor().getId().equals(atual.id());
            case ALUNO -> turma.getAlunos().stream().anyMatch(a -> a.getId().equals(atual.id()));
        };
        if (!podeVer) {
            throw new ForbiddenException("Usuário não tem acesso a esta turma.");
        }
        return turma;
    }

    private void exigirDonoOuAdmin(Turma turma) {
        UsuarioAutenticado atual = CurrentUser.get();
        boolean autorizado = atual.role() == Role.ADMIN
                || (atual.role() == Role.PROFESSOR && turma.getProfessor().getId().equals(atual.id()));
        if (!autorizado) {
            throw new ForbiddenException("Somente o professor dono da turma ou um administrador podem realizar esta ação.");
        }
    }
}
