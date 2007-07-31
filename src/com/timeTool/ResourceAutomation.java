package com.timeTool;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Event;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Hashtable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;




public class ResourceAutomation
{
    public static ResourceBundle resources;
    private JToolBar toolbar;
    private TimeToolWindow owner; 
    private Hashtable menuItems;
    private JMenuBar menubar;
    private static Hashtable commands;
    
    public static final String imageSuffix = "Image";
    public static final String labelSuffix = "Label";
    public static final String actionSuffix = "Action";
    public static final String tipSuffix = "Tooltip";
    public static final String hotKeySuffix = "Hotkey";

    
    public static void initResources()
    {
    	
	    //init the resources
        try 
        {
            resources = ResourceBundle.getBundle("com.timeTool.resources.TimeTool", 
                    Locale.getDefault());

        } catch (MissingResourceException mre) 
        {
            ErrorHandler.showError(null, mre); 
            System.exit(1);
        }    
    }
    
    public ResourceAutomation(TimeToolWindow owner)
    {
    	super(); 
    	this.owner = owner; 
    	menuItems = new Hashtable();
    }
	public void setLookAndFeel()
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
    	owner.setBorder(BorderFactory.createEtchedBorder());
    	owner.setLayout(new BorderLayout());
	}
    /**
     * Create the toolbar.  By default this reads the 
     * resource file for the definition of the toolbar.
     */
    public Component createToolbar() 
    {
		toolbar = new JToolBar();
		String[] toolKeys = tokenize(getResourceString("toolbar"));
		for (int i = 0; i < toolKeys.length; i++) 
		{
		    if (toolKeys[i].equals("-")) 
		    {
		    	toolbar.add(Box.createHorizontalStrut(5));
		    } 
		    else 
		    {
		    	toolbar.add(createTool(toolKeys[i]));
		    }
		}
		toolbar.add(Box.createHorizontalGlue());
		return toolbar;
    }
    /**
     * Hook through which every toolbar item is created.
     */
    public Component createTool(String key) 
    {
    	return createToolbarButton(key);
    }
    /**
     * Create a button to go inside of the toolbar.  By default this
     * will load an image resource.  The image filename is relative to
     * the classpath (including the '.' directory if its a part of the
     * classpath), and may either be in a JAR file or a separate file.
     * 
     * @param key The key in the resource file to serve as the basis
     *  of lookups.
     */
    public JButton createToolbarButton(String key) 
    {
    	URL url = getResource(key + imageSuffix);
        JButton b = new JButton(new ImageIcon(url)) 
        {
			/**
			 * 
			 */
			private static final long serialVersionUID = -9176328438451506073L;
			public float getAlignmentY() { return 0.5f; }
        };
        b.setRequestFocusEnabled(false);
        b.setMargin(new Insets(1,1,1,1));

        String astr = getResourceString(key + actionSuffix);
        
		if (astr == null) 
		{
		    astr = key;
		}
		Action a = getAction(astr);
		if (a != null) 
		{
		    b.setActionCommand(astr);
		    b.addActionListener(a);
		} 
		else 
		{
		    b.setEnabled(false);
		}

		String tip = getResourceString(key + tipSuffix);
		if (tip != null) 
		{
		    b.setToolTipText(tip);
		}
	 
	  	return b;
    }


    /**
     * Take the given string and chop it up into a series
     * of strings on whitespace boundaries.  This is useful
     * for trying to get an array of strings out of the
     * resource file.
     */
    public String[] tokenize(String input) 
    {
		Vector v = new Vector();
		StringTokenizer t = new StringTokenizer(input);
		String cmd[];
	
		while (t.hasMoreTokens())
		{
		    v.addElement(t.nextToken());
		}
		cmd = new String[v.size()];
		for (int i = 0; i < cmd.length; i++)
		{
			cmd[i] = (String) v.elementAt(i);
		}
	
		return cmd;
    }

    public static String getResourceString(String nm) 
    {
    	String str;
    	try 
    	{
    	    str = ResourceAutomation.resources.getString(nm);
    	} 
    	catch (MissingResourceException mre) 
    	{
    	    str = null;
    	}
    	return str;
    }


    public Action getAction(String cmd) 
    {
    	return (Action) commands.get(cmd);
    }

    public URL getResource(String key) 
    {
		String name = getResourceString(key);
		if (name != null) 
		{
		    URL url = owner.getClass().getResource(name);
		    return url;
		}
		return null;
    }
    public Container getToolbar() 
    {
    	return toolbar;
    }

    protected JMenuBar createMenubar()
    {
    	JMenuBar mb = new JMenuBar();

    	String[] menuKeys = tokenize(getResourceString("menubar"));
    	for (int i = 0; i < menuKeys.length; i++) 
    	{
    	    JMenu m = createMenu(menuKeys[i]);
    	    if (m != null) 
    	    {
    	    	mb.add(m);
    	    }
    	}
     	this.menubar = mb;
    	return mb;
     }
    /**
     * Create a menu for the app.  By default this pulls the
     * definition of the menu from the associated resource file.
     */
    protected JMenu createMenu(String key) 
    {
		String[] itemKeys = tokenize(getResourceString(key));
		JMenu menu = new JMenu(getResourceString(key + "Label"));
		for (int i = 0; i < itemKeys.length; i++) 
		{
		    if (itemKeys[i].equals("-")) 
		    {
		    	menu.addSeparator();
		    } 
		    else 
		    {
				JMenuItem mi = createMenuItem(itemKeys[i]);
				menu.add(mi);
		    }
		}
		return menu;
    }
    /**
     * This is the hook through which all menu items are
     * created.  It registers the result with the menuitem
     * hashtable so that it can be fetched with getMenuItem().
     * @see #getMenuItem
     */
    protected JMenuItem createMenuItem(String cmd) 
    {
		JMenuItem mi = new JMenuItem(getResourceString(cmd + labelSuffix));
		
		
		String hotkey = getResourceString(cmd + hotKeySuffix); 
		if (hotkey != null)
		{
			KeyStroke key = null; 
			if (hotkey.length() == 6)
			{
				if (hotkey.substring(0, 5).equals("Ctrl+"))
				{
					if (hotkey.substring(5, 6).equals("O"))
					{
						key = KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK);
					}
					else if (hotkey.substring(5, 6).equals("S"))
					{
						key = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK);
					}
					else if (hotkey.substring(5, 6).equals("Q"))
					{
						key = KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK);
					}
					else if (hotkey.substring(5, 6).equals("E"))
					{
						key = KeyStroke.getKeyStroke(KeyEvent.VK_E, Event.CTRL_MASK);
					}
					else if (hotkey.substring(5, 6).equals("R"))
					{
						key = KeyStroke.getKeyStroke(KeyEvent.VK_R, Event.CTRL_MASK);
					}
					else if (hotkey.substring(5, 6).equals("A"))
					{
						key = KeyStroke.getKeyStroke(KeyEvent.VK_A, Event.CTRL_MASK);
					}
					else if (hotkey.substring(5, 6).equals("D"))
					{
						key = KeyStroke.getKeyStroke(KeyEvent.VK_D, Event.CTRL_MASK);
					}
					else if (hotkey.substring(5, 6).equals("N"))
					{
						key = KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK);
					}
					else if (hotkey.substring(5, 6).equals("H"))
					{
						key = KeyStroke.getKeyStroke(KeyEvent.VK_H, Event.CTRL_MASK);
					}
					else if (hotkey.substring(5, 6).equals("T"))
					{
						key = KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK);
					}
					else if (hotkey.substring(5, 6).equals("P"))
					{
						key = KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK);
					}
					
				}
			}
			else if ((hotkey.length() == 2) &&
					(hotkey.equals("F1")))
			{
				key = KeyStroke.getKeyStroke("F1");
			}
			
			if (key != null)
			{
				mi.setAccelerator(key); 
			}
		}
		String astr = getResourceString(cmd + actionSuffix);
		if (astr == null) 
		{
		    astr = cmd;
		}
		mi.setActionCommand(astr);
		Action a = getAction(astr);
		if (a != null) 
		{
		    mi.addActionListener(a);
		    a.addPropertyChangeListener(createActionChangeListener(mi));
		    mi.setEnabled(a.isEnabled());
		} 
		else 
		{
		    mi.setEnabled(false);
		}
		menuItems.put(cmd, mi);
		return mi;
    }
    // Yarked from JMenu, ideally this would be public.
    protected PropertyChangeListener createActionChangeListener(JMenuItem b) 
    {
    	return new ActionChangedListener(b);
    }
    // Yarked from JMenu, ideally this would be public.
    private class ActionChangedListener implements PropertyChangeListener {
        JMenuItem menuItem;
        
        ActionChangedListener(JMenuItem mi) {
            super();
            this.menuItem = mi;
        }
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if (e.getPropertyName().equals(Action.NAME)) {
                String text = (String) e.getNewValue();
                menuItem.setText(text);
            } else if (propertyName.equals("enabled")) {
                Boolean enabledState = (Boolean) e.getNewValue();
                menuItem.setEnabled(enabledState.booleanValue());
            }
        }
    }



    public JMenuBar getMenubar() {
    	return menubar;
    }


	public void createCommandTable()
	{
		// install the command table
    	commands = new Hashtable();
    	for (int i = 0; i < owner.getActions().length; i++) 
    	{
    	    Action a = owner.getActions()[i];
    	    //commands.put(a.getText(Action.NAME), a);
    	    commands.put(a.getValue(Action.NAME), a);
    	}
	}

}
