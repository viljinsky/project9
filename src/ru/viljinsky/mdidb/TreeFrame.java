package ru.viljinsky.mdidb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import ru.viljinsky.main.*;
import ru.viljinsky.db.*;
import ru.viljinsky.db.form.*;
import java.util.HashMap;
/**
 *
 * @author vadik
 */

class Browser extends JTree{
//    DefaultMutableTreeNode nodeTable,nodeView,nodeForm,nodeReport;
    FrameInfo selectedFrameInfo = null;
    HashMap<String,DefaultMutableTreeNode> roots;
            
    public void setSelectedFrameInfo(FrameInfo info){
        if (info!=null){
            if (!info.equals(selectedFrameInfo)){
                selectedFrameInfo = info;
                System.out.println(selectedFrameInfo);
            }
        }
    }
    
    public FrameInfo getSelectedFrameInfo(){
        return selectedFrameInfo;
    }
    
    public Browser(){
        roots = new HashMap<>();
        DefaultMutableTreeNode root,node;
        root= new DefaultMutableTreeNode("База данных");
        String[] rootsNames = {"TABLE_ROOT","VIEW_ROOT","FORM_ROOT","REPOERT_ROOT"};
        for (String rootName:rootsNames){
            node = new DefaultMutableTreeNode(rootName);
            root.add(node);
            roots.put(rootName, node);
        }
        
        setModel(new DefaultTreeModel(root));
        
        addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)getLastSelectedPathComponent();
                if (node !=null){
                    Object obj = node.getUserObject();
                    if (obj instanceof FrameInfo){
                        setSelectedFrameInfo((FrameInfo)obj);
                    }  else {
                        setSelectedFrameInfo(null);
                    }
                } else {
                    setSelectedFrameInfo(null);
                }            
            }
        });
        
    }
    
    public void setTableNames(String[] tableNames){
        DefaultMutableTreeNode root,node;
        FrameInfo frameInfo;
        
        root = roots.get("TABLE_ROOT");
        root.removeAllChildren();
        for (String tableName:tableNames){
            frameInfo = new FrameInfo(tableName,GridFrame.class.getName());
            frameInfo.putValue("TABLE_NAME", tableName);
            node = new DefaultMutableTreeNode(frameInfo);
            root.add(node);
        }
    }
    public void setViewNames(String[] viewNames){
        
        DefaultMutableTreeNode root,node;
        root=roots.get("VIEW_ROOT");
        root.removeAllChildren();
        FrameInfo frameInfo;
        for (String viewName:viewNames){
            frameInfo = new FrameInfo(viewName,TabbedFrame.class.getName());
            node = new DefaultMutableTreeNode(frameInfo);
            root.add(node);
        }
    }
    
    public void setForms(DatasetForm[] forms){
        DefaultMutableTreeNode root,node;
        root = roots.get("FORM_ROOT");
        root.removeAllChildren();
        FrameInfo frameInfo;
        for (DatasetForm form:forms){
            frameInfo = new FrameInfo(form.getFormName(),DatasetFrame.class.getName());
            frameInfo.putValue("DATASET_FORM",form);
            node = new DefaultMutableTreeNode(frameInfo);
            root.add(node);
        }
    }
}

public class TreeFrame extends MDIFrame {
    DataModule dataModule = DataModule.getInstance();
    Browser    browser;
    
    public TreeFrame(FrameInfo frameInfo) {
        super(frameInfo);
    }

    @Override
    public void initComponents() {
        
        browser = new Browser();
        add(new JScrollPane(browser));
        
        commands = new MDICommand() {

            @Override
            public String[] getCommandList() {
                return new String[]{
                    "OPEN;Открыть",
                    "COLLAPSE_ALL;свернуть",
                    "EXPAND_ALL;развернуть"
                };
            }

            @Override
            public void doCommand(String command) {
                switch (command){
                    case "OPEN":
                        FrameInfo info = browser.getSelectedFrameInfo();
                        desktop.showFrame(info);
                   ;break;
                }
                
            }

            @Override
            public void updateAction(Action action) {
                String command = (String)action.getValue(AbstractAction.ACTION_COMMAND_KEY);
                switch (command){
                    case "OPEN": action.setEnabled(browser.getSelectedFrameInfo()!=null);
                }
                
            }
        };
        
        commands.setComponent(browser);
        for (Action action:commands.getActionList()){
            toolBar.add(action);
        }
        showToolBar(true);
    }

    @Override
    public void opened() {
        if (dataModule.isActive()){
            try{
                browser.setTableNames(dataModule.getTableNames());
                browser.setViewNames(dataModule.getViewNames());
                browser.setForms(dataModule.getDatasetForms());
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    
    
    
    
}