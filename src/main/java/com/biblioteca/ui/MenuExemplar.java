package com.biblioteca.ui;

import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Livro;
import com.biblioteca.service.ExemplarService;
import com.biblioteca.service.LivroService;
import java.util.Scanner;

public class MenuExemplar {
    private final Scanner scanner;
    private final ExemplarService exemplarService;
    private final LivroService livroService;

    public MenuExemplar(Scanner scanner, ExemplarService exemplarService, LivroService livroService) {
        this.scanner = scanner;
        this.exemplarService = exemplarService;
        this.livroService = livroService;
    }

    public void iniciar() {
        int opcao;
        do {
            System.out.println("""

                    --- EXEMPLARES ---
                    1 - Cadastrar
                    2 - Listar
                    3 - Buscar por ID
                    4 - Atualizar
                    5 - Excluir
                    6 - Marcar como perdido
                    0 - Voltar
                    """);
            opcao = LeitorConsole.lerInteiro(scanner, "Escolha uma opção: ");
            try {
                switch (opcao) {
                    case 1 -> cadastrar();
                    case 2 -> exemplarService.buscarTodos().forEach(System.out::println);
                    case 3 -> System.out.println(exemplarService.buscarPorId(lerId()));
                    case 4 -> atualizar();
                    case 5 -> excluir();
                    case 6 -> marcarComoPerdido();
                    case 0 -> { }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (RuntimeException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (opcao != 0);
    }

    private void cadastrar() {
        Long livroId = LeitorConsole.lerLong(scanner, "ID do livro: ");
        Livro livro = livroService.buscarPorId(livroId);
        Exemplar exemplar = new Exemplar(livro,
                LeitorConsole.lerTexto(scanner, "Código de patrimônio: "),
                LeitorConsole.lerTextoOpcional(scanner, "Observações: "));
        System.out.println("Exemplar cadastrado: " + exemplarService.cadastrar(exemplar));
    }

    private void atualizar() {
        Long id = lerId();
        Exemplar atual = exemplarService.buscarPorId(id);
        Exemplar exemplar = new Exemplar(atual.getLivro(),
                LeitorConsole.lerTexto(scanner, "Código de patrimônio: "),
                LeitorConsole.lerTextoOpcional(scanner, "Observações: "));
        exemplar.setId(id);
        exemplarService.atualizar(exemplar);
        System.out.println("Exemplar atualizado com sucesso.");
    }

    private void excluir() {
        exemplarService.deletar(lerId());
        System.out.println("Exemplar excluído com sucesso.");
    }

    private void marcarComoPerdido() {
        exemplarService.marcarComoPerdido(lerId());
        System.out.println("Exemplar marcado como perdido.");
    }

    private Long lerId() {
        return LeitorConsole.lerLong(scanner, "ID: ");
    }
}
