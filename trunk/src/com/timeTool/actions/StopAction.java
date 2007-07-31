package com.timeTool.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.timeTool.TimeTool;

public class StopAction extends AbstractAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5837759531259238087L;
	public static final String stopAction     = "stop";    
    
    public StopAction() 
	{
	    super(stopAction);
	}
    
    public void actionPerformed(ActionEvent e) 
    {
    	TimeTool.getInstance().setCurrentRow(TimeTool.NO_ROW_SELECTED); 
	}


}
