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
import java.awt.event.KeyEvent;

import javax.swing.*;

/**
*  This class pops open a window when an idle occurs. 
*
*	@author Hamlet D'Arcy (hamlet.darcy@pearson.com)
*	@version $Revision:$ submitted $DateTime:$ by $Author:$
*/
class IdleListener extends TimeToolListener {
	private final String labelTemplate;
	private JLabel timeLabel;
	private JDialog dialog;
	private int idleSeconds = 0;
	final TimeTool controller;


    public IdleListener(final TimeTool controller, ResourceAutomation resources) {
		this.controller = controller;
		dialog = new JDialog();
		dialog.setAlwaysOnTop(true);
        dialog.setUndecorated(true);

        labelTemplate = resources.getResourceString("IdleLabel1");
		timeLabel = new JLabel(labelTemplate);

		final JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createLineBorder(Color.blue, 3));
		pane.setLayout(new GridBagLayout());
		final JButton keepButton = new JButton(resources.getResourceString("IdleButtonKeep"));
		final JButton discardButton = new JButton(resources.getResourceString("IdleButtonDiscard"));

        keepButton.setMnemonic('k');
        ActionListener keepAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        };
        keepButton.addActionListener(keepAction);

        discardButton.setMnemonic('d');
        ActionListener discardAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
                controller.adjust("-" + (idleSeconds / 60));
            }
        };
        discardButton.registerKeyboardAction(discardAction,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        discardButton.addActionListener(discardAction);

		pane.add(timeLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, new Insets(2,2,2,2), 0, 0));
		pane.add(new JLabel(resources.getResourceString("IdleLabel2")), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, new Insets(2,2,2,2), 0, 0));
		pane.add(new JLabel(resources.getResourceString("IdleLabel3")), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, CENTER, HORIZONTAL, new Insets(2,2,2,2), 0, 0));
		pane.add(keepButton, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));
		pane.add(discardButton, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, NORTHEAST, NONE, new Insets(2,2,2,2), 0, 0));

		dialog.getContentPane().add(pane);
	}


	@Override
	public void onIdle(int seconds) {
        if (seconds < idleSeconds) {
            //when the seconds resets we need to accumulate the time
            idleSeconds += seconds;             
        } else {
            idleSeconds = seconds;
        }
        System.out.println("Idle: Seconds accumulated: " + idleSeconds);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                timeLabel.setText(labelTemplate.replace("$minutes", String.valueOf(idleSeconds / 60)));
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

                if (!dialog.isVisible()) {
                    dialog.setVisible(true);
                }
            }
        }); 
    }
}
