package com.timeTool.ui;

import com.timeTool.ResourceAutomation;

import java.awt.Container;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class AddTaskDialog extends CommonDialog {
	public static final String DESCRIPTION_LABEL = "DescriptionLabelMessage";
	public static final String TASK_LABEL = "TaskLabelMessage";
	private static final String ADD_A_TASK_WINDOW_TITLE = "AddTaskTitleMessage";
	private JTextField taskField;
	private JTextField descriptionField;
	private String task;  
	private String description;
	private OptionsPlugin activePlugin = null;
	
	public AddTaskDialog(JFrame frame, ResourceAutomation resources) throws Exception {
        super(frame, resources.getResourceString(ADD_A_TASK_WINDOW_TITLE), true, resources);
        
        activePlugin = PluginFactory.getInstance().getActivePlugin();
		Container mainPane = getContentPane(); 
        
        if (activePlugin == null)
        {
	    	task = "";  
	    	description = "";

            JLabel taskLabel = new JLabel(resources.getResourceString(TASK_LABEL), SwingConstants.RIGHT);
            taskField = new JTextField("", 20);
            JLabel descriptionLabel = new JLabel(resources.getResourceString(DESCRIPTION_LABEL), SwingConstants.RIGHT);
            descriptionField = new JTextField("", 20);

            mainPane.setLayout(new GridBagLayout());
            mainPane.add(taskLabel,         new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
            mainPane.add(taskField,         new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
            mainPane.add(descriptionLabel,  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
            mainPane.add(descriptionField,  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
            mainPane.add(getButtons(),      new GridBagConstraints(1, 2, 1, 1, 0.0, 1.0, SOUTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
        } else {
        	setContentPane(activePlugin.getAddTaskPanel(this)); 
        }
        setResizable(false);
        CenterAndResize(frame);
    }

	public String getTask() {
		return task; 
    }
    public String getDescription() {
    	return description; 
    }
    
    protected void onOK() {
    	if (activePlugin == null) {
	    	task = taskField.getText(); 
	    	description = descriptionField.getText();
    	} else {
	    	task = activePlugin.getSelectedTask(); 
	    	description = activePlugin.getSelectedDescription();
    	}
    }
    
    protected void onCancel() {
    	task = "";  
    	description = "";  
    }
    

}
