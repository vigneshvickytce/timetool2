package com.timeTool;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JPanel;

public class MySQLDataGrabber implements OptionsPlugin
{
	private MySQLOptionPlugin optionsPlugin; 
	private Connection connection;
	private ArrayList tasks;
	private JComboBox taskField;
	private JComboBox descriptionField = null;

	private class DropDownEntry
	{
		private String id; 
		private String text; 
		DropDownEntry(String id, String text)
		{
			this.id = id; 
			this.text = text; 
		}
		public String getID()
		{
			return id;
		}
		public String getText()
		{
			return text;
		}
		public String toString()
		{
			return getText(); 
		}
		
	}
	
	public MySQLDataGrabber() throws Exception
	{
		tasks = null; 
		optionsPlugin = new MySQLOptionPlugin(); 
	}

	public ArrayList getTaskList()  throws Exception
	{
		if (tasks == null)
		{
			populateData();
		}
		ArrayList results = new ArrayList(); 
		
		for (int x = 0; x < tasks.size(); x++)
		{
			String[] row = (String[])tasks.get(x);
			String[] task = new String[]{row[0], row[1]}; 
			boolean found = false; 
			for (int y = 0; y < results.size(); y++)
			{
				String[] thisTask = (String[])results.get(y); 
				
				if (thisTask[0].equals(task[0]) == true) 
				{
					found = true; 
				}
			}
			if (found == false)
			{
				results.add(task); 
			}
		}
		return results; 
	}
	public ArrayList getDescriptionList(String key) throws Exception
	{
		if (tasks == null)
		{
			populateData();
		}
		ArrayList results = new ArrayList(); 
		for (int x = 0; x < tasks.size(); x++)
		{
			String[] row = (String[])tasks.get(x);
			if (row[1].equals(key))
			{
				results.add(new String[]{row[2], row[3]}); 
			}
		}
		return results; 
	}
	
	private void populateData() throws Exception, SQLException
	{
		try
		{
			openConnection(); 
			Statement statement = connection.createStatement ();
			statement.executeQuery (optionsPlugin.getSqlSelect());
			ResultSet rs = statement.getResultSet ();
			tasks = new ArrayList();  
			while (rs.next ())
			{
				String taskID = rs.getString ("task_id");
				String taskName = rs.getString ("task_name");
				String descriptionID = rs.getString ("description_id");
				String descriptionName = rs.getString ("description_text");
				String[] row = new String[4]; 
				row[0] = taskID; 
				row[1] = taskName; 
				row[2] = descriptionID; 
				row[3] = descriptionName; 
				tasks.add(row); 
			}
			rs.close ();
			statement.close ();
		}
		finally
		{
			closeConnection();
		}
	}
	private void openConnection() throws Exception
	{
		connection = null;

        String url = MySQLOptionPlugin.getDatabaseURL();
        Class.forName (MySQLOptionPlugin.getDatabaseClass()).newInstance ();
        String userName = MySQLOptionPlugin.getUserName();
		String password = MySQLOptionPlugin.getPassword();
		connection = DriverManager.getConnection (url, userName, password);
	}
	private void closeConnection()
	{
		if (connection != null)
        {
            try
            {
                connection.close ();
            }
            catch (Exception e) { /* ignore close errors */ }
        }
	}

	public JPanel configurationOptions(CommonDialog parent)
	{
		return optionsPlugin.configurationOptions(parent); 
	}

	public void onOK() throws Exception
	{
		optionsPlugin.onOK(); 
	}


	public String getOptionsTitle()
	{
		return "MySQL Options"; 
	}


	public Boolean getEnabled()
	{
		return optionsPlugin.getEnabled(); 
	}

	public JPanel getAddTaskPanel(CommonDialog parent) throws Exception
	{
		JPanel panel = new JPanel(); 
        parent.createGridBag(panel); 
        
		parent.addLabel(ResourceAutomation.getResourceString(AddTaskDialog.TASK_LABEL), 0, panel);
		parent.addLabel(ResourceAutomation.getResourceString(AddTaskDialog.DESCRIPTION_LABEL), 1, panel);

		populateTaskDropDown();
		populateDescriptionDropDown(); 

		parent.addField(taskField, 0, panel); 
		parent.addField(descriptionField, 1, panel); 			
		parent.addButtons(panel); 
		
		return panel;
	}

	private void populateDescriptionDropDown() throws Exception
	{
		if (descriptionField == null)
		{
			descriptionField = new JComboBox();
		}
		else
		{
			descriptionField.removeAllItems(); 
		}
		DropDownEntry currentSelection = (DropDownEntry)taskField.getSelectedItem(); 
		String key = currentSelection.getText(); 

		ArrayList descriptionList = getDescriptionList(key);
		for (int x = 0; x < descriptionList.size(); x++)
		{
			String[] rawEntry = (String[])descriptionList.get(x); 
			DropDownEntry entry = new DropDownEntry(rawEntry[0], rawEntry[1]); 
			descriptionField.addItem(entry); 
		}
	}

	private void populateTaskDropDown() throws Exception
	{
		taskField = new JComboBox();
		ArrayList tasks = getTaskList(); 
		String[] taskList = new String[tasks.size()]; 
		for (int x = 0; x < tasks.size(); x++)
		{
			String[] rawEntry = (String[])tasks.get(x);
			DropDownEntry entry = new DropDownEntry(rawEntry[0], rawEntry[1]); 
			taskField.addItem(entry);  
		}
		taskField.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent arg0)
					{
						try
						{
							populateDescriptionDropDown();
						}
						catch (Exception e)
						{
							ErrorHandler.showError(null, e); 
						} 
					}
				});

	}
	
	public String getSelectedTask()
	{
		DropDownEntry entry = (DropDownEntry)taskField.getSelectedItem(); 
		return entry.getText(); 
	}
	public String getSelectedDescription()
	{
		DropDownEntry entry = (DropDownEntry)descriptionField.getSelectedItem(); 
		return entry.getText(); 
	}
}
