package com.timeTool.actions;

import java.awt.event.ActionEvent;
import javax.swing.*;

import com.timeTool.TimeTool;


public class RenameAction extends AbstractAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7039626610182345324L;
	public static final String renameAction   = "rename";  

    public RenameAction() 
	{
	    super(renameAction);
	}
    public void actionPerformed(ActionEvent e) 
    {
    	TimeTool.getInstance().renameDialog(); 
	}

}
