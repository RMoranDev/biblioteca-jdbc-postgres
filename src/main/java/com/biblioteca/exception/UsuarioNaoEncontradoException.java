package com.biblioteca.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário com id " + id + " não encontrado.");
    }
}
