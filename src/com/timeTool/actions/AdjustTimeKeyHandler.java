package com.timeTool.actions;

import com.timeTool.TimeTool;

import javax.swing.JFrame;


public class AdjustTimeKeyHandler {
	private final TimeTool timeTool;
	private final JFrame frame;


	public AdjustTimeKeyHandler(TimeTool timeTool, JFrame frame) {
		this.timeTool = timeTool;
		this.frame = frame;
	}

	public void perform(char key) {
		timeTool.adjustTime(String.valueOf(key), frame);
	}

}
