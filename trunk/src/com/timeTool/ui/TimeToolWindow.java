package com.timeTool.ui;

import com.jeans.trayicon.WindowsTrayIcon;
import com.timeTool.ErrorHandler;
import com.timeTool.ResourceAutomation;
import com.timeTool.Task;
import com.timeTool.TimePersistence;
import com.timeTool.TimeTool;
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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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

public final class TimeToolWindow
{

	private static JFrame frame;

	private static WindowsTrayIcon trayIcon;
	private final TimeTool controller;
	private TaskTable dataTable;
	private final Color frameBackgroundColor;
	private final Color frameForegroundColor;
	private final ResourceAutomation resources;
	private final Color tableBackgroundColor;
	private final Color tableForegroundColor;
	private final Color tableHeaderBackgroundColor;
	private final Color tableHeaderForegroundColor;
	private JTable taskList;
	private JPanel myPanel = new JPanel(true) {
		// Overrides parent

		public void paintComponent(Graphics g1) {
			if ((frameBackgroundColor == null) || (tableBackgroundColor == null)) {
				super.paintComponent(g1);
			} else {
				Graphics2D g2 = (Graphics2D) g1;
				g2.setPaint(new GradientPaint(0, 0, frameBackgroundColor, 0, getHeight(), tableBackgroundColor));
				g2.fillRect(0, 0, getWidth(), getHeight());
				super.paintComponent(g2);
			}
		}
	};

	public TimeToolWindow(ResourceAutomation resources, TimeTool controller) {

		setLookAndFeel();
		this.resources = resources;
		this.controller = controller;
		frameBackgroundColor = resources.getColorResource("FrameBackgroundColor");
		frameForegroundColor = resources.getColorResource("FrameForegroundColor");
		tableBackgroundColor = resources.getColorResource("TableBackgroundColor");
		tableForegroundColor = resources.getColorResource("TableForegroundColor");
		tableHeaderBackgroundColor = resources.getColorResource("TableHeaderBackgroundColor");
		tableHeaderForegroundColor = resources.getColorResource("TableHeaderForegroundColor");


		resources.createCommandTable(new Action[]{
				 new StopAction(controller),
				 new AboutAction(controller),
				 new AdjustAction(controller),
				 new ResetAction(controller),
					 new ReloadAction(controller),
					new SaveAction(controller),
					new ExportAction(controller),
					new AddAction(controller),
				 new DeleteAction(controller),
				 new RenameAction(controller),
				 new QuitAction(),
				 new HelpAction(resources),
				 new OptionsAction(controller) ,
				 new LicenseAction(resources),
				 new HomePageAction(resources),
				 new SupportAction(resources)
			});

		dataTable = new TaskTable(controller, resources);
		createTaskList();
		final JScrollPane scroller = enableScrolling();
		initPanel(scroller);
		resources.createMenubar();

		createTrayIcon();
		createKeyHandler(controller);
		controller.addListener(new TrayListener());
		controller.addListener(new TableListener());
	}

