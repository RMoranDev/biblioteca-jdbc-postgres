package com.biblioteca.dao.imp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.biblioteca.config.ConnectionFactory;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.exception.DAOException;
import com.biblioteca.model.Usuario;

public class UsuarioDAOImpl implements UsuarioDAO {

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
                }
            }

            return usuario;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar usuário", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {

        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "ID deve ser maior que zero.");
        }

        String sql = """
                    SELECT *
                    FROM usuarios
                    WHERE id = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    var usuario = new Usuario();
                    usuario.setId(rs.getLong("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setCpf(rs.getString("cpf"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setTelefone(rs.getString("telefone"));
                    usuario.setAtivo(rs.getBoolean("ativo"));
                    return Optional.of(usuario);
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar usuário no banco de dados.", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorCpf(String cpf) {

        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException(
                    "CPF não pode ser nulo ou vazio.");
        }

        String sql = """
                    SELECT *
                    FROM usuarios
                    WHERE cpf = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cpf);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    var usuario = new Usuario();
                    usuario.setId(rs.getLong("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setCpf(rs.getString("cpf"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setTelefone(rs.getString("telefone"));
                    usuario.setAtivo(rs.getBoolean("ativo"));
                    return Optional.of(usuario);
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar usuário no banco de dados.", e);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();

        String sql = """
                    SELECT *
                    FROM usuarios
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    var usuario = new Usuario();
                    usuario.setId(rs.getLong("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setCpf(rs.getString("cpf"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setTelefone(rs.getString("telefone"));
                    usuario.setAtivo(rs.getBoolean("ativo"));
                    usuarios.add(usuario);
                }
            }

            return usuarios;

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar usuários no banco de dados.", e);
        }
    }

    @Override
    public void atualizar(Usuario usuario) {
        String sql = """
                            UPDATE usuarios
                SET nome = ?,
                    email = ?,
                    telefone = ?,
                    ativo = ?,
                    atualizado_em = NOW()
                WHERE id = ?;
                        """;

        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getTelefone());
            ps.setBoolean(4, usuario.isAtivo());
            ps.setLong(5, usuario.getId());

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new DAOException("Nenhum usuário encontrado com o ID: " + usuario.getId());
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar usuário no banco de dados", e);
        }
    }

    @Override 
    public void deletar(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID deve ser maior do que zero!");
        }   

        String sql = """
            DELETE FROM usuarios
            WHERE id = ?
        """;
        try (Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao apagar usuário no banco de dados.", e);
        }
    }

}
