import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Agenda extends Menu implements ActionListener
{
	Agenda()
	{
		//
		this.setTitle("Agenda");
		String[] nombres = {"Atras","Siguiente","Actualizar","Salir"};
		rellenaToolBar(nombres, this);
		menuBar.setVisible(false);
		//this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	//JCGE: Este metodo es privado, porque solo quiero que aplique para esta clase en especifico
		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			String boton = arg0.getActionCommand();
			System.out.println(boton);
			if (boton == "Salir")
			{
				//JOptionPane.showMessageDialog(null,"Warning: Los cambios no confirmados... no serán guardados.");
				this.setVisible(false);
				this.dispose();
			}		
		}
}
