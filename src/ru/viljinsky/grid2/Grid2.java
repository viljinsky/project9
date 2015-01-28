/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.grid2;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import ru.viljinsky.db.*;
import ru.viljinsky.main.MDICommand;
import ru.viljinsky.dialogs.*;

/**
 *
 * @author vadik
 */
public class Grid2 extends JTable {
    GridModel2 model;
    MDICommand commands;
    JComponent parent;

    public void setParent(JComponent parent){
        this.parent=parent;
    }
    public Action[] getActionList(){
        return commands.getActionList();
    }
    public boolean hasSelectedRecord() {
        return getSelectedRow() >= 0;
    }

    public MDICommand getCommands(){
        return commands;
    }
    
    public DataRowset getSelectedRowset(){
        int rowIndex = getSelectedRow();
        if (rowIndex<0)
            return null;
        DataRowset rowset = new DataRowset();
        rowset.fields= model.dataset.getFields();
        rowset.data= model.dataset.get(rowIndex);
        return rowset;
    }
    
    //-------------------------  Inserting Data -------------------------------
    class InsertDialog extends DataEditDialog{

        @Override
        public void doEnterClick() throws Exception{
            try{
                DataOperator operator = new DataOperator(model.dataset);
                String sql = operator.getInsertSQL();
                DatasetValues v = operator.getInsertParamValues(model.dataset,getValues() );
                DataModule.getInstance().execute(sql, v);
            } catch (Exception e){
                e.printStackTrace();
                throw new Exception(e.getMessage());
            }
            
        }
    }
    
    public Integer showInsertDialog(){
        InsertDialog dialog = new InsertDialog();
        dialog.setParent(parent);
        dialog.setDataSet(model.dataset);
        dialog.setVisible(true);
        return dialog.getResult();
    }
    
    //------------------- Editing Data ---------------------------------------
    
    class EditDialog extends DataEditDialog{

        @Override
        protected void doEnterClick() throws Exception {
            Object[] oldValues = model.getRowset(getSelectedRow());
            try{
                DataOperator operator = new DataOperator(model.dataset);
                String sql = operator.getUpdateSQL();
                DatasetValues v = operator.getUpdateParamValues(model.dataset, oldValues, getValues());
                DataModule.getInstance().execute(sql, v);
                System.out.println(sql+"\n"+v);
            } catch (Exception e){
                e.printStackTrace();
                throw new Exception(e.getMessage());
            }
        }
        
    }
    
    public Integer showEditDialog(){
        DataRowset rowset = getSelectedRowset();
        System.out.println(rowset);
        DataEditDialog dialog = new EditDialog();
        dialog.setParent(parent);
        dialog.setDataSet(model.dataset);
        dialog.setValues(getSelectedRowset());
        dialog.setVisible(true);
        
        return dialog.getResult();
    }

    public Grid2() {
        super(1,1);
        setAutoResizeMode(AUTO_RESIZE_OFF);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    selectedRowChange();
                }
            }
        });
        commands = new MDICommand() {

            @Override
            public String[] getCommandList() {
                return new String[]{"APPEND", "INSERT", "EDIT", "DELETE", "POST", "CANCEL","REQUERY"};
            }

            @Override
            public void doCommand(String command) {
                DataRowset rowset;
                switch (command) {
                    case "APPEND":
                        if (showInsertDialog()==BaseDialog.RESULT_OK){
                            requeryRec();
//                            appendRec();
//                            postRec();
                        }
                        break;
                    case "INSERT":
                        insertRec();
                        break;
                    case "EDIT":
                        if (showEditDialog()==BaseDialog.RESULT_OK){
                            requeryRec();
//                            editRec();
                        }
                        break;
                    case "DELETE":
                        deleteRec();
                        break;
                    case "POST":
                        postRec();
                        break;
                    case "CANCEL":
                        cancelRec();
                        break;
                    case "REQUERY":
                        requeryRec();
                        break;
                }            
            }

            @Override
            public void updateAction(Action action) {
            String command = (String)action.getValue(AbstractAction.ACTION_COMMAND_KEY);
            if (model==null){
                action.setEnabled(false);
                return;
            }
            switch (command) {
                case "APPEND":
                    action.setEnabled(model.isBowseMode());
                    break;
                case "INSERT":
                    action.setEnabled(hasSelectedRecord() && model.isBowseMode());
                    break;
                case "EDIT":
                    action.setEnabled(hasSelectedRecord() && model.isBowseMode());
                    break;
                case "DELETE":
                    action.setEnabled(hasSelectedRecord() && model.isBowseMode());
                    break;
                case "POST":
                    action.setEnabled(!model.isBowseMode());
                    break;
                case "CANCEL":
                    action.setEnabled(!model.isBowseMode());
                    break;
                }
            }
        };
        commands.setComponent(this);
