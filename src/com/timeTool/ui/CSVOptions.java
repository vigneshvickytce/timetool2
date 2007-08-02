package com.timeTool.ui;

import com.timeTool.ui.CommonDialog;
import com.timeTool.ui.OptionsPlugin;
import com.timeTool.ExportOptions;
import com.timeTool.ResourceAutomation;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class CSVOptions implements OptionsPlugin {
	private static final String QUOTES_LABEL = "CSVQuoteLabelMessage";
	private static final String DECIMAL_LABEL = "CSVDecimalLabelMessage";
	private static final String DELIMITER_LABEL = "CSVDelimiterLabelMessage";
	private static final String CSVOPTIONS_TITLE_MESSAGE = "CSVOptionsTitleMessage";
	private ExportOptions options;
	private JTextField delimiterField;
	private JTextField decimalField;
	private JTextField quotesField;

	public CSVOptions()
	{
        options = new ExportOptions(); 

	}
	
//	public ExportOptions getExportOptions()
//	{
//		return options; 
//	}
	public JPanel configurationOptions(CommonDialog parent)
	{
        JPanel panel = new JPanel(); 
        parent.createGridBag(panel); 
        parent.addLabel(ResourceAutomation.getResourceString(DELIMITER_LABEL), 0, panel);
        parent.addLabel(ResourceAutomation.getResourceString(DECIMAL_LABEL), 1, panel); 
        parent.addLabel(ResourceAutomation.getResourceString(QUOTES_LABEL), 2, panel);
        delimiterField = parent.addField(0, options.getDelimiter(), panel);
		decimalField = parent.addField(1, options.getDecimal(), panel);
		quotesField = parent.addField(2, options.getQuotes(), panel); 

		parent.addButtons(panel); 

		return panel;
	}

	public void onOK() throws Exception
	{
        options.setDelimiter(delimiterField.getText()); 
        options.setDecimal(decimalField.getText()); 
		options.setQuotes(quotesField.getText()); 
		options.serialize();
	}

	public String getOptionsTitle()
	{
		return ResourceAutomation.getResourceString(CSVOPTIONS_TITLE_MESSAGE); 
	}

	public Boolean getEnabled()
	{
		// always return false b/c it is always enabled... 
		//makes sense huh?  
		return new Boolean(false);
	}

	public JPanel getAddTaskPanel(CommonDialog parent)
	{
		// this will never override the add task dialog
		return null;
	}

	public String getSelectedTask()
	{
		// not needed
		return null;
	}

	public String getSelectedDescription()
	{
		// not needed
		return null;
	}

}
