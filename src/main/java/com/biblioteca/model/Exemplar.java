package com.biblioteca.model;

import java.time.OffsetDateTime;
import java.util.Objects;

public class Exemplar {
    private Long id;
    private Livro livro;
    private String codigoPatrimonio;
    private StatusExemplar status;
    private String observacoes;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;

    public Exemplar() {
    }

    // Construtor para novo cadastro (status padrão DISPONIVEL)
    public Exemplar(Livro livro, String codigoPatrimonio, String observacoes) {
        this.livro = livro;
        this.codigoPatrimonio = codigoPatrimonio;
        this.status = StatusExemplar.DISPONIVEL;
        this.observacoes = observacoes;
    }

    // Construtor completo (para mapear do ResultSet do DAO)
    public Exemplar(Long id, Livro livro, String codigoPatrimonio, StatusExemplar status,
            String observacoes, OffsetDateTime criadoEm, OffsetDateTime atualizadoEm) {
        this.id = id;
        this.livro = livro;
        this.codigoPatrimonio = codigoPatrimonio;
        this.status = status;
        this.observacoes = observacoes;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    // Atalho prático para obter o ID do livro nas queries SQL
    public Long getLivroId() {
        return (this.livro != null) ? this.livro.getId() : null;
    }

    public String getCodigoPatrimonio() {
        return codigoPatrimonio;
    }

    public void setCodigoPatrimonio(String codigoPatrimonio) {
        this.codigoPatrimonio = codigoPatrimonio;
    }

    public StatusExemplar getStatus() {
        return status;
    }

    public void setStatus(StatusExemplar status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(OffsetDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public OffsetDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(OffsetDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Exemplar exemplar = (Exemplar) o;
        return Objects.equals(id, exemplar.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Exemplar{" +
                "id=" + id +
                ", livro=" + (livro != null ? livro.getTitulo() : "null") +
                ", codigoPatrimonio='" + codigoPatrimonio + '\'' +
                ", status=" + status +
                ", criadoEm=" + criadoEm +
                ", atualizadoEm=" + atualizadoEm +
                '}';
    }
}
