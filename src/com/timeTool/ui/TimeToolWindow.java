package com.timeTool.ui;

import com.jeans.trayicon.WindowsTrayIcon;
import com.timeTool.*;
import com.timeTool.TimeTool.TimeToolListener;
import com.timeTool.actions.AboutAction;
import com.timeTool.actions.AddAction;
import com.timeTool.actions.AdjustAction;
import com.timeTool.actions.AdjustTimeKeyHandler;
import com.timeTool.actions.DeleteAction;
import com.timeTool.actions.ExportAction;
import com.timeTool.actions.HelpAction;
import com.timeTool.actions.HomePageAction;
import com.timeTool.actions.LicenseAction;
import com.timeTool.actions.OptionsAction;
import com.timeTool.actions.QuitAction;
import com.timeTool.actions.ReloadAction;
import com.timeTool.actions.RenameAction;
import com.timeTool.actions.ResetAction;
import com.timeTool.actions.SaveAction;
import com.timeTool.actions.StopAction;
import com.timeTool.actions.SupportAction;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Container;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;

public final class TimeToolWindow {

	private static WindowsTrayIcon trayIcon;
	private final TimeTool controller;
	private TaskTable dataTable;

	private final JFrame frame;
	private final JPanel myPanel;
	private final ResourceAutomation resources;
	private final JTable taskList;

	public TimeToolWindow(ResourceAutomation resources, TimeTool controller) {
		this.resources = resources;
		this.controller = controller;

		final Color tableBackgroundColor = resources.getColorResource("TableBackgroundColor");
		final Color tableForegroundColor = resources.getColorResource("TableForegroundColor");
		final Color tableHeaderBackgroundColor = resources.getColorResource("TableHeaderBackgroundColor");
		final Color tableHeaderForegroundColor = resources.getColorResource("TableHeaderForegroundColor");
		final Color frameBackgroundColor = resources.getColorResource("FrameBackgroundColor");
		final Color frameForegroundColor = resources.getColorResource("FrameForegroundColor");

		frame = new JFrame();
		myPanel = new GradientPanel(frameBackgroundColor, tableBackgroundColor);
		setLookAndFeel(resources, myPanel);

		resources.createCommandTable(new Action[]{
				new StopAction(controller),
				new AboutAction(controller, frame),
				new AdjustAction(controller, frame),
				new ResetAction(controller, frame),
				new ReloadAction(controller),
				new SaveAction(controller, frame),
				new ExportAction(controller, frame),
				new AddAction(controller, frame),
				new DeleteAction(controller, frame),
				new RenameAction(controller, frame),
				new QuitAction(),
				new HelpAction(resources),
				new OptionsAction(controller, frame) ,
				new LicenseAction(resources),
				new HomePageAction(resources),
				new SupportAction(resources)
			});


		frame.setTitle(resources.getResourceString("Title"));
		frame.getContentPane().setLayout(new BorderLayout());
		frame.setJMenuBar(resources.createMenubar());
		frame.addWindowListener(new WindowEventHandler(controller));
		frame.setIconImage(resources.getImageResource("IconImage").getImage());
		if (frameBackgroundColor != null) frame.setBackground(frameBackgroundColor);
		if (frameForegroundColor != null) frame.setForeground(frameForegroundColor);

		frame.getContentPane().add("Center", myPanel);

        dataTable = new TaskTable(controller.getTaskList(), resources);
        taskList = createTaskList(tableHeaderBackgroundColor, tableHeaderForegroundColor, tableBackgroundColor, tableForegroundColor);


		final JScrollPane scroller = enableScrolling();
		initPanel(scroller);
		resources.createMenubar();

		createTrayIcon();
		createKeyHandler(controller);

		setColors(frame.getContentPane(), frameBackgroundColor, frameForegroundColor);
		frame.pack();
		frame.setSize(500, 300);

		controller.addListener(new TrayListener());
		controller.addListener(new TableListener());
	}

