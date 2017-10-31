import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Checkout extends Menu implements ActionListener
{
	/**
	 * JCGE: Modulo de Checkout
	 * Aqui vamos a despedir a nuestro inquilino o alargarle la visita
	 */
	private static final long serialVersionUID = -4467762444239765332L;
	
	Checkout()
	{
		this.setTitle("Checkout");
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
