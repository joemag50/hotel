import java.sql.*;

public class Principal
{
	//JCGE: La instancia de la ventana principal
	static MainWindow ventana;
	
	public static void main(String[] args) throws SQLException
	{
		//JCGE: Instanciamos la ventana de login
		ventana = new Login();
		ventana.finGUI();
		Login.user.requestFocus();
	}
}
