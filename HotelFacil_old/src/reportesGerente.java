import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class reportesGerente extends Menu implements ActionListener
{
	/**
	 * JCGE: Modulo de reportes del gerente
	 * Modulo donde se van a hacer las consultas de la operacion
	 */
	private static final long serialVersionUID = -409169272934172530L;

	reportesGerente()
	{
		this.setTitle("Reportes del Gerente");
		String[] nombres = {"Nuevo","Guardar","Cancelar","Menu"};
		rellenaToolBar(nombres, actionLins);
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
