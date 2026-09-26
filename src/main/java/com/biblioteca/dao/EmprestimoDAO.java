package com.biblioteca.dao;

import java.util.List;
import java.util.Optional;

import com.biblioteca.model.Emprestimo;

public interface EmprestimoDAO {

    Emprestimo salvar(Emprestimo emprestimo);

    Optional<Emprestimo> buscarPorId(Long id);

    List<Emprestimo> listarTodos();

    void atualizar(Emprestimo emprestimo);

    void deletar(Long id);
}
