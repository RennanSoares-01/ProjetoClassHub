package com.classhub.feedback;

import com.classhub.entrega.Entrega;
import com.classhub.entrega.StatusEntrega;
import com.classhub.usuario.Usuario;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrega_id", nullable = false, unique = true)
    private Entrega entrega;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Usuario professor;

    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal nota;

    @Column(columnDefinition = "TEXT")
    private String comentario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEntrega status;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    protected Feedback() {
    }

    public Feedback(Entrega entrega, Usuario professor, BigDecimal nota, String comentario, StatusEntrega status) {
        this.entrega = entrega;
        this.professor = professor;
        this.nota = nota;
        this.comentario = comentario;
        this.status = status;
        this.criadoEm = Instant.now();
    }

    public void atualizar(Usuario professor, BigDecimal nota, String comentario, StatusEntrega status) {
        this.professor = professor;
        this.nota = nota;
        this.comentario = comentario;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Entrega getEntrega() {
        return entrega;
    }

    public Usuario getProfessor() {
        return professor;
    }

    public BigDecimal getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }

    public StatusEntrega getStatus() {
        return status;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }
}
