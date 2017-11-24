import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;

public class NuevoCuarto extends Menu implements ActionListener
{
	/** 
	 * JCGE 30/10/2017 Nuevo Cuarto
	 * aqui vamos agregar a un nuevo inquilino
	 */
	private static final long serialVersionUID = -137397340528622211L;
	private ArrayList<HFLabel>        etiquetas;
	private ArrayList<HFLabel>        totales;
	private static ArrayList<HFLabel>        totalesm;
	private ArrayList<HFTextField>    textos;
	private ArrayList<HFIntegerField> enteros;
	private ArrayList<HFDoubleField>  dobles;
	private ArrayList<HFDateField>    fechas;
	private ArrayList<JButton>        botones;
	private static JList<String>             huespedes;
	public  static  DefaultListModel<String>  listHuesp;
	private String[]                  labels;
	private JScrollPane               scroll;
	private Font                      fonto;
	private HFDoubleField             adelanto;
	private HFDoubleField             cambio;
	private Agenda                    agenda;
	private altaHuespedes             nuevoHuesped;
	private buscador                  buscador;
	private static Double m_huespedes  = BigDecimal.valueOf(0)
		    .setScale(3, RoundingMode.HALF_UP)
		    .doubleValue();
	private static Double m_habitacion = BigDecimal.valueOf(0)
		    .setScale(3, RoundingMode.HALF_UP)
		    .doubleValue();
	private static Double m_total      = BigDecimal.valueOf(0)
		    .setScale(3, RoundingMode.HALF_UP)
		    .doubleValue();
	private boolean esMayor = false;
	private boolean menorHoy = false;
	private static int dias = 0; 
	NuevoCuarto()
	{
		this.setTitle("Nuevo Cuarto");
		String[] nombres = {"Agenda","Guardar","Cancelar","Menu"};
		rellenaToolBar(nombres, actionLins);
		
		//JCGE: Propiedades especificas.
		labels = new String[] {" Solicitud: ",  //Idsolicitud
							   "Habitacion: ", //idhabitacion
							   "   Titular: ",	   //idhuesped
							   "          Estatus: ",
							   "Fecha reservación: ", //fecha_reserv
							   "     Fecha inicio: ", //fecha_inicio
							   "Fecha fin: " //fecha_fin
							   };
		etiquetas = new ArrayList<HFLabel>();
		textos    = new ArrayList<HFTextField>();
		enteros   = new ArrayList<HFIntegerField>();
		dobles    = new ArrayList<HFDoubleField>();
		fechas    = new ArrayList<HFDateField>();
		botones   = new ArrayList<JButton>();
		totales   = new ArrayList<HFLabel>();
		totalesm  = new ArrayList<HFLabel>();
		fonto     = new Font("Ubuntu Mono", Font.BOLD, 17);
		adelanto  = new HFDoubleField();
		cambio    = new HFDoubleField();
		listHuesp = new DefaultListModel<String>();
		///////////////////////////////////////////////////////////////////////////////
		// Area superior
		int x = 10, y = 70, b = 200, h = 20, espacio = 10;
		for (int i = 0; i<=labels.length -1; i++)
		{
			etiquetas.add(new HFLabel(labels[i]));
			if (i <= 2)
			{
				enteros.add(new HFIntegerField());
				enteros.get(enteros.size()-1).setBounds(x+90,y,100,h);
				if (i >= 1)
				{
					textos.add(new HFTextField(200));
					textos.get(textos.size()-1).setBounds(x+200,y,b,h);
					//JCGE: Si es el del nombre lo hacemos mas grande
					if (textos.size()-1 == 1)
						textos.get(textos.size()-1).setBounds(x+200,y,b + 85,h);
					panelCentro.add(textos.get(textos.size()-1));
				}
				if (i == 2)
				{
					dobles.add(new HFDoubleField());
					dobles.get(dobles.size()-1).setBounds(x+410,y-35,75,h);
					dobles.get(dobles.size()-1).setEnabled(false);
					panelCentro.add(dobles.get(dobles.size()-1));
				}
				etiquetas.get(i).setBounds(x, y, b, h);
				panelCentro.add(enteros.get(enteros.size()-1));
				y+=35;
				if (i == 2)
					y = 70;
			}
			
			if (i > 2)
			{
				x=b*2+100+espacio;
				if (i == 6)
				{
					x+=(b+140+espacio);
					y-=35;
				}
				if (i == 3)
				{
					textos.add(new HFTextField(150));
					textos.get(textos.size()-1).setBounds(x + 145,y,b,h);
					panelCentro.add(textos.get(textos.size()-1));
				}
				if (i > 3)
				{
					fechas.add(new HFDateField());
					if (i == 6)
					{
						fechas.get(fechas.size()-1).setBounds(x + 95,y,b,h);
					}
					else
					{
						fechas.get(fechas.size()-1).setBounds(x + 145,y,b,h);
					}
					panelCentro.add(fechas.get(fechas.size()-1));
				}
				etiquetas.get(i).setBounds(x, y, b, h);
				y+=35;
			}
			panelCentro.add(etiquetas.get(i));
		}
		//JCGE: Este siempre va en falso porque es informativo
		//Pero buscamos que numero de solicitud tiene que tener
		enteros.get(0).setEnabled(false);
		try
		{
			ResultSet res = baseDatos.db.newQuery(" SELECT COALESCE(MAX(idsolicitud),0) + 1 FROM hospedajes ");
			if (res.next())
			{
				System.out.println(res.getString(1));
				enteros.get(0).setText(res.getString(1));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		//JCGE: Estas son propiedades para los querys con F3
		enteros.get(1).busqueda = "Habitaciones";
		enteros.get(1).query = " SELECT array_to_string(array_agg(idhabitacion||': '||"
							 + "                                  'Edificio '||edificio||' Número. '||numero_fisico||"
							 + "                                  ' : '||tabulacion),',') AS habitacion"
							 + "   FROM (SELECT *"
							 + "           FROM habitaciones"
							 + "          ORDER BY idhabitacion) AS foo ";
		enteros.get(1).bloqueaAlEnter(false);
		enteros.get(1).retornaDescripcion(textos.get(0));
		enteros.get(1).retornaDescripcion2(dobles.get(0));
		enteros.get(1).addFocusListener(new FocusListener()
			{
				@Override
				public void focusLost(FocusEvent arg0)
				{
					//JCGE: Vamos a hacer lo mismo que el F3 de este boton
					ResultSet r;
					try {
						r = baseDatos.db.newQuery(String.format(" SELECT 'Edificio '||edificio||' Número. '||numero_fisico AS nombre, *  "
															  + "   FROM habitaciones "
															  + "  WHERE idhabitacion = %s ", enteros.get(1).getText()));
						if (r.next())
						{
							textos.get(0).setText(r.getString("nombre"));
							dobles.get(0).setValue(r.getString("tabulacion"));
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//JCGE: Vamos a tomar el valor de la habitacion y se lo sumamos
					Double costo = Double.valueOf(dobles.get(0).getText());
					m_habitacion = costo * dias;
					actualizaPrecios();
				}
				@Override
				public void focusGained(FocusEvent arg0) {/*JCGE: Aun sin nada*/}
			}
		);
		textos.get(0).setEnabled(false);
		//
		enteros.get(2).busqueda = "Huespedes";
		enteros.get(2).query = (" SELECT array_to_string(array_agg(format('%s: %s %s %s : FN = %s', "
				  			  + "                                         idhuesped,paterno,materno,nombre,fechanacimiento)::TEXT),',') "
				  			  + "   FROM huespedes ");;
		enteros.get(2).bloqueaAlEnter(false);
		enteros.get(2).retornaDescripcion(textos.get(1));
		textos.get(1).setEnabled(false);
		//JCGE: Estatus de la habitacion siempre va a estar desabilitado
		textos.get(2).setEnabled(false);textos.get(2).setText("");
		
		//JCGE: La fecha de reservacion se queda bloqueada
		//Este campo tambien es solo informativo
		fechas.get(0).setEnabl(false);
		fechas.get(1).addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				//JCGE: Vamos a revisar que la fecha inicial no sea menor a hoy
				ResultSet r;
				menorHoy = false;
				try {
					r = baseDatos.db.newQuery(String.format(" SELECT '%s'::DATE < now()::DATE ", fechas.get(1).getDate()));
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
					fechas.get(1).resetDate();
				}
				fechas.get(2).setDate(fechas.get(1).getDate());
			}
		});
		fechas.get(2).addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				//JCGE: Como solo es un solo boton para esta marinola
				//No tenemos que buscar, solo validamos que tranza
				//La fecha final tiene que ser mayor a la fecha inicial
				//Nos vamos a apoyar con SQL
				ResultSet r, t;
				esMayor = false;
				dias = 0;
				try {
					r = baseDatos.db.newQuery(String.format(" SELECT '%s'::DATE > '%s'::DATE ", fechas.get(1).getDate(),fechas.get(2).getDate()));
					if (r.next())
					{
						esMayor = r.getBoolean(1);
						if (!esMayor)
						{
							t = baseDatos.db.newQuery(String.format(" SELECT '%s'::DATE - '%s'::DATE ", fechas.get(2).getDate(),fechas.get(1).getDate()));
							if (t.next())
							{
								dias = t.getInt(1);
							}
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//JCGE: La fecha inicial es mayor a la final
				if (esMayor)
				{
					JOptionPane.showMessageDialog(null, "La fecha inicial es mayor a la fecha final, Favor de corregirlo.");
					fechas.get(2).setDate(fechas.get(1).getDate());
				}
				Double costo = Double.valueOf(dobles.get(0).getText());
				m_habitacion = costo * dias;
				actualizaPrecios();
			}
			
		});
		///////////////////////////////////////////////////////////////////////////////
		// Area inferior
		//JCGE: Reiniciamos las medidas para nuestro scroll
		x = 10; y = 200; b = 380; h = 20;
		etiquetas.add(new HFLabel("Huespedes: "));
		etiquetas.get(etiquetas.size()-1).setBounds(x, y, b, h);
		panelCentro.add(etiquetas.get(etiquetas.size()-1));
		y+=35;
		//JCGE: Jlist verguero
		huespedes = new JList<String>(listHuesp);
		scroll = new JScrollPane(huespedes);
		scroll.setBounds(x, y, b, 350);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panelCentro.add(scroll);
		botones.add(new JButton("Existente"));
		botones.add(new JButton("Nuevo"));
		botones.add(new JButton("Eliminar"));
		x += (b + 10);
		for (JButton bj: botones)
		{
			bj.addActionListener(actionLins);
			bj.setEnabled(true);
			bj.setBounds(x, y, 100, h);
			panelCentro.add(bj);
			y+=25;
		}
		totales.add(new HFLabel(" Habitación: "));totalesm.add(new HFLabel(String.format("%16s","0.00")));
		totales.add(new HFLabel("  Huéspedes: "));totalesm.add(new HFLabel(String.format("%16s","0.00")));
		totales.add(new HFLabel("  Sub-Total: "));totalesm.add(new HFLabel(String.format("%16s","0.00")));
		totales.add(new HFLabel("        IVA: "));totalesm.add(new HFLabel(String.format("%16s","0.00")));
		totales.add(new HFLabel("      Total: "));totalesm.add(new HFLabel(String.format("%16s","0.00")));
		totales.add(new HFLabel("   Adelanto: "));
		totales.add(new HFLabel("     Cambio: "));
		x = 530; y = 300; b = 150;
		for(HFLabel lb: totales)
		{
			lb.setBounds(x, y, b, h);
			lb.setFont(fonto);
			panelCentro.add(lb);
			y+=35;
		}
		x+=160; y = 300;
		for(HFLabel lb: totalesm)
		{
			lb.setBounds(x, y, b, h);
			lb.setFont(fonto);
			panelCentro.add(lb);
			y+=35;
		}
		adelanto.setBounds(x, y, b, h); y+=35;
		adelanto.addFocusListener(new FocusListener()
		{
			@Override
			public void focusLost(FocusEvent e)
			{
				// JCGE: vamos a poner cuanto le tiene que dar de cambio
				Double m_adelanto = Double.valueOf(adelanto.getText());
				if (m_total < m_adelanto)
				{
					cambio.setValue(m_adelanto - m_total);
				}
			}
			@Override
			public void focusGained(FocusEvent e) {/*JCGE: Aun sin nada*/}
		});
		cambio.setBounds(x, y, b, h);
		cambio.setEnabled(false);
		panelCentro.add(adelanto);
		panelCentro.add(cambio);
		//JCGE: lo del estatus lo dejaremos pendiente
		etiquetas.get(3).setVisible(false);
		textos.get(2).setVisible(false);
	}
	public static void actualizaPrecios()
	{
		Double habitacion = BigDecimal.valueOf(m_habitacion).setScale(3, RoundingMode.HALF_UP).doubleValue();
		Double huesped = BigDecimal.valueOf(m_huespedes).setScale(3, RoundingMode.HALF_UP).doubleValue();
		Double iva =  BigDecimal.valueOf( 0.16 * (m_huespedes+m_habitacion) ).setScale(3, RoundingMode.HALF_UP).doubleValue();
		Double total = BigDecimal.valueOf( iva + (huesped+habitacion) ).setScale(3, RoundingMode.HALF_UP).doubleValue();
		
		totalesm.get(0).setText(String.format("%16s",(String.valueOf( habitacion ))));
		totalesm.get(2).setText(String.format("%16s",(String.valueOf( huesped + habitacion ))));
		totalesm.get(3).setText(String.format("%16s",(String.valueOf( iva ))));
		totalesm.get(4).setText(String.format("%16s",(String.valueOf( total ))));
	}
	//JCGE: funcion para rellenar la lista, Actualizamos los montos
	public static void nuevoHuesped(String e)
	{
		listHuesp.addElement(e);
		String fechanac = (e.split(": FN = "))[1];
		String query = String.format(" SELECT hotel_precioxpersona('%s'::DATE) ", fechanac);
		ResultSet r ;
		Double costo = 0.00;
		try {
			r = baseDatos.db.newQuery(query);
			if (r.next())
			{
				costo = r.getDouble(1);
			}
			else
			{
				costo = 0.00;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		m_huespedes  += costo;
		actualizaPrecios();
	}
	public static boolean existeHuesped(String huesped)
	{
        for(int i = 0; i< huespedes.getModel().getSize();i++)
        {
            //System.out.println(huespedes.getModel().getElementAt(i));
        	if (Objects.equals(huespedes.getModel().getElementAt(i), new String(huesped)))
        		return true;
        }
        return false;
	}
	private void limpiarEntradas(boolean e)
	{
		for (HFIntegerField in: enteros)
		{
			//Solicitud
			//Habitación
			//Titular
			in.setText("0");
		}
		//JCGE: Basicamente es informativo, 
		for (HFTextField tx: textos)
		{
			//Nombre de la habitacion
			//Nombre del Titular
			//Estatus de la habitacion
			tx.setText("");
		}
		dobles.get(0).setText("0.00"); //Precio de la habitacion
		for (HFDateField dt: fechas)
		{
			//Fecha de Reservación
			//Fecha de inicio
			//Fecha de fin
			dt.resetDate();
		}
	}
	//JCGE: Este metodo es privado, porque solo quiero que aplique para esta clase en especifico
	private ActionListener actionLins = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			String boton = arg0.getActionCommand();
			System.out.println(boton);
			////////////////////////////////////////////////////////////
			// JCGE: Botones para el SCROLL
			if (boton == "Existente")
			{
				//
				String busqueda = "Huespedes";
				String query = (" SELECT array_to_string(array_agg(format('%s: %s %s %s : FN = %s', "
							  + "                                         idhuesped,paterno,materno,nombre,fechanacimiento)),',') "
								 + "   FROM huespedes ");
				buscador = new buscador(busqueda, query, huespedes);
			}
			if (boton == "Nuevo")
			{
				//
				nuevoHuesped = new altaHuespedes();
				nuevoHuesped.menuBar.setVisible(false);
				nuevoHuesped.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				nuevoHuesped.botonera.get(3).setVisible(false);
				nuevoHuesped.panelSur.setVisible(false);
				nuevoHuesped.finGUI();
				nuevoHuesped.setSize(600, 400);
			}
			if (boton == "Agregar")
			{
				//
			}
			if (boton == "Eliminar")
			{
				//
				if (huespedes.getSelectedIndex() != -1)
				{
					String fechanac = listHuesp.get(huespedes.getSelectedIndex()).split(": FN = ")[1];
					String query = String.format(" SELECT hotel_precioxpersona('%s'::DATE) ", fechanac);
					ResultSet r ;
					Double costo = 0.00;
					try {
						r = baseDatos.db.newQuery(query);
						if (r.next())
						{
							costo = r.getDouble(1);
						}
						else
						{
							costo = 0.00;
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					m_huespedes  -= costo;
					actualizaPrecios();
					listHuesp.remove(huespedes.getSelectedIndex());	
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Favor de seleccionar un registro.");
				}
			}
			////////////////////////////////////////////////////////////
			// JCGE: Botones de toolbar
			if (boton == "Agenda")
			{
				//JCGE: Vamos a mostrar una ventana con la agenda actual
				agenda = new Agenda();
				agenda.finGUI();
			}
			if (boton == "Guardar")
			{
				//JCGE:
				limpiarEntradas(true);
			}
			if (boton == "Cancelar")
			{
				//JCGE: Limpiamos todo el mugrero
				limpiarEntradas(true);
			}
			////////////////////////////////////////////////////////////
			// JCGE: Boton para salir
			if (boton == "Menu")
			{
				//JOptionPane.showMessageDialog(null,"Warning: Los cambios no confirmados... no serán guardados.");
				Principal.ventana.setVisible(false);
				Principal.ventana.dispose();
				Principal.ventana = new Menu();
				Principal.ventana.finGUI();
			}
		}
		
	};
}
