/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.mdi;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class TextFrame  extends MDIFrame{
    JTextPane textPane;
//    MDICommand commands ;
    
    class TextFrameCommand extends MDICommand{

        @Override
        public String[] getCommandList() {
            return new String[]{"copy","past","delete"};
        }

        @Override
        public void doCommand(String command) {
            System.out.println(command);
        }

        @Override
        public void updateAction(Action action) {
            action.setEnabled(true);
        }
    }

    public TextFrame(FrameInfo frameInfo) {
        super(frameInfo);
        
        
    }

    @Override
    public void initComponents() {
        commands = new TextFrameCommand();
        textPane = new JTextPane();
        add(new JScrollPane(textPane));
        textPane.setText("Hello Text");
        commands.setComponent(textPane);
    }
    
    
}
