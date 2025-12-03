-- ============================================
-- SCRIPT DE BASE DE DATOS - TIENDA DE ROPA E-COMMERCE
-- Base de datos: tienda_ropa
-- Fecha: Diciembre 2025
-- ============================================

-- Crear base de datos (si no existe)
CREATE DATABASE IF NOT EXISTS tienda_ropa
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE tienda_ropa;

-- ============================================
-- TABLAS EXISTENTES (del proyecto actual)
-- ============================================

-- Tabla: colecciones
CREATE TABLE IF NOT EXISTS colecciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    temporada VARCHAR(50),
    anio INT,
    estado TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- Tabla: prendas
CREATE TABLE IF NOT EXISTS prendas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    talla VARCHAR(10),
    color VARCHAR(50),
    precio DECIMAL(10,2) NOT NULL,
    estado VARCHAR(20) DEFAULT 'disponible',
    coleccion_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_prendas_coleccion FOREIGN KEY (coleccion_id) 
        REFERENCES colecciones(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ============================================
-- NUEVAS TABLAS PARA E-COMMERCE
-- ============================================

-- -------------------------------------------
-- 1. CATEGORÍAS (con subcategorías)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    imagen_url VARCHAR(500),
    parent_id BIGINT DEFAULT NULL,
    orden INT DEFAULT 0,
    activo TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_categoria_parent FOREIGN KEY (parent_id) 
        REFERENCES categorias(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- -------------------------------------------
-- 2. MARCAS
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS marcas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    logo_url VARCHAR(500),
    descripcion TEXT,
    activo TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- -------------------------------------------
-- 3. PRODUCTOS (concepto base)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    descripcion_corta VARCHAR(500),
    categoria_id BIGINT NOT NULL,
    marca_id BIGINT,
    precio_base DECIMAL(10,2) NOT NULL,
    precio_oferta DECIMAL(10,2),
    descuento_porcentaje INT DEFAULT 0,
    es_nuevo TINYINT(1) DEFAULT 1,
    es_destacado TINYINT(1) DEFAULT 0,
    activo TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) 
        REFERENCES categorias(id) ON DELETE RESTRICT,
    CONSTRAINT fk_producto_marca FOREIGN KEY (marca_id) 
        REFERENCES marcas(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- -------------------------------------------
-- 4. VARIANTES DE PRODUCTO (SKU con talla, color, stock)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS producto_variantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    sku VARCHAR(50) NOT NULL UNIQUE,
    talla VARCHAR(20),
    color VARCHAR(50),
    color_hex VARCHAR(7),
    stock INT DEFAULT 0,
    precio_adicional DECIMAL(10,2) DEFAULT 0.00,
    activo TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_variante_producto FOREIGN KEY (producto_id) 
        REFERENCES productos(id) ON DELETE CASCADE,
    INDEX idx_variante_sku (sku),
    INDEX idx_variante_producto (producto_id)
) ENGINE=InnoDB;

-- -------------------------------------------
-- 5. IMÁGENES DE PRODUCTO (galería)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS producto_imagenes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    variante_id BIGINT,
    imagen_url VARCHAR(500) NOT NULL,
    es_principal TINYINT(1) DEFAULT 0,
    orden INT DEFAULT 0,
    alt_text VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_imagen_producto FOREIGN KEY (producto_id) 
        REFERENCES productos(id) ON DELETE CASCADE,
    CONSTRAINT fk_imagen_variante FOREIGN KEY (variante_id) 
        REFERENCES producto_variantes(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- -------------------------------------------
-- 6. BANNERS DINÁMICOS
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS banners (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200),
    subtitulo VARCHAR(300),
    imagen_url VARCHAR(500) NOT NULL,
    imagen_movil_url VARCHAR(500),
    enlace_url VARCHAR(500),
    texto_boton VARCHAR(50),
    posicion VARCHAR(50) DEFAULT 'home_principal',
    orden INT DEFAULT 0,
    fecha_inicio DATE,
    fecha_fin DATE,
    activo TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- -------------------------------------------
-- 7. USUARIOS (sin roles)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    fecha_nacimiento DATE,
    genero ENUM('M', 'F', 'O') DEFAULT NULL,
    avatar_url VARCHAR(500),
    activo TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_usuario_email (email)
) ENGINE=InnoDB;

-- -------------------------------------------
-- 8. DIRECCIONES DE USUARIO
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS usuario_direcciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    etiqueta VARCHAR(50) DEFAULT 'Casa',
    nombres_receptor VARCHAR(100) NOT NULL,
    apellidos_receptor VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    direccion VARCHAR(300) NOT NULL,
    referencia VARCHAR(200),
    departamento VARCHAR(100),
    provincia VARCHAR(100),
    distrito VARCHAR(100),
    codigo_postal VARCHAR(10),
    es_principal TINYINT(1) DEFAULT 0,
    activo TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_direccion_usuario FOREIGN KEY (usuario_id) 
        REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- -------------------------------------------
-- 9. CARRITO DE COMPRAS
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS carritos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT,
    session_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_carrito_usuario FOREIGN KEY (usuario_id) 
        REFERENCES usuarios(id) ON DELETE CASCADE,
    INDEX idx_carrito_session (session_id),
    INDEX idx_carrito_usuario (usuario_id)
) ENGINE=InnoDB;

