/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

/**
 *
 * @author вадик
 */
import ru.viljinsky.db.*;

public class TestDataoperator {
    
    public static void main(String[] args) throws Exception{
        Object[] r1,r2;
        DataModule dm = DataModule.getInstance();
        dm.open();
        Dataset dataset = dm.createTable("teacher");
        dataset.open();
        r1 = dataset.get(1);
        r2 = dataset.get(2);
        
        DataOperator operator = new DataOperator(dataset);
        String sql = operator.getUpdateSQL();
        DatasetValues v = operator.getUpdateParamValues(dataset, r1, r2);

        System.out.println(sql);
        System.out.println(v);
    }
    
}
