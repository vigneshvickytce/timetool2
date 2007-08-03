package com.timeTool.ui;

import com.timeTool.ResourceAutomation;

import java.awt.*;
import static java.awt.GridBagConstraints.NORTHEAST;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.SOUTHEAST;

import javax.swing.*;

public class RenameDialog extends CommonDialog
{
	private static final String DESCRIPTION_LABEL = "DescriptionLabelMessage";
	private static final String TASK_ID_LABEL = "TaskLabelMessage";
	private static final String RENAME_TITLE = "RenameTaskTitleMessage";
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

        JLabel taskLabel = new JLabel(ResourceAutomation.getResourceString(TASK_ID_LABEL), SwingConstants.RIGHT);
        taskField = new JTextField(task, 20);
        JLabel descriptionLabel = new JLabel(ResourceAutomation.getResourceString(DESCRIPTION_LABEL), SwingConstants.RIGHT);
        descriptionField = new JTextField(description, 20);

        mainPane.setLayout(new GridBagLayout());
        mainPane.add(taskLabel,         new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
        mainPane.add(taskField,         new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
        mainPane.add(descriptionLabel,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
        mainPane.add(descriptionField,  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
        mainPane.add(getButtons(),      new GridBagConstraints(1, 2, 1, 1, 0.0, 1.0, SOUTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
        setResizable(false);
        CenterAndResize(frame);
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
