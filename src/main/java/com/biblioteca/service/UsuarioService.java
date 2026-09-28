package com.biblioteca.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.exception.RegraNegocioException;
import com.biblioteca.exception.UsuarioNaoEncontradoException;
import com.biblioteca.model.Usuario;
import com.biblioteca.util.CpfValidator;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = Objects.requireNonNull(
                usuarioDAO, "UsuarioDAO não pode ser nulo.");
    }

    public Usuario buscarPorId(Long id) {
        validarId(id);

        return usuarioDAO.buscarPorId(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    public Usuario cadastrar(Usuario usuario) {
        Objects.requireNonNull(
                usuario, "Usuário não pode ser nulo.");

        String cpf = validarCpf(usuario.getCpf());
        usuario.setCpf(cpf);

        if (usuarioDAO.buscarPorCpf(cpf).isPresent()) {
            throw new RegraNegocioException(
                    "Já existe um usuário cadastrado com este CPF.");
        }

        return usuarioDAO.salvar(usuario);
    }

    public Optional<Usuario> buscarPorCpf(String cpf) {
        return usuarioDAO.buscarPorCpf(validarCpf(cpf));
    }

    public List<Usuario> buscarTodos() {
        return usuarioDAO.buscarTodos();
    }

    public void atualizar(Usuario usuario) {
        Objects.requireNonNull(
                usuario, "Usuário não pode ser nulo.");

        validarId(usuario.getId());

        String cpf = validarCpf(usuario.getCpf());
        usuario.setCpf(cpf);

        // Verifica se o usuário existe.
        buscarPorId(usuario.getId());

        Optional<Usuario> usuarioCPF =
                usuarioDAO.buscarPorCpf(cpf);

        if (usuarioCPF.isPresent()
                && !usuarioCPF.get().getId().equals(usuario.getId())) {
            throw new RegraNegocioException(
                    "O CPF informado já está cadastrado.");
        }

        boolean atualizado = usuarioDAO.atualizar(usuario);

        if (!atualizado) {
            throw new UsuarioNaoEncontradoException(usuario.getId());
        }
    }

    public void deletar(Long id) {
        validarId(id);

        buscarPorId(id);

        usuarioDAO.deletar(id);
    }

    private void validarId(Long id) {
        Objects.requireNonNull(id, "ID não pode ser nulo.");

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "ID deve ser maior que zero.");
        }
    }

    private String validarCpf(String cpf) {
        if (!CpfValidator.isValid(cpf)) {
            throw new IllegalArgumentException(
                    "CPF inválido.");
        }

        return CpfValidator.apenasDigitos(cpf);
    }
}