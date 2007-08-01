package com.timeTool;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class ColumnHeaderListener extends MouseAdapter 
{
	private final TimeTool controller;


	public ColumnHeaderListener(TimeTool controller) {
		this.controller = controller;
	}


	public void mouseClicked(MouseEvent evt)
	{
		JTableHeader tableHeader = (JTableHeader)evt.getSource();
		JTable table = tableHeader.getTable();
		TableColumnModel colModel = table.getColumnModel();

		// The index of the column whose header was clicked
		int index = colModel.getColumnIndexAtX(evt.getX());
		controller.sort(index);
		//tableHeader.set
	}

}
