package com.classhub.entrega;

import com.classhub.tarefa.Tarefa;
import com.classhub.usuario.Usuario;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "entregas", uniqueConstraints = @UniqueConstraint(columnNames = {"tarefa_id", "autor_id"}))
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarefa_id", nullable = false)
    private Tarefa tarefa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String conteudo;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEntrega status = StatusEntrega.PENDENTE;

    @Column(name = "data_criacao", nullable = false)
    private Instant dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    private Instant dataAtualizacao;

    @OneToMany(mappedBy = "entrega", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EntregaAnexo> anexos = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "entrega_participantes",
            joinColumns = @JoinColumn(name = "entrega_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private Set<Usuario> participantes = new LinkedHashSet<>();

    protected Entrega() {
    }

    public Entrega(Tarefa tarefa, Usuario autor, String conteudo, String comentario) {
        this.tarefa = tarefa;
        this.autor = autor;
        this.conteudo = conteudo;
        this.comentario = comentario;
        this.dataCriacao = Instant.now();
        this.dataAtualizacao = this.dataCriacao;
    }

    /** Regra de overwriting (ADR-006): preserva dataCriacao e atualiza apenas dataAtualizacao. */
    public void sobrescrever(String conteudo, String comentario) {
        this.conteudo = conteudo;
        this.comentario = comentario;
        this.dataAtualizacao = Instant.now();
    }

    public void substituirAnexos(List<EntregaAnexo> novosAnexos) {
        this.anexos.clear();
        this.anexos.addAll(novosAnexos);
    }

    public void substituirParticipantes(Set<Usuario> novosParticipantes) {
        this.participantes.clear();
        this.participantes.addAll(novosParticipantes);
    }

    public void aplicarResultadoAvaliacao(StatusEntrega status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public Usuario getAutor() {
        return autor;
    }

    public String getConteudo() {
        return conteudo;
    }

    public String getComentario() {
        return comentario;
    }

    public StatusEntrega getStatus() {
        return status;
    }

    public Instant getDataCriacao() {
        return dataCriacao;
    }

    public Instant getDataAtualizacao() {
        return dataAtualizacao;
    }

    public List<EntregaAnexo> getAnexos() {
        return anexos;
    }

    public Set<Usuario> getParticipantes() {
        return participantes;
    }
}
