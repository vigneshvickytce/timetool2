package com.timeTool.ui;

import com.timeTool.ResourceAutomation;
import com.timeTool.Task;

import java.util.Collections;
import java.util.List;
import java.util.Comparator;

import javax.swing.table.AbstractTableModel;

public class TaskTable extends AbstractTableModel 
{
	private static final int NUM_COLUMNS = 4;
	private final String[] columns;
	private final List<Task> tasks;
	private  boolean ascending = false;

	public TaskTable(List<Task> tasks, ResourceAutomation resources) {
		this.tasks = Collections.synchronizedList(tasks);
		columns = new String[]{
			resources.getResourceString("GridTaskHeader"),
			resources.getResourceString("GridDescHeader"),
			resources.getResourceString("GridMinsHeader"),
			resources.getResourceString("GridHourHeader")};
	}
	
	@Override
	public String getColumnName(int column) {
		return columns[column]; 	
	}
	
	public int getRowCount() {
		return tasks.size(); 
	}

	public int getColumnCount() {
		return NUM_COLUMNS;
	}

    public Task getTaskAt(int index) {
        return tasks.get(index);
    }
    public Object getValueAt(int row, int col) {
        final Task task = tasks.get(row);
		if (col == 0) {
            return task.getId();
        } else if (col == 1) {
            return task.getDescription();
        } else if (col == 2) {
            return task.getMinutes();
        } else if (col == 3) {
            return task.getHours();
        }
        return null;   
	}

    public int getIndexOf(Task currentTask) {
        return tasks.indexOf(currentTask); 
    }

    public void add(Task task) {
        tasks.add(task); 
    }

    public void remove(Task task) {
        tasks.remove(task); 
    }


	public void sort(int index) {
		ascending = !ascending; //flip sort order

		Collections.sort(tasks, new TaskComparator(index));
	}


	private class TaskComparator implements Comparator<Task> {
		private final int column;


		TaskComparator(int column) {
			this.column = column;
		}


		public int compare(Task o1, Task o2) {
			if (!TaskTable.this.ascending) {
				Task swap = o1;
				o1 = o2; 
				o2 = swap;
			}
			if (column == 0) {
				return o1.getId().compareTo(o2.getId());
			} else if (column == 1) {
				return o1.getDescription().compareTo(o2.getDescription());
			} else if (column == 2) {
				return Long.valueOf(o1.getMinutes()).compareTo(Long.valueOf(o2.getMinutes()));
			} else if (column == 3) {
				return Float.valueOf(o1.getHours()).compareTo(Float.valueOf(o2.getHours()));
			}
			return 0;
		}
	}
}
