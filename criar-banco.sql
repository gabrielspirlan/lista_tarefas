-- Script de criação da tabela Tarefas
-- Banco de Dados: PostgreSQL

CREATE DATABASE tarefas;

CREATE TABLE IF NOT EXISTS tarefas (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    tarefa_status VARCHAR(3) NOT NULL,
    observacoes TEXT,
    data_criacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraint para garantir que apenas valores válidos do Enum sejam inseridos
    -- PE  = PENDENTE
    -- AND = EM_ANDAMENTO
    -- CON = CONCLUIDA
    CONSTRAINT chk_status CHECK (tarefa_status IN ('PE', 'AND', 'CON'))
);

