package com.biblioteca;

import com.biblioteca.dao.EmprestimoDAO;
import com.biblioteca.dao.ExemplarDAO;
import com.biblioteca.dao.LivroDAO;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.dao.impl.EmprestimoDAOImpl;
import com.biblioteca.dao.impl.ExemplarDAOImpl;
import com.biblioteca.dao.impl.LivroDAOImpl;
import com.biblioteca.dao.impl.UsuarioDAOImpl;
import com.biblioteca.service.EmprestimoService;
import com.biblioteca.service.ExemplarService;
import com.biblioteca.service.LivroService;
import com.biblioteca.service.UsuarioService;
import com.biblioteca.ui.MenuEmprestimo;
import com.biblioteca.ui.MenuExemplar;
import com.biblioteca.ui.MenuLivro;
import com.biblioteca.ui.MenuPrincipal;
import com.biblioteca.ui.MenuUsuario;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            LivroDAO livroDAO = new LivroDAOImpl();
            UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
            ExemplarDAO exemplarDAO = new ExemplarDAOImpl();
            EmprestimoDAO emprestimoDAO = new EmprestimoDAOImpl();

            LivroService livroService = new LivroService(livroDAO);
            UsuarioService usuarioService = new UsuarioService(usuarioDAO);
            ExemplarService exemplarService = new ExemplarService(exemplarDAO, livroService, emprestimoDAO);
            EmprestimoService emprestimoService = new EmprestimoService(
                    emprestimoDAO, exemplarService, usuarioService);

            MenuLivro menuLivro = new MenuLivro(scanner, livroService);
            MenuUsuario menuUsuario = new MenuUsuario(scanner, usuarioService);
            MenuExemplar menuExemplar = new MenuExemplar(scanner, exemplarService, livroService);
            MenuEmprestimo menuEmprestimo = new MenuEmprestimo(scanner, emprestimoService);

            new MenuPrincipal(scanner, menuLivro, menuUsuario, menuExemplar, menuEmprestimo).iniciar();
        }
    }
}
