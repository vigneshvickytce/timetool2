package com.timeTool;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class TXTVisitor extends ExportVisitor 
{
	public static final String DATA_FILE = "ttdata.txt";

	public String padID(String id)
	{
		String ret = " " + id + "      "; 
		ret = ret.substring(0, 7); 
		return ret;
	}

	public String padDescription(String description)
	{
		String ret = description + "                                         "; 
		ret = ret.substring(0, 38);
		return ret; 
	}

	public String padMinutes(String minutes)
	{
		String ret = "      " + minutes; 
		ret = ret.substring(ret.length()-5);
		return ret; 
	}

	public String padHours(String hours)
	{
		String ret = "     " + hours; 
		ret = ret.substring(ret.length() - 6); 
		return ret; 
	}

	public String getHeader()
	{
		return "";
	}

	public String getFooter()
	{
		String rowTag = getFormattedRowTag();  
		String timeTag = getFormattedTimeTag(); 
		return rowTag + "\n" + timeTag + "\n"; 
	}

	private String getFormattedRowTag()
	{
		FilePersistence fileHelper = new FilePersistence(); 
		String currentRow = new Integer(TimeTool.getInstance().getCurrentRow()).toString(); 
		String tag = fileHelper.wrapDataInTag(currentRow, TimePersistence.CURRENT_ROW_TAG);
		return tag;
	}
	private String getFormattedTimeTag()
	{
		FilePersistence fileHelper = new FilePersistence();
		String tag = ""; 
		if (TimeTool.getInstance().getTimerStartTime() != null)
		{
			long time = TimeTool.getInstance().getTimerStartTime().getTime(); 
			String timeString = Long.toString(time); 
			tag = fileHelper.wrapDataInTag(timeString, TimePersistence.CURRENT_TIME_TAG);
		}
		return tag;
	}

	public String getColumnSeperator()
	{
		return " ";
	}

	public String getRowSeperator()
	{
		return "\n";
	}

}
