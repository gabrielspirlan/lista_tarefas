package com.api.lista_tarefas.application.services;

import com.api.lista_tarefas.domain.entities.Tarefa;
import com.api.lista_tarefas.integration.repositories.TarefaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

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
        return tarefaRepository.save(tarefa);
    }

    @Override
    public Tarefa update(Tarefa tarefa) throws Exception {

        Optional<Tarefa> tarefaBanco = this.tarefaRepository.findById(tarefa.getId());

        if (tarefaBanco.isEmpty()) {
            throw new Exception("Erro: Tarefa não existe no banco de dados");
        }

        return tarefaRepository.save(tarefa);
    }

    @Override
    public Boolean delete(Integer id) {
        return null;
    }
}
