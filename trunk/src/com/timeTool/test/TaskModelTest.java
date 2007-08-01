package com.timeTool.test;

import java.util.Collections;

import com.timeTool.Task;
import com.timeTool.TimePersistence;
import com.timeTool.TimeTool;

import junit.framework.TestCase;

public class TaskModelTest extends TestCase
{
	private TimeTool data;

	protected void setUp() throws Exception
	{
		super.setUp();
		data = new TimeTool(); 
		TimePersistence file = new TimePersistence(data);
		file.loadFile(data); 
	}

	/*
	 * Test method for 'com.timeTool.TaskModel.sort(int)'
	 */
	public void testSort()
	{
		Task task = null; 
		data.sort(0); 
		task = data.get(0); 
		assertEquals("Anderson - Billed", task.getDescription()); 
		data.sort(0); 
		task = data.get(0); 
		assertEquals("Travel", task.getDescription()); 
		data.sort(1); 
		task = data.get(0); 
		assertEquals("Z-PresAles", task.getDescription()); 
		data.sort(1); 
		task = data.get(0); 
		assertEquals("Anderson - Billed", task.getDescription()); 
		data.sort(2); 
		task = data.get(0); 
		assertEquals("Anderson - Billed", task.getDescription()); 
		data.sort(2); 
		task = data.get(0); 
		assertEquals("Kohls - Billed", task.getDescription()); 
		data.sort(3); 
		task = data.get(0); 
		assertEquals("Kohls - Billed", task.getDescription()); 
		data.sort(3); 
		task = data.get(0); 
		assertEquals("Anderson - Billed", task.getDescription()); 
	}
}
