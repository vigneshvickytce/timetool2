package com.timeTool.actions;

import com.timeTool.BrowserLaunch;
import com.timeTool.ResourceAutomation;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class LicenseAction  extends AbstractAction
{
	public static final String licenseAction  = "license";
	private final ResourceAutomation resources;


	public LicenseAction(ResourceAutomation resources) {
		super(licenseAction);
		this.resources = resources;
	}
	public void actionPerformed(ActionEvent e)
	{
		new BrowserLaunch(resources).openURL("License.txt");
	}
}