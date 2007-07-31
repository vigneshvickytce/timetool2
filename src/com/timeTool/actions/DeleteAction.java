package com.timeTool.actions;

import java.awt.event.ActionEvent;
import javax.swing.*;

import com.timeTool.TimeTool;


public class DeleteAction extends AbstractAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6161942866196776011L;
	public static final String deleteAction   = "delete";  

    public DeleteAction() 
	{
	    super(deleteAction);
	}
    public void actionPerformed(ActionEvent e) 
    {
    	TimeTool.getInstance().removeRowDialog(); 
    	
    }

}
