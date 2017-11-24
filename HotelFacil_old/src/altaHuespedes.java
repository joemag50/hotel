import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JOptionPane;

public class altaHuespedes extends Menu implements ActionListener
{
	/**
	 * JCGE: Modulo de alta de huespedes
	 */
	private static final long serialVersionUID = -820968874016456007L;
	private ArrayList<HFLabel>     etiquetas;
	private ArrayList<HFTextField> textos;
	private ArrayList<ResultSet>   r;
	private ArrayList<HFDateField> datePickers;
	public static HFIntegerField  idhuesped;
	private JButton                buscar;
	private String[]               labels = {"Num huesped: ","Paterno: ","Materno: ",
											 "Nombre: ","Fecha Nacimiento: ","Telefono: ",
											 "Tarjeta: ","Num Seguridad: ","Ultima Actualización: "};
	altaHuespedes()
	{
		this.setTitle("Alta de Huespedes");
		String[] nombres = {"Nuevo","Guardar","Cancelar","Menu"};
		rellenaToolBar(nombres, actionLins);
		
		//JCGE:
		etiquetas   = new ArrayList<HFLabel>();
		textos      = new ArrayList<HFTextField>();
		datePickers = new ArrayList<HFDateField>();
		idhuesped   = new HFIntegerField();
		buscar      = new JButton("Buscar");
		
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
		int i = 0;
		for (HFTextField txt: textos)
		{
			if (i == 4 || i == 8)
			{
				datePickers.add(new HFDateField());
				datePickers.get(datePickers.size()-1).setBounds(x,y,b,h);
				panelCentro.add(datePickers.get(datePickers.size()-1));
			}
			else if (i == 0)
			{
				idhuesped.setBounds(x,y,b,h);
				panelCentro.add(idhuesped);
				idhuesped.busqueda = "Huespedes";
				idhuesped.query = (" SELECT format('%s: %s %s %s : FN = %s',idhuesped,paterno,materno,nombre,fechanacimiento) "
								 + "   FROM huespedes ");
				buscar.setBounds(x + 210,y,100,h);
				buscar.setVisible(true);
				buscar.setEnabled(true);
				buscar.addActionListener(actionLins);
				panelCentro.add(buscar);
			}
			else
			{
				txt.setBounds(x,y,b,h);
				panelCentro.add(txt);
			}
			i++;
			y+=25;
		}
		limpiarEntradas(false);
		datePickers.get(1).setEnabl(false);
		botonera.get(0).setEnabled(true);
		botonera.get(1).setEnabled(false);
		botonera.get(2).setEnabled(false);
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
				botonera.get(0).setEnabled(false);
				botonera.get(1).setEnabled(true);
				botonera.get(2).setEnabled(true);
				limpiarEntradas(true);
				String query = " SELECT COALESCE(max(idhuesped),0) +1 FROM huespedes ";
				try
				{
					r = new ArrayList<ResultSet>();
					r.add(baseDatos.db.newQuery(query));
					if (r.get(0).next())
					{
						idhuesped.setText(r.get(0).getString(1));
					}
					else
					{
						idhuesped.setText("0");
					}
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				textos.get(1).requestFocus();
			}
			if (boton == "Buscar")
			{
				try
				{
					r = new ArrayList<ResultSet>();
					r.add(baseDatos.db.newQuery(String.format(" SELECT * "
															+ "   FROM huespedes "
															+ "  WHERE idhuesped = %s ",idhuesped.getText())));
					if (r.get(0).next())
					{
						limpiarEntradas(true);
						int i = 1;
						for (HFTextField txt: textos)
						{
							txt.setText(r.get(0).getString(i));
							i++;
						}
						i = 0;
						for (HFDateField dt: datePickers)
						{
							if (i == 0)
							{
								dt.setDate(r.get(0).getDate("fechanacimiento"));
							}
							else
							{
								dt.setDate(r.get(0).getDate("ultimaactualizacion"));
							}
							i++;
						}
						botonera.get(0).setEnabled(false);
						botonera.get(1).setEnabled(true);
						botonera.get(2).setEnabled(true);
					}
					else
					{
						botonera.get(0).setEnabled(true);
						botonera.get(1).setEnabled(false);
						botonera.get(2).setEnabled(false);
						limpiarEntradas(false);
						JOptionPane.showMessageDialog(null,"El huesped no existe");
					}
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
					limpiarEntradas(false);
				}
			}
			if (boton == "Guardar")
			{
				if(textos.get(1).getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(null, "Favor de completar el campo Apellido Paterno");
					return;
				}
				if(textos.get(2).getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(null, "Favor de completar el campo Apellido Materno");
					return;
				}
				if(textos.get(3).getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(null, "Favor de completar el campo Nombre");
					return;
				}
				if(textos.get(5).getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(null, "Favor de completar el campo Telefono");
					return;
				}
				//JCGE:
				try
				{
					String query;
					r = new ArrayList<ResultSet>();
					r.add(baseDatos.db.newQuery(String.format(" SELECT * "
															+ "   FROM huespedes "
															+ "  WHERE idhuesped = %s ",idhuesped.getText())));
					if (r.get(0).next())
					{
						//JCGE: Ya existe... lo actualizamos
						query = String.format(" UPDATE huespedes "
											+ "    SET (paterno,materno,nombre,fechanacimiento,telefono,tarjeta,secret_num,ultimaactualizacion) = "
											+ "        ('%s','%s','%s','%s'::DATE,'%s','%s','%s',now()) "
											+ "  WHERE idhuesped = %s ",
											textos.get(1).getText(),textos.get(2).getText(),textos.get(3).getText(),datePickers.get(0).getDate(),
											textos.get(5).getText(),textos.get(6).getText(),textos.get(7).getText(),idhuesped.getText());
						JOptionPane.showMessageDialog(null, "Se actualizó correctamente el huesped.");
					}
					else
					{
						//JCGE: Es nuevo... lo insertamos
						query = String.format(" INSERT INTO huespedes (idhuesped,paterno,materno,nombre,fechanacimiento,telefono,tarjeta,secret_num,ultimaactualizacion) "
											+ " VALUES (DEFAULT,'%s','%s','%s','%s'::DATE,'%s','%s','%s',now()) ",
											textos.get(1).getText(),textos.get(2).getText(),textos.get(3).getText(),datePickers.get(0).getDate(),
											textos.get(5).getText(),textos.get(6).getText(),textos.get(7).getText());
						JOptionPane.showMessageDialog(null, "Se agregó correctamente el nuevo huesped.");
					}
					baseDatos.db.newInsert(query);
					botonera.get(0).setEnabled(true);
					botonera.get(1).setEnabled(false);
					botonera.get(2).setEnabled(false);
					limpiarEntradas(false);
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
				}

			}
			if (boton == "Cancelar")
			{
				//
				botonera.get(0).setEnabled(true);
				botonera.get(1).setEnabled(false);
				botonera.get(2).setEnabled(false);
				limpiarEntradas(false);
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
	private void limpiarEntradas(boolean enabl)
	{
		for (HFTextField txt: textos)
		{
			txt.setEnabled(enabl);
			txt.setText("");
		}
		for (HFDateField dte: datePickers)
		{
			dte.resetDate();
		}
		idhuesped.setEnabled(!enabl);
		buscar.setEnabled(!enabl);
		if (!enabl)
		{
			idhuesped.requestFocus();
		}
		datePickers.get(0).setEnabl(enabl);
	}
}
