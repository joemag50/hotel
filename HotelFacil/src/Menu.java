import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.UtilDateModel;

public class Menu extends MainWindow implements ActionListener, ListSelectionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6700237452042490767L;
	/*
	 * JCGE: Menu es la clase que soporta todos los modulos
	 * */
	public JToolBar toolBar;
	public ArrayList<JButton> botonera;
	public static JDatePanelImpl datePanel;
	//JCGE: Constructor del menu
	Menu()
	{
		//JCGE: Propiedades Generales
		this.setTitle("Principal");                                   //Nombre de la ventana
		checkPermisos();                                              //Revisamos los permisos del usuario
		panelCentro.setLayout(null);                                  //Sin layout para poner los widgets
		botonera = new ArrayList<JButton>();                          //Iniciamos los botones de los modulos
		toolBar = new JToolBar(null, JToolBar.HORIZONTAL);            //Iniciamos el toolbar
		toolBar.setVisible(true);
		toolBar.setBounds(0,0,MainWindow.WIDTH.intValue(),40);
		panelCentro.add(toolBar);
		l_titulo.setText("Usuario: " + baseDatos.user_actual);    //Le cambiamos el usuario que esta usando el sistema
		
		//JCGE: Esto es parte de los campos que tienen fecha
		//Lo ponemos disponible para la clase de HFDateField
		//para que cuando creamos un nuevo campo de fecha, sea mas facil y sin tanto rollo
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		//JCGE: datePanel es la ventana que muestra las fechas
		datePanel = new JDatePanelImpl(model, p); 
	}
	//JCGE: Tenemos una botonera para todos los modulos
	//Los botones dependen del modulo, entonces pueden ser N botones
	public void rellenaToolBar(String[] botones, ActionListener l)
	{
		//JCGE: Dependiendo el tamaño de botones los ponemos
		//De paso les ponemos el escuchador de acciones
		//para detectar los "Clicks"
		for (int i = 0;i <= botones.length-1; i++)
		{
			botonera.add(new JButton(botones[i]));
			botonera.get(i).setEnabled(true);
			botonera.get(i).addActionListener(l);
			//JCGE: El toolbar no deberia de estar en varias locaciones
			//Entonces lo ponemos estatico y facilmente lo agregamos desde aqui
			toolBar.add(botonera.get(i));
		}
	}
	@Override
	public void valueChanged(ListSelectionEvent arg0){/*JCGE: Sin acciones*/}
}
