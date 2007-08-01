package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


public class ResetAction extends AbstractAction
{
	public static final String resetAction    = "reset";
	private final TimeTool controller;


	public ResetAction(TimeTool controller)
	{
		super(resetAction);
		this.controller = controller; 
	}
    public void actionPerformed(ActionEvent e) 
    {
		controller.resetDialog();

	}
}

