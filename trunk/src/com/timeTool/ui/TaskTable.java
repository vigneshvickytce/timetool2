package com.timeTool.ui;

import com.timeTool.ResourceAutomation;
import com.timeTool.TimeTool;
import com.timeTool.Task;

import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Collections;

public class TaskTable extends AbstractTableModel 
{
	private static final int NUM_COLUMNS = 4;
	private final String[] columns;
	private final List<Task> tasks;


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
        Task task = tasks.get(row);
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
}
