package com.timeTool.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.timeTool.TimeTool;


public class ResetAction extends AbstractAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8331275723922788297L;
	public static final String resetAction    = "reset";   
	public ResetAction() 
	{
	    super(resetAction);
	}
    public void actionPerformed(ActionEvent e) 
    {
		TimeTool.getInstance().resetDialog();

	}
}

