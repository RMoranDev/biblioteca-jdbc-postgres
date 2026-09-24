package com.biblioteca.dao;

import java.util.List;
import java.util.Optional;

import com.biblioteca.model.Usuario;

public interface UsuarioDAO {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorCpf(String cpf);

    List<Usuario> buscarTodos();

    void atualizar(Usuario usuario);

    void deletar(Long id);
}
