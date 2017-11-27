import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.JButton;
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
	private HFDoubleField mTotal, cambio, pendiente;
	private JButton cancelarResv;
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
		HFLabel ltotal = new HFLabel("    Total: ");
		ltotal.setBounds(x, y, width, height);
		total  = new HFLabel(sumaCobros()+"");
		total.setBounds(x+ 200, y, width, height); y+=35;
		
		HFLabel lpendiente = new HFLabel("Pendiente: ");
		lpendiente.setBounds(x, y, width, height);
		pendiente = new HFDoubleField();
		pendiente.setBounds(x +200, y, width, height); y+=35;
		pendiente.setEnabled(false);
		pendiente.setText(""+(sumaCobros()-sumaAbonos()));
		
		HFLabel lmtotal = new HFLabel("    Abono: ");
		lmtotal.setBounds(x, y, width, height);
		mTotal = new HFDoubleField();
		mTotal.setBounds(x+ 200, y, width, height); y+=35;
		mTotal.addFocusListener(new FocusListener()
		{
			@Override
			public void focusLost(FocusEvent arg0)
			{
				//JCGE:
				Double c = new Double(Double.parseDouble(mTotal.getText()) - Double.parseDouble(total.getText()));
				if (c < 0)
				{
					c = 0.0;
				}
				cambio.setValue(c);
				guardar.setEnabled(true);
			}
			@Override
			public void focusGained(FocusEvent arg0)
			{
				//JCGE: Cuando el usuario quiera modificar esta entrada
				//Bloqueamos el boton guardar
				guardar.setEnabled(false);
			}
		});
		
		HFLabel lcambio = new HFLabel("   Cambio: ");
		lcambio.setBounds(x, y, width, height);
		cambio = new HFDoubleField();
		cambio.setBounds(x+ 200, y, width, height);
		cambio.setEnabled(false);
		y+=40;
		guardar.setBounds(x, y, width, height);
		guardar.setEnabled(false);
		cancelar.setBounds(x + width, y, width, height);
		
		//JCGE: Cancelar reservacion solo cuando su permiso sea el de supervisor
		if (Objects.equals(baseDatos.nivelUsuario(), new String("SUP")))
		{
			cancelarResv = new JButton("Cancelar Reservación");
			cancelarResv.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					int respuesta = JOptionPane.showConfirmDialog(null,
						    "¿Está totalmente seguro que desea realizar la siguiente operación?",
						    "Confirmación",
						    JOptionPane.YES_NO_OPTION);
					if (respuesta == 1)
					{
						return;
					}
					//JCGE:
					String query2 = String.format(" UPDATE hospedajes SET estatus = 3 WHERE idsolicitud = %s ", idsol);
					int res2 = baseDatos.db.newInsert(query2);
					if (res2 > 0)
					{
						baseDatos.logInsert(baseDatos.user_actual, "Cancelacion de reservacion", "Check Out");
						JOptionPane.showMessageDialog(null, "El hospedaje fue cancelado correctamente.");
					}
					else
					{
						JOptionPane.showMessageDialog(null, "El hospedaje NO fue cancelado, Favor de contactar a su administrador.");
					}
					limpiarEntradas();
					estatusHabitacion.actualizaAgenda();
					setVisible(false);
					dispose();
					return;
				}
			});
			cancelarResv.setEnabled(true);
			cancelarResv.setBounds(30, 400, 300, 50);
			cancelarResv.setBackground(Color.YELLOW);
			panelInterno.add(cancelarResv);
		}
		
		panelInterno.add(pendiente);
		panelInterno.add(lpendiente);
		panelInterno.add(lcambio);
		panelInterno.add(cambio);
		panelInterno.add(ltotal);
		panelInterno.add(lmtotal);
		panelInterno.add(total);
		panelInterno.add(mTotal);
	}
	private void cobrosActuales()
	{
		System.out.println(""+idsol);
		ResultSet k = baseDatos.db.newQuery(String.format(" SELECT * "
														+ "   FROM pagos "
														+ "  WHERE idsolicitud = %s ",idsol));
		try {
			while (k.next())
			{
				System.out.println(k.getString("concepto"));
				Object[] concepto = {k.getString("concepto"), k.getInt("cantidad"), k.getDouble("precio_unitario"), k.getDouble("cargo"), k.getDouble("abono")};
				nuevoCobro(concepto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private Double sumaAbonos()
	{
		Double suma = 0.0;
		String query = String.format(" SELECT sum(abono) FROM pagos WHERE idsolicitud = %s ", idsol);
		ResultSet r = baseDatos.db.newQuery(query);
		try {
			if (r.next())
			{
				suma = r.getDouble(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return suma;
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
			//JCGE: Buscamos los pagos que tiene que hacer
			ArrayList<Integer> idpagos       = new ArrayList<Integer>();
			ArrayList<Double>  pagoNecesario = new ArrayList<Double>();
			String query = String.format(" SELECT idpago,cargo-abono "
									   + "   FROM pagos"
									   + "  WHERE idsolicitud = %s AND abono <> cargo"
									   + "  ORDER BY concepto", idsol);
			ResultSet r = baseDatos.db.newQuery(query);
			try {
				int i = 0;
				while (r.next())
				{
					idpagos.add(r.getInt(1));
					pagoNecesario.add(r.getDouble(2)); 
					i++;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Double pago = new Double(mTotal.getText()) ;
			for (int i = 0; i<idpagos.size(); i++)
			{
				if (pago == 0)
				{
					break;
				}
				if (pago < pagoNecesario.get(i))
				{
					query = String.format(" UPDATE pagos SET abono = abono + %s WHERE idsolicitud = %s AND idpago = %s ", pago, idsol, idpagos.get(i));
					int res = baseDatos.db.newInsert(query);
					System.out.println("1 "+res);
					pago = 0.0;
				}
				else
				{
					pago-=(pagoNecesario.get(i));
					query = String.format(" UPDATE pagos SET abono = abono + %s WHERE idsolicitud = %s AND idpago = %s ", pagoNecesario.get(i), idsol, idpagos.get(i));
					int res = baseDatos.db.newInsert(query);
					System.out.println("2 "+res);
				}
			}
			//JCGE: Vamos a consultar si la suma de los abonos coincide con la suma de los cargos
			query = String.format(" SELECT sum(abono) = sum(cargo) FROM pagos WHERE idsolicitud = %s ", idsol);
			r = baseDatos.db.newQuery(query);
			try {
				if (r.next())
				{
					if (r.getBoolean(1))
					{
						String query2 = String.format(" UPDATE hospedajes SET estatus = 2 WHERE idsolicitud = %s ", idsol);
						int res2 = baseDatos.db.newInsert(query2);
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			limpiarEntradas();
			estatusHabitacion.actualizaAgenda();
			this.setVisible(false);
			this.dispose();
			return;
		}
		if (boton == "Volver")
		{
			limpiarEntradas();
			estatusHabitacion.actualizaAgenda();
			this.setVisible(false);
			this.dispose();
			return;
		}
	}
}
