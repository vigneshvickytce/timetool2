package com.timeTool.test;

import java.io.File;

import com.timeTool.TimeToolPreferences;

import junit.framework.TestCase;

public class ExportOptionsTest extends TestCase
{

	/*
	 * Test method for 'com.timeTool.TimeToolPreferences.serialize()'
	 */
	public void setup()
	{
		File file = new File(TimeToolPreferences.OPTIONS_FILENAME);
		file.delete(); 
	}
	public void tearDown()
	{
		File file = new File(TimeToolPreferences.OPTIONS_FILENAME);
		file.delete(); 
	}
	public void testSerialize() throws Exception
	{
		TimeToolPreferences options = new TimeToolPreferences();
		assertEquals(TimeToolPreferences.DEFAULT_DECIMAL, options.getDecimal());
		assertEquals(TimeToolPreferences.DEFAULT_QUOTE, options.getQuotes());
		assertEquals(TimeToolPreferences.DEFAULT_DELIMITER, options.getDelimiter());
		
		options.setDecimal("q"); 
		options.setDelimiter("r"); 
		options.setQuotes("s"); 
		options.serialize(); 
		
		TimeToolPreferences options2 = new TimeToolPreferences();
		assertEquals("q", options2.getDecimal()); 
		assertEquals("r", options2.getDelimiter()); 
		assertEquals("s", options2.getQuotes()); 
	}

}
