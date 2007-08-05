package com.timeTool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class TimeToolPreferences extends FilePersistence
{
	private static final String DELIMITER_TAG = "delimiter";
	private static final String DECIMAL_TAG = "decimal";
    private static final String QUOTE_TAG = "quotes";
    private static final String AUTOSAVE_TAG = "autosave";
	public static final String OPTIONS_FILENAME = "ttoptions.xml";
	public static final String DEFAULT_QUOTE = "\"";
	public static final String DEFAULT_DECIMAL = ".";
    public static final String DEFAULT_DELIMITER = ",";
    public static final long DEFAULT_AUTOSAVE = 30;

	private String quotes;
	private String decimal;
    private String delimiter;
    private long autosave;

	public TimeToolPreferences() {
		unserialize(); 
	}

    public void setQuotes(String quotes) {
		this.quotes = quotes;
	}

	public String getQuotes() {
		return quotes;
	}

	public void setDecimal(String decimal) {
		this.decimal = decimal;
	}

	public String getDecimal() {
		return decimal;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public String getDelimiter() {
		return delimiter;
	}

    public long getAutosave() {
        return autosave;
    }

    public void setAutosave(long autosave) {
        this.autosave = autosave;
    }
    
    public void serialize() throws Exception {
		PrintWriter file = createWriter(OPTIONS_FILENAME);
		file.write("<timetool_options>"); 
		file.write(wrapDataInTag(quotes, QUOTE_TAG)); 
		file.write(wrapDataInTag(decimal, DECIMAL_TAG)); 
        file.write(wrapDataInTag(delimiter, DELIMITER_TAG));
        file.write(wrapDataInTag(String.valueOf(autosave), AUTOSAVE_TAG));
		file.write("</timetool_options>");

		file.close();
	}

	private void unserialize() {
		setDelimiter(DEFAULT_DELIMITER);
		setDecimal(DEFAULT_DECIMAL);
		setQuotes(DEFAULT_QUOTE);
        setAutosave(DEFAULT_AUTOSAVE);

        String xmlStream = "";
		BufferedReader file = null; 
		try {
			file = new BufferedReader(new FileReader(OPTIONS_FILENAME));
			String line = file.readLine(); 
			while (line != null)
			{
				xmlStream = xmlStream + line; 
				line = file.readLine(); 
			}
			
			quotes = extractFromTag(xmlStream, QUOTE_TAG);   
			decimal = extractFromTag(xmlStream, DECIMAL_TAG);
            delimiter = extractFromTag(xmlStream, DELIMITER_TAG);

            //may be null
            String value = extractFromTag(xmlStream, AUTOSAVE_TAG);
            if (value != null) {
                autosave = Long.valueOf(value);
            }
        } catch (Exception e) {
			//do nothing, exceptions are OK here
		} finally {
			if (file != null) {
				try {
					file.close();
				} catch (IOException ignored){}
			}
		}
	}

}
