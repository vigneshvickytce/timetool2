
package com.timeTool.ui;

import com.timeTool.ResourceAutomation;

import javax.swing.JPanel;

public interface OptionsPlugin
{

	JPanel configurationOptions(CommonDialog parent);
	
	JPanel getAddTaskPanel(CommonDialog parent) throws Exception;

	void onOK() throws Exception;

	String getOptionsTitle(ResourceAutomation resources);
	
	Boolean getEnabled();

	String getSelectedTask();
	
	String getSelectedDescription(); 
	
}