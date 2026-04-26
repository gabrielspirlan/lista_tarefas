package com.api.lista_tarefas.domain.dtos.response;

import com.api.lista_tarefas.domain.enums.TarefaStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TarefaResponseDTO {

    private Integer id;
    private String descricao;
    private TarefaStatusEnum status;
    private String observacoes;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

}
