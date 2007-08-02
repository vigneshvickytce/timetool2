package com.timeTool.ui;

import com.timeTool.ResourceAutomation;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class AdjustTimeDialog extends JDialog {

    private JTextField textField;
    private String response = null;

    public AdjustTimeDialog(Frame parent, String input) {
        super(parent, true);

        String message = ResourceAutomation.getResourceString("AdjustMessage");
        String title = ResourceAutomation.getResourceString("AdjustTitle");
        String okLabel = ResourceAutomation.getResourceString("OKLabel");
        String cancelLable = ResourceAutomation.getResourceString("CancelLabel");

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
