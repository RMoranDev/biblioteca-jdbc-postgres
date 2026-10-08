package com.biblioteca.ui;

import com.biblioteca.model.Usuario;
import com.biblioteca.service.UsuarioService;
import java.util.Scanner;

public class MenuUsuario {
    private final Scanner scanner;
    private final UsuarioService usuarioService;

    public MenuUsuario(Scanner scanner, UsuarioService usuarioService) {
        this.scanner = scanner;
        this.usuarioService = usuarioService;
    }

    public void iniciar() {
        int opcao;
        do {
            System.out.println("""

                    --- USUÁRIOS ---
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
                    case 2 -> usuarioService.buscarTodos().forEach(System.out::println);
                    case 3 -> System.out.println(usuarioService.buscarPorId(lerId()));
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
        Usuario usuario = new Usuario(LeitorConsole.lerTexto(scanner, "Nome: "),
                LeitorConsole.lerTexto(scanner, "CPF: "),
                LeitorConsole.lerTexto(scanner, "E-mail: "),
                LeitorConsole.lerTextoOpcional(scanner, "Telefone: "));
        System.out.println("Usuário cadastrado: " + usuarioService.cadastrar(usuario));
    }

    private void atualizar() {
        Usuario usuario = usuarioService.buscarPorId(lerId());
        usuario.setNome(LeitorConsole.lerTexto(scanner, "Nome: "));
        usuario.setEmail(LeitorConsole.lerTexto(scanner, "E-mail: "));
        usuario.setTelefone(LeitorConsole.lerTextoOpcional(scanner, "Telefone: "));
        usuario.setAtivo(LeitorConsole.lerBooleano(scanner, "Usuário ativo (s/n): "));
        usuarioService.atualizar(usuario);
        System.out.println("Usuário atualizado com sucesso.");
    }

    private void excluir() {
        usuarioService.deletar(lerId());
        System.out.println("Usuário excluído com sucesso.");
    }

    private Long lerId() {
        return LeitorConsole.lerLong(scanner, "ID: ");
    }
}
