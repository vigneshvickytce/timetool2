package com.timeTool.ui;


import com.timeTool.ErrorHandler;
import com.timeTool.ResourceAutomation;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public final class OptionsDialog extends CommonDialog
{

	private List<OptionsPlugin> plugins;
	
	public OptionsDialog(JFrame frame) {
        super(frame, ResourceAutomation.getResourceString("optionsLabel"), true);
        
        try
		{
			plugins = PluginFactory.getInstance().getPlugins();
		}
		catch (Exception e)
		{
			ErrorHandler.showError(this, e);
		} 
        
        final JTabbedPane tabbedPane = new JTabbedPane();

		for (OptionsPlugin plugin : plugins) {
			final JPanel panel = plugin.configurationOptions(this);
			tabbedPane.addTab(plugin.getOptionsTitle(),
				null, // no icon
				panel,
				plugin.getOptionsTitle());
		}
    	setContentPane(tabbedPane);
		setResizable(false);
		CenterAndResize(frame);
    }

	@Override
	protected void onOK() throws Exception
	{
		for (OptionsPlugin plugin : plugins) {
			plugin.onOK();
		}
	}

	@Override
	protected void onCancel()
	{
		//do nothing
	}


}
