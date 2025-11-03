-- Script de creación de base de datos para Sistema de Inventario
CREATE DATABASE IF NOT EXISTS sistema_inventario;
USE sistema_inventario;

-- Tabla de Proveedores
CREATE TABLE proveedor (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    contacto VARCHAR(100),
    UNIQUE KEY (nombre)
);

-- Tabla de Productos
CREATE TABLE producto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    precio DECIMAL(10,2) DEFAULT 0.00,
    cantidad INT DEFAULT 0,
    proveedor_id INT,
    FOREIGN KEY (proveedor_id) REFERENCES proveedor(id) ON DELETE SET NULL
);

-- Tabla de Movimientos de Inventario
CREATE TABLE movimiento_inventario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    producto_id INT NOT NULL,
    tipo VARCHAR(20) NOT NULL, -- 'ENTRADA' o 'SALIDA'
    cantidad INT NOT NULL,
    fecha DATETIME NOT NULL,
    motivo VARCHAR(200),
    FOREIGN KEY (producto_id) REFERENCES producto(id) ON DELETE CASCADE
);

-- Datos de ejemplo
INSERT INTO proveedor (nombre, contacto) VALUES
('Proveedor ABC', 'contacto@abc.com'),
('Distribuidora XYZ', 'ventas@xyz.com');

INSERT INTO producto (codigo, nombre, categoria, precio, cantidad, proveedor_id) VALUES
('PROD001', 'Laptop HP', 'Electrónica', 850.00, 10, 1),
('PROD002', 'Mouse Logitech', 'Accesorios', 25.50, 50, 1),
('PROD003', 'Teclado Mecánico', 'Accesorios', 75.00, 30, 2);