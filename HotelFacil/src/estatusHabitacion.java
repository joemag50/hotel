import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class estatusHabitacion extends MenuInterno implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5755987432527688807L;
	private HFTextField        estatus;
	private JTextArea          desc;
	private ArrayList<JButton> botones;
	private static ArrayList<HFLabel> etiquetas;
	private static ArrayList<JButton> descrip;
	private JPanel             agenda;
	private ArrayList<JPanel>  dias;
	public static int idhabitacion;
	static Font bigFont = new Font("Ubuntu Mono", Font.BOLD, 18);
	public MainWindow NuevoCuarto;
	public Checkout Checkout;
	private JButton siguiente, anterior;
	private HFLabel mes;
	protected static int MESActual = Integer.parseInt(HFDateField.mes.format(HFDateField.DateActual));
	protected static int ANActual = Integer.parseInt(HFDateField.ejercicio.format(HFDateField.DateActual));
	int habitacionDiaHoy = 0;
	estatusHabitacion(String habitacion)
	{
		//JCGE: Tenemos el numero de habitacion
		idhabitacion = Integer.parseInt(habitacion.split(": ")[1]);
		
		//JCGE: Propiedades de la ventana
		this.setTitle("Estatus Habitación");
		//this.setExtendedState(NORMAL);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//this.setLocationRelativeTo(null);
		
		//panelCentro.setLayout(null);
		//menuBar.setVisible(false);
		//String[] nombres = {"Salir"};
		//rellenaToolBar(nombres, this);
		
		//JCGE: Propiedades adentro de la ventana
		int x = 30, y = 30, width = 300, height = 30;
		
		//JCGE: Numero de la habitacion
		HFLabel habi = new HFLabel("Habitación: "+idhabitacion);
		habi.setFont(bigFont);
		habi.setBounds(x, y, width, height); y+=35;
		
		//JCGE: Estatus de la habitacion
		HFLabel lEstatus = new HFLabel("Estatus:");
		lEstatus.setBounds(x, y, width, height); y+=35;
		estatus = new HFTextField(200);
		estatus.setBounds(x, y, width, height); y+=35;
		
		//JCGE: Mas info
		HFLabel lMasInfo = new HFLabel("Mas información: ");
		lMasInfo.setBounds(x, y, width, height);
		y+=35;
		desc = new JTextArea();
		desc.setBounds(x, y, width, 130);
		y+=150;
		
		//JCGE: Botones
		botones = new ArrayList<JButton>();
		botones.add(new JButton("Crear Reservación"));
		botones.add(new JButton("Enviar Limpieza"));
		botones.add(new JButton("Limpieza Terminada"));
		botones.add(new JButton("Cancelar"));
		for (JButton bt: botones)
		{
			bt.setBounds(x, y, width, 50);
			bt.addActionListener(this);
			//panelCentro.add(bt);
			panelInterno.add(bt);
			y+=60;
		}
		botones.get(botones.size()-1).setBackground(Color.RED);
		
		//JCGE: Agenda mamalona
		siguiente = new JButton("Siguiente");
		anterior  = new JButton("Anterior");
		
		HFLabel lAgenda = new HFLabel("Agenda de: ");
		mes       = new HFLabel(HFDateField.meseseses[MESActual-1]+" "+ANActual);
		x+=400; y = 30;
		lAgenda.setBounds(x, y, width, height);
		mes.setBounds(x + 100, y, width, height);
		siguiente.setBounds(x + 500, y, 100, height);
		anterior.setBounds(x+ 300, y, 100, height);
		siguiente.addActionListener(this);
		anterior.addActionListener(this);
		y+=35;
		lAgenda.setFont(bigFont);
		agenda = new JPanel();
		agenda.setLayout(new GridLayout(5,5));
		agenda.setVisible(true);
		agenda.setBounds(x, y, 700, 500);
		
		etiquetas = new ArrayList<HFLabel>();
		descrip = new ArrayList<JButton>();
		dias = new ArrayList<JPanel>();
		
		int dia = 0,
				mez = MESActual,
				ejercicio = ANActual;
		for (int i = 0; i <= 34; i++)
		{
			dia++;
			//JCGE: Metemos lo necesario en cada dia de la agenda
				if ((dia-1 == HFDateField.ducm[mez-1]) || (dia == 30 && mez == 2 && ejercicio % 4 == 0) || (dia == 29 && mez == 2 && ejercicio % 4 != 0))
				{
					dia = 1; mez += 1;
					if ((mez) == 13)
					{
						mez = 1;
						dia = 1;
						ejercicio++;
					}
				}
			etiquetas.add(new HFLabel(""+dia));
			descrip.add(new JButton());
			String fecha = String.format("'%s/%s/%s'", dia, mez, ejercicio);
			String estatus = baseDatos.estatusHabitacion(idhabitacion, fecha);
			descrip.get(descrip.size()-1).setText(estatus);
			descrip.get(descrip.size()-1).setActionCommand(fecha);
			if (Objects.equals(estatus, new String("Ocupada")))
			{
				descrip.get(descrip.size()-1).setBackground(Color.RED);
			}
			else if (Objects.equals(estatus, new String("Libre")))
			{
				descrip.get(descrip.size()-1).setBackground(Color.GREEN);
			}
			else
			{
				descrip.get(descrip.size()-1).setBackground(Color.YELLOW);
			}
			//JCGE: Cuando coincida con la fecha de hoy
			if (Objects.equals(new String(fecha), new String("'"+HFDateField.FechaActual+"'")))
			{
				habitacionDiaHoy = descrip.size()-1;
			}
			dias.add(nuevoDia(etiquetas.get(etiquetas.size()-1),
											descrip.get(descrip.size()-1)));
			agenda.add(dias.get(dias.size()-1));
		}
		
		panelInterno.add(siguiente);
		panelInterno.add(anterior);
		panelInterno.add(mes);
		panelInterno.add(lAgenda);
		panelInterno.add(habi);
		panelInterno.add(agenda);
		panelInterno.add(lMasInfo);
		panelInterno.add(desc);
		panelInterno.add(lEstatus);
		panelInterno.add(estatus);
		//JCGE: bloqueamos o mostramos dependiendo el permiso
		permisos();
		estatus.setText(baseDatos.estatusHabitacion(idhabitacion, "now()"));
		desc.setText(baseDatos.masInfoHabitacion(idhabitacion, "now()"));
	}
	public static void actualizaAgenda()
	{
		int dia = 0,
				mez = MESActual,
				ejercicio = ANActual;
		for (int i = 0; i <= 34; i++)
		{
			dia++;
			//JCGE: Metemos lo necesario en cada dia de la agenda
				if ((dia-1 == HFDateField.ducm[mez-1] && mez !=2 ) || (dia == 30 && mez == 2 && ejercicio % 4 == 0) || (dia == 29 && mez == 2 && ejercicio % 4 != 0))
				{
					dia = 1; mez += 1;
					if ((mez) == 13)
					{
						mez=1;
						dia=1;
						ejercicio++;
					}
				}
			String fecha = String.format("'%s/%s/%s'", dia, mez, ejercicio);
			String estatus = baseDatos.estatusHabitacion(idhabitacion, fecha);
			etiquetas.get(i).setText(""+dia);
			descrip.get(i).setText(estatus);
			descrip.get(i).setActionCommand(fecha);
			if (Objects.equals(estatus, new String("Ocupada")))
			{
				descrip.get(i).setBackground(Color.RED);
			}
			else if (Objects.equals(estatus, new String("Libre")))
			{
				descrip.get(i).setBackground(Color.GREEN);
			}
			else
			{
				descrip.get(i).setBackground(Color.YELLOW);
			}
		}
	}
	public void permisos()
	{
		String nivel = baseDatos.nivelUsuario();
		if (Objects.equals(nivel, new String("ADM")))
		{
			botones.get(0).setEnabled(true);
			botones.get(1).setEnabled(true);
			botones.get(2).setEnabled(true);
			botones.get(3).setEnabled(true);
		}
		else if (Objects.equals(nivel, new String("SUP")))
		{
			botones.get(0).setEnabled(true);
			botones.get(1).setEnabled(true);
			botones.get(2).setEnabled(true);
			botones.get(3).setEnabled(true);
		}
		else if (Objects.equals(nivel, new String("OPE")))
		{
			botones.get(0).setEnabled(true);
			botones.get(1).setEnabled(true);
			botones.get(2).setEnabled(false);
			botones.get(3).setEnabled(false);
		}
		else if (Objects.equals(nivel, new String("LIM")))
		{
			botones.get(0).setEnabled(false);
			botones.get(1).setEnabled(false);
			botones.get(2).setEnabled(false);
			botones.get(3).setEnabled(true);
		}
		else
		{
			botones.get(0).setEnabled(false);
			botones.get(1).setEnabled(false);
			botones.get(2).setEnabled(false);
			botones.get(3).setEnabled(false);
		}
	}
	public JPanel nuevoDia(HFLabel eti, JButton des)
	{
		JPanel panelsito = new JPanel();
		des.addActionListener(this);
		des.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		panelsito.setLayout(new BoxLayout(panelsito, BoxLayout.Y_AXIS));
		panelsito.setVisible(true);
		panelsito.add(eti);
		panelsito.add(des);
		return panelsito;
	}
	//JCGE: Este metodo es privado, porque solo quiero que aplique para esta clase en especifico
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		String boton = arg0.getActionCommand();
		System.out.println(boton);
		if (boton == "Crear Reservación")
		{
			NuevoCuarto nc = new NuevoCuarto();
			MainWindow.newMenuInterno(nc);
			nc.setSize(1170, 500);
			return;
		}
		if (boton == "Siguiente")
		{
			MESActual++;
			if (MESActual == 13)
			{
				MESActual = 1;
				ANActual++;
			}
			mes.setText(HFDateField.meseseses[MESActual-1]+" "+ANActual);
			actualizaAgenda();
			return;
		}
		if (boton == "Anterior")
		{
			MESActual--;
			if (MESActual == 0)
			{
				MESActual = 12;
				ANActual--;
			}
			mes.setText(HFDateField.meseseses[MESActual-1]+" "+ANActual);
			actualizaAgenda();
			return;
		}
		if (boton == "Enviar Limpieza")
		{
			String query = String.format(" UPDATE habitaciones SET limpiar = TRUE"
									   + "  WHERE idhabitacion = %s ", idhabitacion);
			int res = baseDatos.db.newInsert(query);
			habitacionesGrid.actualizaGrid();
			this.setVisible(false);
			this.dispose();
			return;
		}
		if (boton == "Limpieza Terminada")
		{
			String query = String.format(" UPDATE habitaciones SET limpiar = FALSE"
					   				   + "  WHERE idhabitacion = %s ", idhabitacion);
			int res = baseDatos.db.newInsert(query);
			habitacionesGrid.actualizaGrid();
			this.setVisible(false);
			this.dispose();
			return;
		}
		if (boton == "Cancelar")
		{
			//JOptionPane.showMessageDialog(null,"Warning: Los cambios no confirmados... no serán guardados.");
			this.setVisible(false);
			this.dispose();
			return;
		}
		desc.setText(baseDatos.masInfoHabitacion(idhabitacion, boton));
		estatus.setText(baseDatos.estatusHabitacion(idhabitacion, boton));
		if (Objects.equals(estatus.getText(), new String("Ocupada")))
		{
			Checkout co = new Checkout(boton); //JCGE: en boton tenemos una fecha
			MainWindow.newMenuInterno(co);
			co.setSize(950, 500);
			return;
		}
		return;
	}
}
