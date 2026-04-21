-- V1__initial_schema.sql

-- Initial schema matching the provided Alembic migration

-- Create tables with IDENTITY columns (SQL Server / Azure SQL)

CREATE TABLE USUARIOS_VITS (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    nome_completo NVARCHAR(255) NOT NULL,
    email NVARCHAR(255) NOT NULL,
    cpf NVARCHAR(14) NOT NULL,
    senha_hash NVARCHAR(255) NOT NULL,
    data_cadastro DATETIME2 NOT NULL CONSTRAINT DF_USUARIOS_VITS_DATA_CADASTRO DEFAULT SYSDATETIME(),
    CONSTRAINT UQ_USUARIOS_VITS_EMAIL UNIQUE (email),
    CONSTRAINT UQ_USUARIOS_VITS_CPF UNIQUE (cpf)
);

CREATE TABLE LOTES (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    nome_lote NVARCHAR(255) NOT NULL,
    cultura NVARCHAR(100) NOT NULL,
    producao_total DECIMAL(15,2) NOT NULL,
    custo_total DECIMAL(15,2) NOT NULL,
    preco_venda DECIMAL(15,2) NOT NULL,
    status NVARCHAR(50) NOT NULL CONSTRAINT DF_LOTES_STATUS DEFAULT 'ativo',
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_lotes_usuario FOREIGN KEY (usuario_id) REFERENCES USUARIOS_VITS(id) ON DELETE CASCADE
);

CREATE TABLE EVENTOS_CULTIVO (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    lote_id BIGINT NOT NULL,
    tipo_evento NVARCHAR(100) NOT NULL,
    data_plantio DATE NOT NULL,
    data_colheita_estimada DATE NULL,
    descricao NVARCHAR(MAX) NULL,
    CONSTRAINT fk_eventos_lote FOREIGN KEY (lote_id) REFERENCES LOTES(id) ON DELETE CASCADE
);

CREATE TABLE SIMULACOES (
    id BIGINT IDENTITY(1,1) NOT NULL PRIMARY KEY,
    lote_id BIGINT NULL,
    resultado NVARCHAR(MAX) NOT NULL,
    melhor_opcao NVARCHAR(50) NULL,
    CONSTRAINT fk_simulacoes_lote FOREIGN KEY (lote_id) REFERENCES LOTES(id) ON DELETE SET NULL
);

-- Indexes
CREATE INDEX idx_usuarios_email ON USUARIOS_VITS(email);
CREATE INDEX idx_lotes_usuario ON LOTES(usuario_id);
CREATE INDEX idx_eventos_lote ON EVENTOS_CULTIVO(lote_id);
CREATE INDEX idx_simulacoes_lote ON SIMULACOES(lote_id);
