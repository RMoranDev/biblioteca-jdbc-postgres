package com.biblioteca;

import com.biblioteca.config.ConnectionFactory;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Application {

    public static void main(String[] args) {
        System.out.println("=== Testando Conexão com o PostgreSQL ===");

        try (Connection conn = ConnectionFactory.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Conexão estabelecida com sucesso!");

                // Exibe informações do banco de dados conectado
                DatabaseMetaData metaData = conn.getMetaData();
                System.out.println("Banco de Dados: " + metaData.getDatabaseProductName());
                System.out.println("Versão: " + metaData.getDatabaseProductVersion());
                System.out.println("Usuário: " + metaData.getUserName());
                System.out.println("URL: " + metaData.getURL());

                // Teste de consulta simples na tabela livros
                String sql = "SELECT COUNT(*) AS total FROM livros";
                try (PreparedStatement stmt = conn.prepareStatement(sql);
                     ResultSet rs = stmt.executeQuery()) {

                    if (rs.next()) {
                        long totalLivros = rs.getLong("total");
                        System.out.println("✓ Tabela 'livros' acessível. Total de registros: " + totalLivros);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Falha ao conectar ao banco de dados!");
            System.err.println("Código de erro SQL: " + e.getSQLState());
            System.err.println("Mensagem: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("✗ Erro de configuração ou inicialização:");
            System.err.println("Mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }
}