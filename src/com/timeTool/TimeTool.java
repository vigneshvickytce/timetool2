package com.timeTool;

import com.timeTool.ui.AddTaskDialog;
import com.timeTool.ui.AdjustTimeDialog;
import com.timeTool.ui.OptionsDialog;
import com.timeTool.ui.RenameDialog;
import com.timeTool.ui.TimeToolWindow;

import java.awt.FileDialog;
import java.awt.Point;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;


public class TimeTool {

	public static final int NO_ROW_SELECTED = -1;
	private static TimeToolWindow timeToolWindow;
    private ScheduledFuture<?> autoSaveJob;
	private int currentRow;
	private Date currentTime;
    private ScheduledExecutorService jobExecutor;
	private final List<TimeToolListener> listeners = new ArrayList<TimeToolListener>();
    private ResourceAutomation resources;

	private TaskModel rows;
	private Date startTime;

	public static void main(String[] args)

	{
		try
		{
            TimeTool controller = new TimeTool();
            timeToolWindow = new TimeToolWindow(controller.resources, controller);
			timeToolWindow.show();
	    } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(timeToolWindow.getFrame(),
	    			e.getMessage(),
	    			ResourceAutomation.getResourceString("GenericError"),
	    			JOptionPane.DEFAULT_OPTION);
		}
	}

	public TimeTool() {
		rows = new TaskModel();
		currentRow = NO_ROW_SELECTED;
		setStartTime(null);

		jobExecutor = Executors.newScheduledThreadPool(2);

		final TimeToolPreferences options = new TimeToolPreferences();
		resources  = new ResourceAutomation(options.getSkin());
		startTimerJob();
		startAutoSaveJob(options);
	}

	public void about() {
		JOptionPane.showConfirmDialog(
				timeToolWindow.getFrame(),
				ResourceAutomation.getResourceString("AboutMessage"),
				ResourceAutomation.getResourceString("AboutTitle"),
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				new ImageIcon(resources.getResource("IconImage")));
	}

	public void addListener(TimeToolListener listener) {
		listeners.add(listener);
	}

    public void addRow(String number, String description) {
		Task row = new Task(number, description, 0);
		rows.add(row);
		for (TimeToolListener listener : listeners) {
			listener.onTaskChange(row);
		}
	}

	public void addRow(String number, String description, Integer minutes) {
		Task row = new Task(number, description, minutes.longValue() * 60);
		rows.add(row);
	}

	public void addTask() {
		try
		{
			AddTaskDialog dialog = new AddTaskDialog(timeToolWindow.getFrame());
			dialog.setVisible(true);
			if (dialog.getResponse() == AddTaskDialog.OK)
			{
				int row = currentRow;
				currentRow = NO_ROW_SELECTED;
				addRow(dialog.getTask(), dialog.getDescription());
				timeToolWindow.getTable().fireTableRowsInserted(0,getRowCount());
				timeToolWindow.getTable().fireTableDataChanged();
				currentRow = row;
			}
		}
		catch (Exception e)
		{
			ErrorHandler.showError(timeToolWindow.getFrame(), e);
		}
	}


    public void adjust(String adjustment) throws Exception

    {
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
			Task task = get(currentRow);

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
			rows.set(currentRow, task);
			for (TimeToolListener listener : listeners) {
				listener.onTaskChange(task);
			}
		}
    }


	public void adjustTime(String initialValue)

	{
		if (getCurrentTask().equals(ResourceAutomation.getResourceString("NoActiveTask")))
		{
			JOptionPane.showConfirmDialog(timeToolWindow.getFrame(),
	    			ResourceAutomation.getResourceString("NoTaskSelected"),
	    			ResourceAutomation.getResourceString("InformationTitle"),
	    			JOptionPane.DEFAULT_OPTION);
			return;
		}

        AdjustTimeDialog dialog = new AdjustTimeDialog(timeToolWindow.getFrame(), initialValue);
        dialog.setVisible(true);
        dialog.dispose();
        String response = dialog.getResponse();
        if (response != null)
		{
			try
			{
				adjust(response);
			}
			catch (Exception e)
			{
				JOptionPane.showConfirmDialog(timeToolWindow.getFrame(),
		    			e.getMessage(),
		    			ResourceAutomation.getResourceString("GenericError"),
		    			JOptionPane.DEFAULT_OPTION);
			}
		}
	}


    private void calculateMinutes()

    {
    	if (currentRow != NO_ROW_SELECTED)
    	{
	    	if ((currentTime != null) && (getTimerStartTime() != null))
	    	{
	    		Task task = rows.get(currentRow);
		    	//update the selected task
	    		long secondDifference = currentTime.getTime() - getTimerStartTime().getTime();
	    		secondDifference = secondDifference / 1000;
	    		task.addSeconds(secondDifference);
	    		rows.set(currentRow, task);
	    		setStartTime(currentTime);
	    	}
    	}
    }


	public void clear()

	{
		currentRow = NO_ROW_SELECTED;
		for (int x = rows.size() - 1; x >= 0; x--)
		{
			removeRow(x);
		}
	}


	public void close()

	{
		saveTaskList();
		System.exit(0);
	}


    public void exportTaskList()

    {
    	TimePersistence data = new TimePersistence(this);
    	FileDialog fileDialog = new FileDialog(timeToolWindow.getFrame(), "TimeTool - Export to CSV");
    	fileDialog.setMode(FileDialog.SAVE);
    	Date today = new Date();
    	String filename = getDefaultFilename(today);

		fileDialog.setFile(filename);
    	//fileDialog.setFilenameFilter(null);
    	fileDialog.setVisible(true);
    	filename = fileDialog.getDirectory() + fileDialog.getFile();
		try
		{
			data.exportFile(filename);
		}
		catch (Exception e)
		{
			ErrorHandler.showError(timeToolWindow.getFrame(), e);
		}
    }

    public Task get(int index) {
		return rows.get(index);
	}


    public int getCurrentRow()

    {
    	return currentRow;
    }


    public String getCurrentTask()

    {
    	if (currentRow == NO_ROW_SELECTED)
    	{
    		return ResourceAutomation.getResourceString("NoActiveTask");
    	}
    	Task row = rows.get(currentRow);
    	return row.getDescription();
    }


	public String getDefaultFilename(Date today)

	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMd");
        return formatter.format(today) + ".csv";
	}

	private Integer getMinutesFromAdjustString(String adjustment) throws Exception {
		try {
			return new Integer(adjustment);
		} catch (Exception e) {
            throw new Exception(ResourceAutomation.getResourceString("NumericOnly"));
		}
	}


	public int getRowCount() {
		return rows.size();
	}


    public Date getTime() {
    	return currentTime;
    }

	public Date getTimerStartTime() {
		return startTime;
	}


    public String getTotalHours()

    {
    	float totalHours = 0;
        for (Task row : rows) {
            totalHours += new Float(row.getHours());
        }

        return Task.formatHours(Float.toString(totalHours));
    }


    public String getTotalMinutes()

    {
    	int totalMinutes = 0;
        for (Task row : rows) {
            totalMinutes += new Integer(row.getMinutes());
        }
        return Integer.toString(totalMinutes);
    }


	public Object getValueAt(int row, int col)

	{
		Task task = rows.get(row);
		if (col == 0)
		{
			return task.getId();
		}
		else if (col == 1)
		{
			return task.getDescription();
		}
		else if (col == 2)
		{
			return task.getMinutes();
		}
		else
		{
			return task.getHours();
		}
	}

	public void options() {
        final File originalSkin = new TimeToolPreferences().getSkin();
        OptionsDialog dialog = new OptionsDialog(timeToolWindow.getFrame());
        dialog.setVisible(true);
        final TimeToolPreferences newPrefs = new TimeToolPreferences();
        if (!originalSkin.equals(newPrefs.getSkin())) {
            saveTaskList();

            final JFrame frame = timeToolWindow.getFrame();
            final Point origLocation = frame.getLocation();
            frame.setVisible(false);
            frame.dispose();
			listeners.clear();
			resources = new ResourceAutomation(newPrefs.getSkin());
            timeToolWindow = new TimeToolWindow(resources, this);
            timeToolWindow.show();
            timeToolWindow.getFrame().setLocation(origLocation);
        }
        startAutoSaveJob(newPrefs);
    }


    public void reloadTaskList()

    {
		currentRow = NO_ROW_SELECTED;
		TimePersistence data = new TimePersistence(this);
		data.loadFile();
		for (TimeToolListener listener : listeners) {
			listener.onTimerStopped();
		}
    }


	public void removeRow(int row)

	{
		currentRow = NO_ROW_SELECTED;
		rows.remove(row);
		for (TimeToolListener listener : listeners) {
			listener.onTimerStopped();
		}
	}


    public void removeRowDialog()

	{
    	JTable taskList = timeToolWindow.getTaskList();
		if (taskList.getSelectedRow() > -1)
		{
			int response =
	    			JOptionPane.showConfirmDialog(timeToolWindow.getFrame(),
	    			ResourceAutomation.getResourceString("ConfirmDelete"),
	    			ResourceAutomation.getResourceString("ConfirmDeleteTitle"),
	    			JOptionPane.YES_NO_OPTION);
	    	if (response == 0)
	    	{
	    		removeRow(timeToolWindow.getTaskList().getSelectedRow());
	    	}
       	}
	}


	public void rename(int currentRow,
			String id,
			String description)

	{
		Task task = rows.get(currentRow);
		task.setId(id);
		task.setDescription(description);
		for (TimeToolListener listener : listeners) {
			listener.onTaskChange(task);
		}
	}


	public void renameDialog()

	{
    	JTable taskList = timeToolWindow.getTaskList();
    	int currentRow = taskList.getSelectedRow();

		if (currentRow > -1)
		{
			Task task = rows.get(currentRow);
			RenameDialog dialog = new RenameDialog(timeToolWindow.getFrame(), task.getId(), task.getDescription());
			dialog.setVisible(true);
			if (dialog.getResponse() == RenameDialog.OK)
			{
	    		rename(currentRow, dialog.getTask(), dialog.getDescription());
	    	}
       	}
	}


	public void reset()

	{
		currentRow = NO_ROW_SELECTED;
		TaskModel resetList = new TaskModel();
        for (Task row : rows) {
            row.setSeconds(0);
            resetList.add(row);
        }
        rows = resetList;
		for (TimeToolListener listener : listeners) {
			listener.onTimerStopped();
		}
	}


	public void resetDialog()

	{
		int response =
			JOptionPane.showConfirmDialog(timeToolWindow.getFrame(),
			ResourceAutomation.getResourceString("ConfirmReset"),
			ResourceAutomation.getResourceString("ConfirmResetTitle"),
			JOptionPane.YES_NO_OPTION);

		if (response == 0)
		{
			reset();
		}
	}


    public void saveTaskList()

    {
    	TimePersistence data = new TimePersistence(this);
		try {
			data.saveFile(TXTVisitor.DATA_FILE);
			for (TimeToolListener listener : listeners) {
				listener.onSave();
			}
        } catch (Exception e) {
			ErrorHandler.showError(timeToolWindow.getFrame(), e);
		}
    }


    public void setCurrentRow(int index) {
		if (index == NO_ROW_SELECTED) {
			currentRow = index;
    		setStartTime(null);
			for (TimeToolListener listener : listeners) {
				listener.onTimerStopped();
			}
		} else if (currentRow != index) {
			currentRow = index;
			//update the start time to right now
			setStartTime(new Date());
			for (TimeToolListener listener : listeners) {
				listener.onTaskChange(get(currentRow));
			}
    	}


    }

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


	public void sort(int index) {
        Task currentTask = rows.get(currentRow);

        currentRow = NO_ROW_SELECTED;
		rows.sort(index);
        currentRow = rows.indexOf(currentTask);
		for (TimeToolListener listener : listeners) {
			listener.onTaskChange(get(currentRow));
		}
	}

    private void startAutoSaveJob(TimeToolPreferences options) {
        long interval = options.getAutosave();
        if (autoSaveJob != null) {
            autoSaveJob.cancel(false);
        }
        autoSaveJob = jobExecutor.scheduleAtFixedRate(new Runnable() {

            public void run() {
				try {
					saveTaskList();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
        }, interval, interval, TimeUnit.SECONDS);
    }

    private void startTimerJob() {
        jobExecutor.scheduleAtFixedRate(new Runnable(){

            public void run() {
				try {
					tick();
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
        }, 1L, 1L, TimeUnit.SECONDS);
    }


    private void tick() {
		if (currentRow != NO_ROW_SELECTED) {
			currentTime = new Date();
			calculateMinutes();
			for (TimeToolListener listener : listeners) {
				listener.onTaskChange(get(currentRow));
			}
		}
	}


	public static abstract class TimeToolListener {

		public void onSave(){}
		public void onTaskChange(Task task){}
		public void onTimerStopped(){}
	}
}