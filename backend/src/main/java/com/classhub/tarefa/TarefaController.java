package com.classhub.tarefa;

import com.classhub.entrega.EntregaService;
import com.classhub.entrega.dto.EntregaResponse;
import com.classhub.tarefa.dto.AtualizarTarefaRequest;
import com.classhub.tarefa.dto.CriarTarefaRequest;
import com.classhub.tarefa.dto.TarefaResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
@Tag(name = "Tarefas")
public class TarefaController {

    private final TarefaService tarefaService;
    private final EntregaService entregaService;

    public TarefaController(TarefaService tarefaService, EntregaService entregaService) {
        this.tarefaService = tarefaService;
        this.entregaService = entregaService;
    }

    @GetMapping
    public List<TarefaResponse> listar(@RequestParam(required = false) Long turmaId) {
        return tarefaService.listar(turmaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponse criar(@Valid @RequestBody CriarTarefaRequest request) {
        return tarefaService.criar(request);
    }

    @GetMapping("/{id}")
    public TarefaResponse buscarPorId(@PathVariable Long id) {
        return tarefaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public TarefaResponse atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarTarefaRequest request) {
        return tarefaService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        tarefaService.excluir(id);
    }

    @GetMapping("/{id}/entregas")
    public List<EntregaResponse> listarEntregas(@PathVariable Long id) {
        return entregaService.listarPorTarefa(id);
    }
}
