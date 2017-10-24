import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

import javax.swing.JOptionPane;

public class baseDatos
{
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String db_url = "jdbc:postgresql:hotel";
	
	static Connection conn = null;
	static Statement stmt = null;
	static baseDatos db;
	
	public Boolean conectado = false;
	public String mensaje = "";
	
	//JCGE: Hay que encriptar esto
	String usuario = "admhotel";
	String pass = "5e77db835e922beb1920d92174b00b0c"; //MuseMuse50

	//JCGE 30/09/2017: Constructor aca mamalon
	baseDatos()
	{
		//JCGE 30/09/2017: Con esto nos aseguramos de que tiene conexion al instanciar
		try
		{
			//usuario = p_usuario;
			//String n_pass = utilMd5(pass);
			//System.out.println(n_pass);
			try
			{
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
	public void setUsuario (String p_usuario)
	{
		//JCGE: Vamos a asignar el usuario para la base de datos
		baseDatos.db.usuario = p_usuario;
		return;
	}
	public String getUsuario()
	{
		return usuario;
	}
	//JCGE 30/09/2017: Inicio de query
	public void preQuery()
	{
		try
		{
			stmt = conn.createStatement();
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
			// nothing we can do
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
