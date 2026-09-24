package com.biblioteca.dao;

import java.util.List;
import java.util.Optional;

import com.biblioteca.model.Livro;

public interface LivroDAO {
    
    Livro salvar(Livro livro);

    Optional<Livro> buscarPorId(Long id);

    List<Livro> buscarTodos();

    void atualizar(Livro livro);

    void deletar(Long id);
}
