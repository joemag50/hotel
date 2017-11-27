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
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class NuevoCuarto extends MenuInterno implements ActionListener
{
	/**
	 * JCGE: Modulo de Checkout
	 * Aqui vamos a despedir a nuestro inquilino o alargarle la visita
	 */
	private   static final long               serialVersionUID = -4467762444239765332L;
	public    static HFIntegerField           idhuesped;
	protected static ArrayList<HFDateField>   fechas;
	protected static HFIntegerField           huespedes;
	private   static boolean                  esMayor = false;
	private   static int                      dias = 0;
	
	protected ArrayList<HFTextField> textos;
	protected ArrayList<HFDateField> datePickers;
	protected JButton                buscar, guardar, cancelar;
	protected JCheckBox              esNuevo;
	protected HFLabel lPersonas = new HFLabel("       Huespedes: "),
					  lFechaIni = new HFLabel("    Fecha Inicio: "),
					  lFechaFin = new HFLabel("       Fecha Fin: "),
					  lcob      = new HFLabel("Cobros");
	private   ArrayList<HFLabel>     etiquetas;
	private   ArrayList<ResultSet>   r;
	protected   JScrollPane            scroll;
	private   boolean                menorHoy = false;
	private   String[]               labels = {"     Num huesped: ",
			 								   "         Paterno: ",
			 								   "         Materno: ",
			 								   "          Nombre: ",
			 								   "Fecha Nacimiento: ",
			 								   "        Telefono: ",
			 								   "         Tarjeta: ",
											   "   Num Seguridad: "};
	protected String[] columnas = {"Concepto","Cantidad", "P. Unitario","Total","Abonado"};
	//JCGE: Da la tremenda casualidad que soy el que decide que cobros poner
	protected static Object[][] data;
	protected static JTable tabla;
	NuevoCuarto()
	{
		data = new Object[][] {{"Dias", 0.0, 0.0, 0.0,0.0},{"Huespedes", 0.0, 0.0, 0.0,0.0}};
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
		int x = 10, y = 40, b = 200, h = 20;
		for (HFLabel lbl: etiquetas)
		{
			lbl.setBounds(x,y,b,h);
			panelInterno.add(lbl);
			y+=35;
		}
		x = b + 10; y = 40;
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
				idhuesped.query = (" SELECT array_to_string(array_agg(format('%s: %s %s %s : FN = %s',idhuesped,paterno,materno,nombre,fechanacimiento)),',') "
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
				actualizaCobros();
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
				//JCGE: La fecha inicial es mayor a la final
				if (esMayor)
				{
					JOptionPane.showMessageDialog(null, "La fecha inicial es mayor a la fecha final, Favor de corregirlo.");
					fechas.get(1).setDate(fechas.get(0).getDate());
				}
				actualizaCobros();
			}
		});
		panelInterno.add(lPersonas);
		panelInterno.add(lFechaIni);
		panelInterno.add(lFechaFin);
		
		//JCGE: Ponemos la lista de los cobros que se van a hacer
		x += 600; y = 30;
		lcob.setFont(estatusHabitacion.bigFont);
		panelInterno.add(lcob);
		lcob.setBounds(x, y, 200, 35); y+=35;
		tabla = new JTable(data, columnas);
		scroll = new JScrollPane(tabla);
		scroll.setBounds(x, y, 500, 200);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panelInterno.add(scroll);
		
		//JCGE: Botones de guardar y cancelar
		guardar     = new JButton("Guardar");
		cancelar    = new JButton("Volver");
		guardar.setFont(new Font("Ubuntu Mono", Font.BOLD, 15));
		cancelar.setFont(new Font("Ubuntu Mono", Font.BOLD, 15));
		guardar.setBackground(Color.GREEN);
		cancelar.setBackground(Color.RED);
		guardar.addActionListener(this);
		cancelar.addActionListener(this);
		y+=250;
		guardar.setBounds(x, y, 250, 60);
		x+=240;
		cancelar.setBounds(x, y, 250, 60);
		panelInterno.add(guardar);
		panelInterno.add(cancelar);
		limpiarEntradas();
		activarEntradas(true);
	}
	public static boolean existeConcepto(Object[] concepto)
	{
        for(int i = 0; i< data.length;i++)
        {
        	if (Objects.equals(tabla.getModel().getValueAt(i, 0), new String(concepto[0]+"")))
        		return true;
        }
        return false;
	}
	protected static void nuevoCobro(Object[] e)
	{
		if (existeConcepto(e))
		{
			for(int i = 0; i< data.length;i++)
	        {
				if (Objects.equals(tabla.getModel().getValueAt(i, 0), new String(e[0]+"")))
				{
					tabla.getModel().setValueAt(e[0], i, 0);
					tabla.getModel().setValueAt(e[1], i, 1);
					tabla.getModel().setValueAt(e[2], i, 2);
					tabla.getModel().setValueAt(e[3], i, 3);
					tabla.getModel().setValueAt(e[4], i, 4);
				}
	        }
		}
	}
	public static void actualizaCobros()
	{
		//JCGE: Vamos a revisar los conceptos
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
						dias = t.getInt(1) + 1;
						s = baseDatos.db.newQuery(String.format(" SELECT tabulacion FROM habitaciones WHERE idhabitacion = %s ", estatusHabitacion.idhabitacion));
						if (s.next())
						{
							nuevoCobro(new Object[] {"Dias", new Integer(dias), new Double(s.getDouble(1)), new Double(s.getDouble(1)*dias), 0.0});
						}
					}
				}
			}
		} catch (SQLException et) {
			// TODO Auto-generated catch block
			et.printStackTrace();
			return;
		}
		Object[] concepto = {"Huespedes", new Integer(huespedes.getText()), new Double(500), new Double(500 * Integer.parseInt(huespedes.getText())), 0.0};
		nuevoCobro(concepto);
	}
	//JCGE: 
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		String boton = arg0.getActionCommand();
		//System.out.println(boton);
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
			int respuesta = JOptionPane.showConfirmDialog(this,
				    "¿Está totalmente seguro que desea realizar la siguiente operación?",
				    "Confirmación",
				    JOptionPane.YES_NO_OPTION);
			if (respuesta == 1)
			{
				return;
			}
			//JCGE: Revisamos que la info no este vacia
			int j = 0;
			for (HFTextField txt: textos)
			{
				if (!(j == 4 || j == 0))
				{
					if (txt.getText().trim().length() == 0)
					{
						JOptionPane.showMessageDialog(null,"Favor de completar todos los campos.");
						return;
					}
				}
				j++;
			}
			//JCGE: Actualizamos los cobros
			actualizaCobros();
			if (data.length != 2)
			{
				JOptionPane.showMessageDialog(null,"Favor de asignar los cobros correspondientes.");
				return;
			}
			//JCGE: Para evitar que un cobro se haga y otro no, los revisamos ANTES de insertar
			for(int i = 0; i< data.length;i++)
	        {
	        	if (Objects.equals(data[i][3], new Double("0")))
	        	{
	        		JOptionPane.showMessageDialog(null,"Los cobros tienen que ser mayor $0");
	        		return;
	        	}
	        }
			String query = String.format("SELECT * FROM hotel_habitacion_estatus_rango(%s,'%s'::DATE,'%s'::DATE)",
					estatusHabitacion.idhabitacion, fechas.get(0).getDate(), fechas.get(1).getDate());
			ResultSet r = baseDatos.db.newQuery(query);
			try {
				if (r.next())
				{
					String res = r.getString(1);
					//System.out.println(res);
					if (Objects.equals(r.getString(1).split(":")[0], new String("Error")))
					{
						JOptionPane.showMessageDialog(null,r.getString(1));
						return;
					}
					else if (Objects.equals(r.getString(1).split(":")[0], new String("Ocupada")))
					{
						JOptionPane.showMessageDialog(null,r.getString(1));
						return;
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			query = String.format("SELECT *"
								+ "  FROM hospedajes"
								+ " WHERE (estatus,idhuesped) = (1,%s) AND idhabitacion <> %s",
						idhuesped.getText(),
						estatusHabitacion.idhabitacion);
			r = baseDatos.db.newQuery(query);
			try {
				if (r.next())
				{
					JOptionPane.showMessageDialog(null,"El huesped esta siendo alojado en otra habitación.");
					return;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int idhue = Integer.parseInt(idhuesped.getText());
			if (esNuevo.isSelected())
			{
				//JCGE: Lo insertamos
				query = String.format("INSERT INTO huespedes VALUES (DEFAULT, '%s','%s','%s', "
						+ "                               '%s'::DATE, '%s', '%s', '%s', now()::DATE) ",
						textos.get(1).getText(),
						textos.get(2).getText(),
						textos.get(3).getText(),
						datePickers.get(0).getDate(),
						textos.get(5).getText(),
						textos.get(6).getText(),
						textos.get(7).getText());
				int res1 = baseDatos.db.newInsert(query);
				//JCGE: Luego buscamos cual es
				query = String.format("SELECT *"
									+ "  FROM huespedes"
									+ " WHERE (paterno,materno,nombre,fechanacimiento,ultimaactualizacion) ="
									+ "       ('%s','%s','%s','%s'::DATE,now()::DATE) ",
						textos.get(1).getText(),
						textos.get(2).getText(),
						textos.get(3).getText(),
						datePickers.get(0).getDate());
				r = baseDatos.db.newQuery(query);
				try {
					if (r.next())
					{
						idhuesped.setText(r.getString("idhuesped"));
						idhue = r.getInt("idhuesped");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int estatus = 0;
			if (Objects.equals(HFDateField.FechaActual, new String(fechas.get(0).getDate())))
				estatus++; //JCGE: Es activa desde hoy
			query = String.format("INSERT INTO hospedajes VALUES (DEFAULT, %s, %s, %s,"
								+ "                               now()::DATE, '%s'::DATE, '%s'::DATE, '%s') ",
								estatusHabitacion.idhabitacion,
								idhue,
								estatus,
								fechas.get(0).getDate(),
								fechas.get(1).getDate(),
								baseDatos.user_actual);
			int res2 = baseDatos.db.newInsert(query);
			//JCGE: Nos falta agregarle a la cuenta lo que nos debe este puto
			//JCGE: Necesitamos el numero de la solicitud
			query = String.format("SELECT idsolicitud "
								+ "  FROM hospedajes WHERE (idhabitacion,idhuesped,estatus,fecha_inicio,fecha_fin,idusuario) = "
								+ "                        (%s, %s, %s, '%s'::DATE, '%s'::DATE, '%s') ",
					estatusHabitacion.idhabitacion,
					idhue,
					estatus,
					fechas.get(0).getDate(),
					fechas.get(1).getDate(),
					baseDatos.user_actual);
			r = baseDatos.db.newQuery(query);
			int idsolicitud = 0;
			try {
				if (r.next())
				{
					idsolicitud = r.getInt(1);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i = 0; i< data.length;i++)
	        {
	        	query = String.format(" INSERT INTO pagos VALUES (DEFAULT, %s, %s, 0, '%s', %s, %s, '%s')",
	        				idsolicitud,
	        				tabla.getModel().getValueAt(i, 3), //precio Total
	        				tabla.getModel().getValueAt(i, 0), //concepto
	        				tabla.getModel().getValueAt(i, 1), //cantidad
	        				tabla.getModel().getValueAt(i, 2), //precio unitario
	        				baseDatos.user_actual);
	        	int res3 = baseDatos.db.newInsert(query);
	        }
			baseDatos.logInsert(baseDatos.user_actual, "Nueva reservacion", "Nuevo Cuarto");
			JOptionPane.showMessageDialog(null,"Guardado correctamente: ");
			//JCGE: Cuando acabemos todo pues cerramos esta marinola
			limpiarEntradas();
			estatusHabitacion.actualizaAgenda();
			this.setVisible(false);
			this.dispose();
		}
		if (boton == "Volver")
		{
			limpiarEntradas();
			estatusHabitacion.actualizaAgenda();
			this.setVisible(false);
			this.dispose();
		}
	}
	protected void limpiarEntradas()
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
