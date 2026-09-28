package com.biblioteca.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.biblioteca.dao.LivroDAO;
import com.biblioteca.exception.LivroNaoEncontradoException;
import com.biblioteca.exception.RegraNegocioException;
import com.biblioteca.model.Livro;

public class LivroService {

    private final LivroDAO livroDAO;

    public LivroService(LivroDAO livroDAO) {
        this.livroDAO = livroDAO;
    }

    public Livro buscarPorId(Long id) {
        validarId(id);

        return livroDAO.buscarPorId(id)
                .orElseThrow(() -> new LivroNaoEncontradoException(id));
    }

    public Livro cadastrar(Livro livro) {
        Objects.requireNonNull(livro, "Livro não pode ser nulo.");

        String isbn = validarIsbn(livro.getIsbn());
        livro.setIsbn(isbn);

        if (livroDAO.buscarPorIsbn(livro.getIsbn()).isPresent()) {
            throw new RegraNegocioException("Já existe um livro com este ISBN.");
        }

        return this.livroDAO.salvar(livro);
    }

    public Optional<Livro> buscarPorIsbn(String isbn) {
        return livroDAO.buscarPorIsbn(validarIsbn(isbn));
    }

    public List<Livro> buscarTodos() {
        return livroDAO.buscarTodos();
    }

    public void atualizar(Livro livro) {
        Objects.requireNonNull(livro, "Livro não pode ser nulo.");
        validarId(livro.getId());

        String isbn = validarIsbn(livro.getIsbn());
        livro.setIsbn(isbn);

        buscarPorId(livro.getId());

        Optional<Livro> livroISBN = livroDAO.buscarPorIsbn(isbn);

        if (livroISBN.isPresent() && !livroISBN.get().getId().equals(livro.getId())) {
            throw new RegraNegocioException("O ISBN informado já está cadastrado.");
        }

        boolean atualizado = livroDAO.atualizar(livro);

        if (!atualizado) {
            throw new LivroNaoEncontradoException(livro.getId());
        }
    }

    public void deletar(Long id) {
        validarId(id);

        // Garante que o livro existe antes de tentar excluí-lo.
        buscarPorId(id);

        // A integridade referencial do PostgreSQL deve impedir
        // a exclusão caso existam exemplares associados.
        livroDAO.deletar(id);
    }

    private void validarId(Long id) {
        Objects.requireNonNull(id, "ID não pode ser nulo.");

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "ID deve ser maior que zero.");
        }
    }

    private String validarIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException(
                    "ISBN não pode ser nulo ou vazio.");
        }

        return isbn.strip();
    }
}
