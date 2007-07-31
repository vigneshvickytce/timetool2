package com.timeTool.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import com.timeTool.TimeTool;


public class ExportAction extends AbstractAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1184213403685600369L;
	public static final String exportAction   = "export";  

	public ExportAction() 
	{
	    super(exportAction);
	}
    public void actionPerformed(ActionEvent e) 
    {
    	TimeTool.getInstance().exportTaskList();  
	}

}
