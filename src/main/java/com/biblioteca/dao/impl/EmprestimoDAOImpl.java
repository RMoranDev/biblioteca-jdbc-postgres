package com.biblioteca.dao.impl;

import java.sql.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.biblioteca.config.ConnectionFactory;
import com.biblioteca.dao.EmprestimoDAO;
import com.biblioteca.exception.DAOException;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Livro;
import com.biblioteca.model.StatusEmprestimo;
import com.biblioteca.model.StatusExemplar;
import com.biblioteca.model.Usuario;

public class EmprestimoDAOImpl implements EmprestimoDAO {

    public Emprestimo salvar(Emprestimo emprestimo) {
        String sql = """
                            INSERT INTO emprestimos (
                    exemplar_id,
                    usuario_id,
                    data_emprestimo,
                    data_prevista_devolucao,
                    data_devolucao_efetiva,
                    status,
                    multa_paga
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                        """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, emprestimo.getExemplarId());
            ps.setLong(2, emprestimo.getUsuarioId());
            ps.setObject(3, emprestimo.getDataEmprestimo());
            ps.setObject(4, emprestimo.getDataPrevistaDevolucao());
            ps.setObject(5, emprestimo.getDataDevolucaoEfetiva());
            ps.setString(6, emprestimo.getStatus().name());
            ps.setBigDecimal(7, emprestimo.getMultaPaga());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    emprestimo.setId(rs.getLong(1));
                }
            }

            return emprestimo;

        } catch (SQLException e) {
            throw new DAOException("Erro ao salvar emprestimo na base de dados.", e);
        }
    }

    public Optional<Emprestimo> buscarPorId(Long id) {
        String sql = """
                SELECT
                    -- Empréstimo
                    e.id AS emprestimo_id,
                    e.data_emprestimo,
                    e.data_prevista_devolucao,
                    e.data_devolucao_efetiva,
                    e.status AS emprestimo_status,
                    e.multa_paga,
                    e.criado_em AS emprestimo_criado_em,
                    e.atualizado_em AS emprestimo_atualizado_em,

                    -- Exemplar
                    ex.id AS exemplar_id,
                    ex.codigo_patrimonio,
                    ex.status AS exemplar_status,
                    ex.observacoes,
                    ex.criado_em AS exemplar_criado_em,
                    ex.atualizado_em AS exemplar_atualizado_em,

                    -- Livro
                    l.id AS livro_id,
                    l.titulo AS livro_titulo,
                    l.autor AS livro_autor,
                    l.isbn AS livro_isbn,
                    l.ano_publicacao AS livro_ano_publicacao,
                    l.categoria AS livro_categoria,
                    l.criado_em AS livro_criado_em,
                    l.atualizado_em AS livro_atualizado_em,

                    -- Usuário
                    u.id AS usuario_id,
                    u.nome AS usuario_nome,
                    u.cpf AS usuario_cpf,
                    u.email AS usuario_email,
                    u.telefone AS usuario_telefone,
                    u.ativo AS usuario_ativo,
                    u.criado_em AS usuario_criado_em,
                    u.atualizado_em AS usuario_atualizado_em

                FROM emprestimos e
                JOIN exemplares ex ON ex.id = e.exemplar_id
                JOIN livros l ON l.id = ex.livro_id
                JOIN usuarios u ON u.id = e.usuario_id
                WHERE e.id = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(extrairEmprestimo(rs));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar emprestimo na base de dados.", e);
        }
    }

    public List<Emprestimo> listarTodos() {
        List<Emprestimo> emprestimo = new ArrayList<>();

        String sql = """
                    SELECT
                    e.id AS emprestimo_id,
                    e.data_emprestimo,
                    e.data_prevista_devolucao,
                    e.data_devolucao_efetiva,
                    e.status AS emprestimo_status,
                    e.multa_paga,
                    e.criado_em AS emprestimo_criado_em,
                    e.atualizado_em AS emprestimo_atualizado_em,

                    ex.id AS exemplar_id,
                    ex.codigo_patrimonio,
                    ex.status AS exemplar_status,
                    ex.observacoes,
                    ex.criado_em AS exemplar_criado_em,
                    ex.atualizado_em AS exemplar_atualizado_em,

                    l.id AS livro_id,
                    l.titulo AS livro_titulo,
                    l.autor AS livro_autor,
                    l.isbn AS livro_isbn,
                    l.ano_publicacao AS livro_ano_publicacao,
                    l.categoria AS livro_categoria,
                    l.criado_em AS livro_criado_em,
                    l.atualizado_em AS livro_atualizado_em,

                    u.id AS usuario_id,
                    u.nome AS usuario_nome,
                    u.cpf AS usuario_cpf,
                    u.email AS usuario_email,
                    u.telefone AS usuario_telefone,
                    u.ativo AS usuario_ativo,
                    u.criado_em AS usuario_criado_em,
                    u.atualizado_em AS usuario_atualizado_em

                FROM emprestimos e
                JOIN exemplares ex ON ex.id = e.exemplar_id
                JOIN livros l ON l.id = ex.livro_id
                JOIN usuarios u ON u.id = e.usuario_id
                ORDER BY e.id
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                emprestimo.add(extrairEmprestimo(rs));
            }

            return emprestimo;

        } catch (Exception e) {
            throw new DAOException("Erro ao listar emprestimos no banco de dados.", e);
        }
    }

    public void atualizar(Emprestimo emprestimo) {
        String sql = """
                            UPDATE emprestimos
                SET
                    exemplar_id = ?,
                    usuario_id = ?,
                    data_emprestimo = ?,
                    data_prevista_devolucao = ?,
                    data_devolucao_efetiva = ?,
                    status = ?,
                    multa_paga = ?
                WHERE id = ?;
                        """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, emprestimo.getExemplarId());
            ps.setLong(2, emprestimo.getUsuarioId());
            ps.setObject(3, emprestimo.getDataEmprestimo());
            ps.setObject(4, emprestimo.getDataPrevistaDevolucao());
            ps.setObject(5, emprestimo.getDataDevolucaoEfetiva());
            ps.setString(6, emprestimo.getStatus().name());
            ps.setBigDecimal(7, emprestimo.getMultaPaga());
            ps.setLong(8, emprestimo.getId());

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new DAOException("Nenhum emprestimo atualizado. ID não encontrado: " + emprestimo.getId());
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar o emprestimo de ID: " + emprestimo.getId(), e);
        }
    }

    public void deletar(Long id) {
        String sql = """
                    DELETE FROM emprestimos
                    WHERE id = ?
                """;
        try (Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            int linhasAfetadas = ps.executeUpdate();

            if (linhasAfetadas == 0) {
                throw new DAOException("Nenhum emprestimo deletado. ID não encontrado: " + id);
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao deletar o emprestimo de ID: " + id, e);
        }
    }

    private Emprestimo extrairEmprestimo(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario(
                rs.getLong("usuario_id"),
                rs.getString("usuario_nome"),
                rs.getString("usuario_cpf"),
                rs.getString("usuario_email"),
                rs.getString("usuario_telefone"),
                rs.getBoolean("usuario_ativo"),
                rs.getObject("usuario_criado_em", OffsetDateTime.class),
                rs.getObject("usuario_atualizado_em", OffsetDateTime.class));

        Livro livro = new Livro(
                rs.getLong("livro_id"),
                rs.getString("livro_titulo"),
                rs.getString("livro_autor"),
                rs.getString("livro_isbn"),
                rs.getInt("livro_ano_publicacao"),
                rs.getString("livro_categoria"),
                rs.getObject("livro_criado_em", OffsetDateTime.class),
                rs.getObject("livro_atualizado_em", OffsetDateTime.class));

        Exemplar exemplar = new Exemplar(
                rs.getLong("exemplar_id"),
                livro,
                rs.getString("codigo_patrimonio"),
                StatusExemplar.valueOf(rs.getString("exemplar_status")),
                rs.getString("observacoes"),
                rs.getObject("exemplar_criado_em", OffsetDateTime.class),
                rs.getObject("exemplar_atualizado_em", OffsetDateTime.class));

        return new Emprestimo(
                rs.getLong("emprestimo_id"),
                exemplar,
                usuario,
                rs.getObject("data_emprestimo", OffsetDateTime.class),
                rs.getObject("data_prevista_devolucao", LocalDate.class),
                rs.getObject("data_devolucao_efetiva", OffsetDateTime.class),
                StatusEmprestimo.valueOf(rs.getString("emprestimo_status")),
                rs.getBigDecimal("multa_paga"),
                rs.getObject("emprestimo_criado_em", OffsetDateTime.class),
                rs.getObject("emprestimo_atualizado_em", OffsetDateTime.class));
    }
}
