import org.jdatepicker.impl.JDatePickerImpl;

public class HFDateField extends JDatePickerImpl
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9183229062387974403L;
	HFDateField()
	{
		super(Menu.datePanel, new DateLabelFormatter());
	}
}