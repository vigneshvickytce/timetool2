package com.timeTool;

import java.util.Observable;
import java.util.Observer;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;

import com.jeans.trayicon.WindowsTrayIcon;
import com.timeTool.actions.*;

public class TimeToolWindow extends JPanel implements Observer
{
	private JTable taskList;
    private TaskTable dataTable; 
	private static JFrame frame;   
    
    protected Action[] defaultActions = {
    			 new StopAction(),
    			 new AboutAction(),
    			 new AdjustAction(),
    			 new ResetAction(),
	       	 	 new ReloadAction(),
    	       	 new SaveAction(), 
    	       	 new ExportAction(), 
    	       	 new AddAction(),
    	    	 new DeleteAction(),
    	    	 new RenameAction(),
    	         new QuitAction(),
    	         new HelpAction(), 
    	         new OptionsAction() , 
    	         new LicenseAction(), 
    	         new HomePageAction(), 
    	         new SupportAction()
    	    };
	private static WindowsTrayIcon trayIcon;

    TimeToolWindow(KeyboardFocusManager keyboardManager) {
    	super(true);
    	TimeTool.resources = new ResourceAutomation(this); 
    	TimeTool.resources.setLookAndFeel();
    	TimeTool.resources.createCommandTable();

    	dataTable = new TaskTable(); 
    	createTaskList();
    	JScrollPane scroller = enableScrolling();
    	initPanel(scroller);
    	TimeTool.resources.createMenubar(); 
    	TimeTool.getInstance().addObserver(this); 

    	createTrayIcon();
        createKeyHandler();
    }

    private void createKeyHandler() {
        final AdjustTimeKeyHandler adjustTimeKeyHandler = new AdjustTimeKeyHandler(TimeTool.getInstance());
        char[] adjustKeys = new char[]{'+', '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

        for (final char key : adjustKeys) {
            registerKeyboardAction(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    adjustTimeKeyHandler.perform(key);
                }
            }, KeyStroke.getKeyStroke(key), JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
    }

    private void initPanel(JScrollPane scroller)
	{
    	JPanel panel = new JPanel();
    	panel.setLayout(new BorderLayout());	
    	panel.add("North",TimeTool.resources.createToolbar());
    	panel.add("Center", scroller);
    	add("Center", panel);
    	StatusBar statusBar = new StatusBar(); 
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
		TimePersistence data = new TimePersistence(); 
		data.loadFile(TimeTool.getInstance()); 
		taskList = new JTable(dataTable); 
		taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		taskList.setColumnSelectionAllowed(false); 
    	taskList.setShowVerticalLines(false); 
    	taskList.setShowHorizontalLines(false);
    	trapTableClick();
    	trapColumnClick(); 
	}
	
	private void trapColumnClick()
	{
		JTableHeader header = taskList.getTableHeader();
	    header.addMouseListener(new ColumnHeaderListener());	    
	}
	
	private void trapTableClick()
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
    	            TimeTool.getInstance().setCurrentRow(selectedRow); 
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
		    frame.setJMenuBar(TimeTool.resources.getMenubar());
			frame.addWindowListener(new WindowEventHandler());
			frame.setIconImage(new ImageIcon(TimeTool.resources.getResource("IconImage")).getImage());
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

    public Action[] getActions()
    {
    	return defaultActions; 
    }
    public JTable getTaskList()
    {
    	return taskList;     	
    }


	public void update(Observable arg0, Object arg1)
	{
    	taskList.repaint(); 
    	int currentRow = TimeTool.getInstance().getCurrentRow(); 
    	if (currentRow == TimeTool.NO_ROW_SELECTED)
    	{
    		taskList.clearSelection(); 
    	}
    	else
    	{
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
        public void windowClosing(WindowEvent e) 
        {
        	WindowsTrayIcon.cleanUp();
        	TimeTool.getInstance().close();
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
