package com.biblioteca.dao.impl;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.biblioteca.config.ConnectionFactory;
import com.biblioteca.dao.ExemplarDAO;
import com.biblioteca.exception.DAOException;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Livro;
import com.biblioteca.model.StatusExemplar;

public class ExemplarDAOImpl implements ExemplarDAO {

    @Override
    public Exemplar salvar(Exemplar exemplar) {
        String sql = """
                    INSERT INTO exemplares (
                    livro_id,
                    codigo_patrimonio,
                    status,
                    observacoes
                    ) VALUES (?, ?, ?, ?)
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(
                        sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, exemplar.getLivroId());
            ps.setString(2, exemplar.getCodigoPatrimonio());
            ps.setString(3, exemplar.getStatus().name());
            ps.setString(4, exemplar.getObservacoes());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    exemplar.setId(rs.getLong(1));
                }
            }

            return exemplar;

        } catch (SQLException e) {
            throw new DAOException("Erro ao salvar exemplar na base de dados.", e);
        }
    }

    @Override
    public Optional<Exemplar> buscarPorId(Long id) {
        String sql = """
                SELECT
                    e.id,
                    e.codigo_patrimonio,
                    e.status,
                    e.observacoes,
                    e.criado_em,
                    e.atualizado_em,

                    l.id AS livro_id,
                    l.titulo AS livro_titulo,
                    l.autor AS livro_autor,
                    l.isbn AS livro_isbn,
                    l.ano_publicacao AS livro_ano_publicacao,
                    l.categoria AS livro_categoria,
                    l.criado_em AS livro_criado_em,
                    l.atualizado_em AS livro_atualizado_em

                FROM exemplares e
                JOIN livros l ON l.id = e.livro_id
                WHERE e.id = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extrairExemplar(rs));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar exemplar na base de dados", e);
        }
    }

    @Override
    public List<Exemplar> listarTodos() {
        List<Exemplar> exemplares = new ArrayList<>();

        String sql = """
                SELECT
                    e.id,
                    e.codigo_patrimonio,
                    e.status,
                    e.observacoes,
                    e.criado_em,
                    e.atualizado_em,

                    l.id AS livro_id,
                    l.titulo AS livro_titulo,
                    l.autor AS livro_autor,
                    l.isbn AS livro_isbn,
                    l.ano_publicacao AS livro_ano_publicacao,
                    l.categoria AS livro_categoria,
                    l.criado_em AS livro_criado_em,
                    l.atualizado_em AS livro_atualizado_em

                FROM exemplares e
                JOIN livros l ON l.id = e.livro_id
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                exemplares.add(extrairExemplar(rs));
            }

            return exemplares;

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar exemplares no banco de dados.", e);
        }
    }

    @Override
    public void atualizar(Exemplar exemplar) {
        String sql = """
                UPDATE exemplares
                SET livro_id = ?,
                    codigo_patrimonio = ?,
                    status = ?,
                    observacoes = ?
                WHERE id = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, exemplar.getLivroId());
            ps.setString(2, exemplar.getCodigoPatrimonio());
            ps.setString(3, exemplar.getStatus().name());
            ps.setString(4, exemplar.getObservacoes());
            ps.setLong(5, exemplar.getId());

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new DAOException("Nenhum exemplar atualizado. ID não encontrado: " + exemplar.getId());
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar o exemplar de ID: " + exemplar.getId(), e);
        }
    }

    @Override
    public void deletar(Long id) {
        String sql = """
                    DELETE FROM exemplares
                    WHERE id = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new DAOException("Nenhum exemplar deletado. ID não encontrado: " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao deletar o exemplar de ID: " + id, e);
        }
    }

    private Exemplar extrairExemplar(ResultSet rs) throws SQLException {
        Livro livro = new Livro(
                rs.getLong("livro_id"),
                rs.getString("livro_titulo"),
                rs.getString("livro_autor"),
                rs.getString("livro_isbn"),
                rs.getInt("livro_ano_publicacao"),
                rs.getString("livro_categoria"),
                rs.getObject("livro_criado_em", OffsetDateTime.class),
                rs.getObject("livro_atualizado_em", OffsetDateTime.class));

        return new Exemplar(
                rs.getLong("id"),
                livro,
                rs.getString("codigo_patrimonio"),
                StatusExemplar.valueOf(rs.getString("status")),
                rs.getString("observacoes"),
                rs.getObject("criado_em", OffsetDateTime.class),
                rs.getObject("atualizado_em", OffsetDateTime.class));
    }
}