//        commands = new GridCommands2();
//        addMouseListener(commands);
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case KeyEvent.VK_INSERT: insertRec();break;
                    case KeyEvent.VK_DELETE: deleteRec();break;
                    case KeyEvent.VK_CANCEL: cancelRec();break;
                        
                }
            }
            
        });
       
    }

    public void setDataset(Dataset dataset) {
        model = new GridModel2(dataset);
        setModel(model);
    }

    public Dataset getDataset(){
        return model.dataset;
    }
    
    //1
    public void open() {
        try {
            model.open();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public void close() {
        model.dataset.close();        
    }

    //2
    public void appendRec() {
        Object[] rowset;
        int rowIndex;
        try {
            rowset = getEmptyRowset();
            model.append(rowset);
            rowIndex = model.getRecordCount();
            getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // 3
    /**
     * Получение пустого роусет для вставки
     * @return 
     */
    protected Object[] getEmptyRowset(){
        return model.getEmptyRowset();
    }
    /**
     * Добавляет строку в конец таблицы
     */
    public void insertRec() {
        int rowIndex = getSelectedRow();
        Object[] rowset ;
        try {
            rowset = getEmptyRowset();
            model.insert(rowIndex, rowset);
            getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
            model.dataset.setEditable(true);
            int colIndex = getSelectedColumn();
            if (colIndex<0){
                colIndex=0;
            }
            editCellAt(rowIndex,colIndex);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // 4
    /**
     * Удаляет выделенную строку из таблицы
     */
    public void deleteRec() {
        Integer rowIndex;
        try {
            rowIndex = getSelectedRow();
            if (rowIndex>=0){
                model.delete(rowIndex);
                if (rowIndex >= model.getRecordCount()) {
                    rowIndex = model.getRecordCount() ;
                }
                if (rowIndex >= 0) {
                    getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    /**
     *  Заменяет изменённую выделенную строку 
     */
    public void editRec() {
        int rowIndex;
        Object[] rowset;
        try {
            rowIndex = getSelectedRow();
            rowset = model.getRowset(rowIndex);
            model.edit(rowIndex, rowset);
            if (rowIndex>=0){
                getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    public void editRec(Object[] values){
        Object[] oldValues = model.getRowset(getSelectedRow());
        try{
            DataOperator operator = new DataOperator(model.dataset);
            String sql = operator.getUpdateSQL();
            DatasetValues v = operator.getUpdateParamValues(model.dataset, oldValues, values);
            DataModule.getInstance().execute(sql, v);
            System.out.println(sql+"\n"+v);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    // 5
    public void postRec() {
        try{
            if (isEditing()){
                getCellEditor().stopCellEditing();
            }
            model.dataset.post();
            model.dataset.setEditable(false);
        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage());
        } finally{
        }
    }

    // 6
    public void cancelRec() {
        try{
            if (isEditing()){
                getCellEditor().cancelCellEditing();
            }
            int rowIndex = getSelectedRow();
            model.dataset.cancel();
            model.dataset.setEditable(false);
            model.fireTableDataChanged();
            if (rowIndex>=0){
                getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
            }
            
        } catch (Exception e){
        } finally{
        }
    }

    // 7
    public void requeryRec(){
        try{
            model.dataset.close();
            model.dataset.open();
            model.fireTableDataChanged();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //-------------------------------------------------------------------------
    public void selectedRowChange() {
        System.out.println("-->" + getSelectedRow());
    }
    
}
