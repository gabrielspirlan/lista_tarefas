package com.api.lista_tarefas.application.services;

import com.api.lista_tarefas.domain.dtos.mappers.TarefaRequestMapper;
import com.api.lista_tarefas.domain.dtos.mappers.TarefaResponseMapper;
import com.api.lista_tarefas.domain.dtos.request.TarefaRequestDTO;
import com.api.lista_tarefas.domain.dtos.response.TarefaResponseDTO;
import com.api.lista_tarefas.domain.entities.Tarefa;
import com.api.lista_tarefas.domain.enums.TarefaStatusEnum;
import com.api.lista_tarefas.domain.exceptions.NotFoundException;
import com.api.lista_tarefas.infraestructure.repositories.TarefaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarefaServiceTest {

    @InjectMocks
    private TarefaServiceImpl tarefaService;

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private TarefaRequestMapper tarefaRequestMapper;

    @Mock
    private TarefaResponseMapper tarefaResponseMapper;

    private LocalDateTime dataFixa;

    @BeforeEach
    void setUp() {
        dataFixa = LocalDateTime.of(2026, 4, 26, 16, 0);
    }

    @Test
    public void deveLancarErroNotFoundAoBuscarPorIdInexistente() {
        // Cenário
        when(tarefaRepository.findById(1)).thenReturn(Optional.empty());

        // Ação & Validação
        assertThrows(NotFoundException.class, () -> tarefaService.findById(1));
        verify(tarefaRepository).findById(1);
    }

    @Test
    public void deveRetornarDTOAoBuscarPorID() {
        // Cenário
        Tarefa tarefa = tarefaMock();
        TarefaResponseDTO expectedDTO = tarefaResponseDTOMock();
        
        when(tarefaRepository.findById(1)).thenReturn(Optional.of(tarefa));
        when(tarefaResponseMapper.toDTO(tarefa)).thenReturn(expectedDTO);

        // Ação
        TarefaResponseDTO resultado = tarefaService.findById(1);

        // Validação
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.getId());
        Assertions.assertEquals("Trabalho do Xandy Gomes", resultado.getDescricao());
        verify(tarefaRepository).findById(1);
    }

    @Test
    public void deveCriarTarefaComSucesso() {
        // Cenário
        TarefaRequestDTO requestDTO = new TarefaRequestDTO("Nova Tarefa", "Obs", TarefaStatusEnum.EM_ANDAMENTO);
        Tarefa tarefa = tarefaMock();
        TarefaResponseDTO responseDTO = tarefaResponseDTOMock();

        when(tarefaRequestMapper.toEntity(requestDTO)).thenReturn(tarefa);
        when(tarefaRepository.save(tarefa)).thenReturn(tarefa);
        when(tarefaResponseMapper.toDTO(tarefa)).thenReturn(responseDTO);

        // Ação
        TarefaResponseDTO resultado = tarefaService.create(requestDTO);

        // Validação
        Assertions.assertNotNull(resultado);
        verify(tarefaRepository).save(any());
    }

    @Test
    public void deveAtualizarApenasOsCamposAlterados() {
        // Cenário
        Tarefa tarefaOriginal = tarefaMock();
        TarefaRequestDTO updateDTO = new TarefaRequestDTO(null, null, TarefaStatusEnum.CONCLUIDA);
        
        Tarefa tarefaSalva = tarefaMock();
        tarefaSalva.setStatus(TarefaStatusEnum.CONCLUIDA);
        
        TarefaResponseDTO responseDTO = tarefaResponseDTOMock();
        responseDTO.setStatus(TarefaStatusEnum.CONCLUIDA);

        when(tarefaRepository.findById(1)).thenReturn(Optional.of(tarefaOriginal));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefaSalva);
        when(tarefaResponseMapper.toDTO(any(Tarefa.class))).thenReturn(responseDTO);

        // Ação
        TarefaResponseDTO resultado = tarefaService.update(1, updateDTO);

        // Validação
        Assertions.assertEquals(TarefaStatusEnum.CONCLUIDA, resultado.getStatus());
        Assertions.assertEquals(tarefaOriginal.getId(), resultado.getId());
        Assertions.assertEquals(tarefaOriginal.getDescricao(), resultado.getDescricao());
    }
    @Test
    public void deveListarTarefasComPaginacao() {
        // Cenário
        Integer pageNumber = 0;
        Integer pageSize = 10;
        Page<Tarefa> pageTarefa = new PageImpl<>(List.of(tarefaMock()));
        
        when(tarefaRepository.findAll(any(Pageable.class))).thenReturn(pageTarefa);
        when(tarefaResponseMapper.toDTO(any(Tarefa.class))).thenReturn(tarefaResponseDTOMock());

        // Ação
        Page<TarefaResponseDTO> resultado = tarefaService.findAll(pageNumber, pageSize);

        // Validação
        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.getContent().size());
        verify(tarefaRepository).findAll(any(Pageable.class));
    }

    private Tarefa tarefaMock() {
        return Tarefa.builder()
                .id(1)
                .descricao("Trabalho do Xandy Gomes")
                .status(TarefaStatusEnum.EM_ANDAMENTO)
                .observacoes("Entrega até o dia 26/04")
                .dataCriacao(dataFixa)
                .dataAtualizacao(dataFixa)
                .build();
    }

    private TarefaResponseDTO tarefaResponseDTOMock() {
        return new TarefaResponseDTO(
                1,
                "Trabalho do Xandy Gomes",
                TarefaStatusEnum.EM_ANDAMENTO,
                "Entrega até o dia 26/04",
                dataFixa,
                dataFixa
        );
    }
}
