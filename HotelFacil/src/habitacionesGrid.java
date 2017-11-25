import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
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
	public ArrayList<JButton> botones ;
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
		botones = new ArrayList<JButton>();
		
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
            	botones.get(botones.size()-1).addActionListener(actionLins);
                p.add(botones.get(botones.size()-1));
                j++;
            }
        }
        p.setVisible(true);
        p.setBounds(150,150,800,400);
        panelInterno.add(p);
	}
	//JCGE: Este metodo es privado, porque solo quiero que aplique para esta clase en especifico
	private ActionListener actionLins = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			String boton = arg0.getActionCommand();
			System.out.println(boton);
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
