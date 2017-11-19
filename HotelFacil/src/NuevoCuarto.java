import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class NuevoCuarto extends Menu implements ActionListener
{
	/** 
	 * JCGE 30/10/2017 Nuevo Cuarto
	 * aqui vamos agregar a un nuevo inquilino
	 */
	private static final long serialVersionUID = -137397340528622211L;
	private ArrayList<HFLabel>        etiquetas;
	private ArrayList<HFLabel>        totales;
	private ArrayList<HFLabel>        totalesm;
	private ArrayList<HFTextField>    textos;
	private ArrayList<HFIntegerField> enteros;
	private ArrayList<HFDoubleField>  dobles;
	private ArrayList<HFDateField>    fechas;
	private ArrayList<JButton>        botones;
	private String[]                  labels;
	private JScrollPane               scroll;
	private Font                      fonto;
	private HFDoubleField             adelanto;
	private HFDoubleField             cambio;
	private Agenda                    agenda;
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
		//JCGE: Estas son propiedades para los querys con F3
		enteros.get(1).busqueda = "Habitaciones";
		enteros.get(1).query = " SELECT array_to_string(array_agg(idhabitacion||': '||'Edificio '||edificio||' Número. '||numero_fisico),',') AS habitacion"
							 + "   FROM (SELECT *"
							 + "           FROM habitaciones"
							 + "          ORDER BY idhabitacion) AS foo ";
		enteros.get(1).bloqueaAlEnter(false);
		enteros.get(1).retornaDescripcion(textos.get(0));
		textos.get(0).setEnabled(false);
		//
		enteros.get(2).busqueda = "Huespedes";
		enteros.get(2).query = (" SELECT format('%s: %s %s %s : FN = %s',idhuesped,paterno,materno,nombre,fechanacimiento) "
							  + "   FROM huespedes ");
		enteros.get(2).bloqueaAlEnter(false);
		enteros.get(2).retornaDescripcion(textos.get(1));
		textos.get(1).setEnabled(false);
		//JCGE: Estatus de la habitacion siempre va a estar desabilitado
		textos.get(2).setEnabled(false);
		
		///////////////////////////////////////////////////////////////////////////////
		// Area inferior
		//JCGE: Reiniciamos las medidas para nuestro scroll
		x = 10; y = 200; b = 380; h = 20;
		etiquetas.add(new HFLabel("Huespedes: "));
		etiquetas.get(etiquetas.size()-1).setBounds(x, y, b, h);
		panelCentro.add(etiquetas.get(etiquetas.size()-1));
		y+=35;
		scroll = new JScrollPane();
		scroll.setBounds(x, y, b, 350);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		panelCentro.add(scroll);
		botones.add(new JButton("Existente"));
		botones.add(new JButton("Agregar"));
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
		totales.add(new HFLabel(" Habitación: "));totalesm.add(new HFLabel("  999,999,990.00"));
		totales.add(new HFLabel("  Huéspedes: "));totalesm.add(new HFLabel("  999,999,990.00"));
		totales.add(new HFLabel("  Sub-Total: "));totalesm.add(new HFLabel("  999,999,990.00"));
		totales.add(new HFLabel("Promociones: "));totalesm.add(new HFLabel("  999,999,990.00"));
		totales.add(new HFLabel("        IVA: "));totalesm.add(new HFLabel("  999,999,990.00"));
		totales.add(new HFLabel("      Total: "));totalesm.add(new HFLabel("  999,999,990.00"));
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
		cambio.setBounds(x, y, b, h);
		panelCentro.add(adelanto);
		panelCentro.add(cambio);
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
			if (boton == "Buscar")
			{
				//
			}
			if (boton == "Agregar")
			{
				//
			}
			if (boton == "Eliminar")
			{
				//
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
				//
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
