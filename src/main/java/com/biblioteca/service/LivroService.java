package com.biblioteca.service;

import java.util.List;
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

    public Livro cadastrar(Livro livro) {
        if (livroDAO.buscarPorIsbn(livro.getIsbn()).isPresent()) {
            throw new RegraNegocioException("Já existe um livro com este ISBN.");
        }

        return livroDAO.salvar(livro);
    }

    public Optional<Livro> buscarPorId(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }

        return livroDAO.buscarPorId(id);
    }

    public Optional<Livro> buscarPorIsbn(String isbn) {

        if (isbn == null || isbn.isBlank()) {
            throw new IllegalArgumentException("ISBN não pode ser nulo ou vazio.");
        }
        return livroDAO.buscarPorIsbn(isbn);
    }

    public List<Livro> buscarTodos() {
        return livroDAO.buscarTodos();
    }

    public void atualizar(Livro livro) {

        Optional<Livro> livroISBN = livroDAO.buscarPorIsbn(livro.getIsbn());

        if (livroISBN.isPresent() && !livroISBN.get().getId().equals(livro.getId())) {
            throw new RegraNegocioException("O ISBN informado já está cadastrado.");
        }

        boolean atualizado = livroDAO.atualizar(livro);
        if (!atualizado) {
            throw new LivroNaoEncontradoException(livro.getId());
        }
    }

    public void deletar(Long id) {
        boolean deletado = livroDAO.deletar(id);
        if (!deletado) {
            throw new LivroNaoEncontradoException(id);
        }
    }
}
