package com.timeTool.test;

import java.io.File;

import com.timeTool.ExportOptions;

import junit.framework.TestCase;

public class ExportOptionsTest extends TestCase
{

	/*
	 * Test method for 'com.timeTool.ExportOptions.serialize()'
	 */
	public void setup()
	{
		File file = new File(ExportOptions.OPTIONS_FILENAME); 
		file.delete(); 
	}
	public void tearDown()
	{
		File file = new File(ExportOptions.OPTIONS_FILENAME); 
		file.delete(); 
	}
	public void testSerialize() throws Exception
	{
		ExportOptions options = new ExportOptions();
		assertEquals(ExportOptions.DEFAULT_DECIMAL, options.getDecimal()); 
		assertEquals(ExportOptions.DEFAULT_QUOTE, options.getQuotes()); 
		assertEquals(ExportOptions.DEFAULT_DELIMITER, options.getDelimiter()); 
		
		options.setDecimal("q"); 
		options.setDelimiter("r"); 
		options.setQuotes("s"); 
		options.serialize(); 
		
		ExportOptions options2 = new ExportOptions(); 
		assertEquals("q", options2.getDecimal()); 
		assertEquals("r", options2.getDelimiter()); 
		assertEquals("s", options2.getQuotes()); 
	}

}
