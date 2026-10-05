package com.classhub.dashboard;

import com.classhub.dashboard.dto.DashboardAdminResponse;
import com.classhub.dashboard.dto.DashboardAlunoResponse;
import com.classhub.dashboard.dto.DashboardProfessorResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboards")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/aluno")
    public DashboardAlunoResponse aluno() {
        return dashboardService.aluno();
    }

    @GetMapping("/professor")
    public DashboardProfessorResponse professor() {
        return dashboardService.professor();
    }

    @GetMapping("/admin")
    public DashboardAdminResponse admin() {
        return dashboardService.admin();
    }
}
