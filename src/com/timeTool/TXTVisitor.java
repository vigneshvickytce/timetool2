package com.timeTool;

public class TXTVisitor extends ExportVisitor
{
	public static final String DATA_FILE = "ttdata.txt";
	private final TimeTool controller;

	public TXTVisitor(TimeTool controller) {
		this.controller = controller;
	}


	public String padID(String id) {
		String ret = " " + id + "      ";
		ret = ret.substring(0, 7);
		return ret;
	}

	public String padDescription(String description) {
		String ret = description + "                                         ";
		ret = ret.substring(0, 38);
		return ret;
	}

	public String padMinutes(String minutes) {
		String ret = "      " + minutes;
		ret = ret.substring(ret.length()-5);
		return ret;
	}

	public String padHours(String hours) {
		String ret = "     " + hours;
		ret = ret.substring(ret.length() - 6);
		return ret;
	}

	public String getHeader(){
		return "";
	}

	public String getFooter() {
		String rowTag = getFormattedRowTag();
		String timeTag = getFormattedTimeTag();
		return rowTag + "\n" + timeTag + "\n";
	}

	private String getFormattedRowTag() {
		FilePersistence fileHelper = new FilePersistence();
		String currentRow = Integer.toString(controller.getCurrentRow());
        return fileHelper.wrapDataInTag(currentRow, TimePersistence.CURRENT_ROW_TAG);
	}
	private String getFormattedTimeTag() {
		FilePersistence fileHelper = new FilePersistence();
		String tag = "";
		if (controller.getTimerStartTime() != null)
		{
			long time = controller.getTimerStartTime().getTime();
			String timeString = Long.toString(time);
			tag = fileHelper.wrapDataInTag(timeString, TimePersistence.CURRENT_TIME_TAG);
		}
		return tag;
	}

	public String getColumnSeperator() {
		return " ";
	}

	public String getRowSeperator() {
		return "\n";
	}

}