-- -------------------------------------------
-- 10. ITEMS DEL CARRITO
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS carrito_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    carrito_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    variante_id BIGINT,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_item_carrito FOREIGN KEY (carrito_id) 
        REFERENCES carritos(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_producto FOREIGN KEY (producto_id) 
        REFERENCES productos(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_variante FOREIGN KEY (variante_id) 
        REFERENCES producto_variantes(id) ON DELETE SET NULL,
    UNIQUE KEY uk_carrito_variante (carrito_id, variante_id)
) ENGINE=InnoDB;

-- -------------------------------------------
-- 11. PEDIDOS (sin seguimiento)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_pedido VARCHAR(20) NOT NULL UNIQUE,
    usuario_id BIGINT NOT NULL,
    
    -- Datos de envío (snapshot al momento del pedido)
    nombres_receptor VARCHAR(100) NOT NULL,
    apellidos_receptor VARCHAR(100) NOT NULL,
    telefono_receptor VARCHAR(20) NOT NULL,
    direccion_envio TEXT NOT NULL,
    departamento VARCHAR(100),
    provincia VARCHAR(100),
    distrito VARCHAR(100),
    
    -- Totales
    subtotal DECIMAL(10,2) NOT NULL,
    descuento DECIMAL(10,2) DEFAULT 0.00,
    costo_envio DECIMAL(10,2) DEFAULT 0.00,
    total DECIMAL(10,2) NOT NULL,
    
    -- Método de pago
    metodo_pago VARCHAR(50),
    
    -- Notas
    notas_cliente TEXT,
    
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (usuario_id) 
        REFERENCES usuarios(id) ON DELETE RESTRICT,
    
    INDEX idx_pedido_numero (numero_pedido),
    INDEX idx_pedido_usuario (usuario_id),
    INDEX idx_pedido_fecha (fecha_pedido)
) ENGINE=InnoDB;

-- -------------------------------------------
-- 12. DETALLE DE PEDIDOS
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS pedido_detalles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    variante_id BIGINT,
    
    -- Snapshot del producto al momento de la compra
    producto_nombre VARCHAR(200) NOT NULL,
    producto_sku VARCHAR(50),
    talla VARCHAR(20),
    color VARCHAR(50),
    
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    descuento DECIMAL(10,2) DEFAULT 0.00,
    subtotal DECIMAL(10,2) NOT NULL,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_detalle_pedido FOREIGN KEY (pedido_id) 
        REFERENCES pedidos(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) 
        REFERENCES productos(id) ON DELETE RESTRICT,
    CONSTRAINT fk_detalle_variante FOREIGN KEY (variante_id) 
        REFERENCES producto_variantes(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- -------------------------------------------
-- 13. VALORACIONES / RESEÑAS
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS valoraciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    pedido_id BIGINT,
    calificacion INT NOT NULL CHECK (calificacion >= 1 AND calificacion <= 5),
    titulo VARCHAR(200),
    comentario TEXT,
    es_compra_verificada TINYINT(1) DEFAULT 0,
    aprobado TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_valoracion_producto FOREIGN KEY (producto_id) 
        REFERENCES productos(id) ON DELETE CASCADE,
    CONSTRAINT fk_valoracion_usuario FOREIGN KEY (usuario_id) 
        REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_valoracion_pedido FOREIGN KEY (pedido_id) 
        REFERENCES pedidos(id) ON DELETE SET NULL,
    
    UNIQUE KEY uk_valoracion_usuario_producto (usuario_id, producto_id),
    INDEX idx_valoracion_producto (producto_id),
    INDEX idx_valoracion_calificacion (calificacion)
) ENGINE=InnoDB;

