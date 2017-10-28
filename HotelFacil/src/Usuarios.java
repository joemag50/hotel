import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

public class Usuarios extends Menu implements ActionListener
{
	//JCGE: Modulo de usuarios
	public ArrayList<JLabel>  etiquetas;
	static ArrayList<JTextField> textos;
	public ArrayList<ResultSet> r;
	public JButton buscar;
	public JLabel usuarios;
	static JList lista;
	public JFormattedTextField txtDate;
	public ArrayList<JPasswordField> passwds;
	public JPanel panelsito;
	
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
		textos    = new ArrayList<JTextField>();
		passwds   = new ArrayList<JPasswordField>();
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
		int i = 0;
		for (JTextField tex: textos)
		{
			if (i == 6)
			{
				//JCGE: Vamos a poner un campo especial de fecha
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				txtDate = new JFormattedTextField(df);
				txtDate.setBounds(x,y,b,h);
				txtDate.setVisible(true);
				txtDate.setEnabled(false);
				txtDate.setValue(new java.util.Date());
				panelCentro.add(txtDate);
			}
			else
			{
				if (i == 2)
				{
					//JCGE: Esta construyendo la contraseña, vamos a poner dos campos para la confirmacion
					passwds.add(new JPasswordField());
					passwds.get(0).setBounds(x,y,b,h);
					passwds.get(0).addActionListener(actionLins);
					passwds.get(0).setEnabled(false);
					panelCentro.add(passwds.get(0));
					
					passwds.add(new JPasswordField());
					passwds.get(1).setBounds(x+b+10,y,b,h);
					passwds.get(1).addActionListener(actionLins);
					passwds.get(1).setEnabled(false);
					panelCentro.add(passwds.get(1));
				}
				else
				{
					tex.setBounds(x,y,b,h);
					tex.addActionListener(actionLins);
					tex.setEnabled(false);
					tex.putClientProperty("id", Integer.valueOf(i));
					panelCentro.add(tex);
				}
			}
			y = y + 25;
			i++;
		}
		//JCGE: Revisamos que botones le dio click
		txtDate .addKeyListener(new KeyAdapter()
		{
			public void keyTyped(KeyEvent e)
			{
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_TAB) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_SLASH)))        
				{
					JOptionPane.showMessageDialog(null, "Favor de ingresar una fecha válida \ndd/MM/aaaa");
					e.consume();
				}
			}
		});
		
		buscar = new JButton("Buscar");
		
		x += 210; buscar.setBounds(x,70,100,h);
		buscar.setVisible(true);
		buscar.setEnabled(true);
		buscar.addActionListener(actionLins);
		panelCentro.add(buscar);
		
		//JCGE: Aqui vamos a mostrar una pequeña lista de usuarios
		panelsito = new JPanel();
		usuarios = new JLabel("Usuarios actuales: ");
		x += 210; usuarios.setBounds(0,0,200,20);
		panelsito.add(usuarios);
		panelsito.setBounds(x, 90, 200, y-90);
		
		//listaUsuarios(lista, panelCentro, x, 90, 200, y-90);
		try {
			ResultSet res = baseDatos.db.newQuery("SELECT array_to_string(array_agg(idusuario),',') AS usuarios"
												+ "  FROM (SELECT *"
												+ "          FROM usuarios"
												+ "         ORDER BY idusuario) AS foo ");
			res.next();
			String[] usuarios = (" ,"+res.getString("usuarios")).split(",");
			lista = new JList(usuarios);
			lista.setVisibleRowCount(5);
			lista.addListSelectionListener(this);
			lista.setSelectedIndex(0);
			//lista.addFocusListener(fe);
			lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			JScrollPane scroll = new JScrollPane(lista);
			scroll.setBounds(0, 30, 200, y-90);
			panelsito.setLayout(null);
			panelsito.add(scroll);
			panelCentro.add(panelsito);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//JCGE: Dejamos esto activado
		textos.get(0).addFocusListener(fe);
		textos.get(0).setEnabled(true);
		textos.get(0).requestFocus();
	}
	public void valueChanged(ListSelectionEvent arg0)
	{
		// TODO Auto-generated method stub
		System.out.println(lista.getSelectedIndex() + " " + lista.getSelectedValue());
		textos.get(0).setText(""+lista.getSelectedValue().toString().trim());
		textos.get(0).requestFocus();
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
					txtDate.setValue(r.get(0).getDate("fechanacimiento"));
					textos.get(7).setText(r.get(0).getString("telefono"));
					textos.get(8).setText(r.get(0).getString("curp"));
					textos.get(9).setText(r.get(0).getString("rfc"));
					textos.get(10).setText(r.get(0).getString("nss"));
					for (JTextField tex: textos)
					{
						tex.setEnabled(true);
					}
					for (JPasswordField p: passwds)
					{
						p.setEnabled(true);
					}
					txtDate.setEnabled(true);
				}
				catch (SQLException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null,"El usuario "+textos.get(0).getText()+" no existe");
					int i = 0;
					for (JTextField tex: textos)
					{
						if (i != 0)
							tex.setText("");
						
						tex.setEnabled(false);
						i++;
					}
					for (JPasswordField p: passwds)
					{
						p.setEnabled(false);
					}
					textos.get(0).setEnabled(true);
					textos.get(0).requestFocus();
					
				}
			}
			if (boton == "Guardar")
			{
				//JCGE: Necesitamos validar los campos que no esten vacios
				if (textos.get(0).getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(null,"El usuario colocado no es válido.");
					return;
				}
				if (textos.get(3).getText().trim().length() == 0 || textos.get(4).getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(null,"El Apellido Paterno y el Nombre son campos obligatorios. \nFavor de Rellenarlos");
					return;
				}
				//System.out.println(textos.get(0).getText().trim());
				
				//JCGE: Es la señal que dice que quiere ver a un usuario existente
				try
				{
					r = new ArrayList<ResultSet>();
					r.add(baseDatos.db.newQuery(" SELECT * "
												+"  FROM usuarios "
												+" WHERE idusuario = '"+textos.get(0).getText()+"' "));
					//Si lo encuentra le hacemos el update
					if (r.get(0).next())
					{
						//
						
						String query = String.format("UPDATE usuarios SET (pass,paterno,materno,nombre,"
												   + "                           fechanacimiento,telefono,curp,"
												   + "                           rfc,nss,tipo_usuario) ="
												   + "                          ('%s','%s','%s','%s',"
												   + "                           '%s'::DATE,'%s','%s',"
												   + "                           '%s','%s','%s') WHERE idusuario = trim('%s')",
												   baseDatos.db.utilMd5(textos.get(2).getText()),textos.get(4).getText(), textos.get(5).getText(), textos.get(3).getText(),
												   txtDate.getValue(), textos.get(7).getText(), textos.get(8).getText(),
												   textos.get(9).getText(), textos.get(10).getText(), textos.get(1).getText(),
												   textos.get(0).getText());
						baseDatos.db.newInsert(query);
						
						System.out.println(textos.get(0).getText()+" Este men fue actualizado");
					}
					//Si no existe lo insertamos
					else
					{
						//JCGE: Esto si jala solo lo comentarize para que no haga tanto bulto
						
						String query = String.format("INSERT INTO usuarios (idusuario,pass,kusuario,"
												   + "                      paterno,materno,nombre,"
												   + "                      fechanacimiento,telefono,curp,"
												   + "                      rfc,nss,activo,tipo_usuario) "
												   + "VALUES ('%s','%s',DEFAULT,"
												   + "        '%s','%s','%s',"
												   + "        '%s'::DATE,'%s','%s',"
												   + "        '%s','%s',TRUE,'%s')",
												   textos.get(0).getText(), baseDatos.db.utilMd5(textos.get(2).getText()),
												   textos.get(4).getText(), textos.get(5).getText(), textos.get(3).getText(),
												   txtDate.getValue(), textos.get(7).getText(), textos.get(8).getText(),
												   textos.get(9).getText(), textos.get(10).getText(), textos.get(1).getText());
						System.out.println(query);
						baseDatos.db.newInsert(query);
						
						System.out.println(textos.get(0).getText()+" Este men fue insertado");
					}
					//JCGE: Sea insert o update hay que limpiar todo el cagadero
					for (JTextField tex: textos)
					{
						tex.setText("");
						tex.setEnabled(false);
					}
					for (JPasswordField p: passwds)
					{
						p.setEnabled(false);
					}
					lista.setEnabled(true);
					txtDate.setEnabled(false);
					textos.get(0).setEnabled(true);
					textos.get(0).requestFocus();
					buscar.setEnabled(true);
					rellenaLista();
				}
				catch (SQLException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				lista.setEnabled(true);
			}
			if (boton == "Cancelar")
			{
				//
				for (JTextField tex: textos)
				{
					tex.setText("");
					tex.setEnabled(false);
				}
				for (JPasswordField p: passwds)
				{
					p.setEnabled(false);
				}
				lista.setEnabled(true);
				txtDate.setEnabled(false);
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
				for (JPasswordField p: passwds)
				{
					p.setEnabled(true);
				}
				txtDate.setEnabled(true);
				lista.setSelectedIndex(0);
				lista.setEnabled(false);
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
			System.out.println(textos.get(0).getText());
		}
	};
	public void rellenaLista()
	{
		try {
			ResultSet res = baseDatos.db.newQuery("SELECT array_to_string(array_agg(idusuario),',') AS usuarios"
												+ "  FROM (SELECT *"
												+ "          FROM usuarios"
												+ "         ORDER BY idusuario) AS foo ");
			if (res.next())
			{
				String[] usuarios;
				usuarios = (" ,"+res.getString("usuarios")).split(",");
				lista.setListData(usuarios);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
