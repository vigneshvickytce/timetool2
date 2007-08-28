package com.timeTool;

import java.io.*;

public class TimeToolPreferences extends FilePersistence
{
	private static final String DELIMITER_TAG = "delimiter";
	private static final String DECIMAL_TAG = "decimal";
    private static final String QUOTE_TAG = "quotes";
	private static final String AUTOSAVE_TAG = "autosave";
	private static final String IDLE_TAG = "idle_threshold";
    private static final String SKIN_TAG = "skin";
	public static final String OPTIONS_FILENAME = "ttoptions.xml";
	public static final String DEFAULT_QUOTE = "\"";
	public static final String DEFAULT_DECIMAL = ".";
    public static final String DEFAULT_DELIMITER = ",";
    public static final File DEFAULT_SKIN = new File("skins" + File.separator + "TimeTool Classic");
	public static final long DEFAULT_AUTOSAVE = 30;
	public static final int DEFAULT_IDLE = 180;


    private String quotes;
	private String decimal;
    private String delimiter;
	private long autosave;
	private int idleThreshold;
    private File skinDir;

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

	public int getIdleThreshold() {
		return idleThreshold;
	}

	public void setIdleThreshold(int idleThreshold) {
		this.idleThreshold = idleThreshold;
	}

	public void setAutosave(long autosave) {
        this.autosave = autosave;
    }

    public void setSkin(File skin) {
        this.skinDir = skin;
    }

    public File[] getAvailableSkins() {
        return new File("skins").listFiles(new FileFilter(){
            public boolean accept(File file) {
                return file.isDirectory();
            }
        });
    }
    
    public File getSkin() {
        return skinDir;
    }

    public void serialize() throws Exception {
		PrintWriter file = createWriter(OPTIONS_FILENAME);
		file.write("<timetool_options>"); 
		file.write(wrapDataInTag(quotes, QUOTE_TAG)); 
		file.write(wrapDataInTag(decimal, DECIMAL_TAG)); 
        file.write(wrapDataInTag(delimiter, DELIMITER_TAG));
        file.write(wrapDataInTag(skinDir.toString(), SKIN_TAG));
		file.write(wrapDataInTag(String.valueOf(autosave), AUTOSAVE_TAG));
		file.write(wrapDataInTag(String.valueOf(idleThreshold), IDLE_TAG));
		file.write("</timetool_options>");

		file.close();
	}

	private void unserialize() {
		setDelimiter(DEFAULT_DELIMITER);
		setDecimal(DEFAULT_DECIMAL);
		setQuotes(DEFAULT_QUOTE);
        setAutosave(DEFAULT_AUTOSAVE);
		setIdleThreshold(DEFAULT_IDLE);
		setSkin(DEFAULT_SKIN);

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
			final String saveValue = extractFromTag(xmlStream, AUTOSAVE_TAG);
			if (saveValue != null) {
				autosave = Long.valueOf(saveValue);
			}

			//may be null
			final String idleValue = extractFromTag(xmlStream, IDLE_TAG);
			if (idleValue != null) {
				idleThreshold = Integer.valueOf(idleValue);
			}

            //may be null
            final String skinValue = extractFromTag(xmlStream, SKIN_TAG);
            if (skinValue != null) {
                skinDir = new File(skinValue);
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
