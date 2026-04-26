package com.api.lista_tarefas.view.controllers;

import com.api.lista_tarefas.application.services.TarefaService;
import com.api.lista_tarefas.domain.dtos.request.TarefaRequestDTO;
import com.api.lista_tarefas.domain.dtos.response.TarefaResponseDTO;
import com.api.lista_tarefas.domain.entities.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> getById(@PathVariable Integer id) {
        return new ResponseEntity<>(tarefaService.findById(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Page<TarefaResponseDTO>> getAll(@RequestParam(required = false) Integer pageNumber, @RequestParam(required = false) Integer pageSize) {

        return new ResponseEntity<>(tarefaService.findAll(pageNumber, pageSize), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<TarefaResponseDTO> create(@RequestBody TarefaRequestDTO tarefa) {
        return new ResponseEntity<>(tarefaService.create(tarefa), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TarefaResponseDTO> update (@PathVariable Integer id, @RequestBody TarefaRequestDTO tarefa) throws Exception {
        return new ResponseEntity<>(tarefaService.update(id, tarefa), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete (@PathVariable Integer id) {
        tarefaService.delete(id);
        return new  ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
