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
	protected abstract void onOK() throws Exception;
	protected abstract void onCancel();

	public CommonDialog(JFrame frame, String title, boolean modal) {
		super(frame, title, modal);
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
		pack();
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

	protected JPanel getButtons() {
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
		return buttonPanel;
	}


}
