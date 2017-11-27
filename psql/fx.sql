
-- -------------------------------------------------------------
-- JCGE 17/08/2017: Creamos las funciones correspondientes
-- -------------------------------------------------------------

/*
--------------------------
-- JCGE: Funcion general
--------------------------
CREATE OR REPLACE FUNCTION nombre_fx ()
  RETURNS returntype AS $$
DECLARE
  --Variables
BEGIN
  --
END;$$
LANGUAGE plpgsql;
*/


--------------------------
-- JCGE: Funcion para saber si esta activo el usuario
--------------------------
CREATE OR REPLACE FUNCTION hotel_valida_usuario
  (p_user     TEXT,
  OUT r_found BOOLEAN,
  OUT r_mesj  TEXT)
  RETURNS RECORD AS $$
DECLARE
  --Variables
BEGIN
  -- JCGE: Buscamos si el usuario existe y esta activo
  PERFORM * FROM usuarios WHERE idusuario = p_user AND activo;
  IF (NOT FOUND) THEN
    r_mesj  := 'El usuario esta suspendido';
    r_found := FALSE;
  ELSE
    r_mesj  := 'El usuario esta activo';
    r_found := TRUE;
  END IF;
  RETURN;
END;$$
LANGUAGE plpgsql;


--------------------------
-- JCGE: Funcion para validar la contraseña si es correcta
--------------------------
CREATE OR REPLACE FUNCTION hotel_valida_pass
  (p_user     TEXT,
  p_pass      TEXT,
  p_intentos  INTEGER,
  OUT r_found BOOLEAN,
  OUT r_mesj  TEXT)
  RETURNS RECORD AS $$
DECLARE
  --Variables
BEGIN
  r_found := FALSE;
  r_mesj  := '';
  -- JCGE: Le actualizamos si pasa de los intentos
  -- Contamos desde 0
  IF (p_intentos > 2) THEN
    UPDATE usuarios SET activo = FALSE WHERE idusuario = p_user;
  END IF;
  -- JCGE: Buscamos si existe el usuario y contraseña
  PERFORM * FROM usuarios WHERE (idusuario,pass) = (p_user,md5(p_pass));
  IF (FOUND) THEN
    r_found := TRUE;
  END IF;
  RETURN;
END;$$
LANGUAGE plpgsql;



--------------------------
-- JCGE: Funcion para saber si existe una habitacion en el mismo edificio
--------------------------
CREATE OR REPLACE FUNCTION hotel_existe_habitacion (
  p_idhabitacion  INTEGER,
  p_edificio      TEXT,
  p_numero_fisico TEXT,
  OUT r_found     BOOLEAN,
  OUT r_mesj      TEXT)
  RETURNS RECORD AS $$
DECLARE
  --Variables
BEGIN
  r_found := FALSE;
  p_edificio      := trim(p_edificio) ; 
  p_numero_fisico := trim(p_numero_fisico) ; 
  PERFORM *
    FROM habitaciones
   WHERE (edificio,numero_fisico) = (p_edificio,p_numero_fisico) AND
         idhabitacion <> p_idhabitacion;
  IF FOUND THEN
    r_found         := TRUE;
    r_mesj          := 'Existe una habitación con los nombres proporcionados, Favor de intentar con otros nombres.';
  END IF;
  RETURN;
END;$$
LANGUAGE plpgsql;


--------------------------
-- JCGE: Funcion para retornar cuanto cuesta por persona
--------------------------
CREATE OR REPLACE FUNCTION hotel_precioxpersona (p_nacimiento DATE)
  RETURNS NUMERIC AS $$
DECLARE
  --Variables
  edad INTEGER;
BEGIN
  --JCGE: Si la fecha de nacimiento es nula, pues regresamos cero
  IF (p_nacimiento IS NULL) THEN
    RETURN 0.00;
  END IF;
  --JCGE: Obtenemos la edad la fecha de nacimiento
  edad := (string_to_array(age(p_nacimiento)::TEXT,' '))[1];
  --JCGE: Si es menor de edad o de la tercera edad, le cobramos menos
  IF (edad < 18 OR edad >= 65) THEN
    RETURN 250.00;
  ELSE
    --JCGE: Cobro normal
    RETURN 500.00;
  END IF;
END;$$
LANGUAGE plpgsql;


--------------------------
-- JCGE: Funcion para regresar el tipo de solicitud
--------------------------
CREATE OR REPLACE FUNCTION hotel_solicitud_estatus (estatus INTEGER)
  RETURNS TEXT AS $$
DECLARE
  --Variables
  arr TEXT[] := ARRAY['Reservación', 'Activa', 'Terminada', 'Cancelada'];
BEGIN
  --
  RETURN arr[estatus+1];
