-- 1. TABLAS MAESTRAS (No dependen de ninguna otra)


CREATE TABLE "clientes"(
    "id_cliente" bigserial NOT NULL,
    "numero_identificacion" VARCHAR(255) NOT NULL,
    "nombre_completo" VARCHAR(255) NOT NULL,
    "telefono" VARCHAR(255) NOT NULL,
    "correo" VARCHAR(255) NOT NULL,
    "direccion" VARCHAR(255) NOT NULL,
    "estado" VARCHAR(255) NOT NULL,
    "fecha_registro" TIMESTAMP(0) WITH TIME ZONE NOT NULL,
    PRIMARY KEY("id_cliente")
);

CREATE TABLE "users"(
    "id_user" bigserial NOT NULL,
    "username" VARCHAR(255) NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    "nombre_completo" VARCHAR(255) NOT NULL,
    "correo" VARCHAR(255) NOT NULL,
    "rol" VARCHAR(255) NOT NULL,
    "is_activo" BOOLEAN NOT NULL,
    "created_at" TIMESTAMP(0) WITH TIME ZONE NOT NULL,
    PRIMARY KEY("id_user")
);

CREATE TABLE "repuestos"(
    "id_repuesto" bigserial NOT NULL,
    "codigo_referencia" VARCHAR(255) NOT NULL,
    "nombre" VARCHAR(255) NOT NULL,
    "categoria" VARCHAR(255) NOT NULL,
    "proveedor" VARCHAR(255) NOT NULL,
    "stock_total" BIGINT NOT NULL,
    "stock_disponible" BIGINT NOT NULL,
    "precio_unitario" DECIMAL(8, 2) NOT NULL,
    "is_activo" BOOLEAN NOT NULL,
    "created_at" TIMESTAMP(0) WITH TIME ZONE NOT NULL,
    PRIMARY KEY("id_repuesto")
);


-- 2. TABLAS DEPENDIENTES (Contienen relaciones)


CREATE TABLE "vehiculos"(
    "id_vehiculo" bigserial NOT NULL,
    "id_cliente" BIGINT NOT NULL,
    "placa" VARCHAR(255) NOT NULL,
    "marca" VARCHAR(255) NOT NULL,
    "modelo" VARCHAR(255) NOT NULL,
    "categoria" VARCHAR(255) NOT NULL,
    "anio_modelo" SMALLINT NOT NULL,
    PRIMARY KEY("id_vehiculo")
);

CREATE TABLE "ordenes_de_servicios"(
    "id" bigserial NOT NULL,
    "id_vehiculo" BIGINT NOT NULL,
    "id_cliente" BIGINT NOT NULL,
    "id_user" BIGINT NOT NULL,
    "descripcion_falla" TEXT NOT NULL,
    "diagnostico" TEXT NOT NULL,
    "total_mano_obra" DECIMAL(8, 2) NOT NULL,
    "total_repuestos" DECIMAL(8, 2) NOT NULL,
    "total_pagar" DECIMAL(8, 2) NOT NULL,
    "estado" VARCHAR(255) NOT NULL,
    "fecha_registro" TIMESTAMP(0) WITH TIME ZONE NOT NULL,
    PRIMARY KEY("id")
);


-- 3. TABLA INTERMEDIA (Para relacionar órdenes con repuestos)


CREATE TABLE "detalles_orden_repuestos"(
    "id_detalle" bigserial NOT NULL,
    "id_orden" BIGINT NOT NULL,
    "id_repuesto" BIGINT NOT NULL,
    "cantidad" INT NOT NULL,
    "precio_unitario_historico" DECIMAL(8, 2) NOT NULL, 
    PRIMARY KEY("id_detalle")
);


-- 4. DEFINICIÓN DE RELACIONES (FOREIGN KEYS)



ALTER TABLE "vehiculos" 
    ADD CONSTRAINT "fk_vehiculo_cliente" 
    FOREIGN KEY("id_cliente") REFERENCES "clientes"("id_cliente");


ALTER TABLE "ordenes_de_servicios" 
    ADD CONSTRAINT "fk_orden_vehiculo" 
    FOREIGN KEY("id_vehiculo") REFERENCES "vehiculos"("id_vehiculo");

ALTER TABLE "ordenes_de_servicios" 
    ADD CONSTRAINT "fk_orden_cliente" 
    FOREIGN KEY("id_cliente") REFERENCES "clientes"("id_cliente");

ALTER TABLE "ordenes_de_servicios" 
    ADD CONSTRAINT "fk_orden_user" 
    FOREIGN KEY("id_user") REFERENCES "users"("id_user");


ALTER TABLE "detalles_orden_repuestos" 
    ADD CONSTRAINT "fk_detalle_orden" 
    FOREIGN KEY("id_orden") REFERENCES "ordenes_de_servicios"("id");

ALTER TABLE "detalles_orden_repuestos" 
    ADD CONSTRAINT "fk_detalle_repuesto" 
    FOREIGN KEY("id_repuesto") REFERENCES "repuestos"("id_repuesto");


-- 5. INSERTS DE USUARIOS INICIALES

INSERT INTO "users" (
    "username", 
    "password", 
    "nombre_completo", 
    "correo", 
    "rol", 
    "is_activo", 
    "created_at"
) VALUES 
(
    'admin_taller', 
    '1234', 
    'Alejandro Magno', 
    'admin@taller.com', 
    'ADMIN', 
    true, 
    CURRENT_TIMESTAMP
),
(
    'recepcion', 
    'recep123', 
    'Recepcionista Principal', 
    'recepcion@taller.com', 
    'RECEPCIONISTA', 
    true, 
    CURRENT_TIMESTAMP
);