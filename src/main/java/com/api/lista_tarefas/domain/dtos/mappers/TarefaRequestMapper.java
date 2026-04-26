package com.api.lista_tarefas.domain.dtos.mappers;

import com.api.lista_tarefas.domain.dtos.request.TarefaRequestDTO;
import com.api.lista_tarefas.domain.entities.Tarefa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TarefaRequestMapper {
    @Mapping(target = "id", ignore = true)
    Tarefa toEntity(TarefaRequestDTO dto);
}
