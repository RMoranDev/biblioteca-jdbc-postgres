package com.biblioteca.util;

/**
 * Classe utilitária para validação e formatação de CPF.
 *
 * Regras aplicadas:
 *  - Remove máscara (pontos e traço) antes de validar.
 *  - Exige exatamente 11 dígitos numéricos.
 *  - Rejeita sequências com todos os dígitos iguais (ex: 111.111.111-11),
 *    que matematicamente passariam no cálculo do dígito verificador
 *    mas não são CPFs válidos emitidos pela Receita Federal.
 *  - Calcula os dois dígitos verificadores pelo algoritmo oficial (módulo 11).
 */
public final class CpfValidator {

    private static final int TAMANHO_CPF = 11;

    private CpfValidator() {
        // classe utilitária, não deve ser instanciada
    }

    /**
     * Valida se a string informada é um CPF válido.
     * Aceita tanto CPF formatado ("123.456.789-09") quanto sem máscara ("12345678909").
     *
     * @param cpf CPF a validar (pode conter pontuação ou não)
     * @return true se o CPF é válido, false caso contrário (incluindo null/vazio)
     */
    public static boolean isValid(String cpf) {
        if (cpf == null) {
            return false;
        }

        String digits = apenasDigitos(cpf);

        if (digits.length() != TAMANHO_CPF) {
            return false;
        }

        if (todosDigitosIguais(digits)) {
            return false;
        }

        int primeiroDigitoVerificador = calcularDigitoVerificador(digits, 9);
        if (primeiroDigitoVerificador != Character.getNumericValue(digits.charAt(9))) {
            return false;
        }

        int segundoDigitoVerificador = calcularDigitoVerificador(digits, 10);
        return segundoDigitoVerificador == Character.getNumericValue(digits.charAt(10));
    }

    /**
     * Remove pontuação e retorna o CPF só com dígitos.
     * Útil para normalizar antes de persistir no banco.
     *
     * @param cpf CPF com ou sem máscara
     * @return apenas os dígitos, ou string vazia se null
     */
    public static String apenasDigitos(String cpf) {
        if (cpf == null) {
            return "";
        }
        return cpf.replaceAll("[^0-9]", "");
    }

    /**
     * Formata um CPF de 11 dígitos para o padrão 000.000.000-00.
     *
     * @param cpf CPF sem máscara (11 dígitos)
     * @return CPF formatado
     * @throws IllegalArgumentException se não tiver 11 dígitos
     */
    public static String formatar(String cpf) {
        String digits = apenasDigitos(cpf);
        if (digits.length() != TAMANHO_CPF) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos para ser formatado");
        }
        return digits.substring(0, 3) + "." +
               digits.substring(3, 6) + "." +
               digits.substring(6, 9) + "-" +
               digits.substring(9, 11);
    }

    private static boolean todosDigitosIguais(String digits) {
        char primeiro = digits.charAt(0);
        for (int i = 1; i < digits.length(); i++) {
            if (digits.charAt(i) != primeiro) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calcula um dígito verificador do CPF.
     *
     * @param digits          string com os dígitos do CPF (pelo menos até qtdDigitosBase)
     * @param qtdDigitosBase  quantidade de dígitos usados no cálculo (9 para o 1º dígito, 10 para o 2º)
     * @return o dígito verificador calculado (0-9)
     */
    private static int calcularDigitoVerificador(String digits, int qtdDigitosBase) {
        int peso = qtdDigitosBase + 1;
        int soma = 0;

        for (int i = 0; i < qtdDigitosBase; i++) {
            int digito = Character.getNumericValue(digits.charAt(i));
            soma += digito * peso;
            peso--;
        }

        int resto = soma % 11;
        return (resto < 2) ? 0 : (11 - resto);
    }
}