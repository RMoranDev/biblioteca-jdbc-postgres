-- ============================================================
-- BANCO DE DADOS: Biblioteca
-- PostgreSQL
-- ============================================================


-- ============================================================
-- FUNÇÃO: Atualizar automaticamente o campo atualizado_em
-- ============================================================

CREATE OR REPLACE FUNCTION public.atualizar_timestamp()
RETURNS TRIGGER
LANGUAGE plpgsql
AS $$
BEGIN
    NEW.atualizado_em = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$;


-- ============================================================
-- TABELA: livros
-- ============================================================

CREATE TABLE IF NOT EXISTS public.livros
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    titulo VARCHAR(255) NOT NULL,

    autor VARCHAR(150) NOT NULL,

    isbn VARCHAR(17) UNIQUE,

    ano_publicacao SMALLINT,

    categoria VARCHAR(50),

    criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT livros_ano_publicacao_check
        CHECK (ano_publicacao > 0 AND ano_publicacao <= 2100)
);


-- ============================================================
-- TABELA: usuarios
-- ============================================================

CREATE TABLE IF NOT EXISTS public.usuarios
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    nome VARCHAR(150) NOT NULL,

    cpf VARCHAR(11) NOT NULL UNIQUE,

    email VARCHAR(150) NOT NULL UNIQUE,

    telefone VARCHAR(20),

    ativo BOOLEAN NOT NULL DEFAULT TRUE,

    criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- ============================================================
-- TABELA: exemplares
-- ============================================================

CREATE TABLE IF NOT EXISTS public.exemplares
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    livro_id BIGINT NOT NULL,

    codigo_patrimonio VARCHAR(50) NOT NULL UNIQUE,

    status VARCHAR(20) NOT NULL DEFAULT 'DISPONIVEL',

    observacoes VARCHAR(255),

    criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_exemplares_livro
        FOREIGN KEY (livro_id)
        REFERENCES public.livros (id)
        ON DELETE RESTRICT,

    CONSTRAINT exemplares_status_check
        CHECK (
            status IN (
                'DISPONIVEL',
                'EMPRESTADO',
                'EM_MANUTENCAO',
                'PERDIDO'
            )
        )
);


-- ============================================================
-- ÍNDICE: exemplares.livro_id
-- ============================================================

CREATE INDEX IF NOT EXISTS idx_exemplares_livro_id
    ON public.exemplares (livro_id);


-- ============================================================
-- TABELA: emprestimos
-- ============================================================

CREATE TABLE IF NOT EXISTS public.emprestimos
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    exemplar_id BIGINT NOT NULL,

    usuario_id BIGINT NOT NULL,

    data_emprestimo TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    data_prevista_devolucao DATE NOT NULL,

    data_devolucao_efetiva TIMESTAMPTZ,

    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',

    multa_paga NUMERIC(10, 2) NOT NULL DEFAULT 0.00,

    criado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_emprestimos_exemplar
        FOREIGN KEY (exemplar_id)
        REFERENCES public.exemplares (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_emprestimos_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES public.usuarios (id)
        ON DELETE RESTRICT,

    CONSTRAINT emprestimos_multa_paga_check
        CHECK (multa_paga >= 0),

    CONSTRAINT emprestimos_status_check
        CHECK (
            status IN (
                'ATIVO',
                'DEVOLVIDO',
                'ATRASADO'
            )
        )
);


-- ============================================================
-- ÍNDICES: emprestimos
-- ============================================================

CREATE INDEX IF NOT EXISTS idx_emprestimos_exemplar_id
    ON public.emprestimos (exemplar_id);


CREATE INDEX IF NOT EXISTS idx_emprestimos_usuario_id
    ON public.emprestimos (usuario_id);


CREATE INDEX IF NOT EXISTS idx_emprestimos_ativos
    ON public.emprestimos (data_prevista_devolucao)
    WHERE status = 'ATIVO';


-- ============================================================
-- ÍNDICE ÚNICO:
-- Um exemplar não pode possuir mais de um empréstimo ativo.
-- ============================================================

CREATE UNIQUE INDEX IF NOT EXISTS idx_unico_exemplar_ativo
    ON public.emprestimos (exemplar_id)
    WHERE status = 'ATIVO';


-- ============================================================
-- TRIGGERS: livros
-- ============================================================

DROP TRIGGER IF EXISTS trg_livros_atualizado_em
    ON public.livros;

CREATE TRIGGER trg_livros_atualizado_em
    BEFORE UPDATE
    ON public.livros
    FOR EACH ROW
    EXECUTE FUNCTION public.atualizar_timestamp();


-- ============================================================
-- TRIGGERS: usuarios
-- ============================================================

DROP TRIGGER IF EXISTS trg_usuarios_atualizado_em
    ON public.usuarios;

CREATE TRIGGER trg_usuarios_atualizado_em
    BEFORE UPDATE
    ON public.usuarios
    FOR EACH ROW
    EXECUTE FUNCTION public.atualizar_timestamp();


-- ============================================================
-- TRIGGERS: exemplares
-- ============================================================

DROP TRIGGER IF EXISTS trg_exemplares_atualizado_em
    ON public.exemplares;

CREATE TRIGGER trg_exemplares_atualizado_em
    BEFORE UPDATE
    ON public.exemplares
    FOR EACH ROW
    EXECUTE FUNCTION public.atualizar_timestamp();


-- ============================================================
-- TRIGGERS: emprestimos
-- ============================================================

DROP TRIGGER IF EXISTS trg_emprestimos_atualizado_em
    ON public.emprestimos;

CREATE TRIGGER trg_emprestimos_atualizado_em
    BEFORE UPDATE
    ON public.emprestimos
    FOR EACH ROW
    EXECUTE FUNCTION public.atualizar_timestamp();

