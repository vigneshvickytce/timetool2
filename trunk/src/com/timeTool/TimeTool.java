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

import javax.swing.*;


public class TimeTool {

	private static TimeToolWindow timeToolWindow;
    private ScheduledFuture<?> autoSaveJob;
    private ScheduledExecutorService jobExecutor;
	private final List<TimeToolListener> listeners = new ArrayList<TimeToolListener>();
    private ResourceAutomation resources;
	private TaskModel dataModel;

	public static void main(String[] args)	{
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
            }
        });
        
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
		dataModel = new TaskModel();
		jobExecutor = Executors.newScheduledThreadPool(2);

        TimePersistence data = new TimePersistence(dataModel, resources);
        data.loadFile();
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
		synchronized(listeners) {
            listeners.add(listener);
        }
	}

    public void addRow(String number, String description) {
		Task row = dataModel.addRow(number, description);
		update(row);
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


    public void adjust(String adjustment)  {
        Task task = dataModel.adjust(adjustment);
        update(task);
    }


	public void adjustTime(String initialValue)

	{
		if (getCurrentTaskDescription().equals(resources.getResourceString("NoActiveTask")))
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
			try {
				adjust(response);
			} catch (NumberFormatException nfe) {
                JOptionPane.showConfirmDialog(timeToolWindow.getFrame(),
						resources.getResourceString("NumericOnly"),
						resources.getResourceString("GenericError"),
						JOptionPane.DEFAULT_OPTION);
            } catch (Exception e) {
				JOptionPane.showConfirmDialog(timeToolWindow.getFrame(),
						e.getMessage(),
						resources.getResourceString("GenericError"),
						JOptionPane.DEFAULT_OPTION);
			}
		}
	}

	public void clear()	{
		dataModel.clear();
	}


	public void close() {
		saveTaskList();
		System.exit(0);
	}


    public void exportTaskList() {
    	TimePersistence data = new TimePersistence(dataModel, resources);
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

    public Task getCurrentTask() {
    	return dataModel.getCurrentTask();
    }

    public String getCurrentTaskDescription() {
        Task current = dataModel.getCurrentTask();
        if (current == null) {
            return resources.getResourceString("NoActiveTask");
        } else {
            return current.getDescription();
        }
    }


	public String getDefaultFilename(Date today) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMd");
        return formatter.format(today) + ".csv";
	}

	public int getRowCount() {
		return dataModel.asList().size();
	}

    public List<Task> getTaskList() {
		return dataModel.asList();
	}

    public Date getTime() {
    	return dataModel.getCurrentTime();
    }

	public Date getTimerStartTime() {
		return dataModel.getStartTime();
	}


    public String getTotalHours() {
    	float totalHours = 0;
        for (Task row : dataModel.asList()) {
            totalHours += new Float(row.getHours());
        }

        return EditableTask.formatHours(Float.toString(totalHours));
    }


    public String getTotalMinutes()

    {
    	int totalMinutes = 0;
        for (Task row : dataModel.asList()) {
            totalMinutes += new Integer(row.getMinutes());
        }
        return Integer.toString(totalMinutes);
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
            synchronized (listeners) {
                listeners.clear();
            }
            resources = new ResourceAutomation(newPrefs.getSkin());
            timeToolWindow = new TimeToolWindow(resources, this);
			timeToolWindow.getFrame().setLocation(origLocation);
			timeToolWindow.getFrame().setVisible(true);
        }
        startAutoSaveJob(newPrefs);
    }


    public void reloadTaskList()

    {
		dataModel.deselect();
		TimePersistence data = new TimePersistence(dataModel, resources);
		clear();
        data.loadFile();
        updateStopped();
    }

    private void updateStopped() {
        synchronized (listeners) {
            for (TimeToolListener listener : listeners) {
                listener.onTimerStopped();
            }
        }
    }


    public void removeRowDialog() {
        Task task = getCurrentTask();
        int response =
                JOptionPane.showConfirmDialog(timeToolWindow.getFrame(),
                resources.getResourceString("ConfirmDelete"),
                resources.getResourceString("ConfirmDeleteTitle"),
                JOptionPane.YES_NO_OPTION);
        if (response == 0) {
            updateStopped();
            Task removed = dataModel.remove(task);
            if (removed != null) {
                synchronized(listeners) {
                    for (TimeToolListener listener : listeners) {
                        listener.onTaskRemove(task); 
                    }
                }
            }
        }
	}


    public void renameDialog() {
        Task task = getCurrentTask();
        RenameDialog dialog = new RenameDialog(timeToolWindow.getFrame(), task.getId(), task.getDescription(), resources);
        dialog.setVisible(true);
        if (dialog.getResponse() == RenameDialog.OK) {
            Task updated = dataModel.rename(task, dialog.getTask(), dialog.getDescription());
            update(updated);
        }
	}

	private void reset() {
		dataModel.reset();
        updateStopped();
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
    	TimePersistence data = new TimePersistence(dataModel, resources);
		try {
			data.saveFile(TXTVisitor.DATA_FILE);
            synchronized (listeners) {
                for (TimeToolListener listener : listeners) {
                    listener.onSave();
                }
            }
        } catch (Exception e) {
			ErrorHandler.showError(timeToolWindow.getFrame(), e, resources);
		}
    }

    public void setCurrentRow(String current) {
		final Task task = dataModel.setCurrentRow(current);
		if (task == null) {
            updateStopped();
        } else {
            update(task);
		}
    }

	public void setStartTime(Date startTime) {
		dataModel.setStartTime(startTime);
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
		Task task = dataModel.tick();

        update(task);
    }

    private void update(Task task) {

        synchronized(listeners) {
            if (task != null) {
                for (TimeToolListener listener : listeners) {
                    listener.onTaskChange(task);
                }
            }
        }
    }


    public static abstract class TimeToolListener {

		public void onSave(){}

        public void onTaskChange(Task task){}

        public void onTaskRemove(Task task){}

		public void onTimerStopped(){}
	}
}