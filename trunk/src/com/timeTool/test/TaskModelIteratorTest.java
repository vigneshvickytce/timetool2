package com.timeTool.test;

import com.timeTool.Task;
import com.timeTool.TaskIterator;
import com.timeTool.TimeTool;
import com.timeTool.TimePersistence;

import junit.framework.TestCase;

public class TaskModelIteratorTest extends TestCase
{
	TimeTool data = new TimeTool(); 

	protected void setUp() throws Exception
	{
		super.setUp();
		TimePersistence file = new TimePersistence(data, null);
		file.loadFile();
	}

	/*
	 * Test method for 'com.timeTool.TaskModelIterator.getFirst()'
	 */
	public void testGetFirst()
	{
		TaskIterator iterator = new TaskIterator(data);
		Task row = iterator.getFirst(); 
		assertEquals("1", row.getId()); 
		assertEquals("Anderson - Billed", row.getDescription()); 
		assertEquals("0", row.getMinutes()); 
		assertEquals("0.00", row.getHours()); 
	}

	/*
	 * Test method for 'com.timeTool.TaskModelIterator.getNext()'
	 */
	public void testGetNext()
	{
		TaskIterator iterator = new TaskIterator(data);
		iterator.getFirst(); 
		Task row = iterator.getNext(); 
		assertEquals("1", row.getId()); 
		assertEquals("Don - Billed", row.getDescription()); 
		assertEquals("0", row.getMinutes()); 
		assertEquals("0.00", row.getHours()); 
		
		int count = 1; 
		iterator.getFirst(); 
		while (iterator.getNext() != null)
		{
			count++; 
		}
		assertEquals(10, count); 
	}

}
