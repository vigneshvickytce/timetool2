package com.timeTool;

public class Task 
{
	private long seconds; 
	private String id;
	private String description;
	
	public Task(String id, String description, long seconds)
	{
		setId(id); 
		setDescription(description); 
		setSeconds(seconds); 
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getDescription()
	{
		return description;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getId()
	{
		return id;
	} 
	public void setSeconds(long seconds)
	{
		this.seconds = seconds; 
	}
	public void addSeconds(long seconds)
	{
		this.seconds += seconds; 
	}
	public String getMinutes()
	{
		Integer minutes = new Integer(new Float(seconds/60).intValue()); 
		return minutes.toString(); 
	}
	
	public String getHours()
	{
		float hours = seconds; 
		hours = hours / 3600; 
		hours = (float)round((double)hours, 2);
		return formatHours(new Float(hours).toString()); 
	}

	private double round(double val, int places) 
	{
		long factor = (long)Math.pow(10,places);

		// Shift the decimal the correct number of places
		// to the right.
		val = val * factor;

		// Round to the nearest integer.
		long tmp = Math.round(val);

		// Shift the decimal the correct number of places
		// back to the left.
		return (double)tmp / factor;
	}
	
	public static String formatHours(String hours)
	{
		String ret = hours.toString(); 
		int index = hours.indexOf("."); 
		int length = hours.length(); 
		if (index == -1)
		{
			ret = ret + ".00"; 
		}
		else if ((index < (length-1)) && (index > (length-3)))
		{
			ret = ret + "0"; 
		}
		else if (index < (length-3))
		{
			ret = ret.substring(0, index+3);  
		}
		return ret; 
	}
	
}
