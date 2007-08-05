package com.timeTool.ui;

import com.jeans.trayicon.WindowsTrayIcon;
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
import com.timeTool.*;
import com.timeTool.ResourceAutomation;

import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;

public final class TimeToolWindow extends JPanel implements Observer
{
	private JTable taskList;
    private TaskTable dataTable;
	private static JFrame frame;   
    
	private static WindowsTrayIcon trayIcon;
	private final ResourceAutomation resources;
	private final TimeTool controller;


	public TimeToolWindow(ResourceAutomation resources, TimeTool controller) {
		super(true);

		setLookAndFeel();
		this.resources = resources;
		this.controller = controller;

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
    	         new HelpAction(),
    	         new OptionsAction(controller) ,
    	         new LicenseAction(),
    	         new HomePageAction(),
    	         new SupportAction()
    	    });

		dataTable = new TaskTable(controller);
		createTaskList();
		final JScrollPane scroller = enableScrolling();
		initPanel(scroller);
		resources.createMenubar();

		createTrayIcon();
		createKeyHandler(controller);
		controller.addObserver(this);
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
    	    ErrorHandler.showError(null, exc);
    	}
    	setBorder(BorderFactory.createEtchedBorder());
    	setLayout(new BorderLayout());
	}

	private void createKeyHandler(final TimeTool controller) {
        final AdjustTimeKeyHandler adjustTimeKeyHandler = new AdjustTimeKeyHandler(controller);

        char[] numericKeys = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        for (final char key : numericKeys) {
            registerKeyboardAction(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    adjustTimeKeyHandler.perform(key);
                }
            }, KeyStroke.getKeyStroke(key), JComponent.WHEN_IN_FOCUSED_WINDOW);
        }

        char[] adjustKeys = new char[]{'+', '-'};
        for (final char key : adjustKeys) {
            registerKeyboardAction(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    try {
                        if (e.getModifiers() == Event.SHIFT_MASK) {
                            adjustTimeKeyHandler.perform(key);
                        } else if (e.getModifiers() == 0) {
                            controller.adjust(key + "1");
                        }
                    } catch (Exception ex) {
                        ErrorHandler.showError(frame, new Exception(ex)); 
                    }
                }
            }, KeyStroke.getKeyStroke(key), JComponent.WHEN_IN_FOCUSED_WINDOW);
        }

		registerKeyboardAction(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
					int row = controller.getCurrentRow();
					controller.setCurrentRow(--row);
                }
            }, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		registerKeyboardAction(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
					int row = controller.getCurrentRow();
					controller.setCurrentRow(++row);
				}
            }, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

	}

    private void initPanel(JScrollPane scroller)
	{
    	JPanel panel = new JPanel();
    	panel.setLayout(new BorderLayout());	
    	panel.add("North",resources.createToolbar());
    	panel.add("Center", scroller);
    	add("Center", panel);
    	StatusBar statusBar = new StatusBar(controller, resources);
    	add("South", statusBar);
	}

	private JScrollPane enableScrolling()
	{
		JScrollPane scroller = new JScrollPane();
    	JViewport port = scroller.getViewport();
    	port.add(taskList);
		return scroller;
	}
	
	private void createTaskList()
	{
		TimePersistence data = new TimePersistence(controller);
		data.loadFile();
		taskList = new JTable(dataTable); 
		taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		taskList.setColumnSelectionAllowed(false); 
    	taskList.setShowVerticalLines(false); 
    	taskList.setShowHorizontalLines(false);
		trapTableClick(controller);
    	trapColumnClick(); 
	}
	
	private void trapColumnClick()
	{
		JTableHeader header = taskList.getTableHeader();
	    header.addMouseListener(new ColumnHeaderListener(controller));
	}
	
	private void trapTableClick(final TimeTool controller)
	{
		//Ask to be notified of selection changes.
    	ListSelectionModel rowSM = taskList.getSelectionModel();
    	rowSM.addListSelectionListener(new ListSelectionListener() 
    	{
			public void valueChanged(ListSelectionEvent arg0)
			{
				//Ignore extra messages.
    	        if (arg0.getValueIsAdjusting()) return;

    	        ListSelectionModel lsm = (ListSelectionModel)arg0.getSource();
    	        if (!lsm.isSelectionEmpty())
    	        {
    	            int selectedRow = lsm.getMinSelectionIndex();
					controller.setCurrentRow(selectedRow);
    	        }
    	    }
    	});
	}

	public JFrame getFrame()
	{
		return frame; 
	}

	public void show()
	{
		try
		{
	        frame = new JFrame();
			frame.setTitle(ResourceAutomation.resources.getString("Title"));
			frame.setBackground(Color.lightGray);
			frame.getContentPane().setLayout(new BorderLayout());
			frame.getContentPane().add("Center", this);
		    frame.setJMenuBar(resources.getMenubar());
			frame.addWindowListener(new WindowEventHandler(controller));
			frame.setIconImage(new ImageIcon(resources.getResource("IconImage")).getImage());
			frame.pack();
			frame.setSize(500, 300);

		    frame.setVisible(true);
	    }
		catch (Throwable t) 
		{
			ErrorHandler.showError(frame, new Exception(t)); 
            System.out.println(ResourceAutomation.getResourceString("UncaughtException") + t);
            t.printStackTrace();
        	WindowsTrayIcon.cleanUp();
	    }
	}

	public TaskTable getTable()
	{
		return dataTable; 
	}

	public JTable getTaskList()
    {
    	return taskList;     	
    }


	public void update(Observable arg0, Object arg1)
	{
    	taskList.repaint(); 
    	int currentRow = controller.getCurrentRow();
    	if (currentRow == TimeTool.NO_ROW_SELECTED) {
    		taskList.clearSelection(); 
    	} else {

            String title = ResourceAutomation.getResourceString("Title");
            final Task currentTask = controller.get(currentRow);
            final String hoursLabel = ResourceAutomation.getResourceString("GridHourHeader");
            trayIcon.setToolTipText(
                    title + "\n" +
                    controller.getTotalHours() + " " + hoursLabel + "\n" +
                    currentTask.getId() + " " + currentTask.getHours() + " " + hoursLabel);

            taskList.changeSelection(currentRow,
                    1,
                    false,
                    false); 
    	}
	}
	
	private void createTrayIcon() 
	{
		try
		{
			WindowsTrayIcon.initTrayIcon(ResourceAutomation.getResourceString("Title"));
			String imageString = ResourceAutomation.getResourceString("TrayIconImage"); 
			Image image = Toolkit.getDefaultToolkit().getImage(imageString);
			trayIcon = new WindowsTrayIcon(image, 16, 16);
			trayIcon.setToolTipText(ResourceAutomation.getResourceString("Title"));
			trayIcon.addActionListener(new RestoreListener());
		}
		catch (Exception e2)
		{
			//do nothing
		}
	}

	// Callback listener for hide button
	private class RestoreListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent evt) {
            frame.setState(Frame.NORMAL);
            frame.setVisible(true);
            frame.toFront();
            frame.requestFocus();
			trayIcon.setVisible(false);
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
				frame.setVisible(false);
				trayIcon.setVisible(true);
            }
        }

    }


}
