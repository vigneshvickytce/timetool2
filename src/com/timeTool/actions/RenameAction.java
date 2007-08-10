package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;


public class RenameAction extends AbstractAction
{
	public static final String renameAction   = "rename";
	private final TimeTool controller;
	private final JFrame frame;

	public RenameAction(TimeTool controller, JFrame frame) {
		super(renameAction);
		this.controller = controller;
		this.frame = frame;
	}
	public void actionPerformed(ActionEvent e) {
		controller.renameDialog(frame);
	}

}
