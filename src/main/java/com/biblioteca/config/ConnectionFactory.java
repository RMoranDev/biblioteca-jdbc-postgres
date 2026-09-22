package com.biblioteca.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = ConnectionFactory.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {

            if (input == null) {
                throw new IllegalStateException("Ficheiro db.properties não foi encontrado na pasta resources!");
            }

            props.load(input);

            // Carrega explicitamente o driver do PostgreSQL
            // Class.forName(props.getProperty("db.driver", "org.postgresql.Driver"));


        } catch (IOException e) {
            throw new RuntimeException("Erro fatal ao carregar o arquivo db.properties", e);
        }
        // } catch (ClassNotFoundException e) {
        //     throw new RuntimeException("Driver JDBC do PostgreSQL não encontrado no classpath!", e);
        // }
    }

    private ConnectionFactory() {
    }

    /**
     * Abre e devolve uma ligação ativa com o PostgreSQL.
     * 
     * @return Connection ativa (nunca devolve null).
     * @throws SQLException se houver falha de autenticação ou rede.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password"));
    }
}
