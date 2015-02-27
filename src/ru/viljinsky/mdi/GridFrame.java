/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.mdi;

/**
 *
 * @author vadik
 */

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import ru.viljinsky.db.*;
import ru.viljinsky.grid2.*;

/**
 * Базовый класс для создания фреймов приложения
 * @author vadik
 */
public class GridFrame extends MDIFrame {
    DataModule dataModule =DataModule.getInstance();
    Table table;
    
    class Table extends Grid2{

        @Override
        public void selectedRowChange() {
            commands.updateActionList();
        }
        
    }

    public GridFrame(FrameInfo frameInfo) {
        super(frameInfo);
    }

    
    @Override
    public void initComponents() {
        table = new Table();//(3,5);
        table.setParent(this);
        commands = table.getCommands();
        add(new JScrollPane(table));
        
        for (Action action:table.getActionList()){
            toolBar.add(action);
        }
        showToolBar(true);
        
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2){
//                    JOptionPane.showMessageDialog(rootPane, "WoubleClicl");
                    commands.doCommand("EDIT");
                }
            }
            
        });
    }

    @Override
    public void opened() {
        String tableName;
        tableName = (String)frameInfo.getValue("TABLE_NAME");
        if (tableName!=null){
            try{
                Dataset dataset = dataModule.createTable(tableName);
                table.setDataset(dataset);
                table.open();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        commands.updateActionList();
    }
    
    
    
}
