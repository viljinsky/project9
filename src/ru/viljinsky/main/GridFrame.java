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
import java.awt.event.ActionEvent;
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
    Grid2 table;

    public GridFrame(FrameInfo frameInfo) {
        super(frameInfo);
    }

    
    @Override
    public void initComponents() {
        table = new Grid2();//(3,5);
        commands = table.getCommands();
        add(new JScrollPane(table));
        
        for (Action action:table.getActionList()){
            toolBar.add(action);
        }
        showToolBar(true);
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
