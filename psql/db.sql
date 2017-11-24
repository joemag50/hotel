
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
                       tipo_usuario    TEXT NOT NULL DEFAULT 'OPE');  --ADM: administrador, SUP: supervisor, AUD: auditor, OPE: operador

-- -------------------------------------------------------------
-- JCGE: Esta es la relacion de el titular con la solicitud de habitacion
CREATE TABLE hospedajes (idsolicitud     INTEGER PRIMARY KEY,
                         idhabitacion    INTEGER,
                         idhuesped       INTEGER REFERENCES huespedes (idhuesped), -- (responsable)
                         estatus         INTEGER, -- (0 reservacion, 1 activa, 2 terminada, 3 cancelada)
                         fecha_reserv    DATE,    -- Fecha en la que hacen la reservacion
                         fecha_inicio    DATE,    -- Fecha de inicio de reservacion
                         fecha_fin       DATE,    -- Fecha en la que se deben de salir
                         idusuario       TEXT REFERENCES usuarios (idusuario));

-- -------------------------------------------------------------
-- JCGE: Esta tabla es que personas se estan hospedando en cada solicitud, debe de incluir al titular
CREATE TABLE hospedaje_huesped (idsolicitud  INTEGER REFERENCES hospedajes (idsolicitud),
                                idhuesped    INTEGER REFERENCES huespedes (idhuesped));

-- -------------------------------------------------------------
-- JCGE: Esta tabla 
CREATE TABLE cuentas (cuenta      NUMERIC PRIMARY KEY,
                      nombre      TEXT,
                      tipo_cuenta INTEGER);

-- -------------------------------------------------------------
-- JCGE: Esta tabla 
CREATE TABLE polizas (idpoliza SERIAL PRIMARY KEY,
                      cargos   NUMERIC,
                      abonos   NUMERIC,
                      cuenta   NUMERIC REFERENCES cuentas (cuenta));

-- -------------------------------------------------------------
-- JCGE: Esta tabla es donde se vacia cuanto fue el "consumo por solicitud"
CREATE TABLE pagos (idpago      SERIAL NOT NULL PRIMARY KEY,
                    idsolicitud INTEGER REFERENCES hospedajes (idsolicitud),
                    idpoliza    INTEGER REFERENCES polizas (idpoliza),
                    cargo       NUMERIC DEFAULT 0,
                    abono       NUMERIC DEFAULT 0,
                    concepto    TEXT DEFAULT 'Concepto desconocido',
                    idusuario   TEXT REFERENCES usuarios (idusuario));

-- -------------------------------------------------------------
-- JCGE: Esta tabla es para poner las descripciones de las habitaciones
CREATE TABLE habitaciones (idhabitacion  SERIAL PRIMARY KEY,
                           edificio      TEXT,
                           numero_fisico TEXT,
                           tabulacion    NUMERIC, --Cuanto va a multiplicarse la habitacion
                           descripcion   TEXT);


-- -------------------------------------------------------------
-- JCGE: Esta tabla es para poner el precio de las cosas multiplicado por las tabulaciones
CREATE TABLE precios (iditem INTEGER PRIMARY KEY,
                      nombre TEXT,
                      precio NUMERIC);

-- -------------------------------------------------------------
-- JCGE: Esta tabla es para poner el tipo de mediciones
CREATE TABLE tipo_medicion (idtipomedicion TEXT PRIMARY KEY,
                            descripcion    TEXT);

-- -------------------------------------------------------------
-- JCGE: Esta tabla es para los inventarios
CREATE TABLE inventario (idobjeto       SERIAL PRIMARY KEY,
                         descripcion    TEXT,
                         idtipomedicion TEXT REFERENCES tipo_medicion (idtipomedicion),
                         cantidad       NUMERIC);

-- -------------------------------------------------------------
-- JCGE: Esta tabla 
CREATE TABLE log (idlog       SERIAL PRIMARY KEY,
                  descripcion TEXT,
                  modulo      TEXT,
                  idusuario   TEXT REFERENCES usuarios (idusuario),
                  fecha       TEXT,
                  hora        TIME);

-- -------------------------------------------------------------
-- JCGE: Esta tabla 
CREATE TABLE paises (idpais SERIAL PRIMARY KEY,
                     pais   TEXT);

-- -------------------------------------------------------------
-- JCGE: Esta tabla 
CREATE TABLE estados (idestado SERIAL PRIMARY KEY,
                      estado   TEXT,
                      idpais   INTEGER REFERENCES paises (idpais));

-- -------------------------------------------------------------
-- JCGE: Esta tabla 
CREATE TABLE municipios (idmunicipio SERIAL PRIMARY KEY,
                         municipio   TEXT,
                         idestado    INTEGER REFERENCES estados (idestado));

-- -------------------------------------------------------------
-- JCGE: Esta tabla 
CREATE TABLE colonias (idcolonia   SERIAL PRIMARY KEY,
                       colonia     TEXT,
                       idmunicipio INTEGER REFERENCES municipios (idmunicipio));
-- -------------------------------------------------------------
-- JCGE: Esta tabla 
CREATE TABLE calles (idcalle   SERIAL PRIMARY KEY,
                     calle     TEXT,
                     idcolonia INTEGER REFERENCES colonias (idcolonia));

--JCGE datos para rellenar las tablas
--INSERT INTO huespedes VALUES (DEFAULT, 'JOSE CARLOS','GAMBOA', 'ESPARZA','06/09/1996'::DATE,'90909090909','JCGE1234',DEFAULT);
--INSERT INTO usuarios VALUES ('joemag50',md5('MuseMuse50'),DEFAULT,'GAMBOA','ESPARZA','JOSE CARLOS','06/09/1996','83702083','CRUP','RFC','NSS',TRUE,'ADM');


--INSERT INTO habitaciones VALUES (1, 'Azul', '1', 100.00, 'Descripcion');
--INSERT INTO habitaciones VALUES (2, 'Azul', '2', 200.00, 'Descripcion');
--INSERT INTO habitaciones VALUES (3, 'Azul', '3', 300.00, 'Descripcion');
--INSERT INTO habitaciones VALUES (4, 'Azul', '4', 400.00, 'Descripcion');
--INSERT INTO habitaciones VALUES (5, 'Azul', '5', 500.00, 'Descripcion');
--INSERT INTO habitaciones VALUES (6, 'Azul', '6', 600.00, 'Descripcion');
--INSERT INTO habitaciones VALUES (7, 'Azul', '7', 700.00, 'Descripcion');
--INSERT INTO habitaciones VALUES (8, 'Azul', '8', 800.00, 'Descripcion');
--INSERT INTO habitaciones VALUES (9, 'Azul', '9', 900.00, 'Descripcion');

--INSERT INTO precios VALUES (1, 'Habitacion sencilla', 10.0) ;
--INSERT INTO precios VALUES (2, 'Habitacion doble', 20.0) ;
--INSERT INTO precios VALUES (3, 'Habitacion VIP', 50.0) ;