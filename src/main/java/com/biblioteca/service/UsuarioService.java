package com.biblioteca.service;

import java.util.List;
import java.util.Optional;

import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.exception.RegraNegocioException;
import com.biblioteca.exception.UsuarioNaoEncontradoException;
import com.biblioteca.model.Usuario;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario cadastrar(Usuario usuario) {
        if (usuarioDAO.buscarPorCpf(usuario.getCpf()).isPresent()) {
            throw new RegraNegocioException("Já existe um usuário com este CPF.");
        }

        return usuarioDAO.salvar(usuario);
    }

    public Optional<Usuario> buscarPorId(Long id) {

        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }

        return usuarioDAO.buscarPorId(id);
    }

    public Optional<Usuario> buscarPorCpf(String cpf) {

        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF não pode ser nulo ou vazio.");
        }

        return usuarioDAO.buscarPorCpf(cpf);
    }

    public List<Usuario> buscarTodos() {
        return usuarioDAO.buscarTodos();
    }

    public void atualizar(Usuario usuario) {

        Optional<Usuario> usuarioCPF = usuarioDAO.buscarPorCpf(usuario.getCpf());

        if (usuarioCPF.isPresent() && !usuarioCPF.get().getId().equals(usuario.getId())) {
            throw new RegraNegocioException("O CPF informado já está cadastrado.");
        }

        boolean atualizado = usuarioDAO.atualizar(usuario);
        if (!atualizado) {
            throw new UsuarioNaoEncontradoException(usuario.getId());
        }
    }

    public void deletar(Long id) {
        boolean deletado = usuarioDAO.deletar(id);
        if (!deletado) {
            throw new UsuarioNaoEncontradoException(id);
        }
    }
}
