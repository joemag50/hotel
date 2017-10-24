import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Array;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.MenuListener;

public class Usuarios extends Menu implements ActionListener
{
	//JCGE: Modulo de usuarios
	public ArrayList<JLabel>  etiquetas;
	static ArrayList<JTextField> textos;
	public ArrayList<ResultSet> r;
	public JButton buscar;
	public JLabel usuarios;
	public JList lista;
	Usuarios ()
	{
		//JCGE: Propiedades Generales
		this.setTitle("Usuarios");
		checkPermisos();
		panelCentro.setLayout(null);
		
		//JCGE: Propiedades Particulares
		String[] tagz = {"Usuario: ","Tipo Usuario: ",
						"Contraseña: ","Nombre: ",
						"Paterno: ","Materno: ",
						"F. Nacimiento: ","Télefono: ",
						"CURP: ","RFC: ","NSS: "};
		String[] nombres = {"Nuevo","Guardar","Cancelar","Menu"};
		rellenaToolBar(nombres, actionLins);
		etiquetas = new ArrayList<JLabel>();
		textos = new ArrayList<JTextField>();
		for (int i = 0; i <= tagz.length-1; i++)
		{
			etiquetas.add(new JLabel(tagz[i]));
		}
		int x = 10, y = 70, b = 120, h = 20;
		for(JLabel tag: etiquetas)
		{
			tag.setBounds(x,y,b,h);
			panelCentro.add(tag);
			y = y + 25;
			textos.add(new JTextField(200));
		}
		x = b; y = 70; b = 200; h = 20;
		for (JTextField tex: textos)
		{
			tex.setBounds(x,y,b,h);
			tex.addActionListener(actionLins);
			tex.setEnabled(false);
			panelCentro.add(tex);
			y = y + 25;
		}
		buscar = new JButton("Buscar");
		x += 210; buscar.setBounds(x,70,100,h);
		buscar.setVisible(true);
		buscar.setEnabled(true);
		buscar.addActionListener(actionLins);
		panelCentro.add(buscar);
		
		//JCGE: Aqui vamos a mostrar una pequeña lista de usuarios
		usuarios = new JLabel("Usuarios actuales: ");
		x += 120; usuarios.setBounds(x,70,150,h);
		panelCentro.add(usuarios);
		listaUsuarios(lista, panelCentro, x, 90, 200, y-90);
		
		//JCGE: Dejamos esto activado
		textos.get(0).addFocusListener(fe);
		textos.get(0).setEnabled(true);
		textos.get(0).requestFocus();
	}
	public void listaUsuarios(JList list1, JPanel pane, int x, int y, int b, int h)
	{
		try {
			ResultSet res = baseDatos.db.newQuery("SELECT array_to_string(array_agg(idusuario),',') AS usuarios FROM usuarios");
			res.next();
			String[] usuarios = res.getString("usuarios").split(",");
			lista(usuarios,list1,pane,x,y,b,h);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		return;
	}
	public void lista(String[] cosas, JList list1, JPanel pane, int x, int y, int b, int h)
	{
		list1 = new JList(cosas);
		list1.setVisibleRowCount(5);
		JScrollPane scroll = new JScrollPane(list1);
		scroll.setBounds(x,y,b,h);
		pane.add(scroll);
	}
	//JCGE: Este es el metodo que se encarga de tomar las acciones en los botones
	private ActionListener actionLins = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			String boton = e.getActionCommand();
			System.out.println(boton);
			if (boton == "Buscar")
			{
				//JCGE: Es la señal que dice que quiere ver a un usuario existente
				try
				{
					r = new ArrayList<ResultSet>();
					r.add(baseDatos.db.newQuery("SELECT * "
												+" FROM usuarios "
												+"WHERE idusuario = trim('"+textos.get(0).getText()+"')"));
					r.get(0).next();
					
					textos.get(3).setText(r.get(0).getString("nombre"));
					textos.get(4).setText(r.get(0).getString("paterno"));
					textos.get(5).setText(r.get(0).getString("materno"));
					textos.get(1).setText(r.get(0).getString("tipo_usuario"));
					textos.get(6).setText(r.get(0).getString("fechanacimiento"));
					textos.get(7).setText(r.get(0).getString("telefono"));
					textos.get(8).setText(r.get(0).getString("curp"));
					textos.get(9).setText(r.get(0).getString("rfc"));
					textos.get(10).setText(r.get(0).getString("nss"));
					for (JTextField tex: textos)
					{
						tex.setEnabled(true);
					}
				}
				catch (SQLException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null,"El usuario "+textos.get(0).getText()+" no existe");
					for (JTextField tex: textos)
					{
						tex.setText("");
						tex.setEnabled(false);
					}
					textos.get(0).setEnabled(true);
					textos.get(0).requestFocus();
					
				}
			}
			if (boton == "Guardar")
			{
				/*
				baseDatos.db.newInsert("INSERT INTO usuarios (idusuario,pass,kusuario,paterno,materno,nombre,fechanacimiento,telefono" +
															  "curp,rfc,nss,activo,tipo_usuario) "+
										"VALUES ('"+textos.get(0).getText()+"'," +
												"'"+textos.get(1).getText()+"',"+
												"DEFAULT,"+
												"'"+textos.get(2).getText()+"',"+
												"'"+textos.get(3).getText()+"',"+
												"'"+textos.get(4).getText()+"',"+
												"'"+textos.get(5).getText()+"',"+
												"'"+textos.get(6).getText()+"',"+
										")");
				System.out.println("hi");
				*/
			}
			if (boton == "Cancelar")
			{
				//
				for (JTextField tex: textos)
				{
					tex.setText("");
					tex.setEnabled(false);
				}
				textos.get(0).setEnabled(true);
				textos.get(0).requestFocus();
				buscar.setEnabled(true);
			}
			if (boton == "Menu")
			{
				//JOptionPane.showMessageDialog(null,"Warning: Los cambios no confirmados... no serán guardados.");
				Principal.ventana.setVisible(false);
				Principal.ventana.dispose();
				Principal.ventana = new Menu();
				Principal.ventana.finGUI();
			}
			if (boton == "Nuevo")
			{
				for (JTextField tex: textos)
				{
					tex.setEnabled(true);
					tex.setText("");
				}
				textos.get(0).requestFocus();
				buscar.setEnabled(false);
			}
		}
	};
	
	private FocusListener fe = new FocusListener ()
	{

		@Override
		public void focusGained(FocusEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void focusLost(FocusEvent arg0)
		{
			//
			String usuario = textos.get(0).getText();
			if (usuario.length() == 0)
			{
				return;
			}
		}
	};
}
