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

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;


public class TimeTool {

	private static TimeToolWindow timeToolWindow;
    private ScheduledFuture<?> autoSaveJob;
    private ScheduledExecutorService jobExecutor;
	private final List<TimeToolListener> listeners = new ArrayList<TimeToolListener>();
    private ResourceAutomation resources;
	private TaskModel rows;

	public static void main(String[] args)

	{
		TimeTool controller = new TimeTool();
		try
		{
            timeToolWindow = new TimeToolWindow(controller.resources, controller);
			timeToolWindow.show();
	    } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(timeToolWindow.getFrame(),
	    			e.getMessage(),
	    			controller.resources.getResourceString("GenericError"),
	    			JOptionPane.DEFAULT_OPTION);
		}
	}

	public TimeTool() {
		final TimeToolPreferences options = new TimeToolPreferences();
		resources  = new ResourceAutomation(options.getSkin());
		rows = new TaskModel(resources);
		jobExecutor = Executors.newScheduledThreadPool(2);

		startTimerJob();
		startAutoSaveJob(options);
	}

	public void about() {
		JOptionPane.showConfirmDialog(
				timeToolWindow.getFrame(),
				resources.getResourceString("AboutMessage"),
				resources.getResourceString("AboutTitle"),
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				resources.getImageResource("IconImage"));
	}

	public void addListener(TimeToolListener listener) {
		listeners.add(listener);
	}

    public void addRow(String number, String description) {
		Task row = rows.addRow(number, description);
		for (TimeToolListener listener : listeners) {
			listener.onTaskChange(row);
		}
	}

	public void addRow(String number, String description, Integer minutes) {
		rows.addRow(number, description, minutes.longValue() * 60);
	}

	public void addTask() {
		try
		{
			AddTaskDialog dialog = new AddTaskDialog(timeToolWindow.getFrame(), resources);
			dialog.setVisible(true);
			if (dialog.getResponse() == AddTaskDialog.OK) {
				addRow(dialog.getTask(), dialog.getDescription());
			}
		} catch (Exception e) {
			ErrorHandler.showError(timeToolWindow.getFrame(), e, resources);
		}
	}


    public void adjust(String adjustment) throws Exception

    {
		Task task = rows.adjust(adjustment);
		if (task != null) {
			for (TimeToolListener listener : listeners) {
				listener.onTaskChange(task);
			}
		}
	}


	public void adjustTime(String initialValue)

	{
		if (getCurrentTask().equals(resources.getResourceString("NoActiveTask")))
		{
			JOptionPane.showConfirmDialog(timeToolWindow.getFrame(),
					resources.getResourceString("NoTaskSelected"),
					resources.getResourceString("InformationTitle"),
					JOptionPane.DEFAULT_OPTION);
			return;
		}

		AdjustTimeDialog dialog = new AdjustTimeDialog(timeToolWindow.getFrame(), initialValue, resources);
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
						resources.getResourceString("GenericError"),
						JOptionPane.DEFAULT_OPTION);
			}
		}
	}

	public void clear()	{
		rows.clear();
	}


	public void close()

	{
		saveTaskList();
		System.exit(0);
	}


    public void exportTaskList()

    {
    	TimePersistence data = new TimePersistence(this, resources);
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
			ErrorHandler.showError(timeToolWindow.getFrame(), e, resources);
		}
    }

    public int getCurrentRow() {
    	return rows.getCurrentRow();
    }

    public String getCurrentTask() {
		return rows.getCurrentTask();
    }


	public String getDefaultFilename(Date today) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMd");
        return formatter.format(today) + ".csv";
	}

	public int getRowCount() {
		return rows.asList().size();
	}

    public List<Task> getTaskList() {
		return rows.asList();
	}

    public Date getTime() {
    	return rows.getTime();
    }

	public Date getTimerStartTime() {
		return rows.getStartTime();
	}


    public String getTotalHours()

    {
    	float totalHours = 0;
        for (Task row : rows.asList()) {
            totalHours += new Float(row.getHours());
        }

        return Task.formatHours(Float.toString(totalHours));
    }


    public String getTotalMinutes()

    {
    	int totalMinutes = 0;
        for (Task row : rows.asList()) {
            totalMinutes += new Integer(row.getMinutes());
        }
        return Integer.toString(totalMinutes);
    }


	public Object getValueAt(int row, int col)

	{
		Task task = rows.asList().get(row);
		if (col == 0) {
			return task.getId();
		} else if (col == 1) {
			return task.getDescription();
		} else if (col == 2) {
			return task.getMinutes();
		} else {
			return task.getHours();
		}
	}

	public void options() {
        final File originalSkin = new TimeToolPreferences().getSkin();
        OptionsDialog dialog = new OptionsDialog(timeToolWindow.getFrame(), resources);
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
			timeToolWindow.getFrame().setLocation(origLocation);
			timeToolWindow.getFrame().setVisible(true);
        }
        startAutoSaveJob(newPrefs);
    }


    public void reloadTaskList()

    {
		rows.deselect();
		TimePersistence data = new TimePersistence(this, resources);
		data.loadFile();
		for (TimeToolListener listener : listeners) {
			listener.onTimerStopped();
		}
    }


	public void removeRow(int row) {
		rows.remove(row);
		for (TimeToolListener listener : listeners) {
			listener.onTimerStopped();
		}
	}


    public void removeRowDialog() {
    	JTable taskList = timeToolWindow.getTaskList();
		if (taskList.getSelectedRow() > -1)
		{
			int response =
	    			JOptionPane.showConfirmDialog(timeToolWindow.getFrame(),
	    			resources.getResourceString("ConfirmDelete"),
	    			resources.getResourceString("ConfirmDeleteTitle"),
	    			JOptionPane.YES_NO_OPTION);
	    	if (response == 0)
	    	{
	    		removeRow(timeToolWindow.getTaskList().getSelectedRow());
	    	}
       	}
	}

	private void rename(int currentRow, String id, String description) {
		Task task = rows.rename(currentRow, id, description);
		if (task != null) {
			for (TimeToolListener listener : listeners) {
				listener.onTaskChange(task);
			}
		}
	}


	public void renameDialog() {
    	JTable taskList = timeToolWindow.getTaskList();
    	int currentRow = taskList.getSelectedRow();

		if (currentRow > -1)
		{
			Task task = rows.asList().get(currentRow);
			RenameDialog dialog = new RenameDialog(timeToolWindow.getFrame(), task.getId(), task.getDescription(), resources);
			dialog.setVisible(true);
			if (dialog.getResponse() == RenameDialog.OK) {
	    		rename(currentRow, dialog.getTask(), dialog.getDescription());
	    	}
       	}
	}

	private void reset() {
		rows.reset();
		for (TimeToolListener listener : listeners) {
			listener.onTimerStopped();
		}
	}


	public void resetDialog(){
		int response =
			JOptionPane.showConfirmDialog(timeToolWindow.getFrame(),
			resources.getResourceString("ConfirmReset"),
			resources.getResourceString("ConfirmResetTitle"),
			JOptionPane.YES_NO_OPTION);

		if (response == 0)
		{
			reset();
		}
	}


    public void saveTaskList() {
    	TimePersistence data = new TimePersistence(this, resources);
		try {
			data.saveFile(TXTVisitor.DATA_FILE);
			for (TimeToolListener listener : listeners) {
				listener.onSave();
			}
        } catch (Exception e) {
			ErrorHandler.showError(timeToolWindow.getFrame(), e, resources);
		}
    }

    public void setCurrentRow(int index) {
		final Task task = rows.setCurrentRow(index); 
		if (task == null) {
			for (TimeToolListener listener : listeners) {
				listener.onTimerStopped();
			}
		} else {
			for (TimeToolListener listener : listeners) {
				listener.onTaskChange(task);
			}
		}
    }

	public void setStartTime(Date startTime) {
		rows.setStartTime(startTime);
	}

	public void sort(int index) {
		Task task = rows.sort(index);

		if (task != null) {
			for (TimeToolListener listener : listeners) {
				listener.onTaskChange(task);
			}
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
		Task task = rows.tick();

		if (task != null) {
			for (TimeToolListener listener : listeners) {
				listener.onTaskChange(task);
			}
		}
	}


	public static abstract class TimeToolListener {

		public void onSave(){}


		public void onTaskChange(Task task){}


		public void onTimerStopped(){}
	}
}