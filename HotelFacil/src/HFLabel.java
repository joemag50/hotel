import java.awt.Font;

import javax.swing.JLabel;

public class HFLabel extends JLabel
{
	protected Font font = new Font("Ubuntu Mono", Font.PLAIN, 15);
	HFLabel(String text)
	{
		this.setText(text);
		this.setFont(font);
	}
}
