package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


public class ExportAction extends AbstractAction
{
	public static final String exportAction   = "export";
	private final TimeTool controller;


	public ExportAction(TimeTool controller)
	{
		super(exportAction);
		this.controller = controller; 
	}
    public void actionPerformed(ActionEvent e) 
    {
    	controller.exportTaskList();
	}

}
