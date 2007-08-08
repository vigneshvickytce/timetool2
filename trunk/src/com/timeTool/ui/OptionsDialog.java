package com.timeTool.ui;


import com.timeTool.ErrorHandler;
import com.timeTool.ResourceAutomation;

import java.util.List;

import javax.swing.*;

public final class OptionsDialog extends CommonDialog
{

	private List<OptionsPlugin> plugins;
	
	public OptionsDialog(JFrame frame, ResourceAutomation resources) {
        super(frame, resources.getResourceString("optionsLabel"), true, resources);
        
        try {
			plugins = PluginFactory.getInstance().getPlugins();
		} catch (Exception e) {
			ErrorHandler.showError(this, e, resources);
		} 
        
        final JTabbedPane tabbedPane = new JTabbedPane();

        for (OptionsPlugin plugin : plugins) {
			final JPanel panel = plugin.configurationOptions(this);
			tabbedPane.addTab(plugin.getOptionsTitle(resources),
				null, // no icon
				panel,
				plugin.getOptionsTitle(resources));
		}
    	setContentPane(tabbedPane);
		setResizable(false);
		CenterAndResize(frame);
    }

    @Override
	protected void onOK() throws Exception {
		for (OptionsPlugin plugin : plugins) {
			try {
				plugin.onOK();
			} catch (Exception ex) {
				ErrorHandler.showError(this, ex, resources);
			}
		}
	}

	@Override
	protected void onCancel() {
		//do nothing
	}


}
