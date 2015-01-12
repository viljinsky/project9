/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.db;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

interface IDataset{
    public void beforeOpen();
    public void afterOpen();
    
    public void afterClose();
    public void beforeClose();
    
    public void beforeInsert();
    public void afterInsert();
    
    public void beforeDelete();
    public void afterDelete();
    
    public void beforePost();
    public void beforeCancel();
    
    public void afterPost();
    public void afterCancel();
    
}

enum EDatasetEvent{
    OPEN,
    CLOSE,
    INSERT,
    DELETE,
    EDIT,
    POST,
    CANCEL;
}
class DatasetEvent{
    Dataset dataset;
    public DatasetEvent(EDatasetEvent event, Dataset dataset){
        this.dataset=dataset;
    } 
}

//class Datasource{
//    Dataset dataset;
//    public Datasource(Dataset dataset){
//        this.dataset=dataset;
//    }
//}
class Table extends Dataset{
    public Table(DataModule datamodule){
        super(datamodule);
    }
    
}

class DefaultDatasetListener implements IDataset{

    @Override
    public void beforeOpen() {
    }

    @Override
    public void afterOpen() {
    }

    @Override
    public void afterClose() {
    }

    @Override
    public void beforeClose() {
    }

    @Override
    public void beforeInsert() {
    }

    @Override
    public void afterInsert() {
    }

    @Override
    public void beforeDelete() {
    }

    @Override
    public void afterDelete() {
    }

    @Override
    public void beforePost() {
    }

    @Override
    public void beforeCancel() {
    }

    @Override
    public void afterPost() {
    }

    @Override
    public void afterCancel() {
    }
}

public class Dataset extends ArrayList<Object[]>{
    public static final int BROWSE_MODE = 0;
    public static final int INSERT_MODE = 1;
    public static final int EDIT_MODE   = 2;
    public static final int DELETE_MODE = 3;
    
    DataModule dataModule;
    String tableName = null;
    String selectSQL;
    DataField[] fields;
    Boolean editable = false ;
    protected Integer editMode = BROWSE_MODE;

    public Integer getEditMode() {
        return editMode;
    }
    Integer rowIndex;
    List<IDataset> listeners = new ArrayList<>();
    Object[] pendingRowset=null;
    HashMap<Integer,Object> filter = null;
    
    
    
    public Object getValueAt(int rowIndex,int columnIndex){
        Object[] rowset = get(rowIndex);
        return rowset[columnIndex];
    }
    
    public void setValueAt(Object aValue,int rowIndex,int columnIndex){
        Object[] rowset = get(rowIndex);
        rowset[columnIndex]=aValue;
    }
    public void addDatasetListener(IDataset listener){
        listeners.add(listener);
    }
    
    public Boolean isEditable(){
        return editable;
    }
    
    public void setEditable(Boolean value){
        editable=value;
    }
    
    public Dataset(DataModule dataModule){
        super();
        this.dataModule=dataModule;
    }
    
    public String toString(){
        return "dataset '"+tableName+"'  SQL: '"+selectSQL+"'";
    }
    public DataField getFieldByName(String fieldName){
        for (DataField field:fields){
            if (field.columnName.equals(fieldName)){
                return field;
            }
        }
        return null;
    }
    
    public Integer getColumnCount(){
        return fields.length;
    }
    
    public DataField getColumn(int ColumnIndex){
        return fields[ColumnIndex];
    }
    
    public String getColumnName(Integer columnIndex){
        return fields[columnIndex].columnName;
    }
    
    public Integer getRowCount(){
        return size();
    }
    
    public Integer getColumnIndex(String columnName){
        for (DataField field:fields){
            if (field.columnName.equals(columnName)){
                return field.columnIndex;
            }
        }
        return -1;
    }
    
    public Integer getColumnIndex(String tableName,String columName){
        for (DataField field:fields){
            if ((field.columnName.equals(columName))&&(field.tableName.equals(tableName))){
                return field.columnIndex;
            }
        }
        return -1;
    }
    
    public String getTableName(){
        return tableName;
    };
    
    public DataField[] getFields(){
        return fields;
    }
    
    public void setTableName(String tableName) throws Exception{
        this.tableName =tableName;
        setSQL("select * from "+tableName);
    }
    
    public void setSQL(String sql,String tableName) throws Exception{
        this.tableName=tableName;
        setSQL(sql);
    }
    
    public void setSQL(String sql) throws Exception{
        selectSQL = sql;        
        fields = dataModule.getDataFields(sql);
        if (tableName!=null){
            dataModule.updateFieldUsage(this);
        }
    }
    
    public boolean close(){
        clear();
        return true;
    }
    
    public void update(int rowIndex,Object[] rowset){
    }
    
