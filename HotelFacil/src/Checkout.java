import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JOptionPane;

public class Checkout extends NuevoCuarto implements ActionListener
{
	/**
	 * JCGE: Modulo de Checkout
	 * Aqui vamos a despedir a nuestro inquilino o alargarle la visita
	 */
	private static final long serialVersionUID = -4467762444239765332L;
	private ResultSet r;
	private ResultSet solicitud;
	private int idhue = 0;
	private int idsol = 0;
	private HFLabel total;
	private HFDoubleField mTotal, cambio;
	Checkout(String p_fecha)
	{
		this.setTitle("Checkout");
		buscar.setVisible(false);
		esNuevo.setVisible(false);
		idhuesped.setEnabled(false);
		fechas.get(0).setVisible(false);
		fechas.get(1).setVisible(false);
		lPersonas.setVisible(false);
		lFechaIni.setVisible(false);
		lFechaFin.setVisible(false);
		huespedes.setVisible(false);
		try
		{
			solicitud = baseDatos.db.newQuery(String.format(" SELECT * "
					  									  + "   FROM hospedajes "
					  									  + "  WHERE idhabitacion = %s AND estatus <= 1 AND "
					  									  + "        (%s::DATE BETWEEN fecha_inicio AND fecha_fin) ",
					  									  estatusHabitacion.idhabitacion, p_fecha));
			if (!solicitud.next())
			{
				this.setVisible(false);
				this.dispose();
				return;
			}
			idhue = solicitud.getInt("idhuesped");
			idsol = solicitud.getInt("idsolicitud");
			r = baseDatos.db.newQuery(String.format(" SELECT * "
												  + "   FROM huespedes "
												  + "  WHERE idhuesped = %s ", idhue));
			if (r.next())
			{
				limpiarEntradas();
				idhuesped.setText(idhue+"");
				int i = 1;
				for (HFTextField txt: this.textos)
				{
					txt.setText(r.getString(i));
					i++;
				}
				i = 0;
				for (HFDateField dt: datePickers)
				{
					if (i == 0)
					{
						dt.setDate(r.getDate("fechanacimiento"));
					}
					else
					{
						dt.setDate(r.getDate("ultimaactualizacion"));
					}
					i++;
				}
			}
			else
			{
				this.setVisible(false);
				this.dispose();
				return;
			}
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
			this.setVisible(false);
			this.dispose();
			return;
		}
		cobrosActuales();
		
		//JCGE: Vamos agregar unas casillas con lo que debe
		int x = 420, y = 30, width = 200, height = 35;
		lcob.setBounds(x, y, width, height); y+=35;
		scroll.setBounds(x, y, 500, 200); y+=200;
		HFLabel ltotal = new HFLabel(" Total: ");
		ltotal.setBounds(x, y, width, height);
		total  = new HFLabel(sumaCobros()+"");
		total.setBounds(x+ 200, y, width, height); y+=35;
		
		HFLabel lmtotal = new HFLabel(" Abono: ");
		lmtotal.setBounds(x, y, width, height);
		mTotal = new HFDoubleField();
		mTotal.setBounds(x+ 200, y, width, height); y+=35;
		mTotal.addFocusListener(new FocusListener()
		{
			@Override
			public void focusLost(FocusEvent arg0)
			{
				//JCGE:
				Double c = new Double(Double.parseDouble(total.getText()) - Double.parseDouble(mTotal.getText()));
				cambio.setValue(c);
			}
			@Override
			public void focusGained(FocusEvent arg0) {/*JCGE: Sin acciones*/}
		});
		
		HFLabel lcambio = new HFLabel("Cambio: ");
		lcambio.setBounds(x, y, width, height);
		cambio = new HFDoubleField();
		cambio.setBounds(x+ 200, y, width, height);
		cambio.setEnabled(false);
		y+=70;
		guardar.setBounds(x, y, width, height);
		cancelar.setBounds(x + width, y, width, height);
		
		panelInterno.add(lcambio);
		panelInterno.add(cambio);
		panelInterno.add(ltotal);
		panelInterno.add(lmtotal);
		panelInterno.add(total);
		panelInterno.add(mTotal);
	}
	private void cobrosActuales()
	{
		ResultSet k = baseDatos.db.newQuery(String.format(" SELECT * "
														+ "   FROM pagos "
														+ "  WHERE idsolicitud = %s ",idsol));
		try {
			while (k.next())
			{
				System.out.println(k.getString("concepto"));
				Object[] concepto = {k.getString("concepto"), k.getInt("cantidad"), k.getDouble("precio_unitario"), k.getDouble("cargo")};
				nuevoCobro(concepto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private Double sumaCobros()
	{
		Double suma = 0.0;
		for(int i = 0; i< data.length;i++)
        {
            //System.out.println(huespedes.getModel().getElementAt(i));
        	suma += new Double(tabla.getModel().getValueAt(i, 3)+"");
        }
		return suma;
	}
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		String boton = arg0.getActionCommand();
		if (boton == "Guardar")
		{
			int respuesta = JOptionPane.showConfirmDialog(this,
				    "¿Está totalmente seguro que desea realizar la siguiente operación?",
				    "Confirmación",
				    JOptionPane.YES_NO_OPTION);
			if (respuesta == 1)
			{
				return;
			}
			String query = String.format(" UPDATE pagos SET abono = cargo WHERE idsolicitud = %s ", idsol);
			int res = baseDatos.db.newInsert(query);
			
			String query2 = String.format(" UPDATE hospedajes SET estatus = 2 WHERE idsolicitud = %s ", idsol);
			int res2 = baseDatos.db.newInsert(query2);
			
			limpiarEntradas();
			estatusHabitacion.actualizaAgenda();
			this.setVisible(false);
			this.dispose();
			return;
		}
		if (boton == "Cancelar")
		{
			limpiarEntradas();
			estatusHabitacion.actualizaAgenda();
			this.setVisible(false);
			this.dispose();
			return;
		}
	}
}