-- -------------------------------------------
-- 14. PRODUCTOS RECOMENDADOS
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS producto_recomendados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    producto_recomendado_id BIGINT NOT NULL,
    tipo ENUM('similar', 'complementario', 'frecuente') DEFAULT 'similar',
    orden INT DEFAULT 0,
    activo TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_recom_producto FOREIGN KEY (producto_id) 
        REFERENCES productos(id) ON DELETE CASCADE,
    CONSTRAINT fk_recom_producto_rec FOREIGN KEY (producto_recomendado_id) 
        REFERENCES productos(id) ON DELETE CASCADE,
    
    UNIQUE KEY uk_recomendacion (producto_id, producto_recomendado_id)
) ENGINE=InnoDB;

-- -------------------------------------------
-- 15. LISTA DE DESEOS (WISHLIST)
-- -------------------------------------------
CREATE TABLE IF NOT EXISTS wishlists (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_wishlist_usuario FOREIGN KEY (usuario_id) 
        REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_producto FOREIGN KEY (producto_id) 
        REFERENCES productos(id) ON DELETE CASCADE,
    
    UNIQUE KEY uk_wishlist (usuario_id, producto_id)
) ENGINE=InnoDB;

-- ============================================
-- DATOS DE PRUEBA
-- ============================================

-- Categorías principales
INSERT INTO categorias (nombre, descripcion, orden, activo) VALUES
('Hombres', 'Ropa y accesorios para hombres', 1, 1),
('Mujeres', 'Ropa y accesorios para mujeres', 2, 1),
('Niños', 'Ropa para niños y niñas', 3, 1),
('Accesorios', 'Complementos y accesorios de moda', 4, 1);

-- Subcategorías Hombres
INSERT INTO categorias (nombre, descripcion, parent_id, orden, activo) VALUES
('Camisas', 'Camisas casuales y formales', 1, 1, 1),
('Pantalones', 'Pantalones, jeans y shorts', 1, 2, 1),
('Polos', 'Polos y camisetas', 1, 3, 1),
('Casacas', 'Casacas y abrigos', 1, 4, 1);

-- Subcategorías Mujeres
INSERT INTO categorias (nombre, descripcion, parent_id, orden, activo) VALUES
('Blusas', 'Blusas y tops', 2, 1, 1),
('Vestidos', 'Vestidos casuales y de fiesta', 2, 2, 1),
('Faldas', 'Faldas y shorts', 2, 3, 1),
('Pantalones', 'Pantalones y jeans', 2, 4, 1);

-- Marcas
INSERT INTO marcas (nombre, descripcion, activo) VALUES
('Urban Style', 'Moda urbana contemporánea', 1),
('Classic Wear', 'Estilo clásico y elegante', 1),
('Sport Life', 'Ropa deportiva y casual', 1),
('Kids Fashion', 'Moda infantil de calidad', 1);

-- Productos de ejemplo
INSERT INTO productos (nombre, descripcion, descripcion_corta, categoria_id, marca_id, precio_base, precio_oferta, es_nuevo, es_destacado, activo) VALUES
('Camisa Oxford Slim Fit', 'Camisa de algodón premium con corte slim fit. Perfecta para ocasiones formales y casuales.', 'Camisa de algodón premium slim fit', 5, 2, 89.90, 69.90, 1, 1, 1),
('Jeans Stretch Classic', 'Jeans de mezclilla con elastano para mayor comodidad. Corte recto clásico.', 'Jeans stretch corte clásico', 6, 1, 129.90, NULL, 1, 0, 1),
('Polo Piqué Premium', 'Polo de algodón piqué de alta calidad. Ideal para uso diario.', 'Polo algodón piqué', 7, 3, 59.90, 49.90, 0, 1, 1),
('Vestido Floral Verano', 'Vestido estampado floral perfecto para la temporada de verano.', 'Vestido floral para verano', 10, 1, 149.90, 119.90, 1, 1, 1),
('Blusa Elegante Seda', 'Blusa de seda natural con acabados premium. Elegante y sofisticada.', 'Blusa de seda elegante', 9, 2, 199.90, NULL, 1, 0, 1);

