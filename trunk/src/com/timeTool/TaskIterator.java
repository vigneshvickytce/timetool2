package com.timeTool;

public class TaskIterator
{
	private int rowCount; 
	public TaskIterator()
	{
		rowCount = 0; 
	}
	
	public Task getFirst()
	{
		TimeTool data = TimeTool.getInstance(); 
		Task task = null; 
		if (data.getRowCount() > 0)
		{
			task = data.get(0);
		}
		rowCount = 1;
		return task; 
	}
	public Task getNext()
	{
		TimeTool data = TimeTool.getInstance(); 
		Task task = null; 
		
		if (data.getRowCount() > rowCount)
		{
			task = data.get(rowCount); 
			rowCount++;
		}
		return task; 
	}
}
