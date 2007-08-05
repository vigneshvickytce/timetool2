package com.timeTool.ui;


import com.timeTool.ErrorHandler;
import com.timeTool.ResourceAutomation;

import java.util.List;
import java.awt.*;
import static java.awt.GridBagConstraints.NORTHEAST;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.NORTHWEST;
import static java.awt.GridBagConstraints.SOUTHEAST;

import javax.swing.*;

public final class OptionsDialog extends CommonDialog
{

	private List<OptionsPlugin> plugins;
	
	public OptionsDialog(JFrame frame) {
        super(frame, ResourceAutomation.getResourceString("optionsLabel"), true);
        
        try {
			plugins = PluginFactory.getInstance().getPlugins();
		} catch (Exception e) {
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
	protected void onOK() throws Exception {
		for (OptionsPlugin plugin : plugins) {
			plugin.onOK();
		}
	}

	@Override
	protected void onCancel() {
		//do nothing
	}


}
