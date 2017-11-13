import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class NuevoCuarto extends Menu implements ActionListener
{
	/** 
	 * JCGE 30/10/2017 Nuevo Cuarto
	 * aqui vamos agregar a un nuevo inquilino
	 */
	private static final long serialVersionUID = -137397340528622211L;
	private ArrayList<HFLabel> etiquetas;
	private String[] labels;
	NuevoCuarto()
	{
		this.setTitle("Nuevo Cuarto");
		String[] nombres = {"Nuevo","Guardar","Cancelar","Menu"};
		rellenaToolBar(nombres, actionLins);
		
		//JCGE: Propiedades especificas.
		labels = new String[] {"ejemplo"};
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
		}
		
	};
}
