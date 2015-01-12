/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.db.form;

import java.util.ArrayList;
import java.util.List;
import ru.viljinsky.db.DataField;
import ru.viljinsky.db.Dataset;
import ru.viljinsky.db.DataModule;

/**
 *
 * @author vadik
 */
public class DatasetForm {
    Dataset master;
    List<Dataset> slaver;
    List<DataField> referencedFields;
    List<DataField> foreignKeys;

    public List<DataField> getForeignKeys() {
        return foreignKeys;
    }

    public static DatasetForm createForm(DataModule dm,String tableName) throws Exception{
        DatasetForm form = new DatasetForm();
        form.master=dm.createTable(tableName);
        for (DataField field:form.master.getFields()){
            if (!field.getReferencedTable().isEmpty()){
                form.foreignKeys.add(field);
            }
        }
        for (Dataset ds:dm.getTables()){
            for (DataField f:ds.getFields()){
                if (f.getReferencedTable().equals(tableName)){
                    form.addReferecedField(f);
                }
            }
        }
        return form;
    }
    
    public Dataset getMaster(){
        return master;
    }
    public String getFormName(){
        return master.getTableName();
    }
    public DatasetForm(){
        slaver = new ArrayList<>();
        referencedFields = new ArrayList<>();
        foreignKeys = new ArrayList<>();
    }
    
    public DatasetForm(Dataset master) {
        super();
        this.master = master;
    }

    public void addSlaver(Dataset dataset) {
        slaver.add(dataset);
    }

    public void addReferecedField(DataField field) {
        referencedFields.add(field);
    }

    public List<DataField> getReferencedFields() {
        return referencedFields;
    }

    public Boolean isEmpty() {
        return referencedFields.isEmpty();
    }

    @Override
    public String toString() {
        String s = "";
        for (DataField f : referencedFields) {
            s += "\n  - " + f.getTableName() + "." + f.getColumnName() + " ->" + master.getTableName() + "." + f.getReferencedColumn();
        }
        for (DataField f: foreignKeys){
            s+="\n "+f+" "+f.getReferencedTable()+"."+f.getReferencedColumn();
        }
        return master.getTableName() + s;
    }
    
}
