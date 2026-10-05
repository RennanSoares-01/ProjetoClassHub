package com.classhub.entrega;

import com.classhub.entrega.dto.AtualizarEntregaRequest;
import com.classhub.entrega.dto.EntregaResponse;
import com.classhub.entrega.dto.EnviarEntregaRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/entregas")
@Tag(name = "Entregas")
public class EntregaController {

    private final EntregaService entregaService;

    public EntregaController(EntregaService entregaService) {
        this.entregaService = entregaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntregaResponse enviar(@Valid @RequestBody EnviarEntregaRequest request) {
        return entregaService.enviar(request);
    }

    @PutMapping("/{id}")
    public EntregaResponse atualizar(@PathVariable Long id, @Valid @RequestBody AtualizarEntregaRequest request) {
        return entregaService.atualizar(id, request);
    }

    @GetMapping("/{id}")
    public EntregaResponse buscarPorId(@PathVariable Long id) {
        return entregaService.buscarPorId(id);
    }
}
