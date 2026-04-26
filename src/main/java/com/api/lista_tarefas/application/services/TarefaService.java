package com.api.lista_tarefas.application.services;

import com.api.lista_tarefas.domain.dtos.request.TarefaRequestDTO;
import com.api.lista_tarefas.domain.dtos.response.TarefaResponseDTO;
import com.api.lista_tarefas.domain.entities.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TarefaService {

    Page<TarefaResponseDTO> findAll(Integer pageNumber, Integer pageSize);
    TarefaResponseDTO findById(Integer id);
    TarefaResponseDTO create(TarefaRequestDTO tarefa);

    TarefaResponseDTO update(Integer id, TarefaRequestDTO tarefa);

    void delete(Integer id);
}
