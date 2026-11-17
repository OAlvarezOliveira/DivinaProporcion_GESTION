
/*
 * Bodegas Divina Proporción - esquema de la base de datos.
 * Cinco tablas: variedad -> parcela -> cosecha -> producto -> venta.
 * Cada flecha es una relación 1 a N (una variedad tiene varias parcelas, etc.).
 * Todas las claves primarias son autonuméricas.
 * El stock NO se guarda: se calcula como producto.botellas - SUM(venta.cantidad).
 */

/*crear la BBDD y el usuario de la aplicación si no existen (script autosuficiente).
  Hay que ejecutarlo con un usuario con permisos de administración (root).
  AVISO: más abajo se borran y recrean las tablas; no lanzar contra una BD con datos a conservar.*/
CREATE DATABASE IF NOT EXISTS divinaProporcion CHARACTER SET utf8mb4;
CREATE USER IF NOT EXISTS 'dp'@'%' IDENTIFIED BY 'dp_dev';
GRANT ALL PRIVILEGES ON divinaProporcion.* TO 'dp'@'%';

/*definir la BBDD a usar*/
USE divinaProporcion;

/*en modo pruebas borrar las tablas si ya están creadas - comentar para demo.
  El orden va de la más dependiente a la menos: no se puede borrar una tabla
  si otra tiene una clave foránea que apunta a ella.*/
DROP TABLE IF EXISTS venta;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS cosecha;
DROP TABLE IF EXISTS parcela;
DROP TABLE IF EXISTS variedad;

/* Catálogo de uvas. Se guarda aparte para no repetir nombre y color en cada parcela. */
CREATE TABLE variedad (
    id INT AUTO_INCREMENT PRIMARY KEY,
    /* UNIQUE: no puede haber dos variedades con el mismo nombre */
    nombre VARCHAR(100) NOT NULL UNIQUE,
    /* ENUM: solo admite estos tres valores; cualquier otro lo rechaza la BD */
    color ENUM('tinto', 'blanco', 'rosado') NOT NULL
);

/* Terreno de viñedo. Tiene plantada una variedad (FK a variedad). */
CREATE TABLE parcela (
    id INT AUTO_INCREMENT PRIMARY KEY,
    /* UNIQUE: la bodega identifica cada parcela por su nombre */
    nombre VARCHAR(100) NOT NULL UNIQUE,
    /* hectáreas; DECIMAL (no FLOAT) para no perder precisión; (6,2) admite hasta 9999.99; debe ser > 0 */
    extension DECIMAL(6,2) NOT NULL CHECK (extension > 0),
    id_variedad INT NOT NULL,
    ubicacion VARCHAR(100),
    /* sin ON DELETE: no se puede borrar una variedad que tenga parcelas */
    FOREIGN KEY (id_variedad) REFERENCES variedad(id)
);

/* Lo que da una parcela en una vendimia concreta (un año). */
CREATE TABLE cosecha (
    id INT AUTO_INCREMENT PRIMARY KEY,
    /* año de la vendimia; el CHECK evita años imposibles (negativos, 99999...) */
    anualidad INT NOT NULL CHECK (anualidad BETWEEN 1900 AND 2200),
    id_parcela INT NOT NULL,
    /* kilos de uva vendimiados */
    cantidad DECIMAL(10,2) NOT NULL CHECK (cantidad >= 0),
    /* grado alcohólico probable (%). Opcional: si no se cosechó nada (cantidad = 0) no hay
       qué medir y se guarda NULL; si hay valor, debe ser > 0 */
    graduacion DECIMAL(4,2) CHECK (graduacion > 0),
    FOREIGN KEY (id_parcela) REFERENCES parcela(id),
    /* una parcela solo puede tener una cosecha por año */
    UNIQUE (id_parcela, anualidad)
);

/* Vino embotellado con nombre comercial, elaborado a partir de una cosecha.
   La variedad se obtiene a través de cosecha -> parcela -> variedad. */
CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    id_cosecha INT NOT NULL,
    /* botellas embotelladas (no el stock actual; el stock se calcula con las ventas) */
    botellas INT NOT NULL CHECK (botellas >= 0),
    FOREIGN KEY (id_cosecha) REFERENCES cosecha(id),
    /* el mismo vino puede repetirse en distintas cosechas, pero no dos veces en la misma */
    UNIQUE (nombre, id_cosecha)
);

/* Cada salida de botellas de un producto. Es el historial del que se calcula el stock. */
CREATE TABLE venta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    /* botellas vendidas; el CHECK solo exige > 0. Que no superen el stock lo comprobará la app
   con una transacción (pendiente de implementar) */
    cantidad INT NOT NULL CHECK (cantidad > 0),
    /* sin CHECK a propósito: no se busca un realismo estricto, la fecha queda libre */
    fecha DATE NOT NULL,
    FOREIGN KEY (id_producto) REFERENCES producto(id)
);


