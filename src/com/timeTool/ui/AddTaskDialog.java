package com.timeTool.ui;

import com.timeTool.ui.CommonDialog;
import com.timeTool.ui.OptionsPlugin;
import com.timeTool.ResourceAutomation;
import com.timeTool.ui.PluginFactory;

import java.awt.Container;

import javax.swing.JTextField;
import javax.swing.JFrame;

public class AddTaskDialog extends CommonDialog {
	public static final String DESCRIPTION_LABEL = "DescriptionLabelMessage";
	public static final String TASK_LABEL = "TaskLabelMessage";
	private static final String ADD_A_TASK_WINDOW_TITLE = "AddTaskTitleMessage";
	/**
	 * 
	 */
	private static final long serialVersionUID = 282902429285256831L;
	private JTextField taskField;
	private JTextField descriptionField;
	private String task;  
	private String description;
	private OptionsPlugin activePlugin = null;
	
	public AddTaskDialog(JFrame frame) throws Exception 
    {
        super(frame, ResourceAutomation.getResourceString(ADD_A_TASK_WINDOW_TITLE), true);
        
        activePlugin = PluginFactory.getInstance().getActivePlugin();
		Container mainPane = getContentPane(); 
        
        if (activePlugin == null)
        {
	    	task = "";  
	    	description = "";  
			addLabel(ResourceAutomation.getResourceString(TASK_LABEL), 0, mainPane);
			taskField = addField(0, "", mainPane);
			
			addLabel(ResourceAutomation.getResourceString(DESCRIPTION_LABEL), 1, mainPane);
	        descriptionField = addField(1, "", mainPane);
	        addButtons(mainPane);
        }
        else
        {
        	setContentPane(activePlugin.getAddTaskPanel(this)); 
        }
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
    	if (activePlugin == null)
    	{
	    	task = taskField.getText(); 
	    	description = descriptionField.getText();
    	}
    	else
    	{
	    	task = activePlugin.getSelectedTask(); 
	    	description = activePlugin.getSelectedDescription();
    	}
    }
    protected void onCancel()
    {
    	task = "";  
    	description = "";  
    }
    

}
