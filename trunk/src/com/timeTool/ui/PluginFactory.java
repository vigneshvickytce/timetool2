package com.timeTool.ui;

import com.timeTool.ui.CSVOptions;

import java.util.ArrayList;

public class PluginFactory
{
	private static PluginFactory _instance = null;
	private ArrayList plugins; 
	
	private PluginFactory() throws Exception
	{
		plugins = new ArrayList();
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
	
	public ArrayList getPlugins()
	{
		return plugins; 
	}
	
	public OptionsPlugin getActivePlugin()
	{
		OptionsPlugin activePlugin = null; 
		for (int x = 0; x < plugins.size(); x++)
		{
			OptionsPlugin thisPlugin = (OptionsPlugin)plugins.get(x);
			if (thisPlugin.getEnabled().equals(true))
			{
				activePlugin = thisPlugin; 
			}
		}
		return activePlugin; 
	}
}
