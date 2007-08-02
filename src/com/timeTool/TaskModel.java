package com.timeTool;

import java.util.*;

public class TaskModel extends ArrayList<Task>
{

	private boolean ascending = true; 
	private int sortColumn = 0; 
    private static final Map<Integer, Comparator<Task>> ascendingSortMap = new HashMap<Integer, Comparator<Task>>();
    private static final Map<Integer, Comparator<Task>> descendingSortMap = new HashMap<Integer, Comparator<Task>>();

    static {
        ascendingSortMap.put(TaskSort.ID_SORT, TaskSort.ID_ASC);
        ascendingSortMap.put(TaskSort.DESCRIPTION_SORT, TaskSort.DESCRIPTION_ASC);
        ascendingSortMap.put(TaskSort.MINUTES_SORT, TaskSort.MINUTES_ASC);
        ascendingSortMap.put(TaskSort.HOURS_SORT, TaskSort.HOURS_ASC);

        descendingSortMap.put(TaskSort.ID_SORT, TaskSort.ID_DESC);
        descendingSortMap.put(TaskSort.DESCRIPTION_SORT, TaskSort.DESCRIPTION_DESC);
        descendingSortMap.put(TaskSort.MINUTES_SORT, TaskSort.MINUTES_DESC);
        descendingSortMap.put(TaskSort.HOURS_SORT, TaskSort.HOURS_DESC); 
    }

    public TaskModel() {
		super(); 
	}

	public void sort(int index) {
		if (indexOutOfRange(index)) {
			return;
		}

		if (sortColumn == index) {
			ascending = !ascending; 
		}
		sortColumn = index; 
		
		if (ascending) {
            Collections.sort(this, ascendingSortMap.get(sortColumn));
		} else {
            Collections.sort(this, descendingSortMap.get(sortColumn));
		}
	}


	private boolean indexOutOfRange(int index) {
		return (index < 0) || (index > 3);
	}
	
}
