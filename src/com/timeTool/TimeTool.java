package com.timeTool;

import java.awt.FileDialog;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import com.jeans.trayicon.*;


public class TimeTool extends Observable
{
	public static final int NO_ROW_SELECTED = -1;

	private static TimeTool _instance;	//singleton
	private TaskModel rows; 
	private Date currentTime;
	private Date startTime; 
	private int currentRow;
    public static ResourceAutomation resources;
	private static TimeToolWindow timeToolWindow; 

	static
    {
    	ResourceAutomation.initResources(); 
    }

    private TimeTool()
	{
		rows = new TaskModel(); 
		currentRow = NO_ROW_SELECTED;
		setStartTime(null); 
	}
	public static TimeTool getInstance()
    {
      if (_instance == null)
      {
    	  _instance = new TimeTool();
    	  new Timer(1000, new SecondTimer()).start();
      }
   	  return _instance;
    }

	public Task get(int index)
	{
		return (Task)rows.get(index);
	}
	public void addRow(String number, String description)
	{
		Task row = new Task(number, description, 0); 
		rows.add(row); 		
		setChanged(); 
		notifyObservers(); 
	}
	public void addRow(String number, String description, Integer minutes)
	{
		Task row = new Task(number, description, minutes.longValue() * 60); 
		rows.add(row); 
	}
	
	public void about()
	{
		JOptionPane.showConfirmDialog(
				timeToolWindow.getFrame(), 
				ResourceAutomation.getResourceString("AboutMessage"), 
				ResourceAutomation.getResourceString("AboutTitle"), 
				JOptionPane.DEFAULT_OPTION, 
				JOptionPane.PLAIN_MESSAGE,
				new ImageIcon(resources.getResource("IconImage")));
	}

