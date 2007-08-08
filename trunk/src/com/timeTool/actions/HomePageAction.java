package com.timeTool.actions;

import com.timeTool.BrowserLaunch;
import com.timeTool.ResourceAutomation;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class HomePageAction extends AbstractAction
{
	public static final String homepageAction = "homepage";
	private final ResourceAutomation resources;


	public HomePageAction(ResourceAutomation resources) {
		super(homepageAction);
		this.resources = resources;
	}
	public void actionPerformed(ActionEvent e)
	{
		new BrowserLaunch(resources).openURL("http://www.hamletdarcy.com/timetool");
	}
}
