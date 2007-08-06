package com.timeTool;

import junit.framework.TestCase;
import com.timeTool.ResourceAutomation;

import javax.swing.*;


public class ResourceAutomationTest extends TestCase {

    public void testCreateMenu() {
        ResourceAutomation resources = new ResourceAutomation(null);
        JMenu fileMenu = resources.createMenu("file");
        JMenu helpMenu = resources.createMenu("help");
        JMenu timeToolMenu = resources.createMenu("timetool");

        assertNotNull(fileMenu);
        assertNotNull(helpMenu);
        assertNotNull(timeToolMenu);
        
        assertEquals('F', fileMenu.getMnemonic());
        assertEquals('H', helpMenu.getMnemonic());
        assertEquals('T', timeToolMenu.getMnemonic());

    }
}
