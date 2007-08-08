package com.timeTool.actions;

import com.timeTool.BrowserLaunch;
import com.timeTool.ResourceAutomation;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


public class SupportAction extends AbstractAction
{
	public static final String supportAction  = "support";
	private final ResourceAutomation resources;


	public SupportAction(ResourceAutomation resources) {
		super(supportAction);
		this.resources = resources;
	}
	public void actionPerformed(ActionEvent e) {
		new BrowserLaunch(resources).openURL("mailto:hamlet_darcy@hotmail.com?subject=TimeTool");
	}
}
