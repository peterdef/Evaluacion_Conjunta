USE inventario_db;

CREATE TABLE IF NOT EXISTS insumos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    insumo_id VARCHAR(255) UNIQUE,
    nombre_insumo VARCHAR(100) UNIQUE NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    unidad_medida VARCHAR(10) DEFAULT 'kg',
    categoria VARCHAR(30) NOT NULL,
    ultima_actualizacion DATETIME
);
