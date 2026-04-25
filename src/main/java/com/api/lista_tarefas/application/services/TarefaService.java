package com.api.lista_tarefas.application.services;

import com.api.lista_tarefas.domain.entities.Tarefa;

import java.util.List;
import java.util.Optional;

public interface TarefaService {

    List<Tarefa> findAll();
    Optional<Tarefa> findById(Integer id);
    Tarefa create(Tarefa tarefa);

    Tarefa update(Integer id, Tarefa tarefa) throws Exception;

    void delete(Integer id);
}
