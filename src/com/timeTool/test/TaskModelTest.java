package com.timeTool.test;

import java.util.Collections;

import com.timeTool.Task;
import com.timeTool.TimePersistence;
import com.timeTool.TimeTool;

import junit.framework.TestCase;

public class TaskModelTest extends TestCase
{
	TimeTool data = TimeTool.getInstance(); 

	protected void setUp() throws Exception
	{
		super.setUp();
		TimePersistence file = new TimePersistence(); 
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
