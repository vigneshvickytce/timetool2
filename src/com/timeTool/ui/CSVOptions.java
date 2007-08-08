package com.timeTool.ui;

import com.timeTool.TimeToolPreferences;
import com.timeTool.ResourceAutomation;

import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CSVOptions implements OptionsPlugin {
	private static final String QUOTES_LABEL = "CSVQuoteLabelMessage";
	private static final String DECIMAL_LABEL = "CSVDecimalLabelMessage";
	private static final String DELIMITER_LABEL = "CSVDelimiterLabelMessage";
	private static final String CSVOPTIONS_TITLE_MESSAGE = "CSVOptionsTitleMessage";

	private JTextField delimiterField;
	private JTextField decimalField;
	private JTextField quotesField;

	public CSVOptions() {
	}

	public JPanel configurationOptions(CommonDialog parent)
	{
		final JLabel delimiterLabel = new JLabel(parent.getResources().getResourceString(DELIMITER_LABEL));
		final JLabel decimalLabel = new JLabel(parent.getResources().getResourceString(DECIMAL_LABEL));
		final JLabel quoteLabel = new JLabel(parent.getResources().getResourceString(QUOTES_LABEL));

		final TimeToolPreferences options = new TimeToolPreferences();
        delimiterField = new JTextField(options.getDelimiter(), 5);
		decimalField = new JTextField(options.getDecimal(), 5);
		quotesField = new JTextField(options.getQuotes(), 5);

		final JPanel panel = new JPanel(new GridBagLayout());

		panel.add(delimiterLabel,	new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(delimiterField,	new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, NORTHWEST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(decimalLabel, 	new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(decimalField, 	new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, NORTHWEST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(quoteLabel, 		new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(quotesField, 		new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, NORTHWEST, NONE, new Insets(2,2,2,2), 0, 0));
		panel.add(parent.getButtons(), new GridBagConstraints(1, 3, 1, 1, 0.0, 1.0, SOUTHEAST, NONE, new Insets(2,2,2,2), 0, 0));

		return panel;
	}

	public void onOK() throws Exception
	{
        final TimeToolPreferences options = new TimeToolPreferences();
        options.setDelimiter(delimiterField.getText());
		options.setDecimal(decimalField.getText());
		options.setQuotes(quotesField.getText());
		options.serialize();
	}

	public String getOptionsTitle(ResourceAutomation resources)
	{
		return resources.getResourceString(CSVOPTIONS_TITLE_MESSAGE);
	}

	public Boolean getEnabled()
	{
		// always return false b/c it is always enabled... 
		//makes sense huh?  
		return false;
	}

	public JPanel getAddTaskPanel(CommonDialog parent)
	{
        throw new UnsupportedOperationException("method not implemented");
	}

	public String getSelectedTask()
	{
        throw new UnsupportedOperationException("method not implemented");
	}

	public String getSelectedDescription()
	{
        throw new UnsupportedOperationException("method not implemented");
	}

}
