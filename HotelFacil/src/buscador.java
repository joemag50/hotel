import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class buscador extends JFrame implements Action, ListSelectionListener, KeyListener
{
	//
	protected Container frame;
	private JList lista;
	private JTextField campo;
	buscador(String buscador, String query, JTextField entrada)
	{
		frame = getContentPane();
		frame.setLayout(null);
		//JCGE: Preparamos la ventana
		this.setTitle("Buscador");
		this.setSize(300, 500);
		//this.setSize(800, 599);
		this.setResizable(false);
		//this.setExtendedState(MAXIMIZED_BOTH);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.campo = entrada;
		String[] usuarios = new String[] {" "};
		try
		{
			ResultSet res = baseDatos.db.newQuery(query);
			if (res.next())
			{
				System.out.println(res.getString(1));
				if (res.getString(1) != null)
				{
					usuarios = (" ,"+res.getString(1)).split(",");
				}
				else
				{
					usuarios = new String[] {" "};
				}
			}
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lista = new JList(usuarios);
		lista.setVisibleRowCount(5);
		lista.addListSelectionListener(this);
		lista.setSelectedIndex(0);
		lista.addKeyListener(this);
		//lista.addFocusListener(fe);
		lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scroll = new JScrollPane(lista);
		HFLabel titulo = new HFLabel(buscador);
		titulo.setBounds(0,0,300,20);
		scroll.setBounds(0, 20, 300, 480);
		frame.add(scroll);
		frame.add(titulo);
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void putValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
	//JCGE: Para cuando cambie de esta cosa
	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getKeyCode() == 10)
		{
			if (lista.getSelectedValue() != null)
				if (lista.getSelectedValue().toString().trim().split(":")[0] != "")
					campo.setText(lista.getSelectedValue().toString().trim().split(":")[0]);
			
			this.setVisible(false);
			this.dispose();
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}