-- Variantes de productos
INSERT INTO producto_variantes (producto_id, sku, talla, color, color_hex, stock, precio_adicional, activo) VALUES
-- Camisa Oxford
(1, 'CAM-OXF-S-BLA', 'S', 'Blanco', '#FFFFFF', 15, 0.00, 1),
(1, 'CAM-OXF-M-BLA', 'M', 'Blanco', '#FFFFFF', 20, 0.00, 1),
(1, 'CAM-OXF-L-BLA', 'L', 'Blanco', '#FFFFFF', 18, 0.00, 1),
(1, 'CAM-OXF-S-CEL', 'S', 'Celeste', '#87CEEB', 12, 0.00, 1),
(1, 'CAM-OXF-M-CEL', 'M', 'Celeste', '#87CEEB', 25, 0.00, 1),
(1, 'CAM-OXF-L-CEL', 'L', 'Celeste', '#87CEEB', 10, 0.00, 1),
-- Jeans
(2, 'JEA-STR-30-AZU', '30', 'Azul Oscuro', '#00008B', 8, 0.00, 1),
(2, 'JEA-STR-32-AZU', '32', 'Azul Oscuro', '#00008B', 15, 0.00, 1),
(2, 'JEA-STR-34-AZU', '34', 'Azul Oscuro', '#00008B', 12, 0.00, 1),
(2, 'JEA-STR-32-NEG', '32', 'Negro', '#000000', 10, 0.00, 1),
-- Polo
(3, 'POL-PIQ-S-BLA', 'S', 'Blanco', '#FFFFFF', 30, 0.00, 1),
(3, 'POL-PIQ-M-BLA', 'M', 'Blanco', '#FFFFFF', 40, 0.00, 1),
(3, 'POL-PIQ-L-BLA', 'L', 'Blanco', '#FFFFFF', 35, 0.00, 1),
(3, 'POL-PIQ-M-NEG', 'M', 'Negro', '#000000', 25, 0.00, 1),
(3, 'POL-PIQ-M-ROJ', 'M', 'Rojo', '#FF0000', 20, 0.00, 1),
-- Vestido
(4, 'VES-FLO-S-MUL', 'S', 'Multicolor', '#FF69B4', 8, 0.00, 1),
(4, 'VES-FLO-M-MUL', 'M', 'Multicolor', '#FF69B4', 12, 0.00, 1),
(4, 'VES-FLO-L-MUL', 'L', 'Multicolor', '#FF69B4', 6, 0.00, 1),
-- Blusa
(5, 'BLU-SED-S-BLA', 'S', 'Blanco', '#FFFFFF', 5, 0.00, 1),
(5, 'BLU-SED-M-BLA', 'M', 'Blanco', '#FFFFFF', 8, 0.00, 1),
(5, 'BLU-SED-M-NEG', 'M', 'Negro', '#000000', 6, 0.00, 1);

