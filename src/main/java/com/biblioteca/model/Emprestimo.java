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
        setExemplar(exemplar);
        setUsuario(usuario);
        this.dataEmprestimo = OffsetDateTime.now();
        setDataPrevistaDevolucao(dataPrevistaDevolucao);
        this.status = StatusEmprestimo.ATIVO;
        this.multaPaga = BigDecimal.ZERO;
    }

    // Construtor completo (usado pelo DAO para reconstruir a partir do ResultSet)
    public Emprestimo(Long id, Exemplar exemplar, Usuario usuario, OffsetDateTime dataEmprestimo,
            LocalDate dataPrevistaDevolucao, OffsetDateTime dataDevolucaoEfetiva,
            StatusEmprestimo status, BigDecimal multaPaga,
            OffsetDateTime criadoEm, OffsetDateTime atualizadoEm) {

        validarExemplar(exemplar);
        validarUsuario(usuario);
        validarDataPrevistaDevolucao(dataPrevistaDevolucao);
        validarStatus(status);
        validarMultaPaga(multaPaga);
        
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

    /**
     * Registra a devolução do exemplar, atualizando o status e a data de
     * devolução efetiva de forma atômica e consistente.
     *
     * @param dataDevolucao momento em que a devolução ocorreu
     * @throws IllegalStateException    se o empréstimo já estiver devolvido ou
     *                                  cancelado
     * @throws IllegalArgumentException se a data for nula ou anterior à data do
     *                                  empréstimo
     */
    public void registrarDevolucao(OffsetDateTime dataDevolucao) {
        if (this.status == StatusEmprestimo.DEVOLVIDO) {
            throw new IllegalStateException("Este empréstimo já foi devolvido.");
        }
        if (this.status == StatusEmprestimo.CANCELADO) {
            throw new IllegalStateException("Não é possível devolver um empréstimo cancelado.");
        }
        if (dataDevolucao == null) {
            throw new IllegalArgumentException("Data de devolução não pode ser nula.");
        }
        if (this.dataEmprestimo != null && dataDevolucao.isBefore(this.dataEmprestimo)) {
            throw new IllegalArgumentException(
                    "Data de devolução não pode ser anterior à data do empréstimo.");
        }

        this.dataDevolucaoEfetiva = dataDevolucao;
        this.status = StatusEmprestimo.DEVOLVIDO;
    }

    /**
     * Cancela o empréstimo. Não é permitido cancelar um empréstimo que já
     * foi devolvido.
     *
     * @throws IllegalStateException se o empréstimo já estiver devolvido
     */
    public void cancelar() {
        if (this.status == StatusEmprestimo.DEVOLVIDO) {
            throw new IllegalStateException("Não é possível cancelar um empréstimo já devolvido.");
        }
        this.status = StatusEmprestimo.CANCELADO;
    }

    /**
     * Registra o pagamento de multa referente a este empréstimo.
     *
     * @param valor valor pago, deve ser maior ou igual a zero
     */
    public void pagarMulta(BigDecimal valor) {
        validarMultaPaga(valor);
        this.multaPaga = valor;
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
        validarExemplar(exemplar);
        this.exemplar = exemplar;
    }

    public Long getExemplarId() {
        return (this.exemplar != null) ? this.exemplar.getId() : null;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        validarUsuario(usuario);
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
        validarDataPrevistaDevolucao(dataPrevistaDevolucao);
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

    /**
     * Altera o status do empréstimo diretamente. Não permite a transição
     * para DEVOLVIDO — use {@link #registrarDevolucao(OffsetDateTime)} para
     * isso, pois essa transição precisa acontecer junto com o registro da
     * data de devolução efetiva. Para cancelamento, prefira {@link #cancelar()}.
     */
    public void setStatus(StatusEmprestimo status) {
        validarStatus(status);
        if (status == StatusEmprestimo.DEVOLVIDO) {
            throw new IllegalStateException(
                    "Use registrarDevolucao(OffsetDateTime) para marcar como devolvido.");
        }
        this.status = status;
    }

    public BigDecimal getMultaPaga() {
        return multaPaga;
    }

    public void setMultaPaga(BigDecimal multaPaga) {
        validarMultaPaga(multaPaga);
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

    private void validarExemplar(Exemplar exemplar) {
        if (exemplar == null) {
            throw new IllegalArgumentException("Exemplar não pode ser nulo.");
        }
        if (exemplar.getId() == null) {
            throw new IllegalArgumentException("Exemplar deve possuir um ID.");
        }
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario não pode ser nulo.");
        }
        if (usuario.getId() == null) {
            throw new IllegalArgumentException("Usuario deve possuir um ID.");
        }
    }

    private void validarDataPrevistaDevolucao(LocalDate data) {
        if (data == null) {
            throw new IllegalArgumentException("Data de devolução não pode ser nula.");
        }
    }

    private void validarStatus(StatusEmprestimo status) {
        if (status == null) {
            throw new IllegalArgumentException("Status não pode ser nulo.");
        }
    }

    private void validarMultaPaga(BigDecimal multa) {
        if (multa == null) {
            throw new IllegalArgumentException("Multa não pode ser nula.");
        }
        if (multa.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("A multa não pode ser negativa.");
        }
        if (multa.scale() > 2) {
            throw new IllegalArgumentException("Multa não pode ter mais de 2 casas decimais.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Emprestimo that = (Emprestimo) o;
        return id != null && id.equals(that.id);
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
