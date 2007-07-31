package com.timeTool.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.timeTool.TimeTool;

public class AboutAction extends AbstractAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2237459285069603148L;
	public static final String aboutAction    = "about";
    public AboutAction() 
	{
	    super(aboutAction);
	}
    public void actionPerformed(ActionEvent e) 
    {
    	TimeTool.getInstance().about(); 
	}


}
