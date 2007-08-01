package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


public class SaveAction extends AbstractAction
{
	public static final String saveAction     = "save";
	private final TimeTool controller;


	public SaveAction(TimeTool controller)
	{
		super(saveAction);
		this.controller = controller;
	}
    public void actionPerformed(ActionEvent e) 
    {
		controller.saveTaskList();
	}
}
