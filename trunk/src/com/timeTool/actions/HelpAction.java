package com.timeTool.actions;

import com.timeTool.BrowserLaunch;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;

public class HelpAction extends AbstractAction
{
    /**
	 *
	 */
	private static final long serialVersionUID = -5780282110553360589L;
	public static final String helpAction     = "help";

	public HelpAction()
	{
	    super(helpAction);
	}
    public void actionPerformed(ActionEvent e)
    {
    	BrowserLaunch.openURL("docs" + File.separator + "index.htm"); 
	}
}
