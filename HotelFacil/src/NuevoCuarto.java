import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NuevoCuarto extends MenuInterno implements ActionListener
{
	/**
	 * JCGE: Modulo de Checkout
	 * Aqui vamos a despedir a nuestro inquilino o alargarle la visita
	 */
	private static final long serialVersionUID = -4467762444239765332L;
	private ArrayList<HFLabel>     etiquetas;
	private ArrayList<HFTextField> textos;
	private ArrayList<ResultSet>   r;
	private ArrayList<HFDateField> datePickers;
	private ArrayList<HFDateField> fechas;
	public  static HFIntegerField  idhuesped;
	private HFIntegerField         huespedes;
	private JButton                buscar, guardar, cancelar;
	private String[]               labels = {"     Num huesped: ",
											 "         Paterno: ",
											 "         Materno: ",
											 "          Nombre: ",
											 "Fecha Nacimiento: ",
											 "        Telefono: ",
											 "         Tarjeta: ",
											 "   Num Seguridad: "};
	
	private static JList<String>             Cobros;
	public  static  DefaultListModel<String>  conceptosCobros;
	private JScrollPane               scroll;
	private JCheckBox esNuevo;
	private boolean esMayor = false, menorHoy = false;
	private int dias = 0;
	NuevoCuarto()
	{
		//JCGE: Propiedades de la ventana
		this.setTitle("Nueva Reservación");
		//this.setExtendedState(NORMAL);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//this.setLocationRelativeTo(null);
		//menuBar.setVisible(false);
		//panelInterno.setLayout(null);
		//String[] nombres = {"Salir"};
		//rellenaToolBar(nombres, this);
		
		//JCGE: Propiedades especificas
		etiquetas   = new ArrayList<HFLabel>();
		textos      = new ArrayList<HFTextField>();
		datePickers = new ArrayList<HFDateField>();
		idhuesped   = new HFIntegerField();
		buscar      = new JButton("Buscar");
		esNuevo     = new JCheckBox();
		fechas      = new ArrayList<HFDateField>();
		huespedes   = new HFIntegerField();
		
		//JCGE: Formulario de datos del titular
		for (int i = 0; i <= labels.length -1; i++)
		{
			etiquetas.add(new HFLabel(labels[i]));
			textos.add(new HFTextField(200));
		}
		int x = 10, y = 70, b = 200, h = 20;
		for (HFLabel lbl: etiquetas)
		{
			lbl.setBounds(x,y,b,h);
			panelInterno.add(lbl);
			y+=35;
		}
		x = b + 10; y = 70;
		int i = 0;
		for (HFTextField txt: textos)
		{
			if (i == 4)
			{
				datePickers.add(new HFDateField());
				datePickers.get(datePickers.size()-1).setBounds(x,y,b,h);
				panelInterno.add(datePickers.get(datePickers.size()-1));
			}
			else if (i == 0)
			{
				idhuesped.setBounds(x,y,b,h);
				panelInterno.add(idhuesped);
				idhuesped.busqueda = "Huespedes";
				idhuesped.query = (" SELECT format('%s: %s %s %s : FN = %s',idhuesped,paterno,materno,nombre,fechanacimiento) "
								 + "   FROM huespedes ");
				idhuesped.bloqueaAlEnter(false);
				buscar.setBounds(x + 210,y,100,h);
				buscar.addActionListener(this);
				
				esNuevo.setText("¿Es Nuevo?");
				esNuevo.setBounds(x + 210, y + 35, 150, h);
				esNuevo.addItemListener(new ItemListener()
				{
					@Override
					public void itemStateChanged(ItemEvent arg0)
					{
						// JCGE: activamos o desactivamos este pedo
						activarEntradas(arg0.getStateChange() == ItemEvent.DESELECTED);
						limpiarEntradas();
					}
					
				});
				panelInterno.add(buscar);
				panelInterno.add(esNuevo);
			}
			else
			{
				txt.setBounds(x,y,b,h);
				panelInterno.add(txt);
			}
			i++;
			y+=35;
		}
		y+=50;
		x = 10;  b = 200; h = 20;
		//JCGE: Datos de la habitacion
		HFLabel lPersonas = new HFLabel("       Huespedes: "),
				lFechaIni = new HFLabel("    Fecha Inicio: "),
				lFechaFin = new HFLabel("       Fecha Fin: ");
		for (int j = 0; j <= 2; j++)
		{
			if (j == 0)
			{
				lPersonas.setBounds(x, y, b, h);
				huespedes.setBounds(x + b, y, b, h);
				panelInterno.add(huespedes);
			}
			else
			{
				if (j == 1)
				{
					lFechaIni.setBounds(x, y, b, h);
				}
				else
				{
					lFechaFin.setBounds(x, y, b, h);
				}
				fechas.add(new HFDateField());
				fechas.get(fechas.size()-1).setBounds(x + b, y, b, h);
				panelInterno.add(fechas.get(fechas.size()-1));
			}
			y+=35;
		}
		huespedes.addFocusListener(new FocusListener()
		{
			@Override
			public void focusLost(FocusEvent arg0)
			{
				// JCGE:
				String concepto = String.format("Huespedes: Cantidad: %s: Precio Unitario: %s: Precio Total: %s",
						Integer.parseInt(huespedes.getText()),
						500,
						500 * Integer.parseInt(huespedes.getText()));
				nuevoCobro(concepto);
			}
			@Override
			public void focusGained(FocusEvent arg0) {/*JCGE: Aun sin nada*/}
		});
		fechas.get(0).addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				//JCGE: Vamos a revisar que la fecha inicial no sea menor a hoy
				ResultSet r;
				menorHoy = false;
				try {
					r = baseDatos.db.newQuery(String.format(" SELECT '%s'::DATE < now()::DATE ", fechas.get(0).getDate()));
					if (r.next())
					{
						menorHoy = r.getBoolean(1);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//JCGE: La fecha inicial es mayor a la final
				if (menorHoy)
				{
					JOptionPane.showMessageDialog(null, "La fecha inicial tiene que ser hoy o días posteriores, Favor de corregirlo.");
					fechas.get(0).resetDate();
				}
				fechas.get(1).setDate(fechas.get(0).getDate());
			}
		});
		fechas.get(1).addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//JCGE: Como solo es un solo boton para esta marinola
				//No tenemos que buscar, solo validamos que tranza
				//La fecha final tiene que ser mayor a la fecha inicial
				//Nos vamos a apoyar con SQL
				ResultSet r, t, s;
				esMayor = false;
				dias = 0;
				try {
					r = baseDatos.db.newQuery(String.format(" SELECT '%s'::DATE > '%s'::DATE ", fechas.get(0).getDate(),fechas.get(1).getDate()));
					if (r.next())
					{
						esMayor = r.getBoolean(1);
						if (!esMayor)
						{
							t = baseDatos.db.newQuery(String.format(" SELECT '%s'::DATE - '%s'::DATE ", fechas.get(1).getDate(),fechas.get(0).getDate()));
							if (t.next())
							{
								dias = t.getInt(1);
								s = baseDatos.db.newQuery(String.format(" SELECT tabulacion FROM habitaciones WHERE idhabitacion = %s ", estatusHabitacion.idhabitacion));
								if (s.next())
								{
									nuevoCobro(String.format("Dias: Cantidad: %s: Precio Unitario: %s: Precio Total: %s",
											dias, s.getDouble(1),  s.getDouble(1)*dias));
								}
							}
						}
					}
				} catch (SQLException et) {
					// TODO Auto-generated catch block
					et.printStackTrace();
				}
				//JCGE: La fecha inicial es mayor a la final
				if (esMayor)
				{
					JOptionPane.showMessageDialog(null, "La fecha inicial es mayor a la fecha final, Favor de corregirlo.");
					fechas.get(1).setDate(fechas.get(0).getDate());
				}
			}
		});
		panelInterno.add(lPersonas);
		panelInterno.add(lFechaIni);
		panelInterno.add(lFechaFin);
		
		//JCGE: Ponemos la lista de los cobros que se van a hacer
		conceptosCobros = new DefaultListModel<String>();
		Cobros = new JList<String>(conceptosCobros);
		scroll = new JScrollPane(Cobros);
		x += 600; y = 70;
		scroll.setBounds(x, y, 400, 300);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panelInterno.add(scroll);
		
		//JCGE: Botones de guardar y cancelar
		guardar     = new JButton("Guardar");
		cancelar    = new JButton("Cancelar");
		guardar.setFont(new Font("Ubuntu Mono", Font.BOLD, 15));
		cancelar.setFont(new Font("Ubuntu Mono", Font.BOLD, 15));
		guardar.setBackground(Color.GREEN);
		cancelar.setBackground(Color.RED);
		guardar.addActionListener(this);
		cancelar.addActionListener(this);
		y+=400;
		guardar.setBounds(x, y, 200, 60);
		x+=210;
		cancelar.setBounds(x, y, 200, 60);
		panelInterno.add(guardar);
		panelInterno.add(cancelar);
		limpiarEntradas();
		activarEntradas(true);
	}
	public static boolean existeConcepto(String concepto)
	{
        for(int i = 0; i< Cobros.getModel().getSize();i++)
        {
            //System.out.println(huespedes.getModel().getElementAt(i));
        	if (Objects.equals(Cobros.getModel().getElementAt(i).split(":")[0], new String(concepto.split(":")[0])))
        		return true;
        }
        return false;
	}
	public static void nuevoCobro(String e)
	{
		if (existeConcepto(e))
		{
			for(int i = 0; i< Cobros.getModel().getSize();i++)
	        {
	            //System.out.println(huespedes.getModel().getElementAt(i));
	        	if (Objects.equals(Cobros.getModel().getElementAt(i).split(":")[0], new String(e.split(":")[0])))
	        		conceptosCobros.remove(i);
	        }
		}
		conceptosCobros.addElement(e);
	}
	//JCGE: 
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		String boton = arg0.getActionCommand();
		System.out.println(boton);
		if (boton == "Buscar")
		{
			try
			{
				r = new ArrayList<ResultSet>();
				r.add(baseDatos.db.newQuery(String.format(" SELECT * "
														+ "   FROM huespedes "
														+ "  WHERE idhuesped = %s ",idhuesped.getText())));
				if (r.get(0).next())
				{
					limpiarEntradas();
					int i = 1;
					for (HFTextField txt: textos)
					{
						txt.setText(r.get(0).getString(i));
						i++;
					}
					i = 0;
					for (HFDateField dt: datePickers)
					{
						if (i == 0)
						{
							dt.setDate(r.get(0).getDate("fechanacimiento"));
						}
						else
						{
							dt.setDate(r.get(0).getDate("ultimaactualizacion"));
						}
						i++;
					}
				}
				else
				{
					limpiarEntradas();
					JOptionPane.showMessageDialog(null,"El huesped no existe");
				}
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();
				limpiarEntradas();
			}
		}
		if (boton == "Guardar")
		{
			//JCGE: Revisamos que la info no este vacia
			for (HFTextField txt: textos)
			{
				if (txt.getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(null,"Favor de completar todos los campos.");
					return;
				}
			}
			String query = String.format("%s", 1);
			ResultSet r = baseDatos.db.newQuery(query);
			//JCGE: Cuando acabemos todo pues cerramos esta marinola
			limpiarEntradas();
			this.setVisible(false);
			this.dispose();
		}
		if (boton == "Cancelar")
		{
			limpiarEntradas();
		}
		if (boton == "Salir")
		{
			//JOptionPane.showMessageDialog(null,"Warning: Los cambios no confirmados... no serán guardados.");
			this.setVisible(false);
			this.dispose();
		}		
	}
	private void limpiarEntradas()
	{
		for (HFTextField txt: textos)
		{
			txt.setText("");
		}
		for (HFDateField dte: datePickers)
		{
			dte.resetDate();
		}
		idhuesped.requestFocus();
	}
	private void activarEntradas(boolean enalb)
	{
		for (HFTextField txt: textos)
		{
			txt.setEnabled(!enalb);
		}
		for (HFDateField dte: datePickers)
		{
			dte.setEnabl(!enalb);
		}
		idhuesped.setEnabled(enalb);
		if (!enalb)
		{
			idhuesped.requestFocus();
		}
		else
		{
			textos.get(0).requestFocus();
		}
	}
}
