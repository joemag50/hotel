import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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
	public habitacionesGrid hg;
	//JCGE: Constructor del menu
	Menu()
	{
		//JCGE: Propiedades Generales
		panelCentro.setLayout(null);
		this.setExtendedState(NORMAL);
		this.setTitle("Principal");                                   //Nombre de la ventana
		checkPermisos();                                              //Revisamos los permisos del usuario
		panelCentro.setLayout(null);                                  //Sin layout para poner los widgets
		botonera = new ArrayList<JButton>();                          //Iniciamos los botones de los modulos
		toolBar = new JToolBar(null, JToolBar.HORIZONTAL);            //Iniciamos el toolbar
		toolBar.setVisible(false);
		toolBar.setBounds(0,0,MainWindow.WIDTH.intValue(),40);
		panelCentro.add(toolBar);
		l_titulo.setText("Usuario: " + baseDatos.user_actual);    //Le cambiamos el usuario que esta usando el sistema
		hg = new habitacionesGrid();
		newMenuInterno(hg);
		hg.setMaximizable(false);
		try {
			hg.setMaximum(true);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			hg.setSelected(true);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
