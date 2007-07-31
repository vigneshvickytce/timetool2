package com.timeTool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicBorders;

public class StatusBar extends JComponent implements Observer
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3404312053958902491L;
	private JLabel dayOfWeek;
	private JLabel time;
	private JLabel date; 
	private JLabel task; 
	private JLabel minuteTotal; 
	private JLabel hourTotal; 
	
	public StatusBar() 
    {
	    super();
	    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		addTimeField();
		addDayOfWeekField(); 
		addDateField(); 
		addTaskField();
		addMinuteField();
		addHourField();
		TimeTool.getInstance().addObserver(this); 
    }
	private void addHourField()
	{
		hourTotal = new JLabel(); 
		hourTotal.setBorder(BasicBorders.getTextFieldBorder()); 
		this.add(hourTotal);
	}
	private void addMinuteField()
	{
		minuteTotal = new JLabel(); 
		minuteTotal.setBorder(BasicBorders.getTextFieldBorder()); 
		this.add(minuteTotal);
	}
	private void addTaskField()
	{
		task = new JLabel(); 
		task.setBorder(BasicBorders.getTextFieldBorder()); 
		this.add(task);
	}
	private void addDateField()
	{
		date = new JLabel(); 
		date.setBorder(BasicBorders.getTextFieldBorder()); 
		this.add(date);
	}
	private void addDayOfWeekField()
	{
		dayOfWeek = new JLabel();
		dayOfWeek.setBorder(BasicBorders.getTextFieldBorder()); 
		this.add(dayOfWeek);
	}
	private void addTimeField()
	{
		time = new JLabel();
		time.setBorder(BasicBorders.getTextFieldBorder());  
		this.add(time);
	}
	public void update(Observable arg0, Object arg1)
	{
		updateFields(); 
	}
	private void updateFields()
	{
		Date currentTime = TimeTool.getInstance().getTime();  
		
		time.setText(DateFormat.getTimeInstance().format(currentTime)); 
		date.setText(DateFormat.getDateInstance().format(currentTime)); 
		
		SimpleDateFormat df = new SimpleDateFormat("E"); 
		dayOfWeek.setText(df.format(currentTime)); 
		
		minuteTotal.setText(TimeTool.getInstance().getTotalMinutes()); 
		
		hourTotal.setText(TimeTool.getInstance().getTotalHours()); 
		
		task.setText(TimeTool.getInstance().getCurrentTask());
	}
}