	private void createKeyHandler(final TimeTool controller) {
		final AdjustTimeKeyHandler adjustTimeKeyHandler = new AdjustTimeKeyHandler(controller, frame);

		char[] numericKeys = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
		for (final char key : numericKeys) {
			myPanel.registerKeyboardAction(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					adjustTimeKeyHandler.perform(key);
				}
			}, KeyStroke.getKeyStroke(key), JComponent.WHEN_IN_FOCUSED_WINDOW);
		}

		char[] adjustKeys = new char[]{'+', '-'};
		for (final char key : adjustKeys) {
			myPanel.registerKeyboardAction(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					try {
						if (e.getModifiers() == Event.SHIFT_MASK) {
							adjustTimeKeyHandler.perform(key);
						} else if (e.getModifiers() == 0) {
							controller.adjust(key + "1");
						}
					} catch (Exception ex) {
						ErrorHandler.showError(frame, new Exception(ex), resources);
					}
				}
			}, KeyStroke.getKeyStroke(key), JComponent.WHEN_IN_FOCUSED_WINDOW);
		}
	}

	private JTable createTaskList(final Color headerBgColor, final Color headerFgColor, Color tableBgColor, Color tableFgColor) {
		JTable table = new JTable(dataTable);
		table.setName("taskTable"); 
		if (tableBgColor != null) table.setBackground(tableBgColor);
		if (tableFgColor != null) table.setForeground(tableFgColor);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setColumnSelectionAllowed(false);
		table.setShowVerticalLines(false);
		table.setShowHorizontalLines(false);
		if (tableBgColor != null) table.getTableHeader().setBackground(tableBgColor);
		if (tableFgColor != null) table.getTableHeader().setForeground(tableFgColor);


		if (headerBgColor != null && headerFgColor != null) {
			final TableCellRenderer headerRenderer = new DefaultTableCellRenderer(){

				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					comp.setBackground(headerBgColor);
					comp.setForeground(headerFgColor);
					return comp;
				}
			};
			final Enumeration<TableColumn> columns = table.getColumnModel().getColumns();
			while(columns.hasMoreElements()) {
				columns.nextElement().setHeaderRenderer(headerRenderer);
			}
		}
		trapTableClick(table);
		trapColumnClick(table);
        return table;
    }

	private void createTrayIcon() {
		try
		{
			WindowsTrayIcon.initTrayIcon(resources.getResourceString("Title"));
			final ImageIcon trayImage = resources.getImageResource("TrayIconImage");
			trayIcon = new WindowsTrayIcon(trayImage.getImage(), 16, 16);
			trayIcon.setToolTipText(resources.getResourceString("Title"));
			trayIcon.addActionListener(new RestoreListener());
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}


	private JScrollPane enableScrolling()

	{
		JScrollPane scroller = new JScrollPane();
		JViewport port = scroller.getViewport();
		port.add(taskList);
		return scroller;
	}

	public Point getLocation() {
		return frame.getLocation();
	}

	public void hide() {
		frame.setVisible(false);
		frame.dispose();
	}

	private void initPanel(JScrollPane scroller) {
		myPanel.setOpaque(false);

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add("North",resources.createToolbar());
		panel.add("Center", scroller);
		myPanel.add("Center", panel);
		StatusBar statusBar = new StatusBar(controller, resources);
		myPanel.add("South", statusBar);
	}

	private void setColors(Container container, Color backgroundColor, Color foregroundColor) {
		if (backgroundColor != null) container.setBackground(backgroundColor);
		if (foregroundColor != null) container.setForeground(foregroundColor);
		for (Component child : container.getComponents()) {
			if (child instanceof Container) {
				setColors((Container)child, backgroundColor, foregroundColor);
			} else {
				if (backgroundColor != null) child.setBackground(backgroundColor);
				if (foregroundColor != null) child.setForeground(foregroundColor);
			}
		}
	}

	public void setLocation(Point origLocation) {
		frame.setLocation(origLocation);
	}

	private static void setLookAndFeel(ResourceAutomation resources, JPanel myPanel) {
		// Force SwingSet to come up in the Cross Platform L&F
		try
		{
			//UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			// If you want the System L&F instead, comment out the above line and
			// uncomment the following:
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception exc)
		{
			System.err.println("Error loading L&F: " + exc);
			ErrorHandler.showError(null, exc, resources);
		}
		myPanel.setBorder(BorderFactory.createEtchedBorder());
		myPanel.setLayout(new BorderLayout());
	}

	public void show() {
		try {
			frame.setVisible(true);
		} catch (Throwable t) {
			ErrorHandler.showError(frame, new Exception(t), resources);
			System.out.println(resources.getResourceString("UncaughtException") + t);
			t.printStackTrace();
			WindowsTrayIcon.cleanUp();
		}
	}

	private void trapColumnClick(JTable table) {
		JTableHeader header = table.getTableHeader();
		header.addMouseListener(new ColumnHeaderListener(dataTable));
	}

	private void trapTableClick(JTable table) {
		//Ask to be notified of selection changes.
		ListSelectionModel rowSM = table.getSelectionModel();
		rowSM.addListSelectionListener(new MyListSelectionListener());
	}


    private class MyListSelectionListener implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent event) {
            //Ignore extra messages.
            if (event.getValueIsAdjusting()) return;

            ListSelectionModel model = (ListSelectionModel)event.getSource();
            if (!model.isSelectionEmpty()) {
                int selectedRow = model.getMinSelectionIndex();
                Task task = dataTable.getTaskAt(selectedRow);
                controller.setCurrentRow(task.getId());
            }
        }
    }
	// Callback listener for hide button
	private class RestoreListener implements ActionListener
	{

		public void actionPerformed(ActionEvent evt) {
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					frame.setState(Frame.NORMAL);
					frame.setVisible(true);
					frame.toFront();
					frame.requestFocus();
					trayIcon.setVisible(false);
				}
			});
		}
	}
	private final class TableListener extends TimeToolListener {

		private final String hoursLabel = resources.getResourceString("GridHourHeader");
		private final String title = resources.getResourceString("Title");


		@Override

		public void onTaskChange(final Task task) {
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
                    if (dataTable.getIndexOf(task) == -1) {
                        dataTable.add(task);
                        dataTable.fireTableDataChanged();
                    }
                    taskList.repaint();
                    trayIcon.setToolTipText(
						title + "\n" +
							controller.getTotalHours() + " " + hoursLabel + "\n" +
							task.getId() + " " + task.getHours() + " " + hoursLabel);
                    int index = dataTable.getIndexOf(controller.getCurrentTask());

                    taskList.changeSelection(index, 1, false, false);
				}
			});
		}

        public void onTaskRemove(final Task task) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    dataTable.remove(task);
                    dataTable.fireTableDataChanged();
                }
            });
        }


        @Override

		public void onTimerStopped() {
			SwingUtilities.invokeLater(new Runnable() {

				public void run() {
					taskList.repaint();
					taskList.clearSelection();
				}
			});
		}
	}
	private final class TrayListener extends TimeToolListener {

		private final ImageIcon trayImageRunning = resources.getImageResource("TrayIconImage");
		private final ImageIcon trayImageStopped = resources.getImageResource("TrayIconImageStopped");

		@Override

		public void onTaskChange(Task task) {
			setIconImage(trayImageRunning);
		}


		@Override

		public void onTimerStopped() {
			setIconImage(trayImageStopped);
		}

		private void setIconImage(ImageIcon image) {
			try {
				trayIcon.setImage(image.getImage(), 16, 16);
			} catch (Exception t) {
				ErrorHandler.showError(frame, new Exception(t), resources);
				System.out.println(resources.getResourceString("UncaughtException") + t);
				t.printStackTrace();
				WindowsTrayIcon.cleanUp();
			}
		}
	}
	protected final class WindowEventHandler extends WindowAdapter
	{

		private final TimeTool controller;

		public WindowEventHandler(TimeTool controller) {
			this.controller = controller;
		}

		public void windowClosing(WindowEvent e) {
			WindowsTrayIcon.cleanUp();
			controller.close(frame);
		}


		public void windowIconified(WindowEvent e)

		{
			String osName = System.getProperty("os.name");
			if ((osName.startsWith("Windows")) || osName.startsWith("Mac OS")) {
				SwingUtilities.invokeLater(new Runnable() {

					public void run() {
						frame.setVisible(false);
						trayIcon.setVisible(true);
					}
				});
			}
		}
	}
}