/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.db.form;

import java.util.ArrayList;
import java.util.List;
import ru.viljinsky.db.DataField;
import ru.viljinsky.db.DataModule;
import ru.viljinsky.db.Dataset;

/**
 *
 * @author vadik
 */
public class DatasetFormList extends ArrayList<DatasetForm> {
    DataModule dm;

    public DatasetFormList(DataModule dm) {
        super();
        this.dm = dm;
    }

    public void open() throws Exception {
        clear();
        DatasetForm form;
        for (String tableName:dm.getTableNames()){
            form = DatasetForm.createForm(dm, tableName);
            if (!form.isEmpty()){
                add(form);
            }
        }
    }
    
}
