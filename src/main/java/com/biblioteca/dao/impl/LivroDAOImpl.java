package com.biblioteca.dao.impl;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.biblioteca.config.ConnectionFactory;
import com.biblioteca.dao.LivroDAO;
import com.biblioteca.exception.DAOException;
import com.biblioteca.model.Livro;

public class LivroDAOImpl implements LivroDAO {

    @Override
    public Livro salvar(Livro livro) {
        String sql = """
                    INSERT INTO livros (
                    titulo,
                    autor,
                    isbn,
                    ano_publicacao,
                    categoria
                    ) VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setString(3, livro.getIsbn());
            ps.setInt(4, livro.getAnoPublicacao());
            ps.setString(5, livro.getCategoria());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    livro.setId(rs.getLong(1));
                }

            }

            return livro;

        } catch (SQLException e) {
            throw new DAOException("Erro ao salvar livro na base de dados.", e);
        }
    }

    @Override
    public Optional<Livro> buscarPorId(Long id) {
        String sql = """
                    SELECT
                        id,
                        titulo,
                        autor,
                        isbn,
                        ano_publicacao,
                        categoria,
                        criado_em,
                        atualizado_em
                    FROM livros
                    WHERE id = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extrairLivro(rs));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar livro na base de dados.", e);
        }
    }

    @Override
    public List<Livro> buscarTodos() {
        List<Livro> livros = new ArrayList<>();

        String sql = """
                    SELECT
                        id,
                        titulo,
                        autor,
                        isbn,
                        ano_publicacao,
                        categoria,
                        criado_em,
                        atualizado_em
                    FROM livros
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                livros.add(extrairLivro(rs));
            }

            return livros;

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar livros no banco de dados.", e);
        }
    }

    @Override
    public boolean atualizar(Livro livro) {
        String sql = """
                    UPDATE livros
                    SET titulo = ?,
                        autor = ?,
                        isbn = ?,
                        ano_publicacao = ?,
                        categoria = ?
                    WHERE id = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, livro.getTitulo());
            ps.setString(2, livro.getAutor());
            ps.setString(3, livro.getIsbn());
            ps.setInt(4, livro.getAnoPublicacao());
            ps.setString(5, livro.getCategoria());
            ps.setLong(6, livro.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar livro no banco de dados", e);
        }
    }

    @Override
    public boolean deletar(Long id) {
        String sql = """
                    DELETE FROM livros
                    WHERE id = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DAOException("Erro ao apagar livro no banco de dados.", e);
        }
    }

    private Livro extrairLivro(ResultSet rs) throws SQLException {
        return new Livro(
                rs.getLong("id"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("isbn"),
                rs.getObject("ano_publicacao", Integer.class),
                rs.getString("categoria"),
                rs.getObject("criado_em", OffsetDateTime.class),
                rs.getObject("atualizado_em", OffsetDateTime.class));
    }
}