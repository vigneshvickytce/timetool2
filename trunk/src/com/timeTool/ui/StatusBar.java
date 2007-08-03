package com.timeTool.ui;

import com.timeTool.TimeTool;
import com.timeTool.ResourceAutomation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.awt.*;
import static java.awt.GridBagConstraints.NORTHEAST;
import static java.awt.GridBagConstraints.NONE;
import static java.awt.GridBagConstraints.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicBorders;

public class StatusBar extends JComponent implements Observer
{
	private JLabel dayOfWeek;
	private JLabel time;
	private JLabel date; 
	private JLabel task; 
	private JLabel minuteTotal; 
	private JLabel hourTotal;
	private final TimeTool controller;
    private JLabel saveLabel;
    private final Executor executor;


    public StatusBar(TimeTool controller, ResourceAutomation resources) {
		super();
		this.controller = controller;

        executor = Executors.newSingleThreadExecutor();
        setLayout(new GridBagLayout());

        time = createLabel(); 
        dayOfWeek = createLabel();
        date = createLabel(); 
        task = createLabel(); 
        minuteTotal = createLabel();
        hourTotal = createLabel();
        saveLabel = createSaveNotification(resources);

        add(time,       new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, WEST, NONE, new Insets(1,1,1,1), 0, 0));
        add(dayOfWeek,  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, WEST, NONE, new Insets(1,1,1,1), 0, 0));
        add(date,       new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, WEST, NONE, new Insets(1,1,1,1), 0, 0));
        add(task,       new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, WEST, NONE, new Insets(1,1,1,1), 0, 0));
        add(minuteTotal,new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0, WEST, NONE, new Insets(1,1,1,1), 0, 0));
        add(hourTotal,  new GridBagConstraints(5, 0, 1, 1, 1.0, 0.0, WEST, NONE, new Insets(1,1,1,1), 0, 0));
        add(saveLabel,  new GridBagConstraints(6, 0, 1, 1, 1.0, 0.0, EAST, NONE, new Insets(1,1,1,1), 0, 0));

        controller.addObserver(this);
	}

    private JLabel createSaveNotification(ResourceAutomation resources) {
        JLabel label = new JLabel(new ImageIcon(resources.getResource("saveImage")));
        label.setVisible(false);
        return label;
    }

    private JLabel createLabel() {
		JLabel label = new JLabel();
		label.setBorder(BasicBorders.getTextFieldBorder());
        return label; 
    }

    public void update(Observable arg0, Object arg1){
        if (arg1 == TimeTool.EVENT_SAVED_BY_USER) {
            saveLabel.setVisible(true);
           executor.execute(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(600L);
                    } catch (InterruptedException ignored) {
                        //ignored
                    }
                    saveLabel.setVisible(false);
                }
            });
        } else {
            Date currentTime = controller.getTime();

            time.setText(DateFormat.getTimeInstance().format(currentTime));
            date.setText(DateFormat.getDateInstance().format(currentTime));

            SimpleDateFormat df = new SimpleDateFormat("E");
            dayOfWeek.setText(df.format(currentTime));

            minuteTotal.setText(controller.getTotalMinutes());

            hourTotal.setText(controller.getTotalHours());

            task.setText(controller.getCurrentTask());
        }
    }

}