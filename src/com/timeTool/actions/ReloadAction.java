package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


public class ReloadAction extends AbstractAction
{
	public static final String reloadAction   = "reload";
	private final TimeTool controller;


	public ReloadAction(TimeTool controller)
	{
		super(reloadAction);
		this.controller = controller; 
	}
    public void actionPerformed(ActionEvent e) 
    {
		controller.reloadTaskList();
	}
}
