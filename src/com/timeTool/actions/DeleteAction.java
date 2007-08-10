package com.timeTool.actions;

import com.timeTool.TimeTool;
import com.timeTool.Task;

import java.awt.event.ActionEvent;

import javax.swing.*;


public class DeleteAction extends AbstractAction
{
	public static final String deleteAction   = "delete";
	private final TimeTool controller;

    public DeleteAction(TimeTool controller) {
		super(deleteAction);
		this.controller = controller;
    }
    public void actionPerformed(ActionEvent e) {
        controller.removeRowDialog();
   }

}
