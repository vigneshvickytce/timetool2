package com.timeTool.actions;

import java.awt.event.ActionEvent;
import javax.swing.*;

import com.timeTool.TimeTool;



public class AddAction extends AbstractAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4196598151434315697L;
	
	public static final String addAction      = "add";

    public AddAction() 
	{
	    super(addAction);
	}
    public void actionPerformed(ActionEvent e) 
    {
    	TimeTool.getInstance().addTask();     	
	}
}
