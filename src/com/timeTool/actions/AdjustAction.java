package com.timeTool.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.timeTool.TimeTool;

public class AdjustAction extends AbstractAction {

	public static final String adjustAction   = "adjust";  
	
	public AdjustAction() {
	    super(adjustAction);
	}
    
    public void actionPerformed(ActionEvent e) {
    	TimeTool.getInstance().adjustTime(null); 
	}
}
