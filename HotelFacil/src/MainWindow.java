import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

//Heredamos de JFrame para sacar de la libreria swing
public class MainWindow extends JFrame implements ActionListener, MenuListener
{
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static final Double WIDTH = new Double(screenSize.getWidth());
	static final Double HEIGHT = new Double(screenSize.getHeight());
	
	public Container frame;
	public JPanel panelNorte, panelEste, panelOeste, panelSur, panelCentro;
	public JLabel l_titulo;
	public JLabel elTiempo;
	public JMenuBar menuBar;
	
	public ArrayList<JMenu> menuz;
	public ArrayList<JMenuItem> menuItems;
	public String[] nombres_menu = {"Administración","Operación","Mi Cuenta"};
	//JCGE: Constructor aca mamalon
	MainWindow ()
	{
		//JCGE: Preparamos el marco principal
		frame = getContentPane();
		frame.setLayout(new BorderLayout());
		//JCGE: Preparamos la ventana
		this.setTitle("Main Window");
		this.setSize(this.WIDTH.intValue()-200, this.HEIGHT.intValue()-200);
		//this.setSize(800, 599);
		this.setResizable(false);
		//this.setExtendedState(MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//JCGE: Preparamos las areas de trabajo de las ventanas
		panelSur   = new JPanel();
		panelCentro = new JPanel();
		
		//JCGE: Basicamente esto es para identificar las cajas...
		//posiblemente el sistema en general lo ponemos amarillo claro
		//para que no dañe la vista
		panelCentro.setBackground(Color.getHSBColor(210, 72, 100));
		
		//JCGE: Estas instancias las usamos para ver la fecha
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
		Calendar currentCalendar = Calendar.getInstance();
		Date currentTime = currentCalendar.getTime();
		
		//JCGE: Paneles de la parte inferior de la pantalla
		JPanel statusPanel = new JPanel();
		JPanel statusRightPanel = new JPanel();
		
		//JCGE: Esto es la barra del menu
		menuBar = new JMenuBar();
		menuBar.setVisible(true);
		
		menuz = new ArrayList<JMenu>();
		
		for (int i = 0;i <= nombres_menu.length-1; i++)
		{
			menuz.add(new JMenu(nombres_menu[i]));
			menuz.get(i).setEnabled(false);
			menuBar.add(menuz.get(i));
		}
		
		menuItems = new ArrayList<JMenuItem>();
		menuItems.add(new JMenuItem("Usuarios"));
		menuItems.add(new JMenuItem("Reportes del Gerente"));
		menuItems.get(0).addActionListener(this);
		menuItems.get(1).addActionListener(this);
		menuz.get(0).add(menuItems.get(0));
		menuz.get(0).add(menuItems.get(1));
		
		menuItems.add(new JMenuItem("Nuevo Cuarto"));
		menuItems.add(new JMenuItem("CheckOut"));
		menuItems.get(2).addActionListener(this);
		menuItems.get(3).addActionListener(this);
		menuz.get(1).add(menuItems.get(2));
		menuz.get(1).add(menuItems.get(3));
		
		menuItems.add(new JMenuItem("Mi Cuenta"));
		menuItems.get(4).addActionListener(this);
		menuz.get(2).add(menuItems.get(4));
		menuItems.add(new JMenuItem("Salir"));
		menuItems.get(5).addActionListener(this);
		menuz.get(2).add(menuItems.get(5));
		
		//Les asignamos un borde para identificarlo
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusRightPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		
		//Agregamos los paneles nuevos al panel fijo del sur
		panelSur.add(statusPanel, BorderLayout.SOUTH);
		panelSur.add(statusRightPanel, BorderLayout.SOUTH);
		
		//Les asignamos un tamaño
		statusPanel.setPreferredSize(new Dimension(500, 20));
		statusRightPanel.setPreferredSize(new Dimension(500, 20));
		
		//Les asignamos una plantilla de acomodo
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusRightPanel.setLayout(new BoxLayout(statusRightPanel, BoxLayout.X_AXIS));
		
		//Etiquetas con la informacion
		elTiempo = new JLabel("Fecha: " + dateFormat.format(currentTime));
		l_titulo = new JLabel("Bienvenido HOTEL - FACIL");
		
		//Agregamos las etiquetas a los paneles con borde
		statusRightPanel.add(l_titulo);
		statusPanel.add(elTiempo);
		
		//Los paneles con las etiquetas van a dentro del panel sur
		panelSur.add(statusRightPanel);
		panelSur.add(statusPanel);
	}
	public void finGUI()
	{
		//JCGE
		frame.add(menuBar, BorderLayout.NORTH);
		frame.add(panelSur,   BorderLayout.SOUTH);
		frame.add(panelCentro, BorderLayout.CENTER);
		this.setSize(this.WIDTH.intValue()-200, this.HEIGHT.intValue()-201);
		this.setLocationRelativeTo(null);
	}
	public void checkPermisos()
	{
		for (int i = 0;i <= nombres_menu.length-1; i++)
		{
			if (true)
			{
				menuz.get(i).setEnabled(true);	
			}
		}
	}
	//Este es el metodo que se encarga de tomar las acciones en los botones
	public void actionPerformed(ActionEvent arg0)
	{
		String boton = arg0.getActionCommand();
		System.out.println(boton);
		if (boton == "Salir")
		{
			//JCGE:
			baseDatos.db.endQuery();
			Principal.ventana.setVisible(false);
			Principal.ventana.dispose();
			Principal.ventana = new Login();
			Principal.ventana.finGUI();
			//System.out.println("Este men se quiere salir");
		}
		if (boton == "Usuarios")
		{
			//JCGE: 
			Principal.ventana.setVisible(false);
			Principal.ventana.dispose();
			Principal.ventana = new Usuarios();
			Principal.ventana.finGUI();
			Usuarios.textos.get(0).requestFocus();
		}
		if (boton == "Nuevo Cuarto")
		{
			//JCGE: 
			Principal.ventana.setVisible(false);
			Principal.ventana.dispose();
			Principal.ventana = new NuevoCuarto();
			Principal.ventana.finGUI();
		}
		if (boton == "CheckOut")
		{
			//JCGE: 
			Principal.ventana.setVisible(false);
			Principal.ventana.dispose();
			Principal.ventana = new Checkout();
			Principal.ventana.finGUI();
		}
		if (boton == "Reportes del Gerente")
		{
			//JCGE: 
			Principal.ventana.setVisible(false);
			Principal.ventana.dispose();
			Principal.ventana = new reportesGerente();
			Principal.ventana.finGUI();
		}
		if (boton == "Mi Cuenta")
		{
			//JCGE: 
			Principal.ventana.setVisible(false);
			Principal.ventana.dispose();
			Principal.ventana = new miCuenta();
			Principal.ventana.finGUI();
		}
	}
	@Override
	public void menuSelected(MenuEvent e)
	{
		System.out.println("menuSelected");
	}
	@Override
	public void menuCanceled(MenuEvent arg0)
	{
		// TODO Auto-generated method stub
		System.out.println("menuCanceled");
	}
	@Override
	public void menuDeselected(MenuEvent arg0)
	{
		// TODO Auto-generated method stub
		System.out.println("menuDeselected");
	}
}
