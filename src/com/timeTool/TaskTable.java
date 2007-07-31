package com.timeTool;

import javax.swing.table.AbstractTableModel;

public class TaskTable extends AbstractTableModel 
{
	private static final String HOURS_HEADER = ResourceAutomation.getResourceString("GridHourHeader");
	private static final String MINUTES_HEADER = ResourceAutomation.getResourceString("GridMinsHeader");
	private static final String DESCRIPTION_HEADER = ResourceAutomation.getResourceString("GridDescHeader");
	private static final String TASK_HEADER = ResourceAutomation.getResourceString("GridTaskHeader");
	/**
	 * 
	 */
	private static final long serialVersionUID = 4068102326223030882L;
	private static final int NUM_COLUMNS = 4;
	private final String[] columns = {TASK_HEADER, DESCRIPTION_HEADER, MINUTES_HEADER, HOURS_HEADER};
	
	public TaskTable()
	{
	}
	
	public String getColumnName(int column)
	{
		return columns[column]; 	
	}
	
	public int getRowCount()
	{
		return TimeTool.getInstance().getRowCount(); 
	}

	public int getColumnCount()
	{
		return NUM_COLUMNS;
	}

	public Object getValueAt(int row, int col) 
	{ 
		return TimeTool.getInstance().getValueAt(row, col);  
	}

}
