package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;


public class ExportAction extends AbstractAction
{
	public static final String exportAction   = "export";
	private final TimeTool controller;
	private final JFrame frame;


	public ExportAction(TimeTool controller, JFrame frame)
	{
		super(exportAction);
		this.controller = controller;
		this.frame = frame;
	}
	public void actionPerformed(ActionEvent e)
	{
		controller.exportTaskList(frame);
	}

}
