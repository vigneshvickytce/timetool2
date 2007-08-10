package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;


public class SaveAction extends AbstractAction
{
	public static final String saveAction     = "save";
	private final TimeTool controller;
	private final JFrame frame;


	public SaveAction(TimeTool controller, JFrame frame)
	{
		super(saveAction);
		this.controller = controller;
		this.frame = frame;
	}
	public void actionPerformed(ActionEvent e)
	{
		controller.saveTaskList(frame);
	}
}
