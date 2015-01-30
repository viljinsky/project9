/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dialogs;

/**
 *
 * @author вадик
 */
import java.awt.BorderLayout;
import java.awt.Container;
import java.util.HashMap;
import javax.swing.JScrollPane;
import ru.viljinsky.db.*;
import ru.viljinsky.grid2.*;

public class DataSelectDialog extends BaseDialog {
    Grid2 grid;
    Dataset dataset;
    DataModule dataModule = DataModule.getInstance();
    HashMap<String,Object> keyValues;

    @Override
    public void initComponents(Container content) {
        super.initComponents(content);
        grid = new Grid2();
        content.add(new JScrollPane(grid),BorderLayout.CENTER);        
    }
    
    public void setTableName(String tableName) throws Exception{
        Dataset dataset = dataModule.createTable(tableName);
        setDataset(dataset);
        grid.open();
            
    }
    
    public void setDataset(Dataset dataset){
        grid.setDataset(dataset);
        this.dataset=dataset;
    }
    
    public void setValues(HashMap<String,Object> values){
        this.keyValues=values;
        DataRowset rowset;
        for (int i=0;i<dataset.getRowCount();i++){
            rowset = dataset.getDataRowset(i);
            for (String key:values.keySet()){
                if (rowset.getValue(key).equals(values.get(key))){
                    grid.getSelectionModel().setSelectionInterval(i, i);
                    grid.scrollRectToVisible(grid.getCellRect(i,i , true));
                    grid.requestFocus();
                    break;
                }
            }
        }
    }
    
    public HashMap<String,Object> getValues(){
        int row = grid.getSelectedRow();
        if (row<0)
            return null;
        dataset.rowIndex=row;
        
        DataRowset rowset = grid.getSelectedRowset();
        if (rowset!=null){
            for (String key:keyValues.keySet()){
                Object value = rowset.getValue(key);
                keyValues.put(key, value);
                return keyValues;
            }
        }
        return null;
    }
    
    
}
