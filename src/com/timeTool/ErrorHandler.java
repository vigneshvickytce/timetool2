package com.timeTool;

import java.awt.Component;

import javax.swing.JOptionPane;

public class ErrorHandler
{
	public static void showError(Component owner, Exception exception, ResourceAutomation resources)
	{
		JOptionPane.showMessageDialog(owner, 
				exception.getMessage(), 
				resources == null ? "" : resources.getResourceString("GenericError"),
				JOptionPane.ERROR_MESSAGE); 
		System.out.println(exception.getMessage()); 
		exception.printStackTrace(); 
	}
}
