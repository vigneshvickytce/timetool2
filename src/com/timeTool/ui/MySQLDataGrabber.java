package com.timeTool.ui;

import com.timeTool.ErrorHandler;
import com.timeTool.ResourceAutomation;

import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

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


	public String getOptionsTitle(ResourceAutomation resources)
	{
		return "MySQL Options";
	}


	public Boolean getEnabled()
	{
		return optionsPlugin.getEnabled();
	}

	public JPanel getAddTaskPanel(CommonDialog parent) throws Exception
	{
		JPanel panel = new JPanel(new GridBagLayout());

        JLabel taskLabel = new JLabel(parent.getResources().getResourceString(AddTaskDialog.TASK_LABEL), SwingConstants.RIGHT);
        JLabel descriptionLabel = new JLabel(parent.getResources().getResourceString(AddTaskDialog.DESCRIPTION_LABEL), SwingConstants.RIGHT);
        populateTaskDropDown(parent);
		populateDescriptionDropDown();

        panel.setLayout(new GridBagLayout());
        panel.add(taskLabel,           new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
        panel.add(taskField,           new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
        panel.add(descriptionLabel,    new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
        panel.add(descriptionField,    new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
        panel.add(parent.getButtons(), new GridBagConstraints(1, 2, 1, 1, 0.0, 1.0, SOUTHEAST, NONE, new Insets(2,2,2,2), 0, 0));

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

	private void populateTaskDropDown(final CommonDialog parent) throws Exception
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
						try {
							populateDescriptionDropDown();
						} catch (Exception e) {
							ErrorHandler.showError(null, e, parent.getResources());
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
