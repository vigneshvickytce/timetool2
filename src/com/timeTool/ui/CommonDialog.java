package com.timeTool.ui;

import com.timeTool.ErrorHandler;
import com.timeTool.ResourceAutomation;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class CommonDialog extends JDialog implements ActionListener
{

	private static final String CANCEL_LABEL = "CancelLabel";
	private static final String OK_LABEL = "OKLabel";
	public static final int OK = 1;
	public static final int CANCEL = 2;
	protected JButton buttonOK;
	protected int response;
	protected JButton buttonCancel;
	protected GridBagLayout gridLayout;
	protected GridBagConstraints gridConstraints = new GridBagConstraints ();
	private int screenHeight = 1; 
	protected abstract void onOK() throws Exception; 
	protected abstract void onCancel(); 
	
	public CommonDialog(JFrame frame, String title, boolean modal)
	{
		super(frame, title, modal); 
		createGridBag();
        response = CANCEL; 
        CenterAndResize(frame); 
	}
    public void actionPerformed(ActionEvent arg0) 
	{
		if (arg0.getSource () == buttonOK)  
		{
	    	this.response = OK; 
	    	try
			{
				onOK();
			}
			catch (Exception e)
			{
				ErrorHandler.showError(this, e);
			} 
	    }
		else
		{
	    	this.response = CANCEL; 
	    	onCancel(); 
		}
		// Close dialog after OK or Cancel button clicked
	    this.dispose ();		
	}

	protected JButton createButton(String label, char mnemonic)
	{
		JButton button = new JButton (label);
		button.setMnemonic (mnemonic);
		button.setPreferredSize (new Dimension (70, 25));
		button.addActionListener (this);
		return button; 
	}

	public void CenterAndResize(JFrame frame)
	{
	    setSize (400,screenHeight * 80);

	    Dimension dialogDim = getSize ();
	    Dimension frameDim = frame.getSize ();
	    Dimension screenSize = getToolkit ().getScreenSize ();
	    Point location = frame.getLocation ();
	    location.translate (
	             (frameDim.width-dialogDim.width)/2,
	             (frameDim.height-dialogDim.height)/2);
	    location.x = Math.max ( 0, Math.min (location.x,
	                             screenSize.width-getSize ().width));
	    location.y = Math.max (0, Math.min (location.y,
	                             screenSize.height-getSize ().height));
	    setLocation (location.x, location.y);
	}

	public int getResponse()
	{
		return response;
	}

	public void addGB(Component component, int x, int y, Container panel)
	{
	    gridConstraints.gridx=x; gridConstraints.gridy = y;
	    gridLayout.setConstraints (component, gridConstraints);
	    panel.add(component);
	    if (screenHeight <= y)
	    {
	    	screenHeight = y; 
	    }
	}

	protected void createGridBag()
	{
		gridLayout = new GridBagLayout();
	    getContentPane().setLayout(gridLayout);
	    gridConstraints.fill = GridBagConstraints.BOTH;
	    gridConstraints.gridwidth=6; gridConstraints.gridheight=1;
	    gridConstraints.weightx=1; gridConstraints.weighty=1;
	    gridConstraints.insets = new Insets (10,20,10,20);
	}
	public void createGridBag(JPanel panel)
	{
		gridLayout = new GridBagLayout();
		panel.setLayout(gridLayout);
	    gridConstraints.fill = GridBagConstraints.BOTH;
	    gridConstraints.gridwidth=6; gridConstraints.gridheight=1;
	    gridConstraints.weightx=1; gridConstraints.weighty=1;
	    gridConstraints.insets = new Insets (10,20,10,20);
	}

	protected void addButtons(Container panel)
	{
		JPanel buttonPanel = new JPanel(); 
	    buttonOK = createButton(ResourceAutomation.getResourceString(OK_LABEL), 'O');
	    buttonCancel = createButton(ResourceAutomation.getResourceString(CANCEL_LABEL), 'C');
	    buttonPanel.add(buttonOK); 
	    buttonPanel.add(buttonCancel); 
	    addGB (buttonPanel, 1, screenHeight+1, panel);
	}

	protected void addLabel(String text, int yPosition, Container panel)
	{
		JLabel label = new JLabel(text, SwingConstants.RIGHT);
	    //Give the labels a small weightx and a
	    // width of 1 to push them to the left
	    gridConstraints.weightx=0.10; gridConstraints.weighty=1.0;
	    gridConstraints.gridwidth=1; gridConstraints.gridheight=1;
	    gridConstraints.insets = new Insets (0,0,0,10);
	    addGB (label,0,yPosition, panel);
	}

	protected JTextField addField(int yPosition, String defaultText, Container panel)
	{
		// Give the textfields a large weightx and a
	    // width of 5 so they will have a long x width
	    gridConstraints.weightx=1.0; gridConstraints.weighty=1.0;
	    gridConstraints.insets = new Insets (10,0,10,10);
	    gridConstraints.gridwidth=5; gridConstraints.gridheight=1;
		JTextField field = new JTextField();
		field.setText(defaultText); 
	    addGB (field,1,yPosition, panel);
	    return field; 
	}

	protected JCheckBox addCheckbox(int yPosition, boolean defaultState, Container panel)
	{
		// Give the textfields a large weightx and a
	    // width of 5 so they will have a long x width
	    gridConstraints.weightx=1.0; gridConstraints.weighty=1.0;
	    gridConstraints.insets = new Insets (10,0,10,10);
	    gridConstraints.gridwidth=5; gridConstraints.gridheight=1;
	    JCheckBox field = new JCheckBox();
		field.setSelected(defaultState); 
	    addGB (field,1,yPosition, panel);
	    return field; 
	}
	protected JComponent addField(JComponent field, int yPosition, Container panel)
	{
		// Give the textfields a large weightx and a
	    // width of 5 so they will have a long x width
	    gridConstraints.weightx=1.0; gridConstraints.weighty=1.0;
	    gridConstraints.insets = new Insets (10,0,10,10);
	    gridConstraints.gridwidth=5; gridConstraints.gridheight=1;
	    addGB (field,1,yPosition, panel);
	    return field; 
	}


}
