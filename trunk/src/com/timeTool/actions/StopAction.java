package com.timeTool.actions;

import com.timeTool.TimeTool;
import com.timeTool.TaskModel;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class StopAction extends AbstractAction
{
	public static final String stopAction     = "stop";
	private final TimeTool controller;


	public StopAction(TimeTool controller)
	{
		super(stopAction);
		this.controller = controller; 
	}
    
    public void actionPerformed(ActionEvent e) 
    {
    	controller.setCurrentRow(TaskModel.NO_ROW_SELECTED);
	}


}
