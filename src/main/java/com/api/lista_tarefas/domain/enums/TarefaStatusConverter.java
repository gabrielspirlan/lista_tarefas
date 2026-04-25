package com.api.lista_tarefas.domain.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
public class TarefaStatusConverter implements AttributeConverter<TarefaStatusEnum, String> {
    @Override
    public String convertToDatabaseColumn(TarefaStatusEnum status) {
        return status == null ? null : status.getCodigo();
    }

    @Override
    public TarefaStatusEnum convertToEntityAttribute(String codigo) {
        return codigo == null ? null : TarefaStatusEnum.fromCodigo(codigo);
    }

}
