package com.classhub.entrega;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    List<Entrega> findByTarefa_Id(Long tarefaId);

    Optional<Entrega> findByTarefa_IdAndAutor_Id(Long tarefaId, Long autorId);

    long countByTarefa_Turma_Professor_IdAndStatus(Long professorId, StatusEntrega status);

    List<Entrega> findByAutor_Id(Long autorId);
}
