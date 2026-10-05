package com.classhub.dashboard.dto;

import java.util.List;

public record DashboardAlunoResponse(
        List<TurmaResumo> turmas,
        List<TarefaResumo> proximasAtividades,
        List<TarefaResumo> atividadesEntregues,
        List<TarefaResumo> atividadesAtrasadas,
        List<NotaResumo> notasRecebidas
) {
}
