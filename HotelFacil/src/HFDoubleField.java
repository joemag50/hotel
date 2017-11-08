import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class HFDoubleField extends JTextField implements KeyListener, FocusListener
{
	HFDoubleField()
	{
		this.addKeyListener(this);
		this.addFocusListener(this);
		this.setText("0.00");
	}
	@Override
	public void keyTyped(KeyEvent e)
	{
		char c = e.getKeyChar();
		if (!((c >= '0') && (c <= '9') || (c == '.') || (c == KeyEvent.VK_TAB) || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE) ))        
		{
			e.consume();
		}
	}
	@Override
	public void keyPressed(KeyEvent arg0)
	{
		//
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
			this.setText("0.00");
	}
	@Override
	public void focusLost(FocusEvent arg0)
	{
		if (this.getText().length() == 0)
			this.setText("0.00");
	}
}