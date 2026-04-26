package com.api.lista_tarefas.domain.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException (String message) {
        super(message);
    }
}
