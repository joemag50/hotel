import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class HFTextField extends JTextField implements KeyListener
{
	protected Font font = new Font("Ubuntu Mono", Font.PLAIN, 15);
	public buscador frame;
	public String busqueda;
	public String query;
	HFTextField(int columns)
	{
		this.setColumns(columns);
		this.addKeyListener(this);
		this.setFont(font);
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
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
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		char c = e.getKeyChar();
		System.out.println("");
		if ((c == '\'') || (c == '"') || (c == '-') || (c == KeyEvent.VK_BACK_SLASH))
		{
			e.consume();
		}
	}

}
