package com.biblioteca.ui;

import java.util.Scanner;

final class LeitorConsole {
    private LeitorConsole() {
    }

    static int lerInteiro(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static Long lerLong(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        return Long.parseLong(scanner.nextLine().trim());
    }

    static String lerTexto(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    static String lerTextoOpcional(Scanner scanner, String mensagem) {
        String texto = lerTexto(scanner, mensagem);
        return texto.isEmpty() ? null : texto;
    }

    static boolean lerBooleano(Scanner scanner, String mensagem) {
        System.out.print(mensagem);
        String resposta = scanner.nextLine().trim();
        if (resposta.equalsIgnoreCase("s")) {
            return true;
        }
        if (resposta.equalsIgnoreCase("n")) {
            return false;
        }
        throw new IllegalArgumentException("Informe 's' para sim ou 'n' para não.");
    }
}
