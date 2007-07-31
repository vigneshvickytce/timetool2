package com.timeTool.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import com.timeTool.TimeTool;


public class ReloadAction extends AbstractAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1721559511179993893L;
	public static final String reloadAction   = "reload";  
    
    public ReloadAction() 
	{
	    super(reloadAction);
	}
    public void actionPerformed(ActionEvent e) 
    {
		TimeTool.getInstance().reloadTaskList();  
	}
}
