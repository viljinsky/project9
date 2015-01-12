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
public class DatasetInfo {
    String tableName;
    String title;

    public String getTableName() {
        return tableName;
    }

    public String getTitle() {
        return title;
    }
    String selectSQL;

    public String getSelectSQL() {
        return selectSQL;
    }

    public void setSelectSQL(String selectSQL) {
        this.selectSQL = selectSQL;
    }

    public String getInsertSQL() {
        return insertSQL;
    }

    public void setInsertSQL(String insertSQL) {
        this.insertSQL = insertSQL;
    }

    public String getUpdateSQL() {
        return updateSQL;
    }

    public void setUpdateSQL(String updateSQL) {
        this.updateSQL = updateSQL;
    }
    String insertSQL;
    String updateSQL;
    
    public DatasetInfo(String tableName){
        this.tableName=tableName;
        this.title=tableName;
    }
    
    @Override
    public String toString(){
        return title;
    }
    
}
