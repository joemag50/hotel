import java.util.Calendar;
import java.util.Date;

import org.jdatepicker.JDateComponentFactory;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;

public class HFDateField extends JDatePickerImpl
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9183229062387974403L;
	public static JDateComponentFactory jdck = new JDateComponentFactory();
	HFDateField()
	{
		super((JDatePanelImpl) jdck.createJDatePanel(), new DateLabelFormatter());
	}
	public void resetDate()
	{
		this.getJFormattedTextField().setValue(toCalendar(new java.util.Date()));
	}
	public void setDate(Date fechita)
	{
		this.getJFormattedTextField().setValue(toCalendar(fechita));
	}
	//JCGE: Sonaria logico que lo pusiera en Date, pero me sirve mas como String
	public String getDate()
	{
		return this.getJFormattedTextField().getText();
	}
	public static Calendar toCalendar(Date date)
	{ 
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	public void setEnabl(boolean enabl)
	{
		this.getComponent(1).setEnabled(enabl);
	}
}