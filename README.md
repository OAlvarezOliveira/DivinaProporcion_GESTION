# Bodegas Divina Proporción: control de producción

Aplicación de consola en Java para gestionar la producción de una bodega: variedades, parcelas, cosechas, productos embotellados y ventas con control de stock. Los datos se guardan en MariaDB mediante JDBC.

## Requisitos

- Java 17 o superior (desarrollado con Java 21).
- Docker con Docker Compose, para la base de datos.
- Maven, solo si se quiere generar el JAR.

## Puesta en marcha

Todos los comandos se ejecutan **desde la raíz del repositorio**, porque la aplicación usa rutas relativas (`datos/...`).

1. Levantar la base de datos:

   ```bash
   docker compose up -d
   ```

   La primera vez crea la base `divinaProporcion` y ejecuta `sql/schema.sql`, que crea las tablas y carga datos de ejemplo. La base escucha en `127.0.0.1:3307`.

2. Generar el JAR ejecutable:

   ```bash
   mvn clean package -DskipTests
   ```

   El resultado es `target/DivinaProporcion.jar`, con el driver de MariaDB y Gson incluidos.

3. Ejecutar:

   ```bash
   java -jar target/DivinaProporcion.jar
   ```

   Si las tildes salen mal en la consola: `java -Dfile.encoding=UTF-8 -jar target/DivinaProporcion.jar`.

### Reiniciar la base desde cero

```bash
docker compose down -v
docker compose up -d
```

`-v` borra el volumen, por lo que se pierden todos los datos y se vuelve a cargar `sql/schema.sql`.

## Funcionalidades

Menú principal:

| Opción | Qué hace |
|---|---|
| 1. Gestión de parcelas | Crear, listar (con filtros), editar y borrar |
| 2. Gestión de cosechas | Crear, listar, editar y borrar |
| 3. Gestión de variedades | Crear, listar, editar y borrar |
| 4. Importar vendimia (CSV) | Lee `datos/vendimia.csv` y valida cada línea |
| 5. Exportar cosechas (JSON) | Escribe `datos/cosechas_exportadas.json` |
| 6. Importar cosechas (JSON) | Lee `datos/cosechas_importar.json` y valida cada cosecha |
| 7. Generar informe de ventas anual | Escribe `datos/informe_ventas.txt`: botellas y ventas por producto y año, con el total anual |
| 8. Backup y restaurar datos | Genera `datos/backup.bin` o restaura la base desde él |
| 9. Gestión de ventas y stock | Listar productos con stock, registrar una venta y listar ventas |

### Decisiones de diseño

- **El stock no se guarda**: se calcula como las botellas producidas menos la suma de las ventas, para que no pueda desincronizarse.
- **Registrar una venta es una transacción**: se comprueba el stock y se inserta la venta en la misma conexión; si algo falla se hace `rollback`.
- **Las importaciones son transaccionales**: las líneas incorrectas se descartan con un aviso y un resumen; si falla la base de datos se deshace todo lo insertado.
- **El backup es binario** (serialización de objetos) e incluye las cinco tablas. Restaurar **sustituye** el contenido de la base en una sola transacción: borra de `venta` a `variedad`, reinserta en el orden contrario y conserva los ids originales, de forma que o se restaura todo o no cambia nada.
- Un backup generado antes de cambiar las clases del modelo puede no ser compatible: conviene regenerarlo.

## Estructura del proyecto

```
src/main/java/divinaProporcion/
├── Main.java
├── vista/         Menu: único sitio que lee y escribe en consola
├── controlador/   Convierte resultados y errores en mensajes de texto
├── dao/           Acceso a datos con JDBC (incluye DAOBackup)
├── io/            Importadores, exportador, informe y backup
└── modelo/        Variedad, Parcela, Cosecha, Producto, Venta...
sql/schema.sql     Tablas y datos de ejemplo
datos/             Ficheros de entrada y salida
docker-compose.yml Base de datos MariaDB local
```

## Base de datos

Tablas: `variedad`, `parcela`, `cosecha`, `producto` y `venta`, relacionadas por claves foráneas. La conexión usa un usuario de desarrollo creado por `sql/schema.sql` y está pensada **solo para la base local** de Docker; la configuración está en `dao/ConexionDB.java` y en `docker-compose.yml`.

## Documentación

Para generar el Javadoc:

```bash
mvn javadoc:javadoc
```

Se genera en `target/reports/apidocs/index.html`.
