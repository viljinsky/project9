package ru.viljinsky.mdidb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import java.awt.*;
import javax.swing.*;

import ru.viljinsky.db.*;
import ru.viljinsky.db.form.*;
import ru.viljinsky.main.*;
import ru.viljinsky.grid2.*;


//----
class Master extends Grid2{
    java.util.List<Slave> listeners = new ArrayList<>();

    public void addSlave(Slave grid){
        grid.master = this;
        listeners.add(grid);
    }
    
    @Override
    public void selectedRowChange() {
        for (Slave grid:listeners){
            grid.updateFilter();
        }
    }
}



class Slave extends Grid2{
    Grid2 master;

    @Override
    protected Object[] getEmptyRowset() {
        Object[] r = super.getEmptyRowset();
        DataRowset rowset = master.getSelectedRowset();
        String masterTableName = master.getDataset().getTableName();
        for (DataField field:getDataset().getFields()){
            if (field.getReferencedTable().equals(masterTableName)){
                r[field.getColumnIndex()-1]=rowset.getValue(field.getReferencedColumn());
            }
        }
        return  r;
    }
    
    
    public void updateFilter(){
        String masterTableName = master.getDataset().getTableName();
        Integer rowindex= master.getSelectedRow();
        if (rowindex<0) return;
        DataRowset rowset = master.getDataset().getDataRowset(rowindex);
        HashMap<String,Object> filter = new HashMap<>();
        
        for (DataField field: getDataset().getFields()){
            if (field.getReferencedTable().equals(masterTableName)){
                System.out.println(field+ " "+field.getReferencedTable()+"."+field.getReferencedColumn());
                filter.put(field.getColumnName(),rowset.getValue(field.getReferencedColumn()));
            }
        }
        
        System.out.println(filter);
        try{
            getDataset().setFilter(filter);
            close();
            open();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

//----

public class DatasetFrame extends MDIFrame{
    DataModule dataModule = DataModule.getInstance();
    DatasetForm datasetForm;
    Master grid;
    Slave[] slavers;
    DataField[] fields;
    JTabbedPane tabbedPane;

    public DatasetFrame(FrameInfo frameInfo) {
        super(frameInfo);
    }

    @Override
    public void initComponents() {
        datasetForm = (DatasetForm)frameInfo.getValue("DATASET_FORM");
        grid = new Master();
        tabbedPane = new JTabbedPane();
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(new JScrollPane(grid));
        splitPane.setBottomComponent(tabbedPane);
        splitPane.setResizeWeight(.5);
        add(splitPane);
        
        List<DataField> L = datasetForm.getReferencedFields(); 
        fields = L.toArray(new DataField[L.size()]);
        slavers = new Slave[fields.length];
        for (int i = 0;i<slavers.length;i++){
            slavers[i]=new Slave();
            grid.addSlave(slavers[i]);
            tabbedPane.add(fields[i].getTableName(),new JScrollPane(slavers[i]));
        }
        for (Action action : grid.getActionList()){
            toolBar.add(action);
        }
        showToolBar(true);
    }

    @Override
    public void opened() {
        Dataset dataset = datasetForm.getMaster();
        grid.setDataset(dataset);
        
        try{
            for (int i=0;i<fields.length;i++){
                dataset = dataModule.createTable(fields[i].getTableName());
                slavers[i].setDataset(dataset);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        
        grid.open();
        
    }
    
}
