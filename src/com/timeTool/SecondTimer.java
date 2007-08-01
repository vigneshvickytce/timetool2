package com.timeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class SecondTimer extends AbstractAction
{
	private TimeTool controller;


	public SecondTimer(TimeTool controller) {
		this.controller = controller;
	}


	public void actionPerformed(ActionEvent arg0)
	{
		controller.tick();
	}

}
