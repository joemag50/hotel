import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JToolBar;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class Menu extends MainWindow implements ActionListener
{
	public JToolBar toolBar;
	public ArrayList<JButton> botonera;
	//JCGE: Aca lo iniciamos mamalon
	Menu()
	{
		//JCGE: Propiedades Generales
		this.setTitle("Principal");
		checkPermisos();
		toolBar = new JToolBar(null, JToolBar.VERTICAL);
		toolBar.setVisible(true);
		
		//JCGE: Propiedades Especificas
		botonera = new ArrayList<JButton>();
		frame.add(toolBar, BorderLayout.WEST);
		//JCGE: Propiedades Particulares
		l_titulo.setText("Usuario: " + baseDatos.db.getUsuario());
	}
	public void rellenaToolBar(String[] botones, ActionListener l)
	{
		for (int i = 0;i <= botones.length-1; i++)
		{
			botonera.add(new JButton(botones[i]));
			botonera.get(i).setEnabled(true);
			botonera.get(i).addActionListener(l);
			toolBar.add(botonera.get(i));
		}
	}
	//JCGE: Este es el metodo que se encarga de tomar las acciones en los botones dentro de la clase
	private ActionListener actlin = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String boton = e.getActionCommand();
			System.out.println(boton);
			if (boton == "kk")
			{
				//
				System.out.println("hi");
			}
		}
		
	};
}
