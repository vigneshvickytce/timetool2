package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class AboutAction extends AbstractAction
{
	public static final String aboutAction    = "about";
	private final TimeTool controller;

	public AboutAction(TimeTool controller)
	{
		super(aboutAction);
		this.controller = controller;
	}
    public void actionPerformed(ActionEvent e) 
    {
    	controller.about();
	}


}
