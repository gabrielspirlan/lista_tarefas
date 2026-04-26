package com.api.lista_tarefas.infraestructure.repositories;

import com.api.lista_tarefas.domain.entities.Tarefa;
import com.api.lista_tarefas.domain.enums.TarefaStatusEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TarefaRepositoryTest {

    @Autowired
    TarefaRepository tarefaRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Test
    public void devePersistirUmaTarefaNaBaseDeDados() {
        // Cenário
        Tarefa tarefa = criarTarefa();

        // Ação
        Tarefa tarefaSalva = testEntityManager.persist(tarefa);

        // Verificação
        Assertions.assertThat(tarefaSalva.getId()).isNotNull();
    }

    @Test
    public void deveCriarDataDeCriacaoEAtualizacaoAutomaticamente() {
        // Cenário
        Tarefa tarefa = criarTarefa();

        // Ação
        Tarefa tarefaSalva = testEntityManager.persistFlushFind(tarefa);

        // Verificação
        Assertions.assertThat(tarefaSalva.getDataCriacao()).isNotNull();
        Assertions.assertThat(tarefaSalva.getDataAtualizacao()).isNotNull();
    }

    @Test
    public void deveAtualizarDataDeAtualizacao() throws InterruptedException {
        // Cenário
        Tarefa tarefa = criarTarefa();
        Tarefa tarefaSalva = testEntityManager.persistFlushFind(tarefa);
        LocalDateTime dataCriacaoOriginal = tarefaSalva.getDataCriacao();
        LocalDateTime dataAtualizacaoOriginal = tarefaSalva.getDataAtualizacao();

        // Pequena pausa para garantir que o timestamp do banco mude
        Thread.sleep(10);

        // Ação
        tarefaSalva.setStatus(TarefaStatusEnum.CONCLUIDA);
        testEntityManager.flush(); // Força a execução do SQL Update e @PreUpdate
        testEntityManager.clear(); // Limpa o cache para forçar nova leitura do banco

        Tarefa tarefaAtualizada = tarefaRepository.findById(tarefaSalva.getId()).get();

        // Verificação
        Assertions.assertThat(tarefaAtualizada.getDataAtualizacao()).isAfter(dataAtualizacaoOriginal);
        Assertions.assertThat(tarefaAtualizada.getDataCriacao()).isEqualTo(dataCriacaoOriginal);
    }

    @Test
    public void devePersistirASituacaoDaTarefa() {
        // Cenário
        Tarefa tarefa = criarTarefa();

        // Ação
        Tarefa tarefaSalva = testEntityManager.persistFlushFind(tarefa);

        // Verificação
        Assertions.assertThat(tarefaSalva.getStatus().getCodigo()).isEqualTo("PE");
        Assertions.assertThat(tarefaSalva.getStatus()).isEqualTo(TarefaStatusEnum.PENDENTE);
    }

    @Test
    public void deveRetornarVerdadeiroQuandoATarefaExistir() {
        // Cenário
        Tarefa tarefa = criarTarefa();
        Tarefa tarefaSalva = testEntityManager.persistFlushFind(tarefa);

        // Ação
        boolean result = tarefaRepository.existsById(tarefaSalva.getId());

        // Verificação
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void deveRetornarFalsoQuandoATarefaNaoExistir () {
        // Ação
        boolean result = tarefaRepository.existsById(9999);

        // Verificação
        Assertions.assertThat(result).isFalse();
    }

    public static Tarefa criarTarefa() {
        return Tarefa.builder()
                .descricao("Fazer trabalho do Xandy Gomes")
                .observacoes("Entrega até o dia 26/04")
                .status(TarefaStatusEnum.PENDENTE)
                .build();
    }
}
