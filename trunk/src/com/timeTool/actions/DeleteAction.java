package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.*;


public class DeleteAction extends AbstractAction
{
	public static final String deleteAction   = "delete";
	private final TimeTool controller;
	private final JFrame frame;


	public DeleteAction(TimeTool controller, JFrame frame) {
		super(deleteAction);
		this.controller = controller;
		this.frame = frame;
	}
    public void actionPerformed(ActionEvent e) {
        controller.removeRowDialog(frame);
   }

}
