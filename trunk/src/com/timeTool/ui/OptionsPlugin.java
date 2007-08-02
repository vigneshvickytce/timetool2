
package com.timeTool.ui;

import javax.swing.JPanel;

public interface OptionsPlugin
{

	JPanel configurationOptions(CommonDialog parent);
	
	JPanel getAddTaskPanel(CommonDialog parent) throws Exception;

	void onOK() throws Exception;

	String getOptionsTitle();
	
	Boolean getEnabled();

	String getSelectedTask();
	
	String getSelectedDescription(); 
	
}