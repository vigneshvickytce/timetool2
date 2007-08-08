package com.timeTool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.File;
import java.util.Date;


public class TimePersistence extends FilePersistence
{
	public static final String CURRENT_ROW_TAG = "current_row";
	public static final String CURRENT_TIME_TAG = "current_time";

	private ExportVisitor formatter;
	private final TimeTool controller;
	private final ResourceAutomation resources;


	public TimePersistence(TimeTool controller, ResourceAutomation resources) {
		this.controller = controller;
		this.resources = resources;
	}


	public void loadFile() {
        
        //remove all the old entries...
		controller.clear();

        if (!new File(TXTVisitor.DATA_FILE).exists()) {
            controller.addRow("Default", "Default task created by TimeTool", 0);
            controller.addRow("Admin", "Administrative time", 0);
            controller.addRow("Lunch", "Lunch", 0);
        } else {
            try
		    {
			BufferedReader file = new BufferedReader(new FileReader(TXTVisitor.DATA_FILE));
                String record = file.readLine();

                while (record != null)
                {
                    if (record.length() == 59)
                    {
                        String id = record.substring(0, 8);
                        id = id.trim();

                        String description = record.substring(8, 48);
                        description = description.trim();

                        String minutesAsString = record.substring(48, 52);
                        minutesAsString = minutesAsString.trim();
                        Integer minutes = new Integer(minutesAsString);

                        controller.addRow(id, description, minutes);
                    }
                    else
                    {
                        //perhaps this is the current row tag
                        int index = record.indexOf(CURRENT_ROW_TAG);
                        if (index == 1)
                        {
                            //this is the current row tag
                            String currentRow = extractFromTag(record, CURRENT_ROW_TAG);
                            Integer row = new Integer(currentRow);
                            controller.setCurrentRow(row);
                        }
                        else if (record.indexOf(CURRENT_TIME_TAG) == 1)
                        {

                            String currentTime = extractFromTag(record, CURRENT_TIME_TAG);
                            long seconds = new Long(currentTime);
                            Date startTime = new Date(seconds);
                            controller.setStartTime(startTime);
                        }
                    }
                    record = file.readLine();
                }

                file.close();
            }
            catch (Exception e)
            {
                //do nothing, exceptions are OK here
            }
        }
	}

    public void saveFile(String path) throws Exception {
		formatter = new TXTVisitor(controller);
		export(path); 		
	}

    private void writeRecord(PrintWriter file, Task record) {
		String id = formatter.padID(record.getId());  
		String description = formatter.padDescription(record.getDescription()); 
		String minutes = formatter.padMinutes(record.getMinutes()); 
		String hours = formatter.padHours(record.getHours()); 
		
		file.write(id); 
		file.write(formatter.getColumnSeperator()); 
		file.write(description); 
		file.write(formatter.getColumnSeperator()); 
		file.write(minutes); 
		file.write(formatter.getColumnSeperator()); 
		file.write(hours); 
		file.write(formatter.getRowSeperator());  
	}
    
    public void exportFile(String path) throws Exception {
		formatter = new CSVVisitor(resources);
		export(path);
	}

    private void export(String path) throws Exception {
		PrintWriter file = createWriter(path);
		TaskIterator iterator = new TaskIterator(controller); 
		
		file.write(formatter.getHeader()); 
		Task record = iterator.getFirst(); 
		while (record != null)
		{
			writeRecord(file, record); 
			record = iterator.getNext(); 
		}
		file.write(formatter.getFooter()); 
		file.close();
	}
	
	
}
