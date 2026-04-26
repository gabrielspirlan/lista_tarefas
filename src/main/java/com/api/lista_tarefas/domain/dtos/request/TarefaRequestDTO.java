package com.api.lista_tarefas.domain.dtos.request;

import com.api.lista_tarefas.domain.enums.TarefaStatusConverter;
import com.api.lista_tarefas.domain.enums.TarefaStatusEnum;
import jakarta.persistence.Convert;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TarefaRequestDTO {

    private String descricao;

    private String observacoes;

    @Convert(converter = TarefaStatusConverter.class)
    private TarefaStatusEnum status;
}
