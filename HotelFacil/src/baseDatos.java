import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import javax.swing.JOptionPane;

public class baseDatos
{
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String db_url = "jdbc:postgresql:hotel";
	
	//JCGE: Estas variables son para la conexion y para los querys
	static Connection conn = null;
	static Statement stmt = null;
	static baseDatos db;
	
	public Boolean conectado = false;
	public String mensaje = "";
	
	//JCGE: Hay que encriptar esto
	String usuario = "admhotel";
	String pass = "5e77db835e922beb1920d92174b00b0c"; //MuseMuse50
	
	//JCGE: Aqui guardamos el usuario que esta actualmente en la ventana
	public static String user_actual;
	//JCGE 30/09/2017: Constructor aca mamalon
	baseDatos()
	{
		//JCGE 30/09/2017: Con esto nos aseguramos de que tiene conexion al instanciar
		try
		{
			try
			{
				//JCGE: Buscamos la clase necesaria para conectarnos a la base de Datos
				Class.forName("java.sql.Driver");
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			System.out.println("Connecting to database...");
			//JCGE: Nos vamos a conectar con un usuario limitado
			conn = DriverManager.getConnection(db_url, usuario, pass);
			System.out.println("Connected");
			conectado = true;
		}
		catch (SQLException e)
		{
			conectado = false;
			mensaje = e.getMessage();
			//JCGE: Esto es para encontrar mas facil el error
			String sqlState = e.getSQLState();
			JOptionPane.showMessageDialog(null,"Error " + sqlState + ": " + mensaje);
			e.printStackTrace();
		}
	}
	//JCGE: A continuacion funciones para buscar en la base de datos con consultas comunes
	public static String masInfoHabitacion(int p_idhabitacion, String p_fecha)
	{
		String info = "";
		try
		{
			ResultSet res = baseDatos.db.newQuery(String.format("SELECT * FROM hotel_hospedaje_info(%s, %s::DATE)", p_idhabitacion, p_fecha));
			if (res.next())
			{
				if (res.getString(1) != null)
				{
					info = res.getString(1);
				}
			}
		}
		catch (SQLException e)
		{
			String mensaje = e.getMessage();
			//JCGE: Esto es para encontrar mas facil el error
			String sqlState = e.getSQLState();
			JOptionPane.showMessageDialog(null,"Error " + sqlState + ": " + mensaje);
			e.printStackTrace();
		}
		return info;
	}
	//JCGE: Funcion para insertar en el log comportamientos sensibles
	public static int logInsert(String usu, String desc, String mod)
	{
		/*
		 * JCGE: Si se portan mal o vemos algo sospechoso lo ponemos en el log
				  idlog       SERIAL PRIMARY KEY,
                  descripcion TEXT,
                  modulo      TEXT,
                  idusuario   TEXT REFERENCES usuarios (idusuario),
                  fecha       TEXT,
                  hora        TIME*/
		int res = baseDatos.db.newInsert(String.format(" INSERT INTO log VALUES (DEFAULT, '%s', '%s', '%s', now()::DATE, now()::TIME) ", desc, mod, usu));
		return res;
	}
	//JCGE: Estatus de la habitacion a la fecha de consulta
	public static String estatusHabitacion(int p_idhabitacion, String p_fecha)
	{
		String estatus = "";
		try
		{
			ResultSet res = baseDatos.db.newQuery(String.format("SELECT * FROM hotel_habitacion_estatus(%s, %s::DATE)", p_idhabitacion, p_fecha));
			if (res.next())
			{
				if (res.getString(1) != null)
				{
					estatus = res.getString(1);
				}
			}
		}
		catch (SQLException e)
		{
			String mensaje = e.getMessage();
			//JCGE: Esto es para encontrar mas facil el error
			String sqlState = e.getSQLState();
			JOptionPane.showMessageDialog(null,"Error " + sqlState + ": " + mensaje);
			e.printStackTrace();
		}
		return estatus;
	}
	//JCGE: Buscamos el nivel de permiso que tenga el usuario y lo mantenemos en el aire
	public static String nivelUsuario()
	{
		String nivel = "";
		try
		{
			ResultSet res = baseDatos.db.newQuery(String.format("SELECT tipo_usuario FROM usuarios WHERE idusuario = '%s' ", user_actual));
			if (res.next())
			{
				//System.out.println(res.getString(1));
				if (res.getString(1) != null)
				{
					nivel = res.getString(1);
				}
			}
		}
		catch (SQLException e)
		{
			String mensaje = e.getMessage();
			//JCGE: Esto es para encontrar mas facil el error
			String sqlState = e.getSQLState();
			JOptionPane.showMessageDialog(null,"Error " + sqlState + ": " + mensaje);
		}
		return nivel;
	}
	//JCGE 30/09/2017: Inicio de query
	public void preQuery()
	{
		try
		{
			if (conn != null)
			{
				stmt = conn.createStatement();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	//JCGE 30/09/2017: Realizacion de query
	public ResultSet newQuery(String query)
	{
		try
		{
			return stmt.executeQuery(query);
		}
		catch (SQLException e)
		{
			mensaje = e.getMessage();
			e.printStackTrace();
		}
		return null;
	}
	//JCGE: Nuevo Insert
	public int newInsert(String query)
	{
		try
		{
			return stmt.executeUpdate(query);
		}
		catch (SQLException e)
		{
			mensaje = e.getMessage();
			e.printStackTrace();
		}
		return 0;
	}
	//JCGE 30/09/2017: Cerrar la conexion en general
	public void endQuery()
	{
		//finally block used to close resources
		try
		{
			if(stmt!=null)
				stmt.close();
		}
		catch(SQLException se2)
		{
			mensaje = se2.getMessage();
			se2.printStackTrace();
		}
		try
		{
			if(conn!=null)
				conn.close();
		}
		catch(SQLException se)
		{
			mensaje = se.getMessage();
			se.printStackTrace();
		}
	}
	//JCGE: Con esta funcion convertimos un texto a md5 por seguridad
	//JCGE: Esta funcion ya no la vamos a usar
	public String utilMd5 (String unTexto)
	{
        MessageDigest m = null;
		try
		{
			m = MessageDigest.getInstance("MD5");
		}
		catch (NoSuchAlgorithmException e)
		{
			//JCGE: No existe el Algoritmo
			e.printStackTrace();
		}
        m.update(unTexto.getBytes(), 0, unTexto.length());
        //System.out.println("MD5: " + new BigInteger(1,m.digest()).toString(16));
		return new BigInteger(1,m.digest()).toString(16);
	}
}
