package com.timeTool;

public class CSVVisitor extends ExportVisitor
{
	private static final String HOURS_HEADER = "HoursHeader";
	private static final String MINUTES_HEADER = "MinutesHeader";
	private static final String PROJNAME_HEADER = "ProjNameHeader";
	private static final String PROJNUM_HEADER = "ProjNumHeader";
	private TimeToolPreferences options;
	private final ResourceAutomation resources;


	public CSVVisitor(ResourceAutomation resources)
	{
		options = new TimeToolPreferences();
		this.resources = resources;
	}

	public String quotePad(String string)
	{
		return options.getQuotes() + string + options.getQuotes();
	}
	public String padID(String id)
	{
		return quotePad(id);
	}

	public String padDescription(String description)
	{
		return quotePad(description);
	}

	public String padMinutes(String minutes)
	{
		String convertedMinutes = minutes.replace(".", options.getDecimal());
		return quotePad(convertedMinutes);
	}

	public String padHours(String hours)
	{
		String convertedHours = hours.replace(".", options.getDecimal());
		return quotePad(convertedHours);
	}

	public String getHeader()
	{
		String result = quotePad(resources.getResourceString(PROJNUM_HEADER)) + getColumnSeperator();
		result = result + quotePad(resources.getResourceString(PROJNAME_HEADER)) + getColumnSeperator();
		result = result + quotePad(resources.getResourceString(MINUTES_HEADER)) + getColumnSeperator();
		result = result + quotePad(resources.getResourceString(HOURS_HEADER)) + getRowSeperator();

		return result;
	}

	public String getFooter()
	{
		return "";
	}

	public String getColumnSeperator()
	{
		return options.getDelimiter();
	}

	public String getRowSeperator()
	{
		return "\n";
	}

}
