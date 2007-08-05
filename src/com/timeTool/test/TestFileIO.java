package com.timeTool.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;

import com.timeTool.TimeToolPreferences;
import com.timeTool.TXTVisitor;
import com.timeTool.Task;
import com.timeTool.TimeTool;
import com.timeTool.TimePersistence;


import junit.framework.TestCase;

public class TestFileIO extends TestCase
{
	private static final String TEST_CSV = "test.csv";
	private TimeTool data;


	@Override
	protected void setUp() throws Exception {
		super.setUp();
		data = new TimeTool();
	}


	public void tearDown()
	{
		File deleteFile = new File(TEST_CSV); 
		deleteFile.delete(); 
		deleteFile = new File(TimeToolPreferences.OPTIONS_FILENAME);
		deleteFile.delete();
	}
	
	public void test1Load()
	{
		TimePersistence file = new TimePersistence(data);
		file.loadFile();
		assertEquals(10, data.getRowCount()); 
		assertEquals("1", data.getValueAt(0,0)); 
		assertEquals("Anderson - Billed", data.getValueAt(0,1)); 
		assertEquals("0", data.getValueAt(0,2)); 
		assertEquals("0.00", data.getValueAt(0,3)); 

		assertEquals("3", data.getValueAt(9,0)); 
		assertEquals("Z-PresAles", data.getValueAt(9,1)); 
		assertEquals("21", data.getValueAt(9,2)); 
		assertEquals("0.35", data.getValueAt(9,3));
	}
	
	public void test2Delete()
	{
		TimePersistence file = new TimePersistence(data);
		file.loadFile();
		assertEquals(10, data.getRowCount()); 
		data.removeRow(9); 
		assertEquals(9, data.getRowCount()); 
		assertEquals("3", data.getValueAt(8,0)); 
		assertEquals("Travel", data.getValueAt(8,1));
		data.addRow("3", "Z-PresAles", new Integer(21)); 
		assertEquals(10, data.getRowCount()); 
	}
	
	public void test3Save() throws Exception
	{
		TimePersistence file = new TimePersistence(data);
		file.loadFile();

		assertEquals(10, data.getRowCount()); 
		data.removeRow(9); 
		file.saveFile(TXTVisitor.DATA_FILE);
		file.loadFile();
		assertEquals(9, data.getRowCount()); 
		data.addRow("3", "Z-PresAles", new Integer(21)); 
		file.saveFile(TXTVisitor.DATA_FILE);
		file.loadFile();
		assertEquals(10, data.getRowCount()); 
		
	}
	
	public void test4Reload()
	{
		TimePersistence file = new TimePersistence(data);
		file.loadFile();

		assertEquals(10, data.getRowCount()); 
		data.removeRow(9); 
		file.loadFile();

		assertEquals(10, data.getRowCount()); 
	}

	public void test4Export()  throws Exception
	{
		TimePersistence filewriter = new TimePersistence(data);
		filewriter.loadFile();
		filewriter.exportFile(TEST_CSV);

		BufferedReader file = new BufferedReader(new FileReader(TEST_CSV));
		String headerRecord = file.readLine();  
		String record = file.readLine();  
		file.close(); 
		assertEquals("\"PROJNUM\",\"PROJNAME\",\"MINUTES\",\"HOURS\"", headerRecord); 
		assertEquals("\"1\",\"Anderson - Billed\",\"0\",\"0.00\"", record); 
	}
	public void test5ExportOptions()  throws Exception
	{
		TimeToolPreferences options = new TimeToolPreferences();
		options.setQuotes("q"); 
		options.setDelimiter("r"); 
		options.setDecimal("s"); 
		options.serialize(); 
		
		TimePersistence filewriter = new TimePersistence(data);
		filewriter.loadFile();
		filewriter.exportFile(TEST_CSV);

		BufferedReader file = new BufferedReader(new FileReader(TEST_CSV));

		String headerRecord = file.readLine();  
		String record = file.readLine();  
		file.close(); 

		assertEquals("qPROJNUMqrqPROJNAMEqrqMINUTESqrqHOURSq", headerRecord); 
		assertEquals("q1qrqAnderson - Billedqrq0qrq0s00q", record); 
		
	}
	public void test6Reset()  throws Exception
	{
		TimePersistence file = new TimePersistence(data);
		file.loadFile();
		data.reset(); 
		Task record = data.get(4); 
		assertEquals("0", record.getMinutes());  
		assertEquals("0.00", record.getHours());  
		record = data.get(9); 
		assertEquals("0", record.getMinutes());  
		assertEquals("0.00", record.getHours());  
	}
	public void testDefaultFilename()
	{
		Date date = new Date(1134199999999L); //this long is 12/10/2005
		String filename = data.getDefaultFilename(date);
		assertEquals("20051210.csv", filename);
	}
	public void testCurrentRow() throws Exception
	{
		TimePersistence file = new TimePersistence(data);
		file.loadFile();
		assertEquals(-1, data.getCurrentRow()); 
		data.setCurrentRow(3); 
		file.saveFile(TXTVisitor.DATA_FILE);
		data.setCurrentRow(-1); 
		file.loadFile();
		assertEquals(3, data.getCurrentRow()); 
	}
	
}
