package com.biblioteca.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.biblioteca.dao.EmprestimoDAO;
import com.biblioteca.exception.RegraNegocioException;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.StatusEmprestimo;
import com.biblioteca.model.StatusExemplar;
import com.biblioteca.model.Usuario;

public class EmprestimoService {

    private static final int PRAZO_PADRAO_DIAS = 7;
    private static final int DIAS_RENOVACAO = 7;

    private final EmprestimoDAO emprestimoDAO;
    private final ExemplarService exemplarService;
    private final UsuarioService usuarioService;

    public EmprestimoService(EmprestimoDAO emprestimoDAO, ExemplarService exemplarService,
            UsuarioService usuarioService) {
        this.emprestimoDAO = emprestimoDAO;
        this.exemplarService = exemplarService;
        this.usuarioService = usuarioService;
    }

    public Emprestimo buscarPorId(Long id) {
        Objects.requireNonNull(id, "ID não pode ser nulo.");

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "ID deve ser maior que zero.");
        }

        Optional<Emprestimo> emprestimo = this.emprestimoDAO.buscarPorId(id);
        return emprestimo.orElseThrow(() -> new RegraNegocioException(
                "Empréstimo não encontrado! ID: " + id + ", Tipo: " + Emprestimo.class.getSimpleName()));
    }

    public List<Emprestimo> listarTodos() {
        return emprestimoDAO.buscarTodos();
    }

    public Emprestimo cadastrar(Long usuarioId, Long exemplarId) {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        Exemplar exemplar = exemplarService.buscarPorId(exemplarId);

        validarDisponibilidade(exemplar);

        LocalDate dataPrevistaDevolucao = LocalDate.now().plusDays(PRAZO_PADRAO_DIAS);

        Emprestimo novoEmprestimo = new Emprestimo(
                exemplar, usuario, dataPrevistaDevolucao);

        exemplarService.marcarComoEmprestado(exemplarId);
        try {
            return emprestimoDAO.salvar(novoEmprestimo);
        } catch (RuntimeException e) {
            exemplarService.marcarComoDisponivel(exemplarId);
            throw e;
        }
    }

    public Emprestimo devolver(Long emprestimoId) {
        Emprestimo emprestimo = buscarPorId(emprestimoId);

        emprestimo.registrarDevolucao(OffsetDateTime.now());

        Emprestimo salvo = emprestimoDAO.salvar(emprestimo);

        exemplarService.marcarComoDisponivel(emprestimo.getExemplarId());

        return salvo;
    }

    public Emprestimo renovar(Long emprestimoId) {
        Emprestimo emprestimo = buscarPorId(emprestimoId);

        if (emprestimo.getStatus() != StatusEmprestimo.ATIVO) {
            throw new RegraNegocioException(
                    "Só é possível renovar empréstimos ativos.");
        }

        if (LocalDate.now().isAfter(emprestimo.getDataPrevistaDevolucao())) {
            throw new RegraNegocioException("Empréstimo atrasado não pode ser renovado.");
        }

        LocalDate novaData = emprestimo.getDataPrevistaDevolucao()
                .plusDays(DIAS_RENOVACAO);

        emprestimo.setDataPrevistaDevolucao(novaData);

        return emprestimoDAO.salvar(emprestimo);
    }

    public Emprestimo cancelar(Long emprestimoId) {
        Emprestimo emprestimo = buscarPorId(emprestimoId);

        emprestimo.cancelar();
        exemplarService.marcarComoDisponivel(emprestimo.getExemplarId());

        return emprestimoDAO.salvar(emprestimo);
    }

    private void validarDisponibilidade(Exemplar exemplar) {
        if (exemplar.getStatus() != StatusExemplar.DISPONIVEL) {
            throw new RegraNegocioException(
                    "O exemplar não está disponível para empréstimo.");
        }
    }

}
