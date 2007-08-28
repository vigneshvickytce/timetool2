package com.timeTool.ui;

import com.timeTool.TimeTool;
import com.timeTool.ResourceAutomation;
import com.timeTool.TimeTool.TimeToolListener;

import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.BorderFactory;

/**
*  This class pops open a window when an idle occurs. 
*
*	@author Hamlet D'Arcy (hamlet.darcy@pearson.com)
*	@version $Revision:$ submitted $DateTime:$ by $Author:$
*/
class IdleListener extends TimeToolListener {
	private final String labelTemplate;
	private JLabel timeLabel;
	private JWindow dialog;
	private int idleSeconds = 0;
	final TimeTool controller;


	public IdleListener(final TimeTool controller, ResourceAutomation resources) {
		this.controller = controller;
		dialog = new JWindow();
		dialog.setAlwaysOnTop(true);

		labelTemplate = resources.getResourceString("IdleLabel1");
		timeLabel = new JLabel(labelTemplate);

		final JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
		pane.setLayout(new GridBagLayout());
		final JButton keepButton = new JButton(resources.getResourceString("IdleButtonKeep"));
		final JButton discardButton = new JButton(resources.getResourceString("IdleButtonDiscard"));
		keepButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
			}
		});
		discardButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				controller.adjust("-" + (idleSeconds / 60));
			}
		});

		pane.add(timeLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, new Insets(2,2,2,2), 0, 0));
		pane.add(new JLabel(resources.getResourceString("IdleLabel2")), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, new Insets(2,2,2,2), 0, 0));
		pane.add(new JLabel(resources.getResourceString("IdleLabel3")), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, new Insets(2,2,2,2), 0, 0));
		pane.add(keepButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
		pane.add(discardButton, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));

		dialog.getContentPane().add(pane);
	}


	@Override
	public void onIdle(int seconds) {
		idleSeconds = seconds; 
		timeLabel.setText(labelTemplate.replace("$minutes", String.valueOf(seconds / 60)));
		dialog.pack();
		final GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final GraphicsDevice screen = env.getDefaultScreenDevice();
        final GraphicsConfiguration config = screen.getDefaultConfiguration();
		final Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(config);
		final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		final Dimension dialogSize = dialog.getSize();
		dialog.setLocation(
			(int)(screenSize.getWidth() - dialogSize.getWidth() - insets.right - 2),
			(int)(screenSize.getHeight() - dialogSize.getHeight() - insets.bottom - 2));
		dialog.setVisible(true);
	}
}
