package com.timeTool.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.timeTool.BrowserLaunch;

public class HomePageAction extends AbstractAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3273063888004943468L;
	public static final String homepageAction = "homepage";

    public HomePageAction() 
	{
	    super(homepageAction);
	}
    public void actionPerformed(ActionEvent e) 
    {
    	BrowserLaunch.openURL("http://www.hamletdarcy.com/timetool"); 
	}
}
