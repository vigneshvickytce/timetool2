package com.timeTool;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FilePersistence
{
	private static final String END_TAG_BEGIN = "</";
	private static final String TAG_END = ">";
	private static final String START_TAG_BEGIN = "<";

	public PrintWriter createWriter(String filename) throws Exception
	{
		PrintWriter out = null; 
		out = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
		return out;
	}

	public String wrapDataInTag(String data, String tagName)
	{
        return START_TAG_BEGIN + tagName + TAG_END + data + END_TAG_BEGIN + tagName + TAG_END;
	}

	public String extractFromTag(String xmlStream, String tagName)
	{
		String startTag = START_TAG_BEGIN + tagName + TAG_END; 
		String endTag = END_TAG_BEGIN + tagName + TAG_END;
        if (!xmlStream.contains(startTag)) return null; 

        int startTagOffset = xmlStream.indexOf(startTag) + startTag.length();
		int endTagOffset = xmlStream.indexOf(endTag);  
		return xmlStream.substring(startTagOffset, endTagOffset); 
	}

}
