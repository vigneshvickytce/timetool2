package com.timeTool.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import com.timeTool.TimeTool;

public class OptionsAction  extends AbstractAction
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1068951283732409180L;
	public static final String optionsAction  = "options"; 

    public OptionsAction() 
	{
	    super(optionsAction);
	}
    public void actionPerformed(ActionEvent e) 
    {
    	TimeTool.getInstance().options();  
	}
}