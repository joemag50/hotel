import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class HFIntegerField extends JTextField implements KeyListener, FocusListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4923626614052178707L;
	public buscador frame;
	public String busqueda;
	public String query;
	HFIntegerField()
	{
		this.addKeyListener(this);
		this.addFocusListener(this);
		this.setText("0");
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
			frame = new buscador(busqueda, query, this);
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
