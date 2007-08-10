package com.timeTool;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskModel
{

	private Task currentTask = null;
	private Date currentTime = null;
    private Date startTime = null;

	private final List<EditableTask> list = new ArrayList<EditableTask>();

	public synchronized Task addRow(String number, String description) {
	   final EditableTask row = new EditableTask(number, description, 0);
	   list.add(row);
	   return row;
	}

	public synchronized Task addRow(String number, String description, long millis) {
	   final EditableTask row = new EditableTask(number, description, millis);
	   list.add(row);
	   return row;
	}

	public synchronized Task adjust(String adjustment) {
        if ((currentTask != null) && list.contains(currentTask)) {
            EditableTask task = list.get(list.indexOf(currentTask));
			String prefix = "";
			long minutes = 0;
			if (adjustment.length() > 1) {
				prefix = adjustment.substring(0,1);
				if ((!prefix.equals("+")) && (!prefix.equals("-"))) {
					prefix = "";
				}
			}

			if (prefix.equals("")) {
                minutes = new Integer(adjustment);
			} else {
                minutes = new Integer(adjustment.substring(1));
			}

			if (prefix.equals("+")) {
				task.addMillis(minutes * 60000);
			} else if (prefix.equals("-")) {
				task.addMillis(minutes * -60000);
			} else {
				task.setMillis(minutes * 60000);
			}
            
			return task;
		}
		return null;
	}

	public synchronized List<Task> asList() {
        ArrayList<Task> copy = new ArrayList<Task>(list.size());
        for (Task task : list) {
            copy.add(task);
        }
        return copy; 
	}

    public synchronized void clear() {
		currentTask = null;
		list.clear();
	}

	public synchronized void deselect() {
		currentTask = null;
	}

	public synchronized Task getCurrentTask() {
		return currentTask;
	}


    public synchronized Date getStartTime() {
		return startTime;
	}

	public synchronized Date getCurrentTime() {
		return currentTime;
	}

	public synchronized Task remove(Task task) {
		deselect();
		if (list.remove(task)) return task;
        return null;
    }

	public synchronized Task rename(Task original, String id, String description) {

        if (!list.contains(original)) return null;

        EditableTask task = list.get(list.indexOf(original));
        task.setId(id);
        task.setDescription(description);
		return task;
	}

	public synchronized void reset() {
		currentTask = null;
		for (EditableTask row : list) {
            row.setMillis(0L);
        }
	}

	public synchronized Task setCurrentRow(String taskID) {
        if (taskID == null) {
            deselect();
            return null; 
        }

        EditableTask task = new EditableTask(taskID, "", 0);
        if (!list.contains(task)) return null;

        if (task.equals(currentTask)) return currentTask;

        currentTask = list.get(list.indexOf(task)); 
		startTime = new Date();
		return currentTask;
	}

	public synchronized void setStartTime(Date startTime) {
		this.startTime = startTime;
		if (startTime == null) {
			currentTask = null;
		}
	}

	public synchronized Task tick() {
		if ((currentTask != null) && (list.contains(currentTask))) {
			currentTime = new Date();
            if (startTime == null) throw new IllegalStateException("Null: startTime");
            //update the selected task
            long milliDifference = currentTime.getTime() - startTime.getTime();
            EditableTask task = list.get(list.indexOf(currentTask));
            task.addMillis(milliDifference);
            System.out.println("Tick. Start: " + startTime + " Curr: " + currentTime + " Millis: " + milliDifference);
            startTime = currentTime;
            return task;
		}
		return null;
	}
}