import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private ArrayList<HFLabel> etiquetas;
	private ArrayList<JTextArea> descrip;
	private JPanel             agenda;
	private ArrayList<JPanel>  dias;
	public static int idhabitacion;
	private Font bigFont = new Font("Ubuntu Mono", Font.BOLD, 18);
	public MainWindow NuevoCuarto;
	public Checkout Checkout;
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
		int x = 30, y = 80, width = 300, height = 30;
		
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
		desc.setText(baseDatos.masInfoHabitacion(idhabitacion));
		y+=150;
		
		//JCGE: Botones
		botones = new ArrayList<JButton>();
		botones.add(new JButton("Crear Reservación"));
		botones.add(new JButton("Check Out"));
		botones.add(new JButton("Enviar Limpieza"));
		botones.add(new JButton("Limpieza Terminada"));
		for (JButton bt: botones)
		{
			bt.setBounds(x, y, width, 50);
			bt.addActionListener(this);
			//panelCentro.add(bt);
			panelInterno.add(bt);
			y+=60;
		}
		
		//JCGE: Agenda mamalona
		HFLabel lAgenda = new HFLabel("Agenda");
		x+=400; y = 80;
		lAgenda.setBounds(x, y, width, height); y+=35;
		lAgenda.setFont(bigFont);
		agenda = new JPanel();
		agenda.setLayout(new GridLayout(5,5));
		agenda.setVisible(true);
		agenda.setBounds(x, y, 700, 500);
		
		etiquetas = new ArrayList<HFLabel>();
		descrip = new ArrayList<JTextArea>();
		dias = new ArrayList<JPanel>();
		for (int i = 0; i <= 30; i++)
		{
			//JCGE: Metemos lo necesario en cada dia de la agenda
			int dia = (i+1),
				mes = Integer.parseInt(HFDateField.mes.format(HFDateField.DateActual)),
				ejercicio = Integer.parseInt(HFDateField.ejercicio.format(HFDateField.DateActual));
			if (dia == HFDateField.ducm[mes-1])
			{
				dia = 1; mes += 1;
				if ((mes-1) == 12)
				{
					ejercicio++;
				}
			}
			etiquetas.add(new HFLabel(""+dia));
			descrip.add(new JTextArea());
			String fecha = String.format("'%s/%s/%s'", dia, mes, ejercicio);
			String estatus = baseDatos.estatusHabitacion(idhabitacion, fecha);
			descrip.get(descrip.size()-1).setText(estatus);
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
			dias.add(nuevoDia(etiquetas.get(etiquetas.size()-1),
											descrip.get(descrip.size()-1)));
			agenda.add(dias.get(dias.size()-1));
		}
		
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
	public JPanel nuevoDia(HFLabel eti, JTextArea des)
	{
		JPanel panelsito = new JPanel();
		des.setSize(10, 10);
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
			MainWindow.newMenuInterno(new NuevoCuarto());
		}
		if (boton == "Check Out")
		{
			Checkout = new Checkout();
			Checkout.finGUI();
			Checkout.setWindowSize(Checkout, 399, 199);
		}
		if (boton == "Enviar Limpieza")
		{
			
		}
		if (boton == "Limpieza Terminada")
		{
			
		}
		if (boton == "Salir")
		{
			//JOptionPane.showMessageDialog(null,"Warning: Los cambios no confirmados... no serán guardados.");
			this.setVisible(false);
			this.dispose();
		}		
	}
}
