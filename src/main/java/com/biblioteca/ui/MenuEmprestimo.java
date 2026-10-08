package com.biblioteca.ui;

import com.biblioteca.service.EmprestimoService;
import java.util.Scanner;

public class MenuEmprestimo {
    private final Scanner scanner;
    private final EmprestimoService emprestimoService;

    public MenuEmprestimo(Scanner scanner, EmprestimoService emprestimoService) {
        this.scanner = scanner;
        this.emprestimoService = emprestimoService;
    }

    public void iniciar() {
        int opcao;
        do {
            System.out.println("""

                    --- EMPRÉSTIMOS ---
                    1 - Registrar empréstimo
                    2 - Listar
                    3 - Buscar por ID
                    4 - Devolver
                    5 - Renovar
                    6 - Cancelar
                    0 - Voltar
                    """);
            opcao = LeitorConsole.lerInteiro(scanner, "Escolha uma opção: ");
            try {
                switch (opcao) {
                    case 1 -> cadastrar();
                    case 2 -> emprestimoService.listarTodos().forEach(System.out::println);
                    case 3 -> System.out.println(emprestimoService.buscarPorId(lerId()));
                    case 4 -> devolver();
                    case 5 -> renovar();
                    case 6 -> cancelar();
                    case 0 -> { }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (RuntimeException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void cadastrar() {
        Long usuarioId = LeitorConsole.lerLong(scanner, "ID do usuário: ");
        Long exemplarId = LeitorConsole.lerLong(scanner, "ID do exemplar: ");
        System.out.println("Empréstimo registrado: " + emprestimoService.cadastrar(usuarioId, exemplarId));
    }

    private void devolver() {
        System.out.println("Empréstimo devolvido: " + emprestimoService.devolver(lerId()));
    }

    private void renovar() {
        System.out.println("Empréstimo renovado: " + emprestimoService.renovar(lerId()));
    }

    private void cancelar() {
        System.out.println("Empréstimo cancelado: " + emprestimoService.cancelar(lerId()));
    }

    private Long lerId() {
        return LeitorConsole.lerLong(scanner, "ID: ");
    }
}
