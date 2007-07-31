package com.timeTool.test;

import com.timeTool.Task;

import junit.framework.TestCase;

public class TaskTest extends TestCase
{

	/*
	 * Test method for 'timeTool.Task.addSeconds(long)'
	 */
	public void testAddSeconds()
	{
		Task task = new Task("x", "task x", 61); 
		task.addSeconds(60); 
		assertEquals("2", task.getMinutes()); 
	}

	/*
	 * Test method for 'timeTool.Task.getMinutes()'
	 */
	public void testGetMinutes()
	{
		Task task = new Task("x", "task x", 121); 
		assertEquals("2", task.getMinutes()); 
	}

	/*
	 * Test method for 'timeTool.Task.getHours()'
	 */
	public void testGetHours()
	{
		Task task = new Task("x", "task x", 121); 
		assertEquals("0.03", task.getHours()); 
	}
	
}
