package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class AdjustAction extends AbstractAction {

	public static final String adjustAction   = "adjust";
	private final TimeTool controller; 


	public AdjustAction(TimeTool controller) {
		super(adjustAction);
		this.controller = controller;
	}
    
    public void actionPerformed(ActionEvent e) {
    	controller.adjustTime(null);
	}
}
