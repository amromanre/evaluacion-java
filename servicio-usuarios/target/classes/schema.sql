-- Script para crear las tablas de usuarios y telefonos
-- Compatible con H2 Database (según la configuración de tu proyecto)

-- Eliminar tablas si ya existen (para poder ejecutar el script múltiples veces)
DROP TABLE IF EXISTS telefonos;
DROP TABLE IF EXISTS usuarios;

-- Crear tabla de usuarios
CREATE TABLE usuarios (
    id VARCHAR(36) PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    correo VARCHAR(255) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    creado TIMESTAMP NOT NULL,
    modificado TIMESTAMP NOT NULL,
    ultimo_login TIMESTAMP NOT NULL,
    token VARCHAR(1000),
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

-- Crear tabla de teléfonos con relación a usuarios
CREATE TABLE telefonos (
    id VARCHAR(36) PRIMARY KEY,
    numero VARCHAR(255) NOT NULL,
    codigo_ciudad VARCHAR(255) NOT NULL,
    codigo_pais VARCHAR(255) NOT NULL,
    usuario_id VARCHAR(36) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Crear índices para mejorar el rendimiento de consultas comunes
CREATE INDEX idx_usuarios_correo ON usuarios(correo);
CREATE INDEX idx_telefonos_usuario_id ON telefonos(usuario_id);
