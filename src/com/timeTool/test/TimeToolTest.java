package com.timeTool.test;

import com.timeTool.Task;
import com.timeTool.TimeTool;
import com.timeTool.TimePersistence;
import com.timeTool.ResourceAutomation;

import junit.framework.TestCase;

public class TimeToolTest extends TestCase
{
	TimeTool data = new TimeTool();

	protected void setUp() throws Exception
	{
		super.setUp();
		TimePersistence file = new TimePersistence(data);
		file.loadFile();
		data.setCurrentRow(-1); 
	}

	/*
	 * Test method for 'timeTool.TaskModel.get(int)'
	 */
	public void test1Get()
	{
		Task row = data.get(0); 
		assertEquals("1", row.getId()); 
		assertEquals("Anderson - Billed", row.getDescription()); 
		assertEquals("0", row.getMinutes()); 
		assertEquals("0.00", row.getHours()); 
	}

	/*
	 * Test method for 'timeTool.TaskModel.addRow(String, String)'
	 */
	public void test2AddRow()
	{
		data.addRow("X", "Test X"); 
		Task row = data.get(10); 
		assertEquals("X", row.getId()); 
		assertEquals("Test X", row.getDescription()); 
		assertEquals("0", row.getMinutes()); 
		assertEquals("0.00", row.getHours()); 
		data.addRow("Y", "Test Y", new Integer(61)); 
		row = data.get(11); 
		assertEquals("Y", row.getId()); 
		assertEquals("Test Y", row.getDescription()); 
		assertEquals("61", row.getMinutes()); 
		assertEquals("1.02", row.getHours()); 
	}


	/*
	 * Test method for 'timeTool.TaskModel.removeRow(int)'
	 */
	public void test3RemoveRow()
	{
		data.removeRow(9); 
		assertEquals(9, data.getRowCount()); 		
	}
	
	public void testRename()
	{
		data.rename(2, "test1", "test2"); 
		Task task = (Task)data.get(2); 
		assertEquals("test1", task.getId()); 
		assertEquals("test2", task.getDescription()); 
		
	}
	/*
	 * Test method for 'timeTool.TaskModel.getRowCount()'
	 */
	public void testGetRowCount()
	{
		assertEquals(10, data.getRowCount()); 		
	}

	 public void testRowSelection()
	 {
		 assertEquals(ResourceAutomation.getResourceString("NoActiveTask"), data.getCurrentTask());
		 data.setCurrentRow(3); 
		 assertEquals("Gap - Billed", data.getCurrentTask()); 
		 data.setCurrentRow(TimeTool.NO_ROW_SELECTED); 
		 assertEquals(ResourceAutomation.getResourceString("NoActiveTask"), data.getCurrentTask());
		 data.setCurrentRow(-1); 
	 }
	 public void testAdjust()
	 {
		 try
		 {
			 data.setCurrentRow(0); 
			 data.adjust("+61"); 
			 Task task = data.get(0); 
			 assertEquals("61", task.getMinutes()); 
			 data.adjust("-30"); 
			 task = data.get(0); 
			 assertEquals("31", task.getMinutes());
			 data.adjust("60"); 
			 task = data.get(0); 
			 assertEquals("60", task.getMinutes());
		 }
		 catch (Exception e)
		 {
			 fail(); 
		 }		 
	 }
	 public void testAdjustError()
	 {
		 data.setCurrentRow(0); 
		 try
		 {
			 data.adjust("+ZZZ"); 
			 fail(); //ZZZ should throw an exception
		 }
		 catch (Exception e)
		 {
			 assertEquals("Entry must be numeric", e.getMessage()); 
		 }
		 try
		 {
			 data.adjust("-ZZZ"); 
			 fail(); //ZZZ should throw an exception
		 }
		 catch (Exception e)
		 {
			 assertEquals("Entry must be numeric", e.getMessage()); 
		 }
		 try
		 {
			 data.adjust("ZZZ"); 
			 fail(); //ZZZ should throw an exception
		 }
		 catch (Exception e)
		 {
			 assertEquals("Entry must be numeric", e.getMessage()); 
		 }

	 }
}
