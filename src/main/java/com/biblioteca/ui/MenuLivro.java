package com.biblioteca.ui;

import com.biblioteca.model.Livro;
import com.biblioteca.service.LivroService;
import java.util.Scanner;

public class MenuLivro {
    private final Scanner scanner;
    private final LivroService livroService;

    public MenuLivro(Scanner scanner, LivroService livroService) {
        this.scanner = scanner;
        this.livroService = livroService;
    }

    public void iniciar() {
        int opcao;
        do {
            System.out.println("""

                    --- LIVROS ---
                    1 - Cadastrar
                    2 - Listar
                    3 - Buscar por ID
                    4 - Atualizar
                    5 - Excluir
                    0 - Voltar
                    """);
            opcao = LeitorConsole.lerInteiro(scanner, "Escolha uma opção: ");
            try {
                switch (opcao) {
                    case 1 -> cadastrar();
                    case 2 -> livroService.buscarTodos().forEach(System.out::println);
                    case 3 -> System.out.println(livroService.buscarPorId(lerId()));
                    case 4 -> atualizar();
                    case 5 -> excluir();
                    case 0 -> { }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (RuntimeException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void cadastrar() {
        System.out.println("Livro cadastrado: " + livroService.cadastrar(lerDadosLivro()));
    }

    private void atualizar() {
        Long id = lerId();
        Livro livro = lerDadosLivro();
        livro.setId(id);
        livroService.atualizar(livro);
        System.out.println("Livro atualizado com sucesso.");
    }

    private Livro lerDadosLivro() {
        return new Livro(LeitorConsole.lerTexto(scanner, "Título: "),
                LeitorConsole.lerTexto(scanner, "Autor: "),
                LeitorConsole.lerTexto(scanner, "ISBN: "),
                LeitorConsole.lerInteiro(scanner, "Ano de publicação: "),
                LeitorConsole.lerTexto(scanner, "Categoria: "));
    }

    private void excluir() {
        livroService.deletar(lerId());
        System.out.println("Livro excluído com sucesso.");
    }

    private Long lerId() {
        return LeitorConsole.lerLong(scanner, "ID: ");
    }
}
