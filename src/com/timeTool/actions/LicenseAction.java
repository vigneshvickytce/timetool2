package com.timeTool.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.timeTool.BrowserLaunch;

public class LicenseAction  extends AbstractAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 776697994205506055L;
	public static final String licenseAction  = "license"; 

    public LicenseAction() 
	{
	    super(licenseAction);
	}
    public void actionPerformed(ActionEvent e) 
    {
    	BrowserLaunch.openURL("License.txt"); 
	}
}