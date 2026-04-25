package com.api.lista_tarefas.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TarefaStatusEnum {
    PENDENTE ("PE", "Pendente"),
    EM_ANDAMENTO("AND", "Em andamento"),
    CONCLUIDA("CON", "Concluída");

    private final String codigo;
    private final String descricao;

    public static TarefaStatusEnum fromCodigo(String codigo) {
        for (TarefaStatusEnum s : values()) {
            if (s.codigo.equals(codigo)) return s;
        }
        throw new IllegalArgumentException("Código inválido: " + codigo);
    }
}
