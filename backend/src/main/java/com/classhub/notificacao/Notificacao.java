package com.classhub.notificacao;

import com.classhub.usuario.Usuario;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "notificacoes")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Usuario destinatario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoNotificacao tipo;

    @Column(name = "referencia_id")
    private Long referenciaId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensagem;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private Instant criadoEm;

    protected Notificacao() {
    }

    public Notificacao(Usuario destinatario, TipoNotificacao tipo, Long referenciaId, String mensagem) {
        this.destinatario = destinatario;
        this.tipo = tipo;
        this.referenciaId = referenciaId;
        this.mensagem = mensagem;
        this.criadoEm = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Usuario getDestinatario() {
        return destinatario;
    }

    public TipoNotificacao getTipo() {
        return tipo;
    }

    public Long getReferenciaId() {
        return referenciaId;
    }

    public String getMensagem() {
        return mensagem;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }
}
