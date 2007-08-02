package com.timeTool.ui;

import com.timeTool.FilePersistence;

import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
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

	public MySQLOptionPlugin() throws Exception {
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

		driverField = new JTextField(getDatabaseClass(), 20);
		databaseURLField = new JTextField(getDatabaseURL(), 20);
		usernameField = new JTextField(getUserName(), 10);
		passwordField = new JTextField(getPassword(), 10);

		selectField = new JTextField(getSqlSelect(), 30);
		enabledField = new JCheckBox();
		enabledField.setSelected(getEnabled());

		final JLabel driverLabel = new JLabel(DRIVER_FIELD);
		final JLabel dbUrlLabel = new JLabel(DATABASE_URL_FIELD);
		final JLabel usernameLabel = new JLabel(USER_NAME_FIELD);
		final JLabel passwordLabel = new JLabel(PASSWORD_FIELD);
		final JLabel sqlLabel = new JLabel(SQL_FIELD);
		final JLabel enabledLabel = new JLabel(ENABLED_TAG);

		final JPanel panel = 		new JPanel(new GridBagLayout());
		panel.add(driverLabel, 		new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(driverField, 		new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, NORTHWEST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(dbUrlLabel, 		new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(databaseURLField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, NORTHWEST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(usernameLabel, 	new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(usernameField, 	new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, NORTHWEST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(passwordLabel, 	new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(passwordField, 	new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, NORTHWEST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(sqlLabel, 		new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(selectField, 		new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, NORTHWEST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(enabledLabel, 	new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(enabledField, 	new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, NORTHWEST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(parent.getButtons(), new GridBagConstraints(1, 6, 1, 1, 0.0, 1.0, SOUTHEAST, NONE, new Insets(2,2,2,2), 0, 0));

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
