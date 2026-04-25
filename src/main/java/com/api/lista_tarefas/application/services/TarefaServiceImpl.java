package com.api.lista_tarefas.application.services;

import com.api.lista_tarefas.domain.entities.Tarefa;
import com.api.lista_tarefas.integration.repositories.TarefaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TarefaServiceImpl implements TarefaService{

    private TarefaRepository tarefaRepository;

    public TarefaServiceImpl(TarefaRepository tarefaRepository){
        this.tarefaRepository = tarefaRepository;
    }

    @Override
    public List<Tarefa> findAll() {
        return tarefaRepository.findAll();
    }

    @Override
    public Optional<Tarefa> findById(Integer id) {
        return tarefaRepository.findById(id);
    }

    @Override
    public Tarefa create(Tarefa tarefa) {
        tarefa.setDataAtualizacao(null);
        tarefa.setDataCriacao(null);
        return tarefaRepository.save(tarefa);
    }

    @Override
    public Tarefa update(Integer id, Tarefa tarefa) throws Exception {
        Tarefa tarefaBanco = this.tarefaRepository.findById(id).orElseThrow(() -> new RuntimeException("Tarefa com o ID " + id + " não existem"));

        if (!Objects.equals(tarefaBanco.getDescricao(), tarefa.getDescricao()) && tarefa.getDescricao() != null) {
            tarefaBanco.setDescricao(tarefa.getDescricao());
        }

        if(!Objects.equals(tarefaBanco.getStatus(), tarefa.getStatus()) && tarefa.getStatus() != null){
            tarefaBanco.setStatus(tarefa.getStatus());
        }

        if(!Objects.equals(tarefaBanco.getObservacoes(), tarefa.getObservacoes())) {
            tarefaBanco.setObservacoes(tarefa.getObservacoes());
        }

        return tarefaRepository.save(tarefaBanco);
    }

    @Override
    public void delete(Integer id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa com o ID " + id + " não encontrada"));
        tarefaRepository.delete(tarefa);
    }
}