    /**
     * Установка фильтра в форма имя_поля = знфчение
     * @param strFilter 
     */
    public void setFilter(HashMap<String,Object> strFilter) throws Exception{
        if (strFilter==null){
            filter=null;
            return;
        }
        
        filter = new HashMap<>();
        for (String fieldName:strFilter.keySet()){
            filter.put(getColumnIndex(fieldName), strFilter.get(fieldName));
        }
    }
    
    protected boolean onFilterRecord(Object[] data){
        if (filter!=null){
            for (int i : filter.keySet()){
                if (filter.get(i)==null) return false;
                if (!filter.get(i).equals(data[i-1]))
                    return false;
            }
        }
        return true;
    }
    public boolean open() throws Exception{
        ResultSet rs = null;
        Statement stmt = null;
        Object[] data;
        try{
            stmt= dataModule.getConnection().createStatement();
            rs = stmt.executeQuery(selectSQL);
            while (rs.next()){
                data = new Object[fields.length];
                for (int i=0;i<fields.length;i++){
                    data[i]=rs.getObject(i+1);
                }
                if (onFilterRecord(data)){
                    add(data);
                }
            }
            return true;
        } finally {
            if (rs!=null) rs.close();
            if (stmt!=null) stmt.close();
        }
    }
    
    public void reopen() throws Exception{
        clear();
        open();
    }

    public Object[] getEmptyRowset(){
        return new Object[fields.length];
    }
    
    public Object[] getRowset(Integer rowIndex){
        return get(rowIndex);
    }
    
    public DataRowset getDataRowset(Integer rowIndex){
        DataRowset result = new DataRowset();
        result.fields=fields;
        result.data=get(rowIndex);
        return result;
    }
    
    public Integer getRowIndex(){
        return rowIndex;
    }
    
    /**
     * Добавляет пустую запись в конец
     */
    public void append() throws Exception{
        if (editMode == BROWSE_MODE){
            editMode = INSERT_MODE;
            Object[] rowset = getEmptyRowset();
            add(rowset);
            rowIndex = indexOf(rowset);
        } else
            throw new Exception("Dataset not in BROWSE MODE");
    }
    
    public void append(Object[] rowset) throws Exception{
        if (editMode==BROWSE_MODE){
            editMode = INSERT_MODE;
            pendingRowset = rowset;
            add(rowset);
            rowIndex = indexOf(rowset);
            editable = true;
            
        } else
            throw new Exception("Dataset not in BROWSE MODE");
    }
    
    public void insert(Integer rowIndex,Object[] rowset) throws Exception{
        if (editMode==BROWSE_MODE){
            this.rowIndex=rowIndex;
            pendingRowset = rowset;
            editMode = INSERT_MODE;
            add(rowIndex,rowset);
            editable = true;
        } else {
            throw new Exception("Dataset not in BROWSE MODE");
        }
    }
    
    public void edit(Integer rowIndex) throws Exception{
        if (editMode==BROWSE_MODE){
            this.rowIndex=rowIndex;
            editMode = EDIT_MODE;
            pendingRowset = get(rowIndex);
            editable = true;
        } else {
            throw new Exception("Dataset not in BROWSE MODE");
        }
    }

    public boolean delete(int rowIndex) throws Exception{
        if (editMode==BROWSE_MODE){
            this.rowIndex=rowIndex;
            pendingRowset = get(rowIndex);
            remove(rowIndex);
            editMode = DELETE_MODE;
            return true;
        } else {
            throw new Exception("Dataset not in BROWSE MODE");           
        }
    }

    
    
    public void post() throws Exception {
        DataOperator operator = new DataOperator(this);
        System.out.println(" POST OPERATION ");
        DatasetValues values;
        String sql;
        switch (editMode){
            case EDIT_MODE:
                values =operator.getUpdateParamValues(this, pendingRowset, get(rowIndex));
                sql = operator.getUpdateSQL();
                System.out.println("EDIT \n"+sql);
                System.out.println(values);
                dataModule.execute(sql, values)
                ;break;
            case INSERT_MODE:
                values = operator.getInsertParamValues(this, get(rowIndex));
                sql= operator.getInsertSQL();
                System.out.println("APPEND/INSERT\n"+sql);
                System.out.println(values);
                dataModule.execute(sql, values)
                ;break;
            case DELETE_MODE:
                values = operator.getDeleteParamValues(this, pendingRowset);
                sql= operator.getDeleteSQL();
                System.out.println("DELETE\n"+sql);
                System.out.println(values);
                dataModule.execute(sql, values)
                ;break;
        }
        
        
        pendingRowset = null;
        editMode = BROWSE_MODE;
        editable = false;
    }
    
    public void cancel(){
        if (pendingRowset!=null){
            switch (editMode){
                case INSERT_MODE:
                    remove(pendingRowset);
                    break;
                case EDIT_MODE:
                    set(rowIndex, pendingRowset);
                    break;
                case DELETE_MODE:
                    add(rowIndex, pendingRowset);
                    break;
            }
            
            pendingRowset = null;
            editMode = BROWSE_MODE;
            editable = false;
        }
        
    }
}
