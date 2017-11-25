import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

//Heredamos de JFrame para sacar de la libreria swing
public class MainWindow extends JFrame implements ActionListener, MenuListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1171527061718987226L;
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	static final Double WIDTH = new Double(screenSize.getWidth());
	static final Double HEIGHT = new Double(screenSize.getHeight());
	public static int x_int = 50;
	public static int y_int = 50;
	public Container frame;
	public JPanel panelNorte, panelEste, panelOeste, panelSur;
	public static JDesktopPane panelCentro;
	public HFLabel l_titulo, elTiempo;
	public JMenuBar menuBar;
	
	public ArrayList<JMenu> menuz;
	public ArrayList<JMenuItem> menuItems;
	//public String[] nombres_menu = {"Administración","Operación","Mi Cuenta"};
	public String[] nombres_menu = {"Administración","Mi Cuenta"};
	//JCGE: Constructor aca mamalon
	MainWindow ()
	{
		//JCGE: Preparamos el marco principal
		frame = getContentPane();
		frame.setLayout(new BorderLayout());
		//JCGE: Preparamos la ventana
		this.setTitle("Main Window");
		//this.setSize(MainWindow.WIDTH.intValue(), MainWindow.HEIGHT.intValue());
		//this.setSize(800, 599);
		this.setExtendedState(NORMAL);
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//JCGE: Preparamos las areas de trabajo de las ventanas
		panelSur   = new JPanel();
		panelCentro = new JDesktopPane();
		panelCentro.setLayout(null);
		//JCGE: Basicamente esto es para identificar las cajas...
		//posiblemente el sistema en general lo ponemos amarillo claro
		//para que no dañe la vista
		//panelCentro.setBackground(Color.getHSBColor(210, 72, 100));
		panelCentro.setBackground(Color.LIGHT_GRAY);
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
		menuItems.get(menuItems.size()-1).addActionListener(this);
		menuz.get(0).add(menuItems.get(menuItems.size()-1));
		
		menuItems.add(new JMenuItem("Salir"));
		menuItems.get(menuItems.size()-1).addActionListener(this);
		menuz.get(1).add(menuItems.get(menuItems.size()-1));
		
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
		elTiempo = new HFLabel("Fecha: " + dateFormat.format(currentTime));
		l_titulo = new HFLabel("Bienvenido HOTEL - FACIL");
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
		this.setLocationRelativeTo(null);
		pack();
	}
	public static void newMenuInterno(MenuInterno e)
	{
		panelCentro.add(e);
		e.setLocation(x_int, y_int);
		if (x_int > 200)
		{
			x_int=50; y_int=50;
		}
		x_int+=30; y_int+=30;
		e.moveToFront();
		try {
			e.setSelected(true);
		} catch (PropertyVetoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	public void setWindowSize(MainWindow e, int x, int y)
	{
		e.setSize(MainWindow.WIDTH.intValue()-x, MainWindow.HEIGHT.intValue()-y);
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
	protected KeyAdapter solonum = new KeyAdapter()
	{
		public void keyTyped(KeyEvent e)
		{
			char c = e.getKeyChar();
			if (!((c >= '0') && (c <= '9') || (c == '.') || (c == KeyEvent.VK_TAB) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) ))        
			{
				JOptionPane.showMessageDialog(null, "Favor de ingresar solo números en este campo.");
				e.consume();
			}
		}
	};
    protected Boolean isInteger(String s)
    {
        try
        {
        	Integer.parseInt(s);
        }
        catch(NumberFormatException e)
        {
        	return false;
        }
        catch(NullPointerException e)
        {
        	return false;
        }
        return true;  //Regresamos verdadero en caso de que sea numero realmente
    }
    //Esta funcion es para ver si es double o no
    protected Boolean isDouble(String s)
    {
        try
        {
        	Double.parseDouble(s);
        }
        catch(NumberFormatException e)
        {
        	return false;
        }
        catch(NullPointerException e)
        {
        	return false;
        }
        return true;  //Regresamos verdadero en caso de que sea numero realmente
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
			Principal.ventana.setWindowSize(Principal.ventana, 40, 40);
			//System.out.println("Este men se quiere salir");
		}
		if (boton == "Usuarios")
		{
			//JCGE: 
			Principal.ventana.setVisible(false);
			Principal.ventana.dispose();
			Principal.ventana = new Usuarios();
			Principal.ventana.finGUI();
			Principal.ventana.setWindowSize(Principal.ventana, 100, 100);
			Usuarios.textos.get(0).requestFocus();
		}
	}
	@Override
	public void menuSelected(MenuEvent e) {/*JCGE: Aun sin nada*/}
	@Override
	public void menuCanceled(MenuEvent arg0) {/*JCGE: Aun sin nada*/}
	@Override
	public void menuDeselected(MenuEvent arg0) {/*JCGE: Aun sin nada*/}
}