-- Imágenes de productos (URLs de ejemplo)
INSERT INTO producto_imagenes (producto_id, imagen_url, es_principal, orden, alt_text) VALUES
(1, 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=600', 1, 1, 'Camisa Oxford Blanca'),
(1, 'https://images.unsplash.com/photo-1598033129183-c4f50c736f10?w=600', 0, 2, 'Camisa Oxford Detalle'),
(2, 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=600', 1, 1, 'Jeans Classic'),
(3, 'https://images.unsplash.com/photo-1625910513413-5fc7e13ca647?w=600', 1, 1, 'Polo Piqué'),
(4, 'https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=600', 1, 1, 'Vestido Floral'),
(5, 'https://images.unsplash.com/photo-1551163943-3f7fb2caa8d9?w=600', 1, 1, 'Blusa Elegante');

-- Banners
INSERT INTO banners (titulo, subtitulo, imagen_url, enlace_url, texto_boton, posicion, orden, activo) VALUES
('Nueva Colección Verano 2025', 'Descubre las últimas tendencias', 'https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=1200', '/tienda/categoria/1', 'Ver Colección', 'home_principal', 1, 1),
('Hasta 50% de Descuento', 'En productos seleccionados', 'https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?w=1200', '/tienda/ofertas', 'Comprar Ahora', 'home_principal', 2, 1),
('Envío Gratis', 'En compras mayores a S/150', 'https://images.unsplash.com/photo-1558171813-4c088753af8f?w=1200', '/tienda', 'Explorar', 'home_secundario', 1, 1);

-- Usuario de prueba (password: 123456 - encriptada con BCrypt)
INSERT INTO usuarios (email, password, nombres, apellidos, telefono, activo) VALUES
('cliente@test.com', '$2a$10$N.zmTvOB5qPcJQkdNhW2vuXkPuVxeGzTdxw.B.mPbJKlvKiYwjlGK', 'Juan', 'Pérez García', '999888777', 1);

-- Dirección de usuario de prueba
INSERT INTO usuario_direcciones (usuario_id, etiqueta, nombres_receptor, apellidos_receptor, telefono, direccion, departamento, provincia, distrito, es_principal) VALUES
(1, 'Casa', 'Juan', 'Pérez García', '999888777', 'Av. Principal 123, Dpto 401', 'Lima', 'Lima', 'Miraflores', 1);

-- Productos recomendados
INSERT INTO producto_recomendados (producto_id, producto_recomendado_id, tipo, orden, activo) VALUES
(1, 2, 'complementario', 1, 1),
(1, 3, 'similar', 2, 1),
(4, 5, 'complementario', 1, 1),
(2, 3, 'frecuente', 1, 1);

-- ============================================
-- VISTAS ÚTILES
-- ============================================

-- Vista: Productos con stock disponible
CREATE OR REPLACE VIEW v_productos_disponibles AS
SELECT 
    p.id,
    p.nombre,
    p.descripcion_corta,
    p.precio_base,
    p.precio_oferta,
    p.descuento_porcentaje,
    p.es_nuevo,
    p.es_destacado,
    c.nombre AS categoria,
    m.nombre AS marca,
    (SELECT imagen_url FROM producto_imagenes WHERE producto_id = p.id AND es_principal = 1 LIMIT 1) AS imagen_principal,
    (SELECT SUM(stock) FROM producto_variantes WHERE producto_id = p.id AND activo = 1) AS stock_total,
    (SELECT AVG(calificacion) FROM valoraciones WHERE producto_id = p.id AND aprobado = 1) AS calificacion_promedio,
    (SELECT COUNT(*) FROM valoraciones WHERE producto_id = p.id AND aprobado = 1) AS total_valoraciones
FROM productos p
LEFT JOIN categorias c ON p.categoria_id = c.id
LEFT JOIN marcas m ON p.marca_id = m.id
WHERE p.activo = 1;

-- Vista: Resumen de pedidos por usuario
CREATE OR REPLACE VIEW v_mis_pedidos AS
SELECT 
    p.id,
    p.numero_pedido,
    p.fecha_pedido,
    p.total,
    p.metodo_pago,
    u.email,
    CONCAT(u.nombres, ' ', u.apellidos) AS cliente,
    (SELECT COUNT(*) FROM pedido_detalles WHERE pedido_id = p.id) AS total_items
FROM pedidos p
JOIN usuarios u ON p.usuario_id = u.id;

-- ============================================
-- ÍNDICES ADICIONALES PARA RENDIMIENTO
-- ============================================

CREATE INDEX idx_productos_categoria ON productos(categoria_id);
CREATE INDEX idx_productos_marca ON productos(marca_id);
CREATE INDEX idx_productos_activo ON productos(activo);
CREATE INDEX idx_productos_destacado ON productos(es_destacado);
CREATE INDEX idx_productos_nuevo ON productos(es_nuevo);

-- ============================================
-- FIN DEL SCRIPT
-- ============================================

SELECT 'Base de datos creada exitosamente!' AS mensaje;
