package com.classhub.entrega;

import jakarta.persistence.*;

@Entity
@Table(name = "entrega_anexos")
public class EntregaAnexo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrega_id", nullable = false)
    private Entrega entrega;

    @Column(name = "nome_arquivo", nullable = false)
    private String nomeArquivo;

    @Column(nullable = false)
    private String url;

    protected EntregaAnexo() {
    }

    public EntregaAnexo(Entrega entrega, String nomeArquivo, String url) {
        this.entrega = entrega;
        this.nomeArquivo = nomeArquivo;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public String getUrl() {
        return url;
    }
}
