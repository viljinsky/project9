/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.main;

import java.lang.reflect.Modifier;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;
import javax.swing.table.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 *
 * @author vadik
 */
class Grid extends JTable {
    MDICommand commands;
    Integer test = 0;
    Model model;
    
    class Model extends DefaultTableModel{
        public Model(int row,int col){
            super(row,col);
        }
    }

    public Grid(int row, int col) {
        model = new Model(row,col);
        setModel(model);
        
        commands = new MDICommand() {
            @Override
            public String[] getCommandList() {
                return new String[]{
                    "add;Добавить;INSERT",
                    "edit;Изменить",
                    "delete;Удалить;DELETE",
                    "post;Применить",
                    "cancel;Отмена",
                    "refresh;Обновить;F5"
                };
            }

            @Override
            public void doCommand(String command) {
                int row = getSelectedRow();
                switch (command) {
                    case "add":
                        model.addRow(new Object[model.getColumnCount()]);
                        row = model.getRowCount()-1;
                        model.fireTableRowsInserted(row,row);
                        getSelectionModel().setSelectionInterval(row,row);
                        scrollRectToVisible(getCellRect(row, getSelectedColumn(),true));
                        break;
                    case "delete":
                        if (row>=0){
                            model.removeRow(row);
                            model.fireTableRowsDeleted(row, row);
                            if (row>=model.getRowCount()){
                                row = model.getRowCount()-1;
                            }
                            if (row>=0){
                                getSelectionModel().setSelectionInterval(row,row);
                                scrollRectToVisible(getCellRect(row,getSelectedColumn(), true));
                            }
                        }
                        break;
                    case "refresh":
                        refresh();
                        break;
                }
            }

            @Override
            public void updateAction(Action action) {
                String command = (String) action.getValue(AbstractAction.ACTION_COMMAND_KEY);
                boolean hasSelected = getSelectedRow() >= 0;
                switch (command) {
                    case "delete":
                        action.setEnabled(hasSelected);
                        break;
                    case "add":
                        ;
                        break;
                    case "edit":
                        action.setEnabled(hasSelected);
                        break;
                }
            }
        };
        commands.setComponent(this);
        commands.updateActionList();
        getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    commands.updateActionList();
                }
            }
        });
        getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                System.out.println("tableChanged:\n" + e.getType());
            }
        });
    }

    public Action[] getActionList() {
        return commands.getActionList();
    }


    public void refresh() {
        System.out.println("Refresh run");
        
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                for (Integer i=0;i<Integer.MAX_VALUE;i++){
                }
            }
        });
        t.start();
        System.out.println("Refresh end");
        
//        t.interrupt();
        System.out.println("Yes!");
    }
    
}
