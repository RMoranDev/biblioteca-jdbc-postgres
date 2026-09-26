package com.biblioteca.model;

import java.time.OffsetDateTime;
import java.util.Objects;

public class Exemplar {

    private static final int CODIGO_PATRIMONIO_MAX_LENGTH = 50;
    private static final int OBSERVACOES_MAX_LENGTH = 255;

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
        setLivro(livro);
        setCodigoPatrimonio(codigoPatrimonio);
        setStatus(StatusExemplar.DISPONIVEL);
        setObservacoes(observacoes);
    }

    // Construtor completo (para mapear do ResultSet do DAO)
    public Exemplar(Long id, Livro livro, String codigoPatrimonio, StatusExemplar status,
            String observacoes, OffsetDateTime criadoEm, OffsetDateTime atualizadoEm) {
        this.id = id;
        setLivro(livro);
        setCodigoPatrimonio(codigoPatrimonio);
        setStatus(StatusExemplar.DISPONIVEL);
        setObservacoes(observacoes);
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
        validarLivro(livro);
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
        validarCodigoPatrimonio(codigoPatrimonio);
        this.codigoPatrimonio = codigoPatrimonio.trim();
    }

    public StatusExemplar getStatus() {
        return status;
    }

    public void setStatus(StatusExemplar status) {
        validarStatus(status);
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        validarObservacoes(observacoes);
        this.observacoes = observacoes != null ? observacoes.trim() : null;
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

    private void validarLivro(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo.");
        }

        if (livro.getId() == null) {
            throw new IllegalArgumentException("Livro deve possuir um ID.");
        }
    }

    private void validarStatus(StatusExemplar status) {
        if (status == null) {
            throw new IllegalArgumentException("Status do exemplar não pode ser nulo.");
        }
    }

    private void validarCodigoPatrimonio(String codigoPatrimonio) {
        if (codigoPatrimonio == null || codigoPatrimonio.isBlank()) {
            throw new IllegalArgumentException("Código de patrimônio não pode ser nulo ou vazio.");
        }
        if (codigoPatrimonio.length() > CODIGO_PATRIMONIO_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Código de patrimônio excede o tamanho máximo de " + CODIGO_PATRIMONIO_MAX_LENGTH + " caracteres.");
        }
    }

    private void validarObservacoes(String observacoes) {
        if (observacoes != null && observacoes.length() > OBSERVACOES_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Observações excede o tamanho máximo de " + OBSERVACOES_MAX_LENGTH + " caracteres.");
        }
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
                ", observacoes='" + observacoes + '\'' +
                ", criadoEm=" + criadoEm +
                ", atualizadoEm=" + atualizadoEm +
                '}';
    }
}
