package com.timeTool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.File;
import java.util.Date;
import java.util.List;


public class TimePersistence extends FilePersistence
{
	public static final String CURRENT_ROW_TAG = "current_row";
	public static final String CURRENT_TIME_TAG = "current_time";

	private ExportVisitor formatter;
	private final TaskModel dataModel;
	private final ResourceAutomation resources;


	public TimePersistence(TaskModel dataModel, ResourceAutomation resources) {
		this.dataModel = dataModel;
		this.resources = resources;
	}


	public void loadFile() {

		if (!new File(TXTVisitor.DATA_FILE).exists()) {
			dataModel.addRow("Defaul", "Default task created by TimeTool", 0);
			dataModel.addRow("Admin", "Administrative time", 0);
			dataModel.addRow("Lunch", "Lunch", 0);
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

						dataModel.addRow(id, description, minutes * 60000);
					}
					else
					{
						//perhaps this is the current row tag
						int index = record.indexOf(CURRENT_ROW_TAG);
						if (index == 1)
						{
							//this is the current row tag
							String currentRow = extractFromTag(record, CURRENT_ROW_TAG);
							dataModel.setCurrentRow(currentRow);
						}
						else if (record.indexOf(CURRENT_TIME_TAG) == 1)
						{

							String currentTime = extractFromTag(record, CURRENT_TIME_TAG);
							long seconds = new Long(currentTime);
							Date startTime = new Date(seconds);
							dataModel.setStartTime(startTime);
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
		formatter = new TXTVisitor(dataModel);
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
		List<Task> tasks = dataModel.asList();

		file.write(formatter.getHeader());
		for (Task task : tasks) {
			writeRecord(file, task);
		}
		file.write(formatter.getFooter());
		file.close();
	}


}
