
CREATE DATABASE IF NOT EXISTS tienda_ropa;
USE tienda_ropa;

-- Tabla: colecciones
CREATE TABLE IF NOT EXISTS colecciones (
  id_coleccion INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  temporada VARCHAR(50),
  anio INT NOT NULL,
  estado TINYINT(1) NOT NULL DEFAULT 1
);

-- Tabla: prendas
CREATE TABLE IF NOT EXISTS prendas (
  id_prenda BIGINT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  talla VARCHAR(10),
  color VARCHAR(50),
  precio DECIMAL(10,2) NOT NULL,
  estado VARCHAR(20) NOT NULL DEFAULT 'disponible',
  id_coleccion INT NOT NULL,
  FOREIGN KEY (id_coleccion) REFERENCES colecciones(id_coleccion)
);

-- Fin del script
