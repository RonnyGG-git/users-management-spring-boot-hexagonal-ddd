-- =============================================
-- Script de creación de la base de datos (PostgreSQL / Supabase)
-- Gestión de Usuarios - Arquitectura Hexagonal
--
-- Equivalente a schema.sql (MySQL). Diferencias:
--   * ENUM de MySQL -> TEXT + CHECK (el adaptador envía los valores con setString).
--   * ON UPDATE CURRENT_TIMESTAMP no existe: el adaptador ya asigna updated_at = NOW().
--   * La base de datos la crea Supabase (postgres); aquí solo se crea la tabla.
-- =============================================

CREATE TABLE IF NOT EXISTS users (
    id          VARCHAR(36)  NOT NULL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    role        TEXT NOT NULL CHECK (role IN ('ADMIN', 'MEMBER', 'REVIEWER')),
    status      TEXT NOT NULL DEFAULT 'PENDING'
                CHECK (status IN ('ACTIVE', 'INACTIVE', 'PENDING', 'BLOCKED')),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Supabase expone el esquema public por su API REST: sin RLS cualquiera con la
-- clave anon podría leer los hashes de contraseña. La app se conecta como dueño
-- de la tabla (postgres), por lo que RLS no le afecta.
ALTER TABLE users ENABLE ROW LEVEL SECURITY;

-- Usuario administrador inicial (password: Admin1234!)
INSERT INTO users (id, name, email, password, role, status)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'Administrador',
    'admin@example.com',
    '$2a$12$sfwkajsls.fktQb0Ngw5cOiSiV3z4LIwbucSAy4sw2wyNlxbUBt4q',
    'ADMIN',
    'ACTIVE'
)
ON CONFLICT (id) DO NOTHING;
