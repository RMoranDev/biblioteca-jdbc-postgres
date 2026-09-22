package com.biblioteca.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;

public class Emprestimo {
    private Long id;
    private Exemplar exemplar;
    private Usuario usuario;
    private OffsetDateTime dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private OffsetDateTime dataDevolucaoEfetiva;
    private StatusEmprestimo status;
    private BigDecimal multaPaga;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;

    // Construtor padrão
    public Emprestimo() {
    }

    // Construtor para registrar um novo empréstimo
    public Emprestimo(Exemplar exemplar, Usuario usuario, LocalDate dataPrevistaDevolucao) {
        this.exemplar = Objects.requireNonNull(exemplar, "Exemplar é obrigatório");
        this.usuario = Objects.requireNonNull(usuario, "Usuário é obrigatório");
        this.dataPrevistaDevolucao = Objects.requireNonNull(dataPrevistaDevolucao, "Data prevista de devolução é obrigatória");
        this.status = StatusEmprestimo.ATIVO;
        this.multaPaga = BigDecimal.ZERO;
    }

    // Construtor completo (usado pelo DAO para reconstruir a partir do ResultSet)
    public Emprestimo(Long id, Exemplar exemplar, Usuario usuario, OffsetDateTime dataEmprestimo,
                      LocalDate dataPrevistaDevolucao, OffsetDateTime dataDevolucaoEfetiva,
                      StatusEmprestimo status, BigDecimal multaPaga,
                      OffsetDateTime criadoEm, OffsetDateTime atualizadoEm) {
        this.id = id;
        this.exemplar = exemplar;
        this.usuario = usuario;
        this.dataEmprestimo = dataEmprestimo;
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
        this.dataDevolucaoEfetiva = dataDevolucaoEfetiva;
        this.status = status;
        this.multaPaga = multaPaga;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
    }

    public Long getExemplarId() {
        return (this.exemplar != null) ? this.exemplar.getId() : null;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getUsuarioId() {
        return (this.usuario != null) ? this.usuario.getId() : null;
    }

    public OffsetDateTime getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(OffsetDateTime dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public OffsetDateTime getDataDevolucaoEfetiva() {
        return dataDevolucaoEfetiva;
    }

    public void setDataDevolucaoEfetiva(OffsetDateTime dataDevolucaoEfetiva) {
        this.dataDevolucaoEfetiva = dataDevolucaoEfetiva;
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    public void setStatus(StatusEmprestimo status) {
        this.status = status;
    }

    public BigDecimal getMultaPaga() {
        return multaPaga;
    }

    public void setMultaPaga(BigDecimal multaPaga) {
        this.multaPaga = multaPaga;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Emprestimo that = (Emprestimo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Emprestimo{" +
                "id=" + id +
                ", exemplarId=" + getExemplarId() +
                ", usuarioId=" + getUsuarioId() +
                ", status=" + status +
                ", dataPrevistaDevolucao=" + dataPrevistaDevolucao +
                ", multaPaga=" + multaPaga +
                '}';
    }
}
