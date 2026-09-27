package com.biblioteca.dao;

import java.util.List;
import java.util.Optional;

import com.biblioteca.model.Exemplar;

public interface ExemplarDAO {

    Exemplar salvar(Exemplar exemplar);

    Optional<Exemplar> buscarPorId(Long id);

    List<Exemplar> buscarTodos();

    boolean atualizar(Exemplar exemplar);

    boolean deletar(Long id);
}
