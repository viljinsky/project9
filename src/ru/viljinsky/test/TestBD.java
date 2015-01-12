/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

/**
 *
 * @author vadik
 */
import ru.viljinsky.db.*;

public class TestBD {
    
    public static void main(String[] args) throws Exception{
        DataModule dataModule = DataModule.getInstance();
        Dataset dataset;
        try{
            dataModule.open();
            
            String[] tables = dataModule.getViewNames();
            for (String tableName: tables){
                System.out.println(tableName);
            }
            
            dataset = dataModule.createTable("v_teacher");
            try{
                DataRowset rowset;
                dataset.open();
                for (int i=0;i<dataset.getRowCount();i++){
                    rowset = dataset.getDataRowset(i);
                    System.out.println(rowset);
                            
                }
                
            } finally {
                dataset.close();
            }
            
            
        } finally {
            dataModule.close();
        }
    }
    
}
