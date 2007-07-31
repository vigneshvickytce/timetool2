package com.timeTool.test;

import com.timeTool.MySQLOptionPlugin;

import junit.framework.TestCase;

public class MySQLOptionPluginTest extends TestCase
{

	/*
	 * Test method for 'com.timeTool.MySQLOptionPlugin.MySQLOptionPlugin()'
	 */
	public void testMySQLOptionPlugin() throws Exception
	{
		MySQLOptionPlugin data = new MySQLOptionPlugin(); 
		assertEquals("com.mysql.jdbc.Driver", data.getDatabaseClass()); 
		assertEquals("jdbc:mysql://localhost/test", data.getDatabaseURL()); 
		assertEquals("sa", data.getPassword()); 
		assertEquals("select tasks.task_id, tasks.task_name, description_id, description_text from tasks, task_descriptions where tasks.task_id = task_descriptions.task_id order by tasks.task_name", data.getSqlSelect()); 
		assertEquals("root", data.getUserName()); 
		assertEquals(false, data.getEnabled().booleanValue()); 

	}

}
