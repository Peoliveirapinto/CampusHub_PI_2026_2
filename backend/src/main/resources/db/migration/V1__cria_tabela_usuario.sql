CREATE TABLE usuario (
    id UUID PRIMARY KEY,
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    CONSTRAINT uk_usuario_email UNIQUE (email)
);
