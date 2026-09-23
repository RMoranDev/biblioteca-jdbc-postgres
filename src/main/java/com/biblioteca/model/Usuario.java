package com.biblioteca.model;

import java.time.OffsetDateTime;
import java.util.Objects;

import com.biblioteca.util.CpfValidator;

public class Usuario {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private Boolean ativo;
    private OffsetDateTime criadoEm;
    private OffsetDateTime atualizadoEm;

    // Construtor padrão
    public Usuario() {
    }

    // Construtor para cadastro inicial (ativo por padrão)
    public Usuario(String nome, String cpf, String email, String telefone) {
        this.nome = Objects.requireNonNull(nome, "Nome é obrigatório");
        validarEAtribuirCpf(cpf);
        this.email = Objects.requireNonNull(email, "E-mail é obrigatório");
        this.telefone = telefone;
        this.ativo = true;
    }

    // Construtor completo (usado pelo DAO ao mapear o ResultSet)
    public Usuario(Long id, String nome, String cpf, String email, String telefone, Boolean ativo,
            OffsetDateTime criadoEm, OffsetDateTime atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        validarEAtribuirCpf(cpf);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
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

    private void validarEAtribuirCpf(String cpf) {
        if (!CpfValidator.isValid(cpf)) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }
        this.cpf = CpfValidator.apenasDigitos(cpf);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", ativo=" + ativo +
                '}';
    }

}