	public void addTask()
	{
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
	public void sort(int index)
	{
		currentRow = NO_ROW_SELECTED;
		rows.sort(index); 
		setChanged(); 
		notifyObservers();
	}
	
	public void adjustTime()
	{
		if (getCurrentTask() == ResourceAutomation.getResourceString("NoActiveTask"))
		{
			JOptionPane.showConfirmDialog(timeToolWindow.getFrame(),
	    			ResourceAutomation.getResourceString("NoTaskSelected"),
	    			ResourceAutomation.getResourceString("InformationTitle"), 
	    			JOptionPane.DEFAULT_OPTION);
			return; 
		}
		
		JOptionPane dialog = new JOptionPane(); 
		dialog.setOptionType(JOptionPane.OK_CANCEL_OPTION); 

		String response = (String)JOptionPane.showInputDialog(
				timeToolWindow.getFrame(), 
				ResourceAutomation.getResourceString("AdjustMessage"), 
				ResourceAutomation.getResourceString("AdjustTitle"), 
				JOptionPane.PLAIN_MESSAGE);
		
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
	public void removeRow(int row)
	{
		currentRow = NO_ROW_SELECTED; 
		rows.remove(row); 
		setChanged(); 
		notifyObservers();
	}
	public int getRowCount()
	{
		return rows.size();
	}
	public Object getValueAt(int row, int col) 
	{ 
		Task task = (Task)rows.get(row); 
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
	
	public void renameDialog()
	{
    	JTable taskList = timeToolWindow.getTaskList(); 
    	int currentRow = taskList.getSelectedRow(); 
    	
		if (currentRow > -1)
		{
			Task task = (Task)rows.get(currentRow); 
			RenameDialog dialog = new RenameDialog(timeToolWindow.getFrame(), task.getId(), task.getDescription()); 
			dialog.setVisible(true); 
			if (dialog.getResponse() == RenameDialog.OK)
			{
	    		rename(currentRow, dialog.getTask(), dialog.getDescription()); 
	    	}
       	}

	}
	public void rename(int currentRow, 
			String id, 
			String description)
	{
		Task task = (Task)rows.get(currentRow); 
		task.setId(id); 
		task.setDescription(description); 
		setChanged(); 
		notifyObservers();
	}
	public void reset()
	{
		currentRow = NO_ROW_SELECTED; 
		TaskModel resetList = new TaskModel(); 
		for (int x = 0; x < rows.size(); x++)
		{
			Task row = (Task)rows.get(x); 
			row.setSeconds(0);  
			resetList.add(row); 
		}
		rows = resetList; 
		setChanged(); 
		notifyObservers();
	}


    public void reloadTaskList()
    {
		currentRow = NO_ROW_SELECTED; 
		TimePersistence data = new TimePersistence(); 
		data.loadFile(this);
		setChanged(); 
		notifyObservers();
    }
    public void saveTaskList()
    {
    	TimePersistence data = new TimePersistence(); 
		try
		{
			data.saveFile(_instance, TXTVisitor.DATA_FILE);
		}
		catch (Exception e)
		{
			ErrorHandler.showError(timeToolWindow.getFrame(), e); 
		}
    }
    /**
     * @param frame
     */
    public void exportTaskList()
    {
    	TimePersistence data = new TimePersistence(); 
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
			data.exportFile(this, filename);
		}
		catch (Exception e)
		{
			ErrorHandler.showError(timeToolWindow.getFrame(), e); 
		}
		
    }
	public String getDefaultFilename(Date today)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMd");
		String filename = formatter.format(today) + ".csv";
		return filename;
	}
    
    public void tick()
    {
    	currentTime = new Date(); 
    	calculateMinutes(); 
		setChanged(); 
		notifyObservers();
    }
    private void calculateMinutes()
    {
    	if (currentRow != NO_ROW_SELECTED)
    	{
	    	if ((currentTime != null) && (getTimerStartTime() != null))
	    	{
	    		Task task = (Task)rows.get(currentRow); 
		    	//update the selected task
	    		long secondDifference = currentTime.getTime() - getTimerStartTime().getTime();
	    		secondDifference = secondDifference / 1000; 
	    		task.addSeconds(secondDifference); 
	    		rows.set(currentRow, task); 
	    		setStartTime(currentTime); 
	    	}
    	}
    }
    public Date getTime()
    {
    	return currentTime;  
    }
    public String getTotalMinutes()
    {
    	int totalMinutes = 0;  
		for (int x = 0; x < rows.size(); x++)
		{
			Task row = (Task)rows.get(x); 
			Integer rowMinutes = new Integer(row.getMinutes()); 
			totalMinutes = totalMinutes + rowMinutes.intValue(); 
		}
    	return new Integer(totalMinutes).toString(); 
    }
    public String getTotalHours()
    {
    	float totalHours = 0;  
		for (int x = 0; x < rows.size(); x++)
		{
			Task row = (Task)rows.get(x); 
			Float rowHours = new Float(row.getHours()); 
			totalHours = totalHours + rowHours.floatValue(); 
		}

    	return Task.formatHours(new Float(totalHours).toString()); 
    }
    public void setCurrentRow(int index)
    {
    	if (currentRow != index)
    	{
    		//update the start time to right now
    		setStartTime(new Date()); 
    	}
    	
    	if (index == NO_ROW_SELECTED)
    	{
    		setStartTime(null); 
    	}
    	currentRow = index;     	

		setChanged(); 
    	notifyObservers(); 
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
    	Task row = (Task)rows.get(currentRow);
    	return row.getDescription(); 
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
	    		if ((prefix.equals("+") == false) && 
	    			(prefix.equals("-") == false))
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
	    	setChanged(); 
	    	notifyObservers(); 
		}
    }
	private Integer getMinutesFromAdjustString(String adjustment) throws Exception
	{
		try
		{
			return new Integer(adjustment);
		}
		catch (Exception e)
		{
			Exception invalid = new Exception(ResourceAutomation.getResourceString("NumericOnly")); 
			throw invalid; 
		}
	}
	public void options()
	{
		OptionsDialog dialog = new OptionsDialog(timeToolWindow.getFrame()); 
		dialog.setVisible(true); 
	}
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			timeToolWindow = new TimeToolWindow();
			timeToolWindow.show(); 
	    }
		catch (Exception e)
		{
			JOptionPane.showConfirmDialog(timeToolWindow.getFrame(),
	    			e.getMessage(),
	    			ResourceAutomation.getResourceString("GenericError"), 
	    			JOptionPane.DEFAULT_OPTION);
		}
	}
	public void setStartTime(Date startTime)
	{
		this.startTime = startTime;
	}
	public Date getTimerStartTime()
	{
		return startTime;
	}
}
