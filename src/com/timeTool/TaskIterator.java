package com.timeTool;

public class TaskIterator
{
	private int rowCount; 
	private final TimeTool controller;

	public TaskIterator(TimeTool controller)
	{
		rowCount = 0;
		this.controller = controller;
	}
	
	public Task getFirst()
	{
		Task task = null;
		if (controller.getRowCount() > 0)
		{
			task = controller.get(0);
		}
		rowCount = 1;
		return task; 
	}
	public Task getNext()
	{
		Task task = null;
		
		if (controller.getRowCount() > rowCount)
		{
			task = controller.get(rowCount); 
			rowCount++;
		}
		return task; 
	}
}
