package com.timeTool.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.timeTool.BrowserLaunch;


public class SupportAction extends AbstractAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1168273271704808313L;
	public static final String supportAction  = "support"; 

    public SupportAction() 
	{
	    super(supportAction);
	}
    public void actionPerformed(ActionEvent e) 
    {
    	BrowserLaunch.openURL("mailto:hamlet_darcy@hotmail.com?subject=TimeTool"); 
	}
}
