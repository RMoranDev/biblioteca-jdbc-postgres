package com.biblioteca.exception;

/**
 * Lançada quando uma operação viola uma regra de negócio da aplicação
 * (ex.: CPF duplicado, tentativa de ação não permitida pelo estado atual
 * de uma entidade).
 */
public class RegraNegocioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }

    public RegraNegocioException(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }
}