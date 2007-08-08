package com.timeTool.ui;

import com.timeTool.ResourceAutomation;
import com.timeTool.TimeToolPreferences;

import java.awt.Component;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.text.NumberFormat;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class GeneralOptions implements OptionsPlugin {
	private JTextField autosaveField;
	private Component parent;
	private JComboBox skinField;

	public JPanel configurationOptions(CommonDialog parent) {

		this.parent = parent;
		final TimeToolPreferences options = new TimeToolPreferences();
		final JLabel autosaveLabel = new JLabel("Time (seconds) between automatic save");
		autosaveField = new JFormattedTextField(NumberFormat.getNumberInstance());
		autosaveField.setText(String.valueOf(options.getAutosave()));
		autosaveField.setColumns(5);

		final JLabel skinLabel = new JLabel("Look and Feel");
		skinField = new JComboBox(options.getAvailableSkins());
		skinField.setSelectedItem(options.getSkin());
		skinField.setRenderer(new DefaultListCellRenderer(){
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
			 {
				 final File file = (File)value;
				 return super.getListCellRendererComponent(list, file.getName(), index, isSelected, cellHasFocus);
			 }
		});

		final JPanel panel = new JPanel(new GridBagLayout());

		panel.add(autosaveLabel,	   new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(autosaveField,	   new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, NORTHWEST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(skinLabel,	       new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(skinField,	       new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, NORTHWEST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(parent.getButtons(), new GridBagConstraints(1, 2, 1, 1, 0.0, 1.0, SOUTHEAST, NONE, new Insets(2,2,2,2), 0, 0));

		return panel;
	}

	public JPanel getAddTaskPanel(CommonDialog parent) throws Exception {
		throw new UnsupportedOperationException("method not implemented");
	}

	public void onOK() throws Exception {
		String value = autosaveField.getText();

		final TimeToolPreferences options = new TimeToolPreferences();
		options.setSkin((File)skinField.getSelectedItem());
		options.setAutosave(Long.valueOf(value));
		options.serialize();
	}

	public String getOptionsTitle(ResourceAutomation resources) {
		return "General Options";
	}

	public Boolean getEnabled() {
		return false; //does not provide an add task panel.
	}

	public String getSelectedTask() {
		throw new UnsupportedOperationException("method not implemented");
	}

	public String getSelectedDescription() {
		throw new UnsupportedOperationException("method not implemented");
	}

}
