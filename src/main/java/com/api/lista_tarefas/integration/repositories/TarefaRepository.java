package com.api.lista_tarefas.integration.repositories;

import com.api.lista_tarefas.domain.entities.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Integer> {
}
