package com.timeTool.ui;

import com.timeTool.ui.CommonDialog;
import com.timeTool.FilePersistence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MySQLOptionPlugin 
{
	private static final String ENABLED_TAG = "Enabled";
	private static final String DATABASE_URL_FIELD = "Database URL";
	private static final String USER_NAME_FIELD = "User Name";
	private static final String PASSWORD_FIELD = "Password";
	private static final String SQL_FIELD = "SQL";
	private static final String DRIVER_FIELD = "JDBC Driver";
	private static final String DATAFILE = "mysqloptions.xml";
	private static String password; 
	private static String userName; 
	private static String databaseURL; 
	private static String databaseClass; 
	private static String sqlSelect;
	private static Boolean enabled = new Boolean(false); 
	private JTextField driverField; 
	private JTextField databaseURLField;
	private JTextField usernameField;
	private JTextField passwordField;
	private JTextField selectField;
	private JCheckBox enabledField;

	public MySQLOptionPlugin() throws Exception
	{
		loadOptions();
	}
	private void loadOptions() throws Exception
	{
		BufferedReader file = null; 
		String xmlData = ""; 
		try
		{
			file = new BufferedReader(new FileReader(DATAFILE));
			String line = file.readLine();  
			while (line != null)
			{
				xmlData = xmlData + line; 
				line = file.readLine();
			}
			FilePersistence helper = new FilePersistence();
			sqlSelect = helper.extractFromTag(xmlData, SQL_FIELD); 
			databaseClass = helper.extractFromTag(xmlData, DRIVER_FIELD);
			databaseURL = helper.extractFromTag(xmlData, DATABASE_URL_FIELD);
			userName = helper.extractFromTag(xmlData, USER_NAME_FIELD);
			password = helper.extractFromTag(xmlData, PASSWORD_FIELD);
			enabled = new Boolean(helper.extractFromTag(xmlData, ENABLED_TAG));
			 
		}
		finally
		{
			if (file != null)
			{
				try
				{
					file.close();
				}
				catch (IOException e)
				{
					//don't worry about file close messages
				} 
			}
		}

	}
	
	public JPanel configurationOptions(CommonDialog parent)
	{
		JPanel panel = new JPanel(); 
        parent.createGridBag(panel); 
        parent.addLabel(DRIVER_FIELD, 0, panel); 
        driverField = parent.addField(0, getDatabaseClass(), panel);
		parent.addLabel(DATABASE_URL_FIELD, 1, panel); 
        databaseURLField = parent.addField(1, getDatabaseURL(), panel);
		parent.addLabel(USER_NAME_FIELD, 2, panel); 
        usernameField = parent.addField(2, getUserName(), panel);
		parent.addLabel(PASSWORD_FIELD, 3, panel); 
        passwordField = parent.addField(3, getPassword(), panel);
		parent.addLabel(SQL_FIELD, 4, panel); 
        selectField = parent.addField(4, getSqlSelect(), panel);
		
        enabledField = new JCheckBox();
		parent.addLabel(ENABLED_TAG, 5, panel); 
        enabledField = parent.addCheckbox(5, getEnabled(), panel);
        
        parent.addButtons(panel); 
        
		return panel; 
	}
	private void serialize() throws Exception
	{
		FilePersistence helper = new FilePersistence(); 
		PrintWriter file = helper.createWriter(DATAFILE);
		file.write("<mysqloptions>"); 
		file.write(helper.wrapDataInTag(getDatabaseClass(), DRIVER_FIELD)); 
		file.write(helper.wrapDataInTag(getDatabaseURL(), DATABASE_URL_FIELD)); 
		file.write(helper.wrapDataInTag(getUserName(), USER_NAME_FIELD)); 
		file.write(helper.wrapDataInTag(getPassword(), PASSWORD_FIELD)); 
		file.write(helper.wrapDataInTag(getSqlSelect(), SQL_FIELD)); 
		file.write(helper.wrapDataInTag(getEnabled().toString(), ENABLED_TAG)); 
		file.write("</mysqloptions>"); 
		file.close();
	}
	
	public void onOK() throws Exception
	{
		databaseClass = driverField.getText(); 
		databaseURL = databaseURLField.getText(); 
		userName = usernameField.getText(); 
		password = passwordField.getText(); 
		sqlSelect = selectField.getText(); 
		enabled = enabledField.isSelected(); 
		serialize();
	}

	public Boolean getEnabled()
	{
		return enabled; 
	}
	public JPanel getAddTaskPanel(CommonDialog parent)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public static String getPassword()
	{
		return password;
	}
	
	public static String getUserName()
	{
		return userName;
	}

	public static String getDatabaseURL()
	{
		return databaseURL;
	}
	public static String getDatabaseClass()
	{
		return databaseClass;
	}
	public static String getSqlSelect()
	{
		return sqlSelect;
	}
	public static void setEnabled(Boolean state)
	{
		enabled = state; 
	}


}
