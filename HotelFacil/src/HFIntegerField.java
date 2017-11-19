import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class HFIntegerField extends JTextField implements KeyListener, FocusListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4923626614052178707L;
	public buscador frame;
	public String busqueda;
	public String query;
	private   boolean  bloquearAlEnter = true;
	private   JTextField retorno;
	HFIntegerField()
	{
		this.addKeyListener(this);
		this.addFocusListener(this);
		this.setText("0");
		this.setHorizontalAlignment(RIGHT);
	}
	public void bloqueaAlEnter(boolean b)
	{
		this.bloquearAlEnter = b;
	}
	public void retornaDescripcion(JTextField jtf)
	{
		this.retorno = jtf;
	}
	@Override
	public void keyTyped(KeyEvent e)
	{
		char c = e.getKeyChar();
		if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_TAB) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE)))        
		{
			e.consume();
		}
	}
	@Override
	public void keyPressed(KeyEvent e)
	{
		//
		if (busqueda == null)
		{
			return;
		}
		if (e.getKeyCode() == 114) //F3
		{
			if (retorno != null)
			{
				frame = new buscador(busqueda, query, this, retorno);
			}
			else
			{
				frame = new buscador(busqueda, query, this);
			}
			this.setEnabled(!this.bloquearAlEnter);
		}
	}
	@Override
	public void keyReleased(KeyEvent arg0)
	{
		//
	}
	@Override
	public void focusGained(FocusEvent arg0)
	{
		if (this.getText().length() == 0)
			this.setText("0");
	}
	@Override
	public void focusLost(FocusEvent arg0)
	{
		if (this.getText().length() == 0)
			this.setText("0");
	}
}