/*datos de prueba - la bodega y los nombres de los vinos son reales (Toro, Zamora), salvo el rosado,
  que es inventado. Kilos, botellas, graduaciones, parcelas y ventas son inventados*/
INSERT INTO variedad (nombre, color) VALUES
    ('Tinta de Toro', 'tinto'),
    ('Malvasía', 'blanco'),
    ('Verdejo', 'blanco'),
    ('Garnacha Rosada', 'rosado');

/* id_variedad de cada parcela: 1 Tinta de Toro, 2 Malvasía, 3 Verdejo, 4 Garnacha Rosada.
   Resultado: parcelas 1-3 son Tinta de Toro, la 4 Verdejo, la 5 Malvasía y la 6 Garnacha Rosada. */
INSERT INTO parcela (nombre, extension, id_variedad, ubicacion) VALUES
    ('El Cerrado', 4.50, 1, 'Toro (Zamora)'),
    ('Los Pozos', 3.20, 1, 'Morales de Toro (Zamora)'),
    ('El Carrascal', 2.80, 1, 'Venialbo (Zamora)'),
    ('Los Cantos', 3.60, 3, 'Morales de Toro (Zamora)'),
    ('Valhondo', 0.90, 2, 'Venialbo (Zamora)'),
    ('La Cañada', 1.20, 4, 'Toro (Zamora)');

/* id_parcela entre 1 y 6. El UNIQUE de la tabla impide repetir la pareja (parcela, año) */
INSERT INTO cosecha (anualidad, id_parcela, cantidad, graduacion) VALUES
    (2022, 1, 27000.00, 14.20),
    (2023, 1, 29500.00, 14.60),
    (2024, 1, 26800.00, 14.40),
    (2022, 2, 19500.00, 14.80),
    (2023, 2, 21000.00, 14.50),
    (2024, 2, 18700.00, 14.90),
    (2023, 3, 15200.00, 14.30),
    (2024, 3, 16100.00, 14.70),
    (2022, 4, 21000.00, 12.50),
    (2023, 4, 22800.00, 12.80),
    (2024, 4, 20400.00, 12.60),
    (2024, 5, 4300.00, 12.20),
    (2023, 6, 7200.00, 13.50),
    (2024, 6, 7600.00, 13.80);

/* Cada vino cuelga de una cosecha de variedad coherente con el vino. Verificado: 24 Mozas,
   Abracadabra y Madremía son Tinta de Toro. Supuesto, sin verificar: Platón Tinto, Encomienda
   de la Vega y Loquillo Tinto Barrica (Tinta de Toro) y Loquillo Verdejo, El Principito (Malvasía).
   El rosado es inventado: no se encontró un rosado real de la bodega.
   Las cosechas 11 (Verdejo 2024) y 14 (Garnacha Rosada 2024) quedan sin producto a propósito
   (caso válido para probar). */
INSERT INTO producto (nombre, id_cosecha, botellas) VALUES
    ('24 Mozas', 1, 16000),
    ('Abracadabra', 2, 17500),
    ('Madremía', 4, 11500),
    ('24 Mozas', 5, 12500),
    ('Loquillo Tinto Barrica', 7, 9000),
    ('Platón Tinto', 8, 5800),
    ('Encomienda de la Vega', 3, 7400),
    ('Platón Tinto', 6, 4200),
    ('Loquillo Verdejo', 9, 12500),
    ('Loquillo Verdejo', 10, 13500),
    ('El Principito Malvasía', 12, 2500),
    ('Rosado Divina Proporción', 13, 4200);

/* Ninguna venta supera las botellas del producto, así el stock sale siempre >= 0 */
INSERT INTO venta (id_producto, cantidad, fecha) VALUES
    (1, 1200, '2025-03-15'),
    (1, 2500, '2025-09-20'),
    (1, 1800, '2026-02-10'),
    (1, 1000, '2026-10-05'),
    (2, 3000, '2025-11-05'),
    (2, 2200, '2026-04-18'),
    (3, 1500, '2025-10-12'),
    (4, 2000, '2026-01-25'),
    (5, 900, '2026-03-03'),
    (6, 500, '2026-05-14'),
    (7, 1100, '2025-12-01'),
    (8, 800, '2026-06-20'),
    (9, 2400, '2026-07-09'),
    (10, 3100, '2026-08-30'),
    (11, 600, '2026-09-15'),
    (12, 400, '2026-09-20');
