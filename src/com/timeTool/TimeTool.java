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
	private TaskModel dataModel;
    private ScheduledExecutorService jobExecutor;
	private final List<TimeToolListener> listeners = new ArrayList<TimeToolListener>();
    private ResourceAutomation resources;

	public static void main(String[] args)	{
		
		final boolean suppressTasks; 
		if ((args.length == 1) && ("-notasks".equals(args[0]))) {
				suppressTasks = true; 
		} else {
			suppressTasks = false; 
		}

		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler(){

            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
            }
        });

        TimeTool controller = new TimeTool(suppressTasks);
		try
		{
            timeToolWindow = new TimeToolWindow(controller.resources, controller);
			timeToolWindow.show();
	    } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null,
	    			e.getMessage(),
	    			controller.resources.getResourceString("GenericError"),
	    			JOptionPane.DEFAULT_OPTION);
		}
	}

	public TimeTool(boolean suppressTasks) {
		final TimeToolPreferences options = new TimeToolPreferences();
		resources  = new ResourceAutomation(options.getSkin());
		dataModel = new TaskModel();
		jobExecutor = Executors.newScheduledThreadPool(2);

        TimePersistence data = new TimePersistence(dataModel, resources);
        data.loadFile();
    if (!suppressTasks) {
	  	startTimerJob();
  		startAutoSaveJob(options);
    }
	}

	public void about(JFrame frame) {
		JOptionPane.showConfirmDialog(
				frame,
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

	public void addTask(JFrame frame) {
		try
		{
			AddTaskDialog dialog = new AddTaskDialog(frame, resources);
			dialog.setVisible(true);
			if (dialog.getResponse() == AddTaskDialog.OK) {
				addRow(dialog.getTask(), dialog.getDescription());
			}
		} catch (IllegalArgumentException e) {
			JOptionPane.showConfirmDialog(frame,
					"A task with this ID already exists.",
					resources.getResourceString("InformationTitle"),
					JOptionPane.DEFAULT_OPTION);
		} catch (Exception e) {
			ErrorHandler.showError(frame, e, resources);
		}
	}

    public void adjust(String adjustment)  {
        Task task = dataModel.adjust(adjustment);
        update(task);
    }

	public void adjustTime(String initialValue, JFrame frame) {
		if (getCurrentTaskDescription().equals(resources.getResourceString("NoActiveTask")))
		{
			JOptionPane.showConfirmDialog(frame,
					resources.getResourceString("NoTaskSelected"),
					resources.getResourceString("InformationTitle"),
					JOptionPane.DEFAULT_OPTION);
			return;
		}

		AdjustTimeDialog dialog = new AdjustTimeDialog(frame, initialValue, resources);
		dialog.setVisible(true);
		dialog.dispose();
		String response = dialog.getResponse();
		if (response != null)
		{
			try {
				adjust(response);
			} catch (NumberFormatException nfe) {
                JOptionPane.showConfirmDialog(frame,
						resources.getResourceString("NumericOnly"),
						resources.getResourceString("GenericError"),
						JOptionPane.DEFAULT_OPTION);
            } catch (Exception e) {
				JOptionPane.showConfirmDialog(frame,
						e.getMessage(),
						resources.getResourceString("GenericError"),
						JOptionPane.DEFAULT_OPTION);
			}
		}
	}

	public void clear()	{
		dataModel.clear();
	}

	public void close(JFrame frame) {
		saveTaskList(frame);
		System.exit(0);
	}

    public void exportTaskList(JFrame frame) {
    	TimePersistence data = new TimePersistence(dataModel, resources);
    	FileDialog fileDialog = new FileDialog(frame, "TimeTool - Export to CSV");
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
			ErrorHandler.showError(frame, e, resources);
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

	public void options(JFrame frame) {
        final File originalSkin = new TimeToolPreferences().getSkin();
        OptionsDialog dialog = new OptionsDialog(frame, resources);
        dialog.setVisible(true);
        final TimeToolPreferences newPrefs = new TimeToolPreferences();
        if (!originalSkin.equals(newPrefs.getSkin())) {
            saveTaskList(frame);

            final Point origLocation = timeToolWindow.getLocation();
            timeToolWindow.hide();
            synchronized (listeners) {
                listeners.clear();
            }
            resources = new ResourceAutomation(newPrefs.getSkin());
            timeToolWindow = new TimeToolWindow(resources, this);
			timeToolWindow.setLocation(origLocation);
			timeToolWindow.show();
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

    public void removeRowDialog(JFrame frame) {
        Task task = getCurrentTask();
        int response =
                JOptionPane.showConfirmDialog(frame,
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

    public void renameDialog(JFrame frame) {
        Task task = getCurrentTask();
        RenameDialog dialog = new RenameDialog(frame, task.getId(), task.getDescription(), resources);
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

	public void resetDialog(JFrame frame){
		int response =
			JOptionPane.showConfirmDialog(frame,
			resources.getResourceString("ConfirmReset"),
			resources.getResourceString("ConfirmResetTitle"),
			JOptionPane.YES_NO_OPTION);

		if (response == 0)
		{
			reset();
		}
	}

    public void saveTaskList(JFrame frame) {
    	TimePersistence data = new TimePersistence(dataModel, resources);
		try {
			data.saveFile(TXTVisitor.DATA_FILE);
            synchronized (listeners) {
                for (TimeToolListener listener : listeners) {
                    listener.onSave();
                }
            }
        } catch (Exception e) {
			ErrorHandler.showError(frame, e, resources);
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
					saveTaskList(null);
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

    private void updateStopped() {
        synchronized (listeners) {
            for (TimeToolListener listener : listeners) {
                listener.onTimerStopped();
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