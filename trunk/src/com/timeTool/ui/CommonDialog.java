package com.timeTool.ui;

import com.timeTool.ErrorHandler;
import com.timeTool.ResourceAutomation;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public abstract class CommonDialog extends JDialog
{

	private static final String CANCEL_LABEL = "CancelLabel";
	private static final String OK_LABEL = "OKLabel";
	public static final int OK = 1;
	public static final int CANCEL = 2;
	private JButton buttonOK;
	protected int response;
	private JButton buttonCancel;
	private GridBagLayout gridLayout;
	private GridBagConstraints gridConstraints = new GridBagConstraints ();
	private int screenHeight = 1;
	protected abstract void onOK() throws Exception;
	protected abstract void onCancel();

	public CommonDialog(JFrame frame, String title, boolean modal) {
		super(frame, title, modal);
		createGridBag();
		response = CANCEL;
		CenterAndResize(frame);
	}

	private JButton createButton(String label, char mnemonic, ActionListener action, KeyStroke keystroke, String name) {
		final JButton button = new JButton (label);
		button.setName(name);
		button.setMnemonic (mnemonic);
		button.setPreferredSize (new Dimension (70, 25));
		button.addActionListener(action);
		button.registerKeyboardAction(action,keystroke,JComponent.WHEN_IN_FOCUSED_WINDOW);
		return button;
	}

	public void CenterAndResize(JFrame frame)
	{
		setSize (400,screenHeight * 80);

		final  Dimension dialogDim = getSize ();
		final  Dimension frameDim = frame.getSize ();
		final  Dimension screenSize = getToolkit ().getScreenSize ();
		final  Point location = frame.getLocation ();
		location.translate (
				 (frameDim.width-dialogDim.width)/2,
				 (frameDim.height-dialogDim.height)/2);
		location.x = Math.max ( 0, Math.min (location.x,
								 screenSize.width-getSize ().width));
		location.y = Math.max (0, Math.min (location.y,
								 screenSize.height-getSize ().height));
		setLocation (location.x, location.y);
	}

	public int getResponse() {
		return response;
	}

	public void addGB(Component component, int x, int y, Container panel) {
		gridConstraints.gridx=x; gridConstraints.gridy = y;
		gridLayout.setConstraints (component, gridConstraints);
		panel.add(component);
		if (screenHeight <= y) {
			screenHeight = y;
		}
	}

	private void createGridBag() {
		gridLayout = new GridBagLayout();
		getContentPane().setLayout(gridLayout);
		gridConstraints.fill = GridBagConstraints.BOTH;
		gridConstraints.gridwidth=6; gridConstraints.gridheight=1;
		gridConstraints.weightx=1d; gridConstraints.weighty=1;
		gridConstraints.insets = new Insets (10,20,10,20);
	}

	public void createGridBag(JPanel panel) {
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

		buttonOK = createButton(
			ResourceAutomation.getResourceString(OK_LABEL),
			'O',
			new ActionListener(){
				public void actionPerformed(ActionEvent event) {
					response = OK;
					try {
						onOK();
					} catch (Exception e) {
						ErrorHandler.showError(CommonDialog.this, e);
					}
					dispose();
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "OKButton");

		buttonCancel = createButton(
			ResourceAutomation.getResourceString(CANCEL_LABEL),
			'C',
			new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					response = CANCEL;
					onCancel();
					dispose();
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "CancelButton");
		buttonPanel.add(buttonOK);
		buttonPanel.add(buttonCancel);
		addGB (buttonPanel, 1, screenHeight+1, panel);
	}

	protected void addLabel(String text, int yPosition, Container panel) {
		JLabel label = new JLabel(text, SwingConstants.RIGHT);
		//Give the labels a small weightx and a
		// width of 1 to push them to the left
		gridConstraints.weightx=0.10; gridConstraints.weighty=1.0;
		gridConstraints.gridwidth=1; gridConstraints.gridheight=1;
		gridConstraints.insets = new Insets (0,0,0,10);
		addGB (label,0,yPosition, panel);
	}

	protected JTextField addField(int yPosition, String defaultText, Container panel) {
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

	protected JCheckBox addCheckbox(int yPosition, boolean defaultState, Container panel) {
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

	protected JComponent addField(JComponent field, int yPosition, Container panel) {
		// Give the textfields a large weightx and a
		// width of 5 so they will have a long x width
		gridConstraints.weightx=1.0; gridConstraints.weighty=1.0;
		gridConstraints.insets = new Insets (10,0,10,10);
		gridConstraints.gridwidth=5; gridConstraints.gridheight=1;
		addGB (field,1,yPosition, panel);
		return field;
	}


}
