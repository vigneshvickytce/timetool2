package com.timeTool;

public abstract class ExportVisitor
{
	public abstract String padID(String id); 
	public abstract String padDescription(String description); 
	public abstract String padMinutes(String minutes); 
	public abstract String padHours(String hours); 
	public abstract String getHeader(); 
	public abstract String getFooter(); 
	public abstract String getColumnSeperator(); 
	public abstract String getRowSeperator(); 
}
