package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;



public class AddAction extends AbstractAction
{

	public static final String addAction      = "add";
	private final TimeTool controller;


	public AddAction(TimeTool controller)
	{
		super(addAction);
		this.controller = controller; 
	}
    public void actionPerformed(ActionEvent e) 
    {
    	controller.addTask();
	}
}
