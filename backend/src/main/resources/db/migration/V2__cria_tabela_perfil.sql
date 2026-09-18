CREATE TABLE perfil (
    id UUID PRIMARY KEY,
    usuario_id UUID NOT NULL,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT NOT NULL,
    curso VARCHAR(50) NOT NULL,
    especializacao VARCHAR(100),
    ano_de_ingresso INT NOT NULL,
    semestre_de_ingresso INT NOT NULL,
    disponibilidade TEXT NOT NULL,
    contato VARCHAR(100) NOT NULL,
    CONSTRAINT uk_perfil_usuario_id UNIQUE (usuario_id),
    CONSTRAINT fk_perfil_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario (id)
        ON DELETE CASCADE
);
