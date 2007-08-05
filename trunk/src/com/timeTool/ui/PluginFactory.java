package com.timeTool.ui;

import java.util.ArrayList;
import java.util.List;

public class PluginFactory
{
	private static PluginFactory _instance = null;
	private final List<OptionsPlugin> plugins = new ArrayList<OptionsPlugin>();

	private PluginFactory() throws Exception {
        plugins.add(new GeneralOptions());
        plugins.add(new CSVOptions());
		plugins.add(new MySQLDataGrabber());
	}

	public static PluginFactory getInstance() throws Exception
	{
		if (_instance == null)
		{
			_instance = new PluginFactory();
		}
		return _instance;
	}

	public List<OptionsPlugin> getPlugins() {
		return plugins;
	}

	public OptionsPlugin getActivePlugin() {
		OptionsPlugin activePlugin = null;
        for (OptionsPlugin thisPlugin : plugins) {
            if (thisPlugin.getEnabled().equals(true)) {
                activePlugin = thisPlugin;
            }
        }
        return activePlugin;
	}
}