	private void createKeyHandler(final TimeTool controller) {
		final AdjustTimeKeyHandler adjustTimeKeyHandler = new AdjustTimeKeyHandler(controller);

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

		myPanel.registerKeyboardAction(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					int row = controller.getCurrentRow();
					controller.setCurrentRow(--row);
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		myPanel.registerKeyboardAction(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					int row = controller.getCurrentRow();
					controller.setCurrentRow(++row);
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
	}


	private void createTaskList()

	{
		TimePersistence data = new TimePersistence(controller, resources);
		data.loadFile();
		taskList = new JTable(dataTable);
		if (tableBackgroundColor != null) taskList.setBackground(tableBackgroundColor);
		if (tableForegroundColor != null) taskList.setForeground(tableForegroundColor);
		taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		taskList.setColumnSelectionAllowed(false);
		taskList.setShowVerticalLines(false);
		taskList.setShowHorizontalLines(false);
		if (tableBackgroundColor != null) taskList.getTableHeader().setBackground(tableBackgroundColor);
		if (tableForegroundColor != null) taskList.getTableHeader().setForeground(tableForegroundColor);


		if (tableHeaderBackgroundColor != null && tableHeaderForegroundColor != null) {
			final TableCellRenderer headerRenderer = new DefaultTableCellRenderer(){

				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
					comp.setBackground(tableHeaderBackgroundColor);
					comp.setForeground(tableHeaderForegroundColor);
					return comp;
				}
			};
			final Enumeration<TableColumn> columns = taskList.getColumnModel().getColumns();
			while(columns.hasMoreElements()) {
				columns.nextElement().setHeaderRenderer(headerRenderer);
			}
		}
		trapTableClick();
		trapColumnClick();
	}


	private void createTrayIcon()

	{
		try
		{
			WindowsTrayIcon.initTrayIcon(resources.getResourceString("Title"));
			final ImageIcon trayImage = resources.getImageResource("TrayIconImage");
			trayIcon = new WindowsTrayIcon(trayImage.getImage(), 16, 16);
			trayIcon.setToolTipText(resources.getResourceString("Title"));
			trayIcon.addActionListener(new RestoreListener());
		}
		catch (Exception e2)
		{
			//do nothing
		}
	}


	private JScrollPane enableScrolling()

	{
		JScrollPane scroller = new JScrollPane();
		JViewport port = scroller.getViewport();
		port.add(taskList);
		return scroller;
	}

	public JFrame getFrame() {
		return frame;
	}


	public TaskTable getTable() {
		return dataTable;
	}


	public JTable getTaskList() {
		return taskList;
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


	private void setLookAndFeel()

	{
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

			frame = new JFrame();
			frame.setTitle(resources.getResourceString("Title"));
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add("Center", myPanel);
			frame.setJMenuBar(resources.getMenubar());
			frame.addWindowListener(new WindowEventHandler(controller));
			frame.setIconImage(resources.getImageResource("IconImage").getImage());

			if (frameBackgroundColor != null) frame.setBackground(frameBackgroundColor);
			if (frameForegroundColor != null) frame.setForeground(frameForegroundColor);
			setColors(frame.getContentPane(), frameBackgroundColor, frameForegroundColor);

			frame.pack();
			frame.setSize(500, 300);

			frame.setVisible(true);
		} catch (Throwable t) {
			ErrorHandler.showError(frame, new Exception(t), resources);
			System.out.println(resources.getResourceString("UncaughtException") + t);
			t.printStackTrace();
			WindowsTrayIcon.cleanUp();
		}
	}


	private void trapColumnClick()

	{
		JTableHeader header = taskList.getTableHeader();
		header.addMouseListener(new ColumnHeaderListener(controller));
	}


	private void trapTableClick()

	{
		//Ask to be notified of selection changes.
		ListSelectionModel rowSM = taskList.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener()
		{

			public void valueChanged(ListSelectionEvent event)

			{
				//Ignore extra messages.
				if (event.getValueIsAdjusting()) return;

				ListSelectionModel model = (ListSelectionModel)event.getSource();
				if (!model.isSelectionEmpty()) {
					int selectedRow = model.getMinSelectionIndex();
					controller.setCurrentRow(selectedRow);
				}
			}
		});
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
					taskList.repaint();
					trayIcon.setToolTipText(
						title + "\n" +
							controller.getTotalHours() + " " + hoursLabel + "\n" +
							task.getId() + " " + task.getHours() + " " + hoursLabel);

					taskList.changeSelection(controller.getCurrentRow(),
						1,
						false,
						false);
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
	protected static final class WindowEventHandler extends WindowAdapter
	{

		private final TimeTool controller;

		public WindowEventHandler(TimeTool controller) {
			this.controller = controller;
		}


		public void windowClosing(WindowEvent e)

		{
			WindowsTrayIcon.cleanUp();
			controller.close();
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