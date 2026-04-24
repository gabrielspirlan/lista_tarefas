package com.api.lista_tarefas.domain.entities;

import com.api.lista_tarefas.domain.enums.TarefaStatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tarefas")
public class Tarefa {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String descricao;
    private TarefaStatusEnum status;
    private String observacoes;
    private Date dataCriacao;
    private Date dataAtualizacao;
}
