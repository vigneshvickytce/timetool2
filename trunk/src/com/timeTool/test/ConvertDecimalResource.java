package com.timeTool.test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.timeTool.FilePersistence;

import junit.framework.TestCase;

public class ConvertDecimalResource extends TestCase
{
	public void testConvertDecimalResource() throws IOException
	{
		//this isn't a real test... it actually
		//takes a resource file and converts html
		//decimal unicode chars to java-readable
		//hex unicode chars
		
		//read the file line by line
		BufferedReader inputFile = null;
		PrintWriter outputFile = null; 
		try
		{
			inputFile = new BufferedReader(new FileReader(".\\src\\com\\timeTool\\resources\\TimeTool_jp.properties"));
			String inputLine = inputFile.readLine();
			outputFile = new FilePersistence().createWriter(".\\src\\com\\timeTool\\resources\\translated.properties"); 
			
			while (inputLine != null)
			{
				//ignore lines without an = in it
				while (inputLine.contains("&#") == true)
				{
					int start = inputLine.indexOf("&#"); 
					int end = inputLine.indexOf(";"); 
					
					String decimalString = inputLine.substring(start+2, end);
					int decimalCode = new Integer(decimalString).intValue(); 
					String replaceString = "&#" + decimalString + ";"; 
					
					inputLine = inputLine.replaceAll(replaceString, "~~~~" + "u" + Integer.toHexString(decimalCode)); 
					
				}

				//write the full output line
				System.out.println(inputLine); 
				//loop thru the line until all $# are gone
				
				//write line to output
				outputFile.write(inputLine + "\n"); 
				inputLine = inputFile.readLine(); 
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			if (inputFile != null)
			{
				inputFile.close(); 
			}
			if (outputFile != null)
			{
				outputFile.close(); 
			}
		}
	}
}
