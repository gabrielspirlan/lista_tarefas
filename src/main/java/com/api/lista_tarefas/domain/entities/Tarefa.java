package com.api.lista_tarefas.domain.entities;

import com.api.lista_tarefas.domain.enums.TarefaStatusConverter;
import com.api.lista_tarefas.domain.enums.TarefaStatusEnum;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tarefas")
@Builder
@Data
public class Tarefa {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Descrição não pode ser nulo ou vazio")
    private String descricao;

    @NotNull(message = "Status da tarefa não pode ser nulo")
    @Convert(converter = TarefaStatusConverter.class)
    private TarefaStatusEnum status;

    @Nullable
    private String observacoes;

    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @PrePersist
    public void prePersist() {
        dataCriacao = LocalDateTime.now();
        dataAtualizacao = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }

}
