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
/**
 * Пример фрейма исаользующего мастер талицу и подчинённые таблицы на вкладках
 * @author vadik
 */
public class TabbedFrame extends MDIFrame{
    Grid table;
    JTabbedPane tabbedPane;

    public TabbedFrame(FrameInfo frameInfo) {
        super(frameInfo);
    }

    @Override
    public void initComponents() {
        
        table = new Grid(2,8);
        
        tabbedPane = new JTabbedPane();
        for (int i=0;i<3;i++){
            tabbedPane.addTab("tab"+i, new JPanel());
        }
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(new JScrollPane(table));
        splitPane.setBottomComponent(tabbedPane);
        splitPane.setResizeWeight(0.5);
        add(splitPane);
    }

    @Override
    protected void updateFrameMenu(JMenu menu) {
        for (Action action:table.getActionList()){
            menu.add(action);
        }
        menu.setEnabled(true);
    }
    
    
    
    
    
}
