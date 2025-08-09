USE facturacion_db;

CREATE TABLE IF NOT EXISTS facturas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    factura_id VARCHAR(255) UNIQUE NOT NULL,
    cosecha_id VARCHAR(255) NOT NULL,
    monto_total DECIMAL(10,2) NOT NULL,
    pagado BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_emision DATETIME,
    metodo_pago VARCHAR(30),
    codigo_qr TEXT
);
