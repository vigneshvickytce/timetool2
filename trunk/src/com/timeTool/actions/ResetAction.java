package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;


public class ResetAction extends AbstractAction
{
	public static final String resetAction    = "reset";
	private final TimeTool controller;
	private final JFrame frame;


	public ResetAction(TimeTool controller, JFrame frame) {
		super(resetAction);
		this.controller = controller;
		this.frame = frame; 
	}
	public void actionPerformed(ActionEvent e) {
		controller.resetDialog(frame);

	}
}

