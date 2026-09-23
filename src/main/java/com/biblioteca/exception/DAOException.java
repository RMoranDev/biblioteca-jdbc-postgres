package com.biblioteca.exception;

public class DAOException extends RuntimeException {

    public DAOException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public DAOException(String mensagem) {
        super(mensagem);
    }
}
