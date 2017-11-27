import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;

import javax.swing.BoxLayout;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MenuInterno extends JInternalFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1383293379076831880L;
	protected JPanel panelInterno;
	protected int ext_b = MainWindow.WIDTH.intValue()-300;
	protected int ext_h = MainWindow.HEIGHT.intValue()-200;
	MenuInterno()
	{
		super("MenuInterno", false, true, false, true);
		this.setSize(ext_b, ext_h);
		this.setVisible(true);
		this.setLayout(null);
		panelInterno = new JPanel();
		panelInterno.setLayout(null);
		panelInterno.setVisible(true);
		panelInterno.setBounds(0, 0, ext_b, ext_h);
		panelInterno.setBackground(MainWindow.colores.get(2));
		this.setBackground(MainWindow.colores.get(2));
		this.add(panelInterno);
		this.moveToFront();
		try {
			this.setSelected(true);
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
