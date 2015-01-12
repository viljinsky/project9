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

public class TestDB2 {
    public static void main(String[] args) throws Exception{
        DataModule dm = DataModule.getInstance();
        dm.open();
        
        dm.begin();
        try{
            dm.execute("drop table if exists t1");
            dm.execute("create table t1 (f1 integer,t1 varchar(10))");

            Dataset dataset = dm.createTable("t1");
            dataset.open();


            for (int i=0;i<10;i++){
                dataset.append();
                DataRowset rowset = dataset.getDataRowset(dataset.getRowIndex());
                rowset.setValue("f1", 128);
                rowset.setValue("t1", "Field"+i);
                dataset.post();
            }
            dm.commit();
        } finally {
            if (!dm.isAutoCommit())
                dm.rallback();
        }
        
    }
    
}
