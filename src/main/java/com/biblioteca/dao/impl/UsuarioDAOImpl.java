package com.biblioteca.dao.impl;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.biblioteca.config.ConnectionFactory;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.exception.DAOException;
import com.biblioteca.model.Usuario;

public class UsuarioDAOImpl implements UsuarioDAO {

    private static final String SELECT_USUARIO = """
            SELECT
                id,
                nome,
                cpf,
                email,
                telefone,
                ativo,
                criado_em,
                atualizado_em
            FROM usuarios
            """;

    @Override
    public Usuario salvar(Usuario usuario) {
        String sql = """
                INSERT INTO usuarios (
                    nome,
                    cpf,
                    email,
                    telefone
                ) VALUES (?, ?, ?, ?)
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getCpf());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getTelefone());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setId(rs.getLong(1));
                } else {
                    throw new DAOException("Não foi possível obter o ID gerado para o usuário.");
                }
            }

            return usuario;

        } catch (SQLException e) {
            throw new DAOException("Já existe um usuário com este CPF.", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        String sql = SELECT_USUARIO + " WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extrairUsuario(rs));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar usuário pelo ID: " + id, e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorCpf(String cpf) {
        String sql = SELECT_USUARIO + " WHERE id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cpf);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extrairUsuario(rs));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar usuário no banco de dados.", e);
        }
    }

    @Override
    public List<Usuario> buscarTodos() {
        List<Usuario> usuarios = new ArrayList<>();

        String sql = SELECT_USUARIO + " ORDER BY id";
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(extrairUsuario(rs));
                }
            }

            return usuarios;

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar usuários no banco de dados.", e);
        }
    }

    @Override
    public boolean atualizar(Usuario usuario) {
        String sql = """
                UPDATE usuarios
                SET id = ?
                    nome = ?,
                    email = ?,
                    telefone = ?,
                    ativo = ?,
                    atualizado_em = NOW()
                WHERE id = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getTelefone());
            ps.setBoolean(4, usuario.isAtivo());
            ps.setLong(5, usuario.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar usuário no banco de dados", e);
        }
    }

    @Override
    public boolean deletar(Long id) {
        String sql = """
                    DELETE FROM usuarios
                    WHERE id = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Erro ao apagar usuário no banco de dados.", e);
        }
    }

    private Usuario extrairUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("cpf"),
                rs.getString("email"),
                rs.getString("telefone"),
                rs.getBoolean("ativo"),
                rs.getObject("criado_em", OffsetDateTime.class),
                rs.getObject("atualizado_em", OffsetDateTime.class));
    }

}
