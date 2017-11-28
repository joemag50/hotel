import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Login extends MainWindow
{
	/**
	 * JCGE: Modulo Login
	 * Modulo para darle acceso a nuestro usuario y pueda acceder a sus funcionalidades
	 */
	private static final long serialVersionUID = -1183981314966144116L;
	static JTextField user;
	JPasswordField passt;
	Integer intentos = 0;
	BufferedImage img = null;
	//JCGE: Construimos desde el MainWindow
	Login()
	{
		//JCGE: Propiedades Generales
		menuBar.setVisible(false);
		baseDatos.user_actual = null; //JCGE: Cuando instanciamos el Login aun no sabemos el user
		this.setExtendedState(MAXIMIZED_BOTH);
		
		//JCGE: Propiedades Particulares
		user = new JTextField(50);
		user.requestFocusInWindow();
		passt = new JPasswordField(50);
		HFLabel l_user = new HFLabel("Usuario: ");
		HFLabel l_passt = new HFLabel("Contraseña: ");
		JPanel loginBox = new JPanel();
		JButton boton = new JButton("Entrar");
		boton.addActionListener(this);
		loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
		loginBox.add(l_user);
		loginBox.add(user);
		loginBox.add(l_passt);
		loginBox.add(passt);
		loginBox.add(boton);
		//JCGE: Vamos a prepararnos para poner una imagen aca loca
		int x = 320,y = 450,b = 500,h = 100;
		try
		{
		    img = ImageIO.read(new File("media/logo_hf.png"));
			JLabel picLabel = new JLabel(new ImageIcon(img));
			picLabel.setBounds((WIDTH.intValue()/2)-(b), y - 300, b * 2, h * 2);
			panelCentro.add(picLabel);
		}
		catch (IOException e)
		{
			//JCGE
			System.out.println(e.getMessage());
		}
		loginBox.setBounds((WIDTH.intValue()/2)-260,y,b,h+20);
		loginBox.setBackground(colores.get(4));
		panelCentro.add(loginBox);
	}
	//JCGE: Este es el metodo que se encarga de tomar las acciones en los botones
	public void actionPerformed(ActionEvent arg0)
	{
		String boton = arg0.getActionCommand();
		System.out.println(boton);
		if (boton == "Entrar")
		{
			if (user.getText().length() == 0)
			{
				//JCGE: le decimos que no manche, que ponga un usuario
				JOptionPane.showMessageDialog(null,"Error: Favor de capturar un nombre de usuario válido.");
				return;
			}
			if (passt.getPassword().toString().length() == 0)
			{
				//JCGE: le decimos que ya ni la muela, que ponga la contraseña
				JOptionPane.showMessageDialog(null,"Error: Favor de capturar una contraseña válida");
				return;
			}
			//JCGE: La pregunta buenera... puede o no entrar?
			if (validaLogin())
			{
				baseDatos.user_actual = user.getText();
				Principal.ventana.setVisible(false);
				Principal.ventana.dispose();
				Principal.ventana = new Menu();
				Principal.ventana.finGUI();
				//Pequeño hack para que actualice la pantalla
				Principal.ventana.setWindowSize(Principal.ventana, 40, 40);
			}
		}
	}
	public Boolean validaLogin()
	{
		//JCGE: Intentamos la conexion
		baseDatos.db = new baseDatos();
		//JCGE: Si no esta conectado le mandamos el error y tronamos todo
		if (!baseDatos.db.conectado)
		{
			//JOptionPane.showMessageDialog(null,"Error: "+baseDatos.db.mensaje);
			return false;
		}
		//JCGE: No existe el usuario... lo tronamos
		if (!existeUsuario())
		{
			JOptionPane.showMessageDialog(null,"El usuario que usted ingresó no existe o está suspendido.\nContacte a su administrador");
			return false;
		}
		//JCGE: Lo tronamos si tiene mas de tres intentos
		if (intentos >= 3)
		{
			baseDatos.logInsert(user.getText(), "Contraseña Incorrecta", "Login");
			JOptionPane.showMessageDialog(null,"Su usuario fue suspendido por pasar 3 intentos\nContacte a su administrador.");
			return false;
		}
		baseDatos.db.preQuery();
		ResultSet prueba = baseDatos.db.newQuery("SELECT * FROM hotel_valida_pass ('"+user.getText()+"','" + new String(passt.getPassword())+"', "+intentos+")");
		try {
			prueba.next();
			Boolean resul = prueba.getBoolean(1);
			if (!resul)
			{
				intentos++;
				JOptionPane.showMessageDialog(null,"Contraseña incorrecta\nIntentos: "+intentos+" \n"+prueba.getString(2));
			}
			return resul;
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
		//Principal.db.endQuery();
		return false;
	}
	//JCGE: Esto es para revisar si existe el usuario
	public Boolean existeUsuario()
	{
		baseDatos.db.preQuery();
		ResultSet prueba = baseDatos.db.newQuery("SELECT * FROM hotel_valida_usuario ('"+user.getText()+"')");
		try
		{
			prueba.next();
			Boolean resul = prueba.getBoolean(1);
			return resul;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
