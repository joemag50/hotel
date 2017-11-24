import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class habitacionesGrid extends Menu implements ActionListener
{
	/**
	 * JCGE: Modulo de habitacionesGrid
	 * 
	 */
	public ArrayList<JButton> botones ;
	public estatusHabitacion estHab;
	private static final long serialVersionUID = -1400423738500557806L;
	int N = 4;
	habitacionesGrid()
	{
		this.setTitle("Menu");
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
        panelCentro.add(p);
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
				return;
			}
			//JCGE: Cuando de click siempre va a mostrar
			estHab = new estatusHabitacion(boton);
			estHab.finGUI();
			estHab.setSize(MainWindow.WIDTH.intValue()-400, MainWindow.HEIGHT.intValue()-200);
		}
		
	};
}
