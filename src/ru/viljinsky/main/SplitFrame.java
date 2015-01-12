/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.main;

/**
 *
 * @author vadik
 */
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Пример фрайма использующего две области данных
 * @author vadik
 */
public class SplitFrame extends MDIFrame{
    Grid table1,table2;

    public SplitFrame(FrameInfo frameInfo) {
        super(frameInfo);
    }

    @Override
    public void initComponents() {
        table1 = new Grid(10,5);
        table2 = new Grid(8,3);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(new JScrollPane(table1));
        splitPane.setBottomComponent(new JScrollPane(table2));
        splitPane.setResizeWeight(.5);
        
        add(splitPane);
        for (Action actio:table1.getActionList()){
            toolBar.add(actio);
        }
        showToolBar(true);
    }

    @Override
    protected void updateFrameMenu(JMenu menu) {
        for (Action action:table1.getActionList()){
            menu.add(action);
        }
        menu.addSeparator();
        for (Action action:table2.getActionList()){
            menu.add(action);
        }
        
        menu.setEnabled(true);
    }
    
    
    
    
}
