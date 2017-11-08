import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

public class Habitaciones extends Menu implements ActionListener
{
	/**
	 * JCGE: Modulo para agregar habitaciones
	 */
	private static final long serialVersionUID = 4995360916941152629L;
	private ArrayList<JLabel> labels;
	static  ArrayList<HFTextField> textos;
	private ArrayList<ResultSet> r;
	private JPanel panelsito;
	private JLabel habitaciones;
	private JList lista;
	private JTextArea desc;
	private JButton buscar;
	static HFIntegerField idhabitacion;
	private HFDoubleField tabulacion;
	private String[] etiquetas;
	Habitaciones()
	{
		this.setTitle("Habitaciones");
		String[] nombres = {"Nuevo","Guardar","Cancelar","Menu"};
		rellenaToolBar(nombres, actionLins);
		botonera.get(1).setEnabled(false); //Guardar
		//JCGE: Propiedades especificas
		etiquetas = new String[] {"ID habitacion: ","Edificio: ", "Número: ", "Tabulación: ", "Descripción: "};
		labels = new ArrayList<JLabel>();
		textos = new ArrayList<HFTextField>();
		int x = 10, y = 70, b = 200, h = 20;
		for (int i = 0; i<= etiquetas.length-1; i++)
		{
			labels.add(new JLabel(etiquetas[i]));
		}
		for (JLabel tag: labels)
		{
			tag.setBounds(x, y, b, h);
			panelCentro.add(tag);
			y += 25;
			textos.add(new HFTextField(200));
		}
		y = 70; x = b;
		int i = 0;
		for (HFTextField txt: textos)
		{
			if (i == 0)
			{
				//
				idhabitacion = new HFIntegerField();
				idhabitacion.setBounds(x,y,b,h);
				idhabitacion.setEnabled(true);
				panelCentro.add(idhabitacion);
			}
			else if (i == 3)
			{
				//
				tabulacion = new HFDoubleField();
				tabulacion.setBounds(x,y,b,h);
				tabulacion.setEnabled(false);
				panelCentro.add(tabulacion);
			}
			else if (i == 4)
			{
				desc = new JTextArea();
				desc.setBounds(x, y, b, 200);
				desc.setEnabled(false);
				panelCentro.add(desc);
			}
			else
			{
				txt.setBounds(x, y, b, h);
				txt.setEnabled(false);
				panelCentro.add(txt);
			}
			y += 25;
			i++;
		}
		buscar = new JButton("Buscar");
		buscar.addActionListener(actionLins);
		x += 210; buscar.setBounds(x,70,100,h);
		panelCentro.add(buscar);
		//JCGE: Aqui vamos a mostrar una pequeña lista de usuarios
		panelsito = new JPanel();
		habitaciones = new JLabel("Habitaciones actuales: ");
		x += 110; y = 70;
		habitaciones.setBounds(0,0,b,20);
		panelsito.add(habitaciones);
		panelsito.setBounds(x, y, b, 300);
		
		//listaUsuarios(lista, panelCentro, x, 90, 200, y-90);
		try
		{
			String[] usuarios = new String[] {" "};
			ResultSet res = baseDatos.db.newQuery("SELECT array_to_string(array_agg(idhabitacion||': '||'E. '||edificio||' N. '||numero_fisico),',') AS habitacion"
												+ "  FROM (SELECT *"
												+ "          FROM habitaciones"
												+ "         ORDER BY idhabitacion) AS foo ");
			if (res.next())
			{
				System.out.println(res.getString("habitacion"));
				if (res.getString("habitacion") != null)
				{
					usuarios = (" ,"+res.getString("habitacion")).split(",");
				}
				else
				{
					usuarios = new String[] {" "};
				}
			}
			lista = new JList(usuarios);
			lista.setVisibleRowCount(5);
			lista.addListSelectionListener(this);
			lista.setSelectedIndex(0);
			//lista.addFocusListener(fe);
			lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			JScrollPane scroll = new JScrollPane(lista);
			scroll.setBounds(0, 30, b, 270);
			panelsito.setLayout(null);
			panelsito.add(scroll);
			panelCentro.add(panelsito);
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void valueChanged(ListSelectionEvent arg0)
	{
		// TODO Auto-generated method stub
		//System.out.println(lista.getSelectedIndex() + " " + lista.getSelectedValue());
		if (lista.getSelectedValue() != null)
			if (lista.getSelectedValue().toString().trim().split(":")[0] != "")
				idhabitacion.setText(""+lista.getSelectedValue().toString().trim().split(":")[0]);
		
		idhabitacion.requestFocus();
	}
	//JCGE: Este metodo es privado, porque solo quiero que aplique para esta clase en especifico
	private ActionListener actionLins = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			String boton = arg0.getActionCommand();
			System.out.println(boton);
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
				botonera.get(0).setEnabled(false); //Nuevo
				botonera.get(1).setEnabled(true); //Guardar
				botonera.get(2).setEnabled(true); //Cancelar
				buscar.setEnabled(false);
				limpiarEntradas(true);
			}
			if (boton == "Guardar")
			{
				//JCGE: Revisamos los objetos
				int i = 0;
				for (HFTextField txt: textos)
				{
					if (i == 0)
					{
						if (!(isInteger(idhabitacion.getText())))
						{
							JOptionPane.showMessageDialog(null, "El identificador de la habitación tiene que ser un número.");
							return;
						}
					}
					else if (i == 3)
					{
						if (!(isDouble(tabulacion.getText())))
						{
							JOptionPane.showMessageDialog(null, "La tabulación de la habitación tiene que ser un número.");
							return;
						}
					}
					else if (i == 4)
					{
						if (desc.getText().trim().length() == 0)
						{
							JOptionPane.showMessageDialog(null, "Favor de escribir una descripción para la habitación.");
							return;
						}
					}
					else
					{
						if (txt.getText().trim().length() == 0)
						{
							JOptionPane.showMessageDialog(null, String.format("Favor de rellenar el campo de %s.",etiquetas[i]));
							return;
						}
					}
					i++;
				}
				//JCGE: Es la señal que dice que quiere ver a un usuario existente
				try
				{
					r = new ArrayList<ResultSet>();
					r.add(baseDatos.db.newQuery(" SELECT * "
												+"  FROM habitaciones "
												+" WHERE idhabitacion = "+idhabitacion.getText()));
					//Si lo encuentra le hacemos el update
					if (r.get(0).next())
					{
						String query;
						//JCGE: Si existe la habitacion
						query = String.format("UPDATE habitaciones SET (edificio,numero_fisico,"
											+ "                         tabulacion,descripcion) ="
											+ "                        ('%s','%s',%s::NUMERIC,'%s') "
											+ " WHERE idhabitacion = %s",
											textos.get(1).getText(), textos.get(2).getText(),
											tabulacion.getText(), desc.getText(),
											idhabitacion.getText());
						baseDatos.db.newInsert(query);
						
						JOptionPane.showMessageDialog(null, "La habitación: "+idhabitacion.getText()+" fue actualizada.");
					}
					//Si no existe lo insertamos
					else
					{
						//JCGE:
						String query = String.format("INSERT INTO habitaciones (idhabitacion,edificio,numero_fisico,tabulacion,descripcion) "
												   + "VALUES (DEFAULT,'%s','%s',%s,'%s')",
												   textos.get(1).getText(), textos.get(2).getText(),
												   tabulacion.getText(), desc.getText());
						System.out.println(query);
						baseDatos.db.newInsert(query);
						
						JOptionPane.showMessageDialog(null, "La habitación: "+idhabitacion.getText()+" fue agregada.");
					}
					buscar.setEnabled(true);
				}
				catch (SQLException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				botonera.get(0).setEnabled(true); //Nuevo
				botonera.get(1).setEnabled(false); //Guardar
				botonera.get(2).setEnabled(false); //Cancelar
				limpiarEntradas(false);
			}
			if (boton == "Cancelar")
			{
				botonera.get(0).setEnabled(true); //Nuevo
				botonera.get(1).setEnabled(false); //Guardar
				botonera.get(2).setEnabled(true); //Cancelar
				limpiarEntradas(false);
			}
			if (boton == "Buscar")
			{
				//JCGE: Es la señal que dice que quiere ver a un usuario existente
				try
				{
					r = new ArrayList<ResultSet>();
					r.add(baseDatos.db.newQuery("SELECT *,to_char(tabulacion,'FM999999990.00') AS tabulacionx "
												+" FROM habitaciones "
												+"WHERE idhabitacion = "+idhabitacion.getText()));
					if (r.get(0).next())
					{
						botonera.get(0).setEnabled(false); //Nuevo
						botonera.get(1).setEnabled(true); //Guardar
						botonera.get(2).setEnabled(true); //Cancelar
						limpiarEntradas(true);
						int i = 1;
						for (HFTextField txt: textos)
						{
							if (i == 1)
							{
								idhabitacion.setEnabled(false);
							}
							else if (i == 4)
							{
								tabulacion.setText(r.get(0).getString("tabulacionx"));
								tabulacion.setEnabled(true);
							}
							else if (i == 5)
							{
								desc.setText(r.get(0).getString("descripcion"));
								desc.setEnabled(true);
							}
							else
							{
								txt.setText(r.get(0).getString(i));
								txt.setEnabled(true);
							}
							i++;
						}
					}
					else
					{
						limpiarEntradas(false);
						JOptionPane.showMessageDialog(null,"La habitación "+idhabitacion.getText()+" no existe");
					}
				}
				catch (SQLException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	};
	private void limpiarEntradas(boolean enabl)
	{
		int i = 1;
		lista.setEnabled(!enabl);
		for (HFTextField txt: textos)
		{
			if (i == 1)
			{
				if (!enabl)
				{
					idhabitacion.setText("0");
				}
				idhabitacion.setEnabled(!enabl);
			}
			else if (i == 4)
			{
				tabulacion.setText("0.00");
				tabulacion.setEnabled(enabl);
			}
			else if (i == 5)
			{
				desc.setText("");
				desc.setEnabled(enabl);
			}
			else
			{
				txt.setText("");
				txt.setEnabled(enabl);
			}
			i++;
		}
		idhabitacion.requestFocus();
	}
}
