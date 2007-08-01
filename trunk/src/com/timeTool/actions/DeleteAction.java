package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


public class DeleteAction extends AbstractAction
{
	public static final String deleteAction   = "delete";
	private TimeTool controller; 


	public DeleteAction(TimeTool controller)
	{
		super(deleteAction);
		this.controller = controller;
	}
    public void actionPerformed(ActionEvent e) 
    {
    	controller.removeRowDialog();
    	
    }

}
