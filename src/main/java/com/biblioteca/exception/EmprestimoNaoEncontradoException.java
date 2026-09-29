package com.biblioteca.exception;

public class EmprestimoNaoEncontradoException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public EmprestimoNaoEncontradoException(Long id) {
        super("Empréstimo com id " + id + " não encontrado.");
    }
}
