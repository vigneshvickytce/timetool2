package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

public class AboutAction extends AbstractAction
{
	public static final String aboutAction    = "about";
	private final TimeTool controller;
	private final JFrame frame;


	public AboutAction(TimeTool controller, JFrame frame)
	{
		super(aboutAction);
		this.controller = controller;
		this.frame = frame;
	}
	public void actionPerformed(ActionEvent e)
	{
		controller.about(frame);
	}


}
