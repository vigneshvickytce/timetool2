package com.timeTool;

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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;

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

    TimeToolWindow() {
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


		registerKeyboardAction(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
					int row = TimeTool.getInstance().getCurrentRow();
					TimeTool.getInstance().setCurrentRow(--row);
                }
            }, KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		registerKeyboardAction(new ActionListener(){
                public void actionPerformed(ActionEvent e) {
					int row = TimeTool.getInstance().getCurrentRow();
					TimeTool.getInstance().setCurrentRow(++row);
				}
            }, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

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
