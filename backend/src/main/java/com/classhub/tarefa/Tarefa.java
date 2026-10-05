package com.classhub.tarefa;

import com.classhub.turma.Turma;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id", nullable = false)
    private Turma turma;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTarefa tipo;

    @Column(name = "data_limite", nullable = false)
    private LocalDateTime dataLimite;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    protected Tarefa() {
    }

    public Tarefa(Turma turma, String titulo, String descricao, TipoTarefa tipo, LocalDateTime dataLimite) {
        this.turma = turma;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.dataLimite = dataLimite;
        this.criadoEm = Instant.now();
    }

    public boolean estaAberta() {
        return dataLimite.isAfter(LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public Turma getTurma() {
        return turma;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoTarefa getTipo() {
        return tipo;
    }

    public void setTipo(TipoTarefa tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDateTime dataLimite) {
        this.dataLimite = dataLimite;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }
}
