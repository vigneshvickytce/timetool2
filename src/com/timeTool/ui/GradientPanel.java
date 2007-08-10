package com.timeTool.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
* User: hdarcy
* Date: Aug 9, 2007
* Time: 9:52:13 PM
* To change this template use File | Settings | File Templates.
*/
class GradientPanel extends JPanel {
    private final Color gradientStop;
    private final Color gradientStart;

    public GradientPanel(Color gradientStart, Color gradientStop) {
        super(true);
        this.gradientStart = gradientStart;
        this.gradientStop = gradientStop;
    }
    // Overrides parent

    public void paintComponent(Graphics g1) {
        if ((gradientStart == null) || (gradientStop == null)) {
            super.paintComponent(g1);
        } else {
            Graphics2D g2 = (Graphics2D) g1;
            g2.setPaint(new GradientPaint(0, 0, gradientStart, 0, getHeight(), gradientStop));
            g2.fillRect(0, 0, getWidth(), getHeight());
            super.paintComponent(g2);
        }
    }
}
