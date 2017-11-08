import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class HFTextField extends JTextField implements KeyListener
{
	HFTextField(int columns)
	{
		this.setColumns(columns);
		this.addKeyListener(this);
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		char c = e.getKeyChar();
		if ((c == '\'') || (c == '"') || (c == '-') || (c == KeyEvent.VK_BACK_SLASH))
		{
			e.consume();
		}
	}

}
