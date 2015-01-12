/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.db;

import java.sql.*;

/**
 *
 * @author vadik
 */
public class DataField {
    Integer columnIndex;
    String tableName;
    String columnName;
    String columnLabel;
    String columnTypeName;
    String columnClassName;
    Integer precision;
    Integer scale;
    Boolean autoIncrement;
    Boolean readOnly;
    
    Boolean primaryKey=false;
    String referencedTable = "";
    String referencedColumn = "";

    public String getReferencedTable() {
        return referencedTable;
    }

    public Boolean isPrimary(){
        return primaryKey;
    }
    
    public Boolean isReadOnly(){
        return readOnly;
    }
    public String getReferencedColumn() {
        return referencedColumn;
    }
    
    public Integer getColumnIndex(){
        return columnIndex;
    }


    public boolean isReferenced(){
        return !referencedTable.isEmpty();
    }
    
    
    public Class<?> getColumnClass() throws Exception{
        return Class.forName(columnClassName);
    }
    
    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumnLabel() {
        return columnLabel;
    }
    public DataField(DataModule dm, ResultSetMetaData rsmeta, Integer columnIndex) throws SQLException {
        this.columnIndex = columnIndex;
        tableName = rsmeta.getTableName(columnIndex);
        columnName = rsmeta.getColumnName(columnIndex);
        columnLabel = rsmeta.getColumnLabel(columnIndex);
        columnTypeName = rsmeta.getColumnTypeName(columnIndex);
        columnClassName = rsmeta.getColumnClassName(columnIndex);
        precision = rsmeta.getPrecision(columnIndex);
        scale = rsmeta.getScale(columnIndex);
        autoIncrement = rsmeta.isAutoIncrement(columnIndex);
        readOnly = rsmeta.isReadOnly(columnIndex);
    }
    
    @Override
    public String toString(){
        return columnName+(primaryKey?" pk ":"")+" ("+columnTypeName+" ai:"+autoIncrement+" ro:"+readOnly+")";
    }
}
