package com.timeTool.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class QuitAction extends AbstractAction
{
	public static final String quitAction     = "quit";    

	public QuitAction() 
	{
	    super(quitAction);
	}

    public void actionPerformed(ActionEvent e) 
    {
        System.exit(0);
	}
}
