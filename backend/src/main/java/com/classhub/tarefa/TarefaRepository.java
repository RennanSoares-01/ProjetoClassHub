package com.classhub.tarefa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    List<Tarefa> findByTurma_Id(Long turmaId);

    List<Tarefa> findByTurma_IdIn(List<Long> turmaIds);

    List<Tarefa> findByTurma_Professor_Id(Long professorId);

    long countByTurma_Professor_IdAndDataLimiteAfter(Long professorId, LocalDateTime referencia);

    long countByTurma_Professor_IdAndDataLimiteBefore(Long professorId, LocalDateTime referencia);

    List<Tarefa> findByDataLimiteBetween(LocalDateTime inicio, LocalDateTime fim);
}
