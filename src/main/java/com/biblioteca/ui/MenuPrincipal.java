package com.biblioteca.ui;

import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner scanner;

    public MenuPrincipal(Scanner scanner) {
        this.scanner = scanner;
    }

    public void iniciar() {
        int opcao;
        do {
            exibir();
            opcao = lerOpcao();

            switch (opcao) {
                case 1 -> System.out.println("Livros: em construção.");
                case 2 -> System.out.println("Usuários: em construção.");
                case 3 -> System.out.println("Exemplares: em construção.");
                case 4 -> System.out.println("Empréstimos: em construção.");
                case 0 -> System.out.println("Encerrando o sistema...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void exibir() {
        System.out.println("""

                =================================
                        SISTEMA BIBLIOTECA
                =================================
                1 - Livros
                2 - Usuários
                3 - Exemplares
                4 - Empréstimos
                0 - Sair
                """);
    }

    private int lerOpcao() {
        System.out.print("Escolha uma opção: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}