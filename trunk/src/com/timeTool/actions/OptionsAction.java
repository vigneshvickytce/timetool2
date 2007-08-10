package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

public class OptionsAction  extends AbstractAction
{
	public static final String optionsAction  = "options";
	private final TimeTool controller;
	private final JFrame frame;


	public OptionsAction(TimeTool controller, JFrame frame)
	{
		super(optionsAction);
		this.controller = controller;
		this.frame = frame;
	}
	public void actionPerformed(ActionEvent e)
	{
		controller.options(frame);
	}
}