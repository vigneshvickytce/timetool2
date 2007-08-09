package com.timeTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskModel
{

	public static final int NO_ROW_SELECTED = -1;
	private static final Map<Integer, Comparator<Task>> ascendingSortMap = new HashMap<Integer, Comparator<Task>>();
    private static final Map<Integer, Comparator<Task>> descendingSortMap = new HashMap<Integer, Comparator<Task>>();
	private boolean ascending = true;
	private int currentRow = NO_ROW_SELECTED;
	private Date currentTime;

	private final List<Task> list = new ArrayList<Task>();
	private final ResourceAutomation resources;
	private int sortColumn = 0;
	private Date startTime = null;

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

	public TaskModel(ResourceAutomation resources) {
		this.resources = resources;
	}

	public synchronized Task addRow(String number, String description) {
	   final Task row = new Task(number, description, 0);
	   list.add(row);
	   return row;
	}

	public synchronized Task addRow(String number, String description, long seconds) {
	   final Task row = new Task(number, description, seconds);
	   list.add(row);
	   return row;
	}

	public synchronized Task adjust(String adjustment) throws Exception {
		if (currentRow != NO_ROW_SELECTED)
		{
			String prefix = "";
			Integer minutes = null;
			if (adjustment.length() > 1)
			{
				prefix = adjustment.substring(0,1);
				if ((!prefix.equals("+")) && (!prefix.equals("-")))
				{
					prefix = "";
				}
			}

			if (prefix.equals(""))
			{
				minutes = getMinutesFromAdjustString(adjustment);
			}
			else
			{
				minutes = getMinutesFromAdjustString(adjustment.substring(1));
			}
			Task task = list.get(currentRow);

			if (prefix.equals("+"))
			{
				task.addSeconds(minutes.longValue() * 60);
			}
			else if (prefix.equals("-"))
			{
				task.addSeconds(minutes.longValue() * -60);
			}
			else
			{
				task.setSeconds(minutes.longValue() * 60);
			}
			list.set(currentRow, task);
			return task;
		}
		return null;
	}

	public List<Task> asList() {
		return Collections.unmodifiableList(list);
	}

	private synchronized void calculateMinutes() {
		if (currentRow != NO_ROW_SELECTED) {
			if ((currentTime != null) && (startTime != null)) {
				final Task task = list.get(currentRow);
				//update the selected task
				long secondDifference = currentTime.getTime() - startTime.getTime();
				secondDifference = secondDifference / 1000;
				task.addSeconds(secondDifference);
				startTime = currentTime;
			}
		}
	}

	public synchronized void clear() {
		currentRow = NO_ROW_SELECTED;
		list.clear();
	}

	public synchronized void deselect() {
		currentRow = NO_ROW_SELECTED;
	}

	public synchronized int getCurrentRow() {
		return currentRow;
	}

	public synchronized String getCurrentTask() {
		if (currentRow == NO_ROW_SELECTED) {
    		return resources.getResourceString("NoActiveTask");
    	}
    	Task row = list.get(currentRow);
    	return row.getDescription();
	}

	private Integer getMinutesFromAdjustString(String adjustment) throws Exception {
		try {
			return new Integer(adjustment);
		} catch (Exception e) {
            throw new Exception(resources.getResourceString("NumericOnly"));
		}
	}

	public synchronized Date getStartTime() {
		return startTime;
	}

	public synchronized Date getTime() {
		return currentTime;
	}

	private static boolean indexOutOfRange(int index) {
		return (index < 0) || (index > 3);
	}

	private void innerSort(int columnIndex) {
		if (indexOutOfRange(columnIndex)) return;

		if (sortColumn == columnIndex) {
			ascending = !ascending;
		}
		sortColumn = columnIndex;

		if (ascending) {
            Collections.sort(list, ascendingSortMap.get(sortColumn));
		} else {
            Collections.sort(list, descendingSortMap.get(sortColumn));
		}
	}

	public synchronized void remove(int index) {
		deselect();
		if (list.size() > index) list.remove(index);
	}

	public synchronized Task rename(int index, String id, String description) {
		if (list.size() > index) {
			final Task task = list.get(index);
			task.setId(id);
			task.setDescription(description);
			return task;
		}
		return null;
	}

	public synchronized void reset() {
		currentRow = NO_ROW_SELECTED;
		for (Task row : list) {
            row.setSeconds(0L);
        }
	}

	public synchronized Task setCurrentRow(int index) {
		if (index >= list.size()) return null;

		currentRow = index;
		if (currentRow == NO_ROW_SELECTED) {
			setStartTime(null);
			return null;
		} else {
			setStartTime(new Date());
			return list.get(currentRow);
		}
	}

	public synchronized void setStartTime(Date startTime) {
		this.startTime = startTime;
		if (startTime == null) {
			currentRow = NO_ROW_SELECTED;
		}
	}

	public synchronized Task sort(int index) {
		final Task currentTask = list.get(currentRow);
        currentRow = NO_ROW_SELECTED;
		innerSort(index);
        currentRow = list.indexOf(currentTask);
		return currentTask;
	}

	public synchronized Task tick() {
		if (currentRow != NO_ROW_SELECTED) {
			currentTime = new Date();
			calculateMinutes();
			return list.get(currentRow);
		}
		return null;
	}
}