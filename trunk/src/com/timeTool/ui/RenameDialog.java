package com.timeTool.ui;

import com.timeTool.ResourceAutomation;

import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class RenameDialog extends CommonDialog
{
	private static final String DESCRIPTION_LABEL = "DescriptionLabelMessage";
	private static final String TASK_ID_LABEL = "TaskLabelMessage";
	private static final String RENAME_TITLE = "RenameTaskTitleMessage";
	/**
	 * 
	 */
	private static final long serialVersionUID = -4687588566238107965L;
	private JTextField taskField;
	private JTextField descriptionField;
	private String task;  
	private String description;
	
	public RenameDialog(JFrame frame, String task, String description) 
    {
        super(frame, ResourceAutomation.getResourceString(RENAME_TITLE), true);
        
    	this.task = task;  
    	this.description = description;  
        
    	Container mainPane = getContentPane(); 
		addLabel(ResourceAutomation.getResourceString(TASK_ID_LABEL), 0, mainPane);
		taskField = addField(0, this.task, mainPane);
		
		addLabel(ResourceAutomation.getResourceString(DESCRIPTION_LABEL), 1, mainPane);
        descriptionField = addField(1, this.description, mainPane);
        
        addButtons(mainPane);
    }

	public String getTask()
    {
		return task; 
    }
    public String getDescription()
    {
    	return description; 
    }
    
    protected void onOK()
    {
    	task = taskField.getText(); 
    	description = descriptionField.getText(); 
    }
    protected void onCancel()
    {
    	//do nothing
    }
}
