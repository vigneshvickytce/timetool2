package com.timeTool.ui;

import com.timeTool.ResourceAutomation;
import com.timeTool.TimeTool;

import javax.swing.table.AbstractTableModel;

public class TaskTable extends AbstractTableModel 
{
	private static final int NUM_COLUMNS = 4;
	private final String[] columns;
	private final TimeTool controller;


	public TaskTable(TimeTool controller, ResourceAutomation resources) {
		this.controller = controller;
		columns = new String[]{
			resources.getResourceString("GridTaskHeader"),
			resources.getResourceString("GridDescHeader"),
			resources.getResourceString("GridMinsHeader"),
			resources.getResourceString("GridHourHeader")};
	}
	
	public String getColumnName(int column)
	{
		return columns[column]; 	
	}
	
	public int getRowCount()
	{
		return controller.getRowCount();
	}

	public int getColumnCount()
	{
		return NUM_COLUMNS;
	}

	public Object getValueAt(int row, int col) 
	{ 
		return controller.getValueAt(row, col);  
	}

}
