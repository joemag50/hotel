import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class HFTextField extends JTextField implements KeyListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -347408485368150523L;
	
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
	public void keyPressed(KeyEvent e)
	{
		if (busqueda == null)
		{
			return;
		}
		if (e.getKeyCode() == 114) //F3
		{
			frame = new buscador(busqueda, query, this);
			this.setEnabled(false);
		}
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
	@Override
	public void keyReleased(KeyEvent e) {/*JCGE: Aun sin nada*/}

}
