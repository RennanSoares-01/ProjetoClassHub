package com.classhub.turma;

import com.classhub.turma.dto.AtualizarTurmaRequest;
import com.classhub.turma.dto.CriarTurmaRequest;
import com.classhub.turma.dto.TurmaResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turmas")
@Tag(name = "Turmas")
public class TurmaController {

    private final TurmaService turmaService;

    public TurmaController(TurmaService turmaService) {
        this.turmaService = turmaService;
    }

    @GetMapping
    public List<TurmaResponse> listar() {
        return turmaService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TurmaResponse criar(@Valid @RequestBody CriarTurmaRequest request) {
        return turmaService.criar(request);
    }

    @GetMapping("/{id}")
    public TurmaResponse buscarPorId(@PathVariable Long id) {
        return turmaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public TurmaResponse atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarTurmaRequest request) {
        return turmaService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        turmaService.excluir(id);
    }

    @PostMapping("/{id}/alunos/{matricula}")
    public TurmaResponse matricularAluno(@PathVariable Long id, @PathVariable String matricula) {
        return turmaService.matricularAluno(id, matricula);
    }

    @DeleteMapping("/{id}/alunos/{matricula}")
    public TurmaResponse removerAluno(@PathVariable Long id, @PathVariable String matricula) {
        return turmaService.removerAluno(id, matricula);
    }
}
