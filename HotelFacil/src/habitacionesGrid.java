import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class habitacionesGrid extends MenuInterno implements ActionListener
{
	/**
	 * JCGE: Modulo de habitacionesGrid
	 * 
	 */
	private static final long serialVersionUID = -1400423738500557806L;
	public static ArrayList<JButton> botones ;
	public estatusHabitacion estHab;
	public MenuInterno ventanitas;
	habitacionesGrid()
	{
		//JCGE: Propiedades de ventana
		this.setTitle("Menu");
		this.setClosable(false);
		//this.setExtendedState(MAXIMIZED_BOTH);
		//this.setLocationRelativeTo(null);
		//panelCentro.setLayout(null);
		//toolBar.setVisible(false);
		
		//JCGE: Propiedades especificas
		JPanel p = new JPanel(new GridLayout(3, 3));
		p.setBackground(Color.WHITE);
		botones = new ArrayList<JButton>();
		HFLabel piso = new HFLabel("PISO: 1");
		piso.setBounds(200, 50, 300, 40);
		piso.setFont(new Font("Ubuntu Mono", Font.BOLD, 30));
		panelInterno.add(piso);
		int j = 1;
        for (int i = 0; i < 3 * 5; i++) {
            if (i >= 4 && i <= 9)
            {
            	HFLabel etiqueta;
            	if (i == 4)
            	{
                	etiqueta = new HFLabel("      Elevador");
            	}
            	else
            	{
                	etiqueta = new HFLabel(" ");
            	}
            	p.add(etiqueta);
            }
            else
            {
            	botones.add(new JButton("Habitacion: " + j));
            	botones.get(botones.size()-1).setBackground(MainWindow.colores.get(4));
            	botones.get(botones.size()-1).addActionListener(actionLins);
				String query = String.format(" SELECT limpiar FROM habitaciones WHERE idhabitacion = %s ", j);
				ResultSet r = baseDatos.db.newQuery(query);
				try {
					if (r.next())
					{
						if (r.getBoolean(1))
						{
							botones.get(botones.size()-1).setBackground(Color.ORANGE);
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                p.add(botones.get(botones.size()-1));
                j++;
            }
        }
        p.setVisible(true);
        p.setBounds(200,100, 1000,600);
        panelInterno.add(p);
	}
	public static void actualizaGrid()
	{
		int j = 1;
		for (JButton bt: botones)
		{
			bt.setBackground(MainWindow.colores.get(4));
			String query = String.format(" SELECT limpiar FROM habitaciones WHERE idhabitacion = %s ", j);
			ResultSet r = baseDatos.db.newQuery(query);
			try {
				if (r.next())
				{
					if (r.getBoolean(1))
					{
						bt.setBackground(Color.ORANGE);
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			j++;
		}
	}
	//JCGE: Este metodo es privado, porque solo quiero que aplique para esta clase en especifico
	private ActionListener actionLins = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			String boton = arg0.getActionCommand();
			//System.out.println(boton);
			//JCGE: Cuando de click siempre va a mostrar
			MainWindow.newMenuInterno(new estatusHabitacion(boton));
			//estHab.finGUI();
			//estHab.setWindowSize(estHab, 400, 200);
			//estHab.setSize(MainWindow.WIDTH.intValue()-400, MainWindow.HEIGHT.intValue()-200);
		}
		
	};
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
