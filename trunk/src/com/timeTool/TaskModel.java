package com.timeTool;

import java.util.ArrayList;
import java.util.Collections;

public class TaskModel extends ArrayList<Task>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5718562040056751624L;

	private boolean ascending = true; 
	private int sortColumn = 0; 
	
	public TaskModel()
	{
		super(); 
	}

	public void sort(int index)
	{
		if (indexOutOfRange(index)) 
		{
			return;
		}

		if (sortColumn == index)
		{
			ascending = !ascending; 
		}
		sortColumn = index; 
		
		if (ascending)
		{
			if (sortColumn == TaskSort.ID_SORT)
			{
				Collections.sort(this, TaskSort.ID_ASC); 
			}
			else if (sortColumn == TaskSort.DESCRIPTION_SORT)
			{
				Collections.sort(this, TaskSort.DESCRIPTION_ASC); 
			}
			else if (sortColumn == TaskSort.MINUTES_SORT)
			{
				Collections.sort(this, TaskSort.MINUTES_ASC); 
			}
			else if (sortColumn == TaskSort.HOURS_SORT)
			{
				Collections.sort(this, TaskSort.HOURS_ASC); 
			}
		}
		else
		{
			if (sortColumn == TaskSort.ID_SORT)
			{
				Collections.sort(this, TaskSort.ID_DESC); 
			}
			else if (sortColumn == TaskSort.DESCRIPTION_SORT)
			{
				Collections.sort(this, TaskSort.DESCRIPTION_DESC); 
			}
			else if (sortColumn == TaskSort.MINUTES_SORT)
			{
				Collections.sort(this, TaskSort.MINUTES_DESC); 
			}
			else if (sortColumn == TaskSort.HOURS_SORT)
			{
				Collections.sort(this, TaskSort.HOURS_DESC); 
			}
		}
	}


	private boolean indexOutOfRange(int index)
	{
		return (index < 0) || (index > 3);
	}
	
}
