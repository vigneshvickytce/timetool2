package com.timeTool.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class ColumnHeaderListener extends MouseAdapter 
{
	private final TaskTable model;

	public ColumnHeaderListener(TaskTable model) {
		if (model == null) throw new IllegalArgumentException("Null: model");
		this.model = model;
	}


	@Override
	public void mouseClicked(MouseEvent event) {
		final JTableHeader tableHeader = (JTableHeader)event.getSource();
		final TableColumnModel colModel = tableHeader.getTable().getColumnModel();
		final int index = colModel.getColumnIndexAtX(event.getX());
		model.sort(index);
	}

}
