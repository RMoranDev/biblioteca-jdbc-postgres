package com.biblioteca.exception;

public class LivroNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public LivroNaoEncontradoException(Long id) {
        super("Livro com id " + id + " não encontrado.");
    }
}