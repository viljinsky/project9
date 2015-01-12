/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.db;

/**
 *
 * @author vadik
 */
public class DataRowset {

    /**
     *
     */
    public Object[] data;

    /**
     *
     */
    public DataField[] fields;
    
    /**
     *
     * @param fieldName
     * @return
     */
    public Object getValue(String fieldName){
        for (DataField field:fields){
            if (field.columnName.equals(fieldName)){
                return data[field.columnIndex-1];
            }
        }
        return null;
    }
    
    /**
     *
     * @param fieldName
     * @param aValue
     */
    public void setValue(String fieldName,Object aValue){
        for (DataField field:fields){
            if (field.columnName.equals(fieldName)){
                data[field.columnIndex-1]=aValue;
            }
        }
    }
    
    /**
     *
     * @return
     */
    public String[] getFieldNames(){
        String[] result = new String[fields.length];
        for (DataField field:fields){
            result[field.columnIndex-1]=field.columnName;
        }
        return result;
    }
    
    @Override
    public String toString(){
        String result = "";
        for (DataField field:fields){
            result += field.tableName+"."+field.columnName+" = "+getValue(field.columnName)+"\n";
        }
        return result;
    }
}
