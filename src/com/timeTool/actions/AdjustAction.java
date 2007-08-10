package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

public class AdjustAction extends AbstractAction {

	public static final String adjustAction   = "adjust";
	private final TimeTool controller;
	private final JFrame frame;


	public AdjustAction(TimeTool controller, JFrame frame) {
		super(adjustAction);
		this.controller = controller;
		this.frame = frame;
	}

	public void actionPerformed(ActionEvent e) {
		controller.adjustTime(null, frame);
	}
}
