package com.biblioteca;

import java.util.Optional;

import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.dao.imp.UsuarioDAOImpl;
import com.biblioteca.model.Usuario;

public class Application {

    public static void main(String[] args) {
        // CADASTRAR USUARIO
        // UsuarioDAO userDAO = new UsuarioDAOImpl();

        // Usuario user = new Usuario();
        // user.setNome("Carlos Silva");
        // user.setCpf("10000000019");
        // user.setEmail("carlos.silva@email.com");
        // user.setTelefone("41988887777");

        // userDAO.salvar(user);


        // DELETAR USUARIO
        UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

        Optional<Usuario> usuarioEncontrado = usuarioDAO.buscarPorId(1L);

        // if (usuarioEncontrado.isPresent()) {
        //     Usuario usuario = usuarioEncontrado.get();

        //     usuarioDAO.deletar(usuario.getId());
        //     System.out.println("Usuario deletado com sucesso!");

        // } else {
        //     System.out.println("Nenhum usuário encontrado com esse ID.");
        // }

        // BUSCAR USUARIO
        if (usuarioEncontrado.isPresent()) {

            Usuario usuario = usuarioEncontrado.get();
            System.out.println(usuario);
        } else {
             System.out.println("Nenhum usuário encontrado com esse ID.");
        }

    }
}