package com.biblioteca.model;

import java.time.OffsetDateTime;
import java.time.Year;
import java.util.Objects;
import java.util.regex.Pattern;

public class Livro {
    private static final int TITULO_MAX_LENGTH = 500;
    private static final int AUTOR_MAX_LENGTH = 300;
    private static final int CATEGORIA_MAX_LENGTH = 100;
    private static final int ANO_PUBLICACAO_MINIMO = 1450;

    private static final Pattern ISBN_PATTERN = Pattern.compile("^(?:\\d{9}[\\dXx]|\\d{13})$");
    private static final Pattern CARACTERES_ISBN_REMOVER = Pattern.compile("[-\\s]");

    private Long id;
    private String titulo;
    private String isbn;
    private String autor;
    private Integer anoPublicacao;
    private String categoria;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;

    public Livro() {
    }

    public Livro(String titulo, String autor, String isbn, Integer anoPublicacao, String categoria) {
        setTitulo(titulo);
        setAutor(autor);
        setIsbn(isbn);
        setAnoPublicacao(anoPublicacao);
        setCategoria(categoria);
    }

    public Livro(Long id, String titulo, String autor, String isbn, Integer anoPublicacao,
            String categoria, OffsetDateTime criadoEm, OffsetDateTime atualizadoEm) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.anoPublicacao = anoPublicacao;
        this.categoria = categoria;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        validarTitulo(titulo);
        this.titulo = titulo.trim();
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        String normalizado = normalizarIsbn(isbn);
        validarIsbn(normalizado);
        this.isbn = normalizado;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        validarAutor(autor);
        this.autor = autor.trim();
    }

    public Integer getAnoPublicacao() {
        return anoPublicacao;
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        validarAnoPublicacao(anoPublicacao);
        this.anoPublicacao = anoPublicacao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        validarCategoria(categoria);
        this.categoria = categoria.trim();
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(OffsetDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public OffsetDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(OffsetDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    private void validarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Título não pode ser nulo ou vazio.");
        }
        if (titulo.length() > TITULO_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Título excede o tamanho máximo de " + TITULO_MAX_LENGTH + " caracteres.");
        }
    }

    private void validarAutor(String autor) {
        if (autor == null || autor.isBlank()) {
            throw new IllegalArgumentException("Autor não pode ser nulo ou vazio.");
        }
        if (autor.length() > AUTOR_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Autor excede o tamanho máximo de " + AUTOR_MAX_LENGTH + " caracteres.");
        }
    }

    private void validarCategoria(String categoria) {
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("Categoria não pode ser nula ou vazia.");
        }
        if (categoria.length() > CATEGORIA_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "Categoria excede o tamanho máximo de " + CATEGORIA_MAX_LENGTH + " caracteres.");
        }
    }

    private void validarAnoPublicacao(Integer anoPublicacao) {
        if (anoPublicacao == null) {
            throw new IllegalArgumentException("Ano de publicação não pode ser nulo.");
        }
        int anoAtual = Year.now().getValue();
        if (anoPublicacao < ANO_PUBLICACAO_MINIMO || anoPublicacao > anoAtual) {
            throw new IllegalArgumentException(
                    "Ano de publicação deve estar entre " + ANO_PUBLICACAO_MINIMO + " e " + anoAtual + ".");
        }
    }

    private String normalizarIsbn(String rawIsbn) {
        if (rawIsbn == null || rawIsbn.isBlank()) {
            throw new IllegalArgumentException("ISBN não pode ser nulo ou vazio.");
        }
        return CARACTERES_ISBN_REMOVER.matcher(rawIsbn).replaceAll("").toUpperCase();
    }

    private void validarIsbn(String isbnNormalizado) {
        if (!ISBN_PATTERN.matcher(isbnNormalizado).matches()) {
            throw new IllegalArgumentException(
                    "ISBN inválido: deve ter 10 ou 13 dígitos (hífens e espaços são ignorados).");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Livro outro))
            return false;
        return isbn != null && isbn.equals(outro.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", isbn='" + isbn + '\'' +
                ", anoPublicacao=" + anoPublicacao +
                ", categoria='" + categoria + '\'' +
                ", criadoEm=" + criadoEm +
                ", atualizadoEm=" + atualizadoEm +
                '}';
    }
}