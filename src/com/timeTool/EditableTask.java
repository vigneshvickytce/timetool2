package com.timeTool;

public class EditableTask implements Task {
	//@GuardedBy(this);
	private volatile long millis;
	private String id;
	private String description;
	
	public EditableTask(String id, String description, long millis) {
		this.id = id;
		this.description = description;
		this.millis = millis;
	}


    public String toString() {
        return "Task: " + id + ", " + description + ", " + millis;
    }

    public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	} 
	public synchronized void setMillis(long seconds) {
		this.millis = seconds;
	}
	public synchronized void addMillis(long millis) {
		this.millis += millis;
	}
	public synchronized String getMinutes() {
		Integer minutes = new Float(millis / 60000).intValue();
		return minutes.toString(); 
	}

    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof EditableTask) {
            return this.getId().equals(((Task)obj).getId());
        }
        return false;
    }


    public int hashCode() {
        return 37 * id.hashCode() + description.hashCode(); 
    }

    public synchronized String getHours() {
		float hours = millis / 1000;
		hours = hours / 3600; 
		hours = (float)round((double)hours, 2);
		return formatHours(Float.toString(hours));
	}

	private double round(double val, int places) {
		long factor = (long)Math.pow(10,places);

		// Shift the decimal the correct number of places
		// to the right.
		val = val * factor;

		// Round to the nearest integer.
		long tmp = Math.round(val);

		// Shift the decimal the correct number of places
		// back to the left.
		return (double)tmp / factor;
	}
	
	public static String formatHours(String hours) {
		int index = hours.indexOf("."); 
		int length = hours.length(); 
		if (index == -1) {
			return hours + ".00";
		} else if ((index < (length-1)) && (index > (length-3))) {
			return hours + "0";
		} else if (index < (length-3)) {
			return hours.substring(0, index+3);
		}
		return hours;
	}
	
}
