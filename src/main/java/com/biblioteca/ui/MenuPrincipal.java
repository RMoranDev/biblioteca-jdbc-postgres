package com.biblioteca.ui;

import java.util.Scanner;

public class MenuPrincipal {
    private final Scanner scanner;
    private final MenuLivro menuLivro;
    private final MenuUsuario menuUsuario;
    private final MenuExemplar menuExemplar;
    private final MenuEmprestimo menuEmprestimo;

    public MenuPrincipal(Scanner scanner, MenuLivro menuLivro, MenuUsuario menuUsuario,
            MenuExemplar menuExemplar, MenuEmprestimo menuEmprestimo) {
        this.scanner = scanner;
        this.menuLivro = menuLivro;
        this.menuUsuario = menuUsuario;
        this.menuExemplar = menuExemplar;
        this.menuEmprestimo = menuEmprestimo;
    }

    public void iniciar() {
        int opcao;
        do {
            exibir();
            opcao = LeitorConsole.lerInteiro(scanner, "Escolha uma opção: ");
            switch (opcao) {
                case 1 -> menuLivro.iniciar();
                case 2 -> menuUsuario.iniciar();
                case 3 -> menuExemplar.iniciar();
                case 4 -> menuEmprestimo.iniciar();
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
}
