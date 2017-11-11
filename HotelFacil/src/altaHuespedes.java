import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class altaHuespedes extends Menu implements ActionListener
{
	/**
	 * JCGE: Modulo de alta de huespedes
idhuesped           | integer                    
paterno             | text                       
materno             | text                       
nombre              | text                       
fechanacimiento     | date                       
telefono            | text                       
tarjeta             | text                       
secret_num          | text                       
ultimaactualizacion | timestamp without time zone

	 */
	private static final long serialVersionUID = -820968874016456007L;
	private ArrayList<HFLabel> etiquetas;
	private ArrayList<HFTextField> textos;
	private String[] labels = {"Num huesped: ","Paterno: ","Materno: ",
								"Nombre: ","Fecha Nacimiento: ","Telefono: ",
								"Tarjeta: ","Num Seguridad: ","Ultima Actualización: "};
	altaHuespedes()
	{
		this.setTitle("Alta de Huespedes");
		String[] nombres = {"Nuevo","Guardar","Cancelar","Menu"};
		rellenaToolBar(nombres, actionLins);
		
		//JCGE:
		etiquetas = new ArrayList<HFLabel>();
		textos    = new ArrayList<HFTextField>();
		for (int i = 0; i <= labels.length -1; i++)
		{
			//
			etiquetas.add(new HFLabel(labels[i]));
			textos.add(new HFTextField(200));
		}
		int x = 10, y = 70, b = 200, h = 20;
		for (HFLabel lbl: etiquetas)
		{
			lbl.setBounds(x,y,b,h);
			panelCentro.add(lbl);
			y+=25;
		}
		x = b + 10; y = 70; b = 200; h = 20;
		for (HFTextField txt: textos)
		{
			txt.setBounds(x,y,b,h);
			panelCentro.add(txt);
			y+=25;
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
			if (boton == "Nuevo")
			{
				//
			}
			if (boton == "Guardar")
			{
				//
			}
			if (boton == "Cancelar")
			{
				//
			}
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
