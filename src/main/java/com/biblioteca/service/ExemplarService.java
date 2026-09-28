package com.biblioteca.service;

import java.util.Objects;
import java.util.Optional;

import com.biblioteca.dao.ExemplarDAO;
import com.biblioteca.exception.RegraNegocioException;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Livro;
import com.biblioteca.model.StatusExemplar;

public class ExemplarService {

    private final ExemplarDAO exemplarDAO;
    private final LivroService livroService;

    public ExemplarService(ExemplarDAO exemplarDAO, LivroService livroService) {
        this.exemplarDAO = exemplarDAO;
        this.livroService = livroService;
    }

    public Exemplar buscarPorId(Long id) {
        Objects.requireNonNull(id, "ID não pode ser nulo.");

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "ID deve ser maior que zero.");
        }

        Optional<Exemplar> exemplar = this.exemplarDAO.buscarPorId(id);
        return exemplar.orElseThrow(() -> new RegraNegocioException(
                "Exemplar não encontrado! ID: " + id + ", Tipo: " + Exemplar.class.getName()));
    }

    public Exemplar cadastrar(Exemplar exemplar) {
        Objects.requireNonNull(exemplar, "Exemplar não pode ser nulo.");

        validarCodigoPatrimonio(exemplar.getCodigoPatrimonio());

        if (exemplarDAO.buscarPorCodigoPatrimonio(exemplar.getCodigoPatrimonio()).isPresent()) {
            throw new RegraNegocioException("Já existe um exemplar com este código de patrimônio.");
        }

        Livro livro = livroService.buscarPorId(exemplar.getLivroId());
        exemplar.setLivro(livro);
        exemplar.setCodigoPatrimonio(
                exemplar.getCodigoPatrimonio().trim());

        return this.exemplarDAO.salvar(exemplar);

    }

    public void atualizar(Exemplar exemplar) {
        Objects.requireNonNull(exemplar, "Exemplar não pode ser nulo.");

        validarCodigoPatrimonio(exemplar.getCodigoPatrimonio());

        Exemplar existente = buscarPorId(exemplar.getId());

        String codigo = exemplar.getCodigoPatrimonio().trim();

        Optional<Exemplar> outroExemplar = exemplarDAO.buscarPorCodigoPatrimonio(codigo);

        if (outroExemplar.isPresent()
                && !outroExemplar.get().getId()
                        .equals(exemplar.getId())) {

            throw new RegraNegocioException(
                    "O código de patrimônio já pertence a outro exemplar.");
        }

        existente.setCodigoPatrimonio(codigo);
        existente.setObservacoes(exemplar.getObservacoes());

        this.exemplarDAO.atualizar(existente);
    }

    public void deletar(Long id) {
        Exemplar exemplar = buscarPorId(id);

        if (exemplar.getStatus() == StatusExemplar.EMPRESTADO) {
            throw new RegraNegocioException(
                    "Não é possível excluir um exemplar emprestado.");
        }

        this.exemplarDAO.deletar(id);
    }

    public void marcarComoEmprestado(Long id) {
        Exemplar exemplar = buscarPorId(id);

        if (exemplar.getStatus() != StatusExemplar.DISPONIVEL) {
            throw new RegraNegocioException("Apenas exemplares disponíveis podem ser emprestados.");
        }

        exemplar.setStatus(StatusExemplar.EMPRESTADO);
        this.exemplarDAO.atualizar(exemplar);
    }

    public void marcarComoDisponivel(Long id) { // usado na devolução
        Exemplar exemplar = buscarPorId(id);

        if (exemplar.getStatus() != StatusExemplar.EMPRESTADO) {
            throw new RegraNegocioException("Apenas exemplares emprestados podem ser devolvidos.");
        }

        exemplar.setStatus(StatusExemplar.DISPONIVEL);
        this.exemplarDAO.atualizar(exemplar);
    }

    public void marcarComoPerdido(Long id) {
        Exemplar exemplar = buscarPorId(id);

        if (exemplar.getStatus() == StatusExemplar.PERDIDO) {
            throw new RegraNegocioException(
                    "O exemplar já está marcado como perdido.");
        }

        if (exemplar.getStatus() == StatusExemplar.EM_MANUTENCAO) {
            throw new RegraNegocioException(
                    "Não é possível marcar como perdido "
                            + "um exemplar em manutenção.");
        }

        if (exemplar.getStatus() == StatusExemplar.EMPRESTADO) {
            throw new RegraNegocioException(
                    "É necessário encerrar o empréstimo "
                            + "antes de marcar o exemplar como perdido.");
        }

        // emprestimoService.encerrarPorPerda(id); //precisa implementar depois

        exemplar.setStatus(StatusExemplar.PERDIDO);
        this.exemplarDAO.atualizar(exemplar);
    }

    private void validarCodigoPatrimonio(String codigo) {
        if (codigo == null || codigo.isBlank()) {
            throw new IllegalArgumentException(
                    "Código de patrimônio não pode ser nulo ou vazio.");
        }
    }
}
