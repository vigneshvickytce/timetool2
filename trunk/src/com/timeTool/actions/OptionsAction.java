package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class OptionsAction  extends AbstractAction
{
	public static final String optionsAction  = "options";
	private final TimeTool controller;


	public OptionsAction(TimeTool controller)
	{
		super(optionsAction);
		this.controller = controller; 
	}
    public void actionPerformed(ActionEvent e) 
    {
    	controller.options();
	}
}