package com.api.lista_tarefas.application.services;

import com.api.lista_tarefas.domain.dtos.mappers.TarefaRequestMapper;
import com.api.lista_tarefas.domain.dtos.mappers.TarefaResponseMapper;
import com.api.lista_tarefas.domain.dtos.request.TarefaRequestDTO;
import com.api.lista_tarefas.domain.dtos.response.TarefaResponseDTO;
import com.api.lista_tarefas.domain.entities.Tarefa;
import com.api.lista_tarefas.domain.exceptions.NotFoundException;
import com.api.lista_tarefas.infraestructure.repositories.TarefaRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@AllArgsConstructor
public class TarefaServiceImpl implements TarefaService{

    private final TarefaRepository tarefaRepository;
    private final TarefaRequestMapper tarefaRequestMapper;
    private final TarefaResponseMapper tarefaResponseMapper;

    @Override
    public Page<TarefaResponseDTO> findAll(Integer pageNumber, Integer pageSize) {

        if (pageNumber == null) {
            pageNumber = 0;
        }

        if (pageSize == null) {
            pageSize = 10;
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return tarefaRepository.findAll(pageable)
                .map(tarefaResponseMapper::toDTO);
    }

    @Override
    public TarefaResponseDTO findById(Integer id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tarefa com o ID " + id + " não encontrado"));

        return tarefaResponseMapper.toDTO(tarefa);
    }

    @Override
    public TarefaResponseDTO create(TarefaRequestDTO tarefaDTO) {
        Tarefa tarefa = tarefaRepository.save(tarefaRequestMapper.toEntity(tarefaDTO));
        return tarefaResponseMapper.toDTO(tarefa);
    }

    @Override
    public TarefaResponseDTO update(Integer id, TarefaRequestDTO tarefaDTO) {
        Tarefa tarefaBanco = tarefaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tarefa com o ID " + id + " não encontrado"));

        if (!Objects.equals(tarefaBanco.getDescricao(), tarefaDTO.getDescricao()) && tarefaDTO.getDescricao() != null) {
            tarefaBanco.setDescricao(tarefaDTO.getDescricao());
        }

        if(!Objects.equals(tarefaBanco.getStatus(), tarefaDTO.getStatus()) && tarefaDTO.getStatus() != null){
            tarefaBanco.setStatus(tarefaDTO.getStatus());
        }

        if(!Objects.equals(tarefaBanco.getObservacoes(), tarefaDTO.getObservacoes())) {
            tarefaBanco.setObservacoes(tarefaDTO.getObservacoes());
        }

        tarefaBanco = tarefaRepository.save(tarefaBanco);

        return tarefaResponseMapper.toDTO(tarefaBanco);
    }

    @Override
    public void delete(Integer id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Tarefa com o ID " + id + " não encontrada"));
        tarefaRepository.delete(tarefa);
    }
}
