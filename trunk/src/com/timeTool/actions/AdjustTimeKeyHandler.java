package com.timeTool.actions;

import com.timeTool.TimeTool;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class AdjustTimeKeyHandler {
    private final TimeTool timeTool;

    public AdjustTimeKeyHandler(TimeTool timeTool) {
        this.timeTool = timeTool;
    }

    public void perform(char key) {
        timeTool.adjustTime(String.valueOf(key));
    }

}
