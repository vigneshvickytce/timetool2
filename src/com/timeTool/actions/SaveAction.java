package com.timeTool.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.timeTool.TimeTool;


public class SaveAction extends AbstractAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8391794940416088293L;
	public static final String saveAction     = "save";    
    
    public SaveAction() 
	{
	    super(saveAction);
	}
    public void actionPerformed(ActionEvent e) 
    {
		TimeTool.getInstance().saveTaskList(); 
	}
}
