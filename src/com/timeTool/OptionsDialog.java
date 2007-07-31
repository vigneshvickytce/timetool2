package com.timeTool;


import java.awt.Container;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class OptionsDialog extends CommonDialog
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -742579022162992678L;
	private ArrayList plugins;  
	
	public OptionsDialog(JFrame frame) 
    {
        super(frame, ResourceAutomation.getResourceString("optionsLabel"), true);
        
        try
		{
			plugins = PluginFactory.getInstance().getPlugins();
		}
		catch (Exception e)
		{
			ErrorHandler.showError(this, e); 
		} 
        
        JTabbedPane tabbedPane = new JTabbedPane(); 
 
        for (int x = 0; x < plugins.size(); x++)
        {
        	OptionsPlugin plugin = (OptionsPlugin)plugins.get(x); 
	        JPanel panel = plugin.configurationOptions(this); 
	        tabbedPane.addTab(plugin.getOptionsTitle(), 
	        		null, // no icon
	        		panel,
	        		plugin.getOptionsTitle());
        }
    	setContentPane(tabbedPane); 
        CenterAndResize(frame); 
    }

	protected void onOK() throws Exception
	{
        for (int x = 0; x < plugins.size(); x++)
        {
        	OptionsPlugin plugin = (OptionsPlugin)plugins.get(x); 
			plugin.onOK();
        }
	}

	protected void onCancel()
	{
		//do nothing
	}

	//public ExportOptions getOptions()
	//{
	//	return csvOptions.getExportOptions();
	//}


}
