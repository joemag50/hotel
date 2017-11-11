
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

