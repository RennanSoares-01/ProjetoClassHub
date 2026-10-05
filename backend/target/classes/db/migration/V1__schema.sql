-- Schema do MVP Class Hub (ADR-007 em .ai/architecture.md)

CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    matricula VARCHAR(50) NOT NULL UNIQUE,
    nome VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL,
    senha_hash VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('ALUNO', 'PROFESSOR', 'ADMIN')),
    criado_em TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE turmas (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    professor_id BIGINT NOT NULL REFERENCES usuarios (id),
    criado_em TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE turma_alunos (
    turma_id BIGINT NOT NULL REFERENCES turmas (id) ON DELETE CASCADE,
    usuario_id BIGINT NOT NULL REFERENCES usuarios (id) ON DELETE CASCADE,
    PRIMARY KEY (turma_id, usuario_id)
);

CREATE TABLE tarefas (
    id BIGSERIAL PRIMARY KEY,
    turma_id BIGINT NOT NULL REFERENCES turmas (id) ON DELETE CASCADE,
    titulo VARCHAR(200) NOT NULL,
    descricao TEXT,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('INDIVIDUAL', 'GRUPO')),
    data_limite TIMESTAMP NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE entregas (
    id BIGSERIAL PRIMARY KEY,
    tarefa_id BIGINT NOT NULL REFERENCES tarefas (id) ON DELETE CASCADE,
    autor_id BIGINT NOT NULL REFERENCES usuarios (id),
    conteudo TEXT NOT NULL,
    comentario TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE' CHECK (status IN ('PENDENTE', 'APROVADA', 'REPROVADA')),
    data_criacao TIMESTAMP NOT NULL DEFAULT now(),
    data_atualizacao TIMESTAMP NOT NULL DEFAULT now(),
    UNIQUE (tarefa_id, autor_id)
);

CREATE TABLE entrega_anexos (
    id BIGSERIAL PRIMARY KEY,
    entrega_id BIGINT NOT NULL REFERENCES entregas (id) ON DELETE CASCADE,
    nome_arquivo VARCHAR(200) NOT NULL,
    url VARCHAR(500) NOT NULL
);

CREATE TABLE entrega_participantes (
    entrega_id BIGINT NOT NULL REFERENCES entregas (id) ON DELETE CASCADE,
    usuario_id BIGINT NOT NULL REFERENCES usuarios (id) ON DELETE CASCADE,
    PRIMARY KEY (entrega_id, usuario_id)
);

CREATE TABLE feedbacks (
    id BIGSERIAL PRIMARY KEY,
    entrega_id BIGINT NOT NULL UNIQUE REFERENCES entregas (id) ON DELETE CASCADE,
    professor_id BIGINT NOT NULL REFERENCES usuarios (id),
    nota NUMERIC(4, 2) NOT NULL CHECK (nota >= 0 AND nota <= 10),
    comentario TEXT,
    status VARCHAR(20) NOT NULL CHECK (status IN ('APROVADA', 'REPROVADA')),
    criado_em TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE notificacoes (
    id BIGSERIAL PRIMARY KEY,
    destinatario_id BIGINT NOT NULL REFERENCES usuarios (id) ON DELETE CASCADE,
    tipo VARCHAR(30) NOT NULL CHECK (tipo IN ('ENTREGA_AVALIADA', 'PRAZO_PROXIMO')),
    referencia_id BIGINT,
    mensagem TEXT NOT NULL,
    criado_em TIMESTAMP NOT NULL DEFAULT now()
);
