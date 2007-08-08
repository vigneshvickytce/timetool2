package com.timeTool.ui;

import com.timeTool.ResourceAutomation;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

public class AdjustTimeDialog extends JDialog {

    private JTextField textField;
    private String response = null;

    public AdjustTimeDialog(Frame parent, String input, ResourceAutomation resources) {
        super(parent, true);

        String message = resources.getResourceString("AdjustMessage");
        String title = resources.getResourceString("AdjustTitle");
        String okLabel = resources.getResourceString("OKLabel");
        String cancelLable = resources.getResourceString("CancelLabel");

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(title);
        setComponentOrientation(parent.getComponentOrientation());
        setResizable(false);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton ok = new JButton(okLabel);
        ok.setDefaultCapable(true);
        ok.addActionListener(new OKActionListener());
        JButton cancel = new JButton(cancelLable);
        cancel.addActionListener(new CancelActionListener());
        buttons.add(ok);
        buttons.add(cancel);

        Container panel = new JPanel(new BorderLayout(2, 4));
        textField = new JTextField(input, 20);
        panel.add(new JLabel(message), BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);
        panel.add(buttons, BorderLayout.SOUTH);

        Container parentPanel = new JPanel(new BorderLayout());
        parentPanel.add(new JLabel(" "), BorderLayout.NORTH);
        parentPanel.add(new JLabel("   "), BorderLayout.EAST);
        parentPanel.add(panel, BorderLayout.CENTER);
        parentPanel.add(new JLabel("   "), BorderLayout.WEST);
        parentPanel.add(new JLabel(" "), BorderLayout.SOUTH);

        setContentPane(parentPanel);

        //Ensure the text field always gets the first focus.
        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent ce) {
                textField.requestFocusInWindow();
            }
        });

        pack();
        setLocationRelativeTo(parent);
    }

    protected JRootPane createRootPane() {
        JRootPane rootPane = new JRootPane();

        rootPane.registerKeyboardAction(
            new CancelActionListener(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW);

        rootPane.registerKeyboardAction(
            new OKActionListener(),
            KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
            JComponent.WHEN_IN_FOCUSED_WINDOW);

        return rootPane;
    }

    public String getResponse() {
        return response;         
    }

    private class CancelActionListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            setVisible(false);
        }
    }

    private class OKActionListener implements ActionListener {
        public void actionPerformed(ActionEvent actionEvent) {
            response = textField.getText();
            setVisible(false);
        }
    }
}
