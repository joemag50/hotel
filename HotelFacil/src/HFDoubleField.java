import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.MaskFormatter;

public class HFDoubleField extends JFormattedTextField implements KeyListener, FocusListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1885229317474902943L;
	private NumberFormat amountFormat;
	HFDoubleField()
	{
		this.addKeyListener(this);
		this.addFocusListener(this);
		this.setHorizontalAlignment(RIGHT);
		MaskFormatter mascara = null;
		try {
			mascara = new MaskFormatter("HHHHHHHHH0.00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setFormatter(mascara);
		this.setValue("0.00");
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
		//JCGE: Validamos que no metan cochinero a mi entrada de numeros
		if (this.getText().length() == 0)
			this.setText("0.00");
		
		try
		{
			Double.parseDouble(this.getText());
		}
        catch(NumberFormatException e)
        { 
        	this.setText("0.00");
        }
        catch(NullPointerException e)
		{
        	this.setText("0.00");
		}
		this.setValue(this.getText());
	}
}





