import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

public class Usuarios extends Menu implements ActionListener
{
	/**
	 * JCGE: Modulo de Usuarios
	 */
	private static final long serialVersionUID = 3600455330726028640L;
	//JCGE: Modulo de usuarios
	private ArrayList<HFLabel>  etiquetas;
	static ArrayList<HFTextField> textos;
	private ArrayList<ResultSet> r;
	private JButton buscar;
	private HFDateField txtDate;
	private ArrayList<JPasswordField> passwds;
	private String[] tagz;
	private Boolean esNuevo = false;
	private Boolean cambiarPss = false;
	private JRadioButton estatus;
	Usuarios ()
	{
		//JCGE: Propiedades Generales
		this.setTitle("Usuarios");
		//JCGE: Propiedades Particulares
		tagz = new String[] {"Usuario: ","Tipo Usuario: ",
						"Contraseña: ","Nombre: ",
						"Paterno: ","Materno: ",
						"F. Nacimiento: ","Télefono: ",
						"CURP: ","RFC: ","NSS: ","Estatus: "};
		String[] nombres = {"Nuevo","Guardar","Cancelar","Menu"};
		rellenaToolBar(nombres, actionLins);
		botonera.get(1).setEnabled(false);
		etiquetas = new ArrayList<HFLabel>();
		textos    = new ArrayList<HFTextField>();
		passwds   = new ArrayList<JPasswordField>();
		for (int i = 0; i <= tagz.length-1; i++)
		{
			etiquetas.add(new HFLabel(tagz[i]));
		}
		int x = 10, y = 70, b = 120, h = 20;
		for(HFLabel tag: etiquetas)
		{
			tag.setBounds(x,y,b,h);
			panelCentro.add(tag);
			y += 25;
			textos.add(new HFTextField(200));
		}
		
		x = b; y = 70; b = 200; h = 20;
		
		int i = 0;
		for (HFTextField tex: textos)
		{
			if (i == 6)
			{
				//JCGE: Buscar date picker
				//JCGE: Vamos a poner un campo especial de fecha
				txtDate = new HFDateField();
				txtDate.setBounds(x,y,b,h);
				txtDate.resetDate();
				panelCentro.add(txtDate);
			}
			else if (i == 2)
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
			else if (i == 11) //JCGE: Construyendo el combobox de estatus
			{
				estatus = new JRadioButton("Activo");
				estatus.setBounds(x,y,b,h);
				estatus.setEnabled(false);
				estatus.setSelected(false);
				panelCentro.add(estatus);
			}
			else
			{
				if (i == 0)
				{
					tex.busqueda = "Usuarios";
					tex.query = "SELECT array_to_string(array_agg(idusuario),',') AS usuarios"
							+ "  FROM (SELECT *"
							+ "          FROM usuarios"
							+ "         ORDER BY idusuario) AS foo ";
				}
				tex.setBounds(x,y,b,h);
				tex.addActionListener(actionLins);
				tex.setEnabled(false);
				panelCentro.add(tex);
			}
			y += 25;
			i++;
		}
		//JCGE: Revisamos que botones le dio click
		txtDate.addKeyListener(new KeyAdapter()
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
		
		//JCGE: Dejamos esto activado
		textos.get(0).addFocusListener(fe);
		textos.get(0).setEnabled(true);
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
					if (r.get(0).next())
					{
						textos.get(3).setText(r.get(0).getString("nombre"));
						textos.get(4).setText(r.get(0).getString("paterno"));
						textos.get(5).setText(r.get(0).getString("materno"));
						textos.get(1).setText(r.get(0).getString("tipo_usuario"));
						txtDate.setDate(r.get(0).getDate("fechanacimiento"));
						textos.get(7).setText(r.get(0).getString("telefono"));
						textos.get(8).setText(r.get(0).getString("curp"));
						textos.get(9).setText(r.get(0).getString("rfc"));
						textos.get(10).setText(r.get(0).getString("nss"));
						estatus.setSelected(r.get(0).getBoolean("activo"));
						for (HFTextField tex: textos)
						{
							tex.setEnabled(true);
						}
						for (JPasswordField p: passwds)
						{
							p.setEnabled(true);
						}
						txtDate.setEnabled(true);
						esNuevo = false;
						botonera.get(1).setEnabled(true);
						botonera.get(0).setEnabled(false);
						buscar.setEnabled(false);
						estatus.setEnabled(true);
						textos.get(0).setEnabled(false);
					}
					else
					{
						JOptionPane.showMessageDialog(null,"El usuario "+textos.get(0).getText()+" no existe");
						int i = 0;
						for (HFTextField tex: textos)
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
						buscar.setEnabled(true);
						estatus.setEnabled(false);
						estatus.setSelected(false);
						txtDate.resetDate();
					}
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}
			if (boton == "Guardar")
			{
				//JCGE: Le damos una leida a las contraseñas
				String pss1 = new String(passwds.get(0).getPassword());
				String rpss = new String(passwds.get(1).getPassword());
				//JCGE: Necesitamos validar los campos que no esten vacios
				for (int i = 0; i<= tagz.length-1; i++)
				{
					//JCGE: Como el campo de contraseña y fecha de nacimiento son diferentes
					// Tenemos que validarlos diferentes
					if (i == 2 || i == 6 || i == 11)
					{
						if (i == 2) //JCGE: Es el campo de contraseña
						{
							if (esNuevo)
							{
								//JCGE: Nos aseguremos de que tengan info
								if (pss1.length() < 6 || rpss.length() < 6)
								{
									JOptionPane.showMessageDialog(null,"Las contraseñas tienen que ser mayores a 6 caracteres. \nFavor de rellenarlos");
									return;
								}
								if (!(pss1.equals(rpss)))
								{
									JOptionPane.showMessageDialog(null,"Las contraseñas deben de coincidir vaca . \nFavor de verificar de nuevo.");
									return;
								}
							}
							else //JCGE: No es nuevo
							{
								//JCGE: Tiene la intencion de cambiar la contraseña
								if (pss1.length() > 0 || rpss.length() > 0)
								{
									//JCGE: Nos aseguremos de que tengan info
									if (pss1.length() < 6 || rpss.length() < 6)
									{
										JOptionPane.showMessageDialog(null,"Las contraseñas tienen que ser mayores a 6 caracteres. \nFavor de rellenarlos");
										return;
									}
									//System.out.println(pss1.toString() + " " + rpss.toString());
									if (!(pss1.equals(rpss)))
									{
										JOptionPane.showMessageDialog(null,"Las contraseñas deben de coincidir perro. \nFavor de verificar de nuevo.");
										return;
									}
									cambiarPss = true;
								}
							}
						}
						else
						{
							//JCGE: Es el campo de fecha
						}
					}
					else
					{
						if (textos.get(i).getText().length() == 0)
						{
							JOptionPane.showMessageDialog(null,"El campo "+(tagz[i].split(":"))[0]+" es obligatorio. \nFavor de Rellenarlo");
							return;
						}
					}
				}
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
						String query;
						//JCGE: En este si cambiamos la contraseña
						if (cambiarPss)
						{
							query = String.format("UPDATE usuarios SET (pass,paterno,materno,nombre,"
								    + "                     fechanacimiento,telefono,curp,"
									+ "                     rfc,nss,tipo_usuario,activo) ="
									+ "                    ('%s','%s','%s','%s',"
									+ "                     '%s'::DATE,'%s','%s',"
									+ "                     '%s','%s','%s', %s) "
									+ "WHERE idusuario = trim('%s')",
									baseDatos.db.utilMd5(pss1),textos.get(4).getText(),  textos.get(5).getText(), textos.get(3).getText(),
									txtDate.getJFormattedTextField().getText(),textos.get(7).getText(),  textos.get(8).getText(),
									textos.get(9).getText(),   textos.get(10).getText(), textos.get(1).getText(), estatus.isSelected(),
									textos.get(0).getText());
						}
						else //JCGE: En este no cambiamos la contraseña
						{
							query = String.format("UPDATE usuarios SET (paterno,materno,nombre,"
								    + "                     fechanacimiento,telefono,curp,"
									+ "                     rfc,nss,tipo_usuario,activo) ="
									+ "                    ('%s','%s','%s',"
									+ "                     '%s'::DATE,'%s','%s',"
									+ "                     '%s','%s','%s',%s) "
									+ "WHERE idusuario = trim('%s')",
									textos.get(4).getText(), textos.get(5).getText(),  textos.get(3).getText(),
									txtDate.getJFormattedTextField().getText(),      textos.get(7).getText(),  textos.get(8).getText(),
									textos.get(9).getText(), textos.get(10).getText(), textos.get(1).getText(), estatus.isSelected(),
									textos.get(0).getText());
						}
						baseDatos.db.newInsert(query);
						
						JOptionPane.showMessageDialog(null, "El Usuario: "+textos.get(0).getText()+" fue actualizado.");
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
												   textos.get(4).getText(), textos.get(5).getText(),  textos.get(3).getText(),
												   txtDate.getJFormattedTextField().getText(),textos.get(7).getText(),  textos.get(8).getText(),
												   textos.get(9).getText(), textos.get(10).getText(), textos.get(1).getText());
						System.out.println(query);
						baseDatos.db.newInsert(query);
						
						JOptionPane.showMessageDialog(null, "El Usuario: "+textos.get(0).getText()+" fue agregado.");
					}
					//JCGE: Sea insert o update hay que limpiar todo el cagadero
					for (HFTextField tex: textos)
					{
						tex.setText("");
						tex.setEnabled(false);
					}
					for (JPasswordField p: passwds)
					{
						p.setEnabled(false);
						p.setText("");
					}
					txtDate.resetDate();
					textos.get(0).setEnabled(true);
					textos.get(0).requestFocus();
					buscar.setEnabled(true);
					estatus.setEnabled(false);
					estatus.setSelected(false);
					botonera.get(1).setEnabled(false);
					botonera.get(0).setEnabled(true);
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}
			}
			if (boton == "Cancelar")
			{
				//
				for (HFTextField tex: textos)
				{
					tex.setText("");
					tex.setEnabled(false);
				}
				for (JPasswordField p: passwds)
				{
					p.setEnabled(false);
					p.setText("");
				}
				txtDate.resetDate();
				textos.get(0).setEnabled(true);
				textos.get(0).requestFocus();
				buscar.setEnabled(true);
				botonera.get(1).setEnabled(false);
				botonera.get(0).setEnabled(true);
				estatus.setEnabled(false);
				estatus.setSelected(false);
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
				for (HFTextField tex: textos)
				{
					tex.setEnabled(true);
					tex.setText("");
				}
				for (JPasswordField p: passwds)
				{
					p.setEnabled(true);
				}
				txtDate.setEnabled(true);
				textos.get(0).requestFocus();
				buscar.setEnabled(false);
				esNuevo = true;
				botonera.get(1).setEnabled(true);
				botonera.get(0).setEnabled(false);
				estatus.setEnabled(false);
				estatus.setSelected(true);
			}
		}
	};
	
	private FocusListener fe = new FocusListener ()
	{
		@Override
		public void focusLost(FocusEvent arg0)
		{
			System.out.println(textos.get(0).getText());
		}
		@Override
		public void focusGained(FocusEvent arg0) {/*JCGE: Aun sin nada*/}
	};
}
