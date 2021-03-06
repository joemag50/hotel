import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Action;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class buscador extends JFrame implements Action, ListSelectionListener, KeyListener
{
	/*
	 * JCGE: Clase que muestra una pantalla con la busqueda obtenida desde el HFTextField 
	 * */
	private static final long serialVersionUID = 2109227608072651896L;
	
	protected Container     frame;
	private   JList<String> lista;
	private   JTextField    campo;
	private   JTextField    retorno;
	private   JFormattedTextField    retorno2;
	
	//JCGE: Son varios constructores dependiendo de lo que se necesitaba
	buscador(String buscador, String query, JTextField entrada)
	{
		this(buscador, query, entrada, null);
	}
	buscador(String buscador, String query, JTextField entrada, JTextField salida)
	{
		this(buscador, query, entrada, salida, null);
	}
	buscador(String buscador, String query, JTextField entrada, JTextField salida, JFormattedTextField salida2)
	{
		frame = getContentPane();
		frame.setLayout(null);
		//JCGE: Preparamos la ventana
		this.setTitle("Buscador");
		this.setSize(300, 500);
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		if (entrada != null)
			this.campo = entrada;
		if (salida != null)
			this.retorno = salida;
		if (salida2 != null)
			this.retorno2 = salida2;
		String[] usuarios = new String[] {" "};
		try
		{
			ResultSet res = baseDatos.db.newQuery(query);
			if (res.next())
			{
				System.out.println(res.getString(1));
				if (res.getString(1) != null)
				{
					usuarios = (res.getString(1)).split(",");
				}
				else
				{
					usuarios = new String[] {" "};
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		lista = new JList<String>(usuarios);
		lista.setVisibleRowCount(5);
		lista.addListSelectionListener(this);
		lista.setSelectedIndex(0);
		lista.addKeyListener(this);
		lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scroll = new JScrollPane(lista);
		HFLabel titulo = new HFLabel(buscador);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		titulo.setBounds(0,0,300,20);
		scroll.setBounds(0, 20, 300, 455);
		frame.add(scroll);
		frame.add(titulo);
	}
	@Override
	public void keyPressed(KeyEvent arg0)
	{
		if (arg0.getKeyCode() == 10)
		{
			if (lista.getSelectedValue() != null)
				if (lista.getSelectedValue().toString().trim().split(": ")[0] != "")	
					campo.setText(lista.getSelectedValue().toString().trim().split(": ")[0]);
					if (retorno != null)
					{
						retorno.setText(lista.getSelectedValue().toString().trim().split(": ")[1]);
					}
					if (retorno2 != null)
					{
						retorno2.setValue(lista.getSelectedValue().toString().trim().split(": ")[2]);
					}
			this.setVisible(false);
			this.dispose();
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0) {/*JCGE: Aun sin nada*/}
	@Override
	public void keyTyped(KeyEvent arg0) {/*JCGE: Aun sin nada*/}
	@Override
	public void actionPerformed(ActionEvent arg0) {/*JCGE: Aun sin nada*/}
	@Override
	public Object getValue(String arg0) {return null;}
	@Override
	public void putValue(String arg0, Object arg1) {/*JCGE: Aun sin nada*/}
	@Override
	public void valueChanged(ListSelectionEvent arg0) {/*JCGE: Aun sin nada*/}

}
