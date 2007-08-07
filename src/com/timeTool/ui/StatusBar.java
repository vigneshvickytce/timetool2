package com.timeTool.ui;

import com.timeTool.ResourceAutomation;
import com.timeTool.Task;
import com.timeTool.TimeTool;
import com.timeTool.TimeTool.TimeToolListener;

import java.awt.GridBagConstraints;
import static java.awt.GridBagConstraints.*;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicBorders;

public class StatusBar extends JComponent
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

		controller.addListener(new MyTimeToolListener());
	}

    private JLabel createSaveNotification(ResourceAutomation resources) {
        JLabel label = new JLabel(resources.getImageResource("saveImage"));
        label.setVisible(false);
        return label;
    }

    private JLabel createLabel() {
		JLabel label = new JLabel();
		label.setBorder(BasicBorders.getTextFieldBorder());
        return label; 
    }

	private final class MyTimeToolListener extends TimeToolListener {
		@Override
		public void onSave() {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					saveLabel.setVisible(true);
				}
			});
			executor.execute(new Runnable() {
				public void run() {
					try {
						Thread.sleep(600L);
					} catch (InterruptedException ignored) {
						//ignored
					}
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							saveLabel.setVisible(false);
						}
					});
				}
			});
		}


		@Override
		public void onTaskChange(Task unused) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					{
						Date currentTime = controller.getTime();
						if (currentTime != null) {
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
			});
		}
	}
}