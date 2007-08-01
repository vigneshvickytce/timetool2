package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


public class RenameAction extends AbstractAction
{
	public static final String renameAction   = "rename";
	private final TimeTool controller;


	public RenameAction(TimeTool controller)
	{
		super(renameAction);
		this.controller = controller; 
	}
    public void actionPerformed(ActionEvent e) 
    {
    	controller.renameDialog();
	}

}
