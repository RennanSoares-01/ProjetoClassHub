package com.classhub.dashboard.dto;

import java.util.List;

public record DashboardProfessorResponse(
        List<TurmaProfessorResumo> turmas,
        long atividadesAbertas,
        long atividadesEncerradas,
        long entregasPendentesAvaliacao
) {
}
