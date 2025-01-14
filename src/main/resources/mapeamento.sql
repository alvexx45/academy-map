
-- Tabela academia
CREATE SEQUENCE public.academia_id_academia_seq;

CREATE TABLE public.academia (
    id_academia integer NOT NULL DEFAULT nextval('public.academia_id_academia_seq'),
    nome_academia varchar(255),
    localizacao varchar(255),
    destaque boolean DEFAULT FALSE, -- Coluna destaque por padrão
    email varchar(255),
    senha varchar(255),
    cnpj varchar(14),
    preco_aula numeric,
    foto_perfil varchar(255),
    CONSTRAINT academia_pkey PRIMARY KEY (id_academia)
);

-- Tabela cliente
CREATE SEQUENCE public.cliente_id_cliente_seq;

CREATE TABLE public.cliente (
    id_cliente integer NOT NULL DEFAULT nextval('public.cliente_id_cliente_seq'),
    nome_cliente varchar(255),
    email_cliente varchar(255),
    cpf varchar(11),
    senha varchar(255),
    ativo BOOLEAN DEFAULT TRUE, -- Coluna ativa por padrão
    CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente)
);

-- Tabela avaliacao
CREATE SEQUENCE public.avaliacao_id_avaliacao_seq;

CREATE TABLE public.avaliacao (
    id_avaliacao integer NOT NULL DEFAULT nextval('public.avaliacao_id_avaliacao_seq'),
    nota integer,
    comentario text,
    id_academia integer,
    CONSTRAINT avaliacao_pkey PRIMARY KEY (id_avaliacao),
    CONSTRAINT avaliacao_id_academia_fkey FOREIGN KEY (id_academia) REFERENCES academia(id_academia)
);

-- Tabela favoritos
CREATE SEQUENCE public.favoritos_id_favorito_seq;

CREATE TABLE public.favoritos (
    id_favorito integer NOT NULL DEFAULT nextval('public.favoritos_id_favorito_seq'),
    id_academia integer,
    id_cliente integer,
    CONSTRAINT favoritos_pkey PRIMARY KEY (id_favorito),
    CONSTRAINT favoritos_id_academia_fkey FOREIGN KEY (id_academia) REFERENCES academia(id_academia),
    CONSTRAINT favoritos_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);

-- Tabela pagamento
CREATE SEQUENCE public.pagamento_id_pagamento_seq;

CREATE TABLE public.pagamento (
    id_pagamento integer NOT NULL DEFAULT nextval('public.pagamento_id_pagamento_seq'),
    id_academia integer,
    id_cliente integer,
    quantidade_aulas integer,
    preco_final numeric,
    CONSTRAINT pagamento_pkey PRIMARY KEY (id_pagamento),
    CONSTRAINT pagamento_id_academia_fkey FOREIGN KEY (id_academia) REFERENCES academia(id_academia),
    CONSTRAINT pagamento_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
);
