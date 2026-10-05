package com.classhub.turma;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TurmaRepository extends JpaRepository<Turma, Long> {

    List<Turma> findByProfessor_Id(Long professorId);

    List<Turma> findByAlunos_Id(Long alunoId);

    long countByProfessor_Id(Long professorId);
}
