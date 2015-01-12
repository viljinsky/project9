/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.grid2;

import javax.swing.table.AbstractTableModel;
import ru.viljinsky.db.*;

/**
 *
 * @author vadik
 */
public class GridModel2 extends AbstractTableModel {
    Dataset dataset;
   
    public boolean isBowseMode(){
        return (dataset.getEditMode() == Dataset.BROWSE_MODE);
    }

    public GridModel2(Dataset dataset) {
        this.dataset = dataset;
    }
    
    public Object[] getEmptyRowset(){
        return dataset.getEmptyRowset();
    }
    
    public Object[] getRowset(int rowIndex){
        return dataset.get(rowIndex);
    }
    
    public int getRecordCount(){
        return dataset.size()-1;
    }

    @Override
    public int getRowCount() {
        return dataset.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return dataset.getColumnCount();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dataset.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        dataset.setValueAt(aValue,rowIndex,columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        
        if (!dataset.isEditable() || dataset.getColumn(columnIndex).isReadOnly())
             return false;
        
        DataField field = dataset.getColumn(columnIndex);
        return (field.getTableName().equals(dataset.getTableName()));
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        try {
            return dataset.getColumn(columnIndex).getColumnClass();
        } catch (Exception e) {
            return Object.class;
        }
    }

    @Override
    public String getColumnName(int column) {
        return dataset.getColumn(column).getColumnLabel();//getColumnName();
    }
    
    

    //--------------------------------------------
    // 1
    public void open() throws Exception {
        dataset.open();
        fireTableDataChanged();
    }

    // 2
    public void close() {
        dataset.close();
        fireTableDataChanged();
    }

    // 3
    public void append(Object[] rowset) throws Exception{
        dataset.append(rowset);
        fireTableDataChanged();
    }

    // 4
    public void insert(int rowIndex, Object[] rowset) throws Exception{
        dataset.insert(rowIndex, rowset);
        fireTableDataChanged();
    }

    // 5
    public void edit(int rowIndex, Object[] rowset) throws Exception {
        dataset.edit(rowIndex);
        fireTableDataChanged();
    }

    // 6
    public void delete(int rowIndex) throws Exception{
        dataset.delete(rowIndex);
        fireTableDataChanged();
    }
    
}
