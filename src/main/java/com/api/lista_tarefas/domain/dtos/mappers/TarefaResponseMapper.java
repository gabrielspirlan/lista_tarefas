package com.api.lista_tarefas.domain.dtos.mappers;

import com.api.lista_tarefas.domain.dtos.response.TarefaResponseDTO;
import com.api.lista_tarefas.domain.entities.Tarefa;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TarefaResponseMapper {
    TarefaResponseDTO toDTO(Tarefa tarefa);
}
