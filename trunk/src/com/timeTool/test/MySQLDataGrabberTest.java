package com.timeTool.test;

import java.util.ArrayList;
import java.util.HashMap;

import com.timeTool.MySQLDataGrabber;
import com.timeTool.OptionsDialog;

import junit.framework.TestCase;

public class MySQLDataGrabberTest extends TestCase
{

	public void testGetTaskList() throws Exception
	{
		MySQLDataGrabber data = new MySQLDataGrabber(); 
		ArrayList descriptions = data.getDescriptionList("C-ANDE02-BAYP-01");
		assertEquals(2, descriptions.size());
		String[] desc = (String[])descriptions.get(1); 
		assertEquals("2", desc[0]); 
		assertEquals("CPTK-MANAGE", desc[1]); 
		
		ArrayList tasks = data.getTaskList();
		assertEquals(8, tasks.size());
		String[] task = (String[])tasks.get(4); 
		assertEquals("5", task[0]); 
		assertEquals("C-LIBT01-PORT-FL", task[1]); 
	}

	/*
	 * Test method for 'com.timeTool.SpyderDataGrabber.getDescriptionList(String)'
	 */
	public void testGetDescriptionList()
	{

	}
	
}
