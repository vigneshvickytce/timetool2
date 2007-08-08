package com.timeTool.actions;

import com.timeTool.BrowserLaunch;
import com.timeTool.ResourceAutomation;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;

public class HelpAction extends AbstractAction
{
	public static final String helpAction     = "help";
	private final ResourceAutomation resources;


	public HelpAction(ResourceAutomation resources) {
		super(helpAction);
		this.resources = resources;
	}
    public void actionPerformed(ActionEvent e)
    {
    	new BrowserLaunch(resources).openURL("docs" + File.separator + "index.htm");
	}
}
