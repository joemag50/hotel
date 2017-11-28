
/*
JCGE 17/08/2017: Iniciamos el archivo para generar la base
createdb hotel -U postgres
*/

-- -------------------------------------------------------------
-- JCGE 17/08/2017: Creamos las tablas correspondientes
-- -------------------------------------------------------------


--Nota: si no vas a hacer operaciones no vale la pena que sea entero
--Nota: Existe timestamp en java?

-- -------------------------------------------------------------
-- JCGE: Personas, ya sean huespedes o usuarios, proveedores?
CREATE TABLE huespedes (idhuesped           SERIAL PRIMARY KEY, 
                        paterno             TEXT NOT NULL,
                        materno             TEXT,
                        nombre              TEXT NOT NULL,
                        fechanacimiento     DATE,
                        telefono            TEXT,
                        tarjeta             TEXT,
                        secret_num          TEXT,
                        ultimaactualizacion TIMESTAMP DEFAULT now());

-- -------------------------------------------------------------
-- JCGE: Usuarios que van a manipular el sistema, ovbiamente son empleados
CREATE TABLE usuarios (idusuario       TEXT NOT NULL PRIMARY KEY,
                       pass            TEXT NOT NULL,
                       kusuario        SERIAL NOT NULL,
                       paterno         TEXT NOT NULL,
                       materno         TEXT,
                       nombre          TEXT NOT NULL,
                       fechanacimiento DATE,
                       telefono        TEXT,
                       curp            TEXT,
                       rfc             TEXT,
                       nss             TEXT,
                       activo          BOOLEAN NOT NULL DEFAULT TRUE,
                       tipo_usuario    TEXT NOT NULL DEFAULT 'OPE');  --ADM: administrador, SUP: supervisor, OPE: operador

-- -------------------------------------------------------------
-- JCGE: Esta es la relacion de el titular con la solicitud de habitacion
CREATE TABLE hospedajes (idsolicitud     SERIAL PRIMARY KEY,
                         idhabitacion    INTEGER,
                         idhuesped       INTEGER REFERENCES huespedes (idhuesped), -- (responsable)
                         estatus         INTEGER, -- (0 reservacion, 1 activa, 2 terminada, 3 cancelada)
                         fecha_reserv    DATE,    -- Fecha en la que hacen la reservacion
                         fecha_inicio    DATE,    -- Fecha de inicio de reservacion
                         fecha_fin       DATE,    -- Fecha en la que se deben de salir
                         idusuario       TEXT REFERENCES usuarios (idusuario));

-- -------------------------------------------------------------
-- JCGE: Esta tabla es donde se vacia cuanto fue el "consumo por solicitud"
CREATE TABLE pagos (idpago          SERIAL NOT NULL PRIMARY KEY,
                    idsolicitud     INTEGER REFERENCES hospedajes (idsolicitud),
                    cargo           NUMERIC DEFAULT 0,
                    abono           NUMERIC DEFAULT 0,
                    concepto        TEXT DEFAULT 'Concepto desconocido',
                    cantidad        INTEGER,
                    precio_unitario NUMERIC,
                    idusuario       TEXT REFERENCES usuarios (idusuario));

-- -------------------------------------------------------------
-- JCGE: Esta tabla es para poner las descripciones de las habitaciones
CREATE TABLE habitaciones (idhabitacion  SERIAL PRIMARY KEY,
                           edificio      TEXT,
                           numero_fisico TEXT,
                           tabulacion    NUMERIC, --Cuanto va a multiplicarse la habitacion
                           descripcion   TEXT,
                           limpiar       BOOLEAN);

-- -------------------------------------------------------------
-- JCGE: Esta tabla 
CREATE TABLE log (idlog       SERIAL PRIMARY KEY,
                  descripcion TEXT,
                  modulo      TEXT,
                  idusuario   TEXT REFERENCES usuarios (idusuario),
                  fecha       TEXT,
                  hora        TIME);

--JCGE datos para rellenar las tablas
INSERT INTO usuarios VALUES ('admhotel',md5('123456'),DEFAULT,'GAMBOA','ESPARZA','JOSE CARLOS','06/09/1996','83702083','CRUP','RFC','NSS',TRUE,'ADM');
INSERT INTO usuarios VALUES ('supervisor1',md5('123456'),DEFAULT,'supervisor','supervisor','supervisor','06/09/1996','83702083','CRUP','RFC','NSS',TRUE,'SUP');
INSERT INTO usuarios VALUES ('operador1',md5('123456'),DEFAULT,'operador1','operador1','operador1','06/09/1996','83702083','CRUP','RFC','NSS',TRUE,'OPE');
INSERT INTO usuarios VALUES ('limpieza1',md5('123456'),DEFAULT,'limpieza1','limpieza1','limpieza1','06/09/1996','83702083','CRUP','RFC','NSS',TRUE,'LIM');


INSERT INTO habitaciones VALUES (1, 'Azul', '1', 100.00, 'Descripcion');
INSERT INTO habitaciones VALUES (2, 'Azul', '2', 200.00, 'Descripcion');
INSERT INTO habitaciones VALUES (3, 'Azul', '3', 300.00, 'Descripcion');
INSERT INTO habitaciones VALUES (4, 'Azul', '4', 400.00, 'Descripcion');
INSERT INTO habitaciones VALUES (5, 'Azul', '5', 500.00, 'Descripcion');
INSERT INTO habitaciones VALUES (6, 'Azul', '6', 600.00, 'Descripcion');
INSERT INTO habitaciones VALUES (7, 'Azul', '7', 700.00, 'Descripcion');
INSERT INTO habitaciones VALUES (8, 'Azul', '8', 800.00, 'Descripcion');
INSERT INTO habitaciones VALUES (9, 'Azul', '9', 900.00, 'Descripcion');
INSERT INTO habitaciones VALUES (10, 'Azul', '10', 1000.00, 'Descripcion');
