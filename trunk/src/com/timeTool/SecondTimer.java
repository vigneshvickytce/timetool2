package com.timeTool;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class SecondTimer extends AbstractAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1131558329689813598L;

	public void actionPerformed(ActionEvent arg0)
	{
		TimeTool.getInstance().tick();  		
	}

}
