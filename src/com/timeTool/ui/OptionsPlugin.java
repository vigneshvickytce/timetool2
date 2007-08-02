
package com.timeTool.ui;

import com.timeTool.ui.CommonDialog;

import javax.swing.JPanel;

public interface OptionsPlugin
{

	public abstract JPanel configurationOptions(CommonDialog parent);
	
	public abstract JPanel getAddTaskPanel(CommonDialog parent) throws Exception;

	public abstract void onOK() throws Exception;

	public abstract String getOptionsTitle();
	
	public abstract Boolean getEnabled(); 

	public abstract String getSelectedTask(); 
	
	public abstract String getSelectedDescription(); 
	
}