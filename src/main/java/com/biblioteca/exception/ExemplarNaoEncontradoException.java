package com.biblioteca.exception;

public class ExemplarNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExemplarNaoEncontradoException(Long id) {
        super("Exemplar com id " + id + " não encontrado.");
    }
}
