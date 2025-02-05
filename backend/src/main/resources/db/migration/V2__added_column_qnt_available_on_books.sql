-- Adicionar a coluna qnt_available à tabela books
ALTER TABLE books
    ADD COLUMN qnt_available INTEGER NOT NULL DEFAULT 0;

-- Garantir que todas as linhas existentes tenham o valor 0
UPDATE books
SET qnt_available = 0;
