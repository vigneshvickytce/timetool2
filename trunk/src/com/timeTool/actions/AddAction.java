package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;


public class AddAction extends AbstractAction {

	public static final String addAction      = "add";
	private final TimeTool controller;
	private final JFrame frame;


	public AddAction(TimeTool controller, JFrame frame) {
		super(addAction);
		this.controller = controller;
		this.frame = frame;
	}
	public void actionPerformed(ActionEvent e) {
		controller.addTask(frame);
	}
}
