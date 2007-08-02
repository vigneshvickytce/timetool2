package com.timeTool;

import com.timeTool.ResourceAutomation;

import java.awt.Component;

import javax.swing.JOptionPane;

public class ErrorHandler
{
	public static void showError(Component owner, Exception exception)
	{
		JOptionPane.showMessageDialog(owner, 
				exception.getMessage(), 
				ResourceAutomation.getResourceString("GenericError"),
				JOptionPane.ERROR_MESSAGE); 
		System.out.println(exception.getMessage()); 
		exception.printStackTrace(); 
	}
}