END;$$
LANGUAGE plpgsql;


--------------------------
-- JCGE: Funcion para regresar el estatus de una habitacion
--------------------------
CREATE OR REPLACE FUNCTION hotel_habitacion_estatus (p_idhabitacion INTEGER, p_fecha DATE)
  RETURNS TEXT AS $$
DECLARE
  --Variables
  arr TEXT[] := ARRAY['Libre', 'Ocupada', 'X'];
BEGIN
  --JCGE: De jodido que exista la habitacion
  PERFORM * FROM habitaciones WHERE idhabitacion = p_idhabitacion;
  IF (NOT FOUND) THEN
    RETURN arr[3];
  END IF;
  --JCGE: Si es un dia que ya paso
  IF (p_fecha < now()::DATE) THEN
    RETURN arr[3];
  END IF;
  --JCGE: Buscamos si en el futuro va a estar libre u ocupada
  PERFORM *
    FROM hospedajes
   WHERE idhabitacion = p_idhabitacion AND 
         (p_fecha BETWEEN fecha_inicio AND fecha_fin) AND 
         estatus <= 1;
  IF FOUND THEN
    RETURN arr[2];
  END IF;
  --JCGE: Significa que no encontro ninguna reservacion
  RETURN arr[1];
END;$$
LANGUAGE plpgsql;


--------------------------
-- JCGE: Funcion para regresar el estatus de una habitacion
--------------------------
CREATE OR REPLACE FUNCTION hotel_hospedaje_info (p_idhabitacion INTEGER, p_fecha DATE)
  RETURNS TEXT AS $$
DECLARE
  --Variables
  regreso TEXT := 'Sin Información';
  r       RECORD;
BEGIN
  --JCGE: De jodido que exista la habitacion
  PERFORM * FROM habitaciones WHERE idhabitacion = p_idhabitacion;
  IF (NOT FOUND) THEN
    RETURN regreso;
  END IF;
  p_fecha := COALESCE(p_fecha,now()::DATE);
  --JCGE: Buscamos quien actualmente la esta ocupando
  SELECT INTO r paterno||' '||materno||' '||nombre AS nombre_completo,
                fecha_inicio, fecha_fin
    FROM hospedajes
    LEFT JOIN huespedes USING (idhuesped)
   WHERE idhabitacion = p_idhabitacion AND 
         (p_fecha BETWEEN fecha_inicio AND fecha_fin) AND 
         estatus <= 1;
  IF FOUND THEN
    regreso := format(E'Titular: %s \nInicio: %s Final: %s',r.nombre_completo,r.fecha_inicio, r.fecha_fin);
  END IF;
  --JCGE: Significa que no encontro ninguna reservacion
  RETURN regreso;
END;$$
LANGUAGE plpgsql;


--------------------------
-- JCGE: Funcion para saber si esta disponible un rango de fechas y en cierta habitacion
--------------------------
CREATE OR REPLACE FUNCTION hotel_habitacion_estatus_rango (p_idhabitacion INTEGER, p_fecha_ini DATE, p_fecha_fin DATE)
  RETURNS TEXT AS $$
DECLARE
  --Variables
  arr     TEXT[] := ARRAY['Libre', 'Ocupada: ', 'Error: '];
  r       RECORD;
  dias    INTEGER := (p_fecha_fin - p_fecha_ini);
  p_fecha DATE;
BEGIN
  --JCGE: De jodido que exista la habitacion
  PERFORM * FROM habitaciones WHERE idhabitacion = p_idhabitacion;
  IF (NOT FOUND) THEN
    RETURN arr[3]::TEXT |+ 'No existe la habitación.'::TEXT;
  END IF;
  --JCGE: Buscamos en cada fecha para saber si esta siendo ocupada para la misma habitacion
  p_fecha := p_fecha_ini;
  FOR i IN 0..COALESCE(dias,0) LOOP
      --JCGE: Si es un dia que ya paso
    IF (p_fecha < now()::DATE) THEN
      RETURN arr[3]::TEXT |+ 'Día transcurrido.'::TEXT;
    END IF;
    FOR r IN SELECT *
               FROM hospedajes
              WHERE idhabitacion = p_idhabitacion AND 
                    (p_fecha BETWEEN fecha_inicio AND fecha_fin) AND
         estatus <= 1 LOOP
      arr[2] := arr[2]::TEXT || ': '::TEXT || (p_fecha)::TEXT;
    END LOOP;
    p_fecha := p_fecha_ini + i;
  END LOOP;
  IF (arr[2] <> 'Ocupada: ') THEN
    RETURN arr[2];
  END IF;
  --JCGE: Significa que no encontro ninguna reservacion
  RETURN arr[1];
END;$$
LANGUAGE plpgsql;
