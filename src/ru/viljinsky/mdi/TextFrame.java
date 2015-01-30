/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.mdi;


import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.*;

public class TextFrame  extends MDIFrame{
    JTextPane textPane;
    File file;
    Boolean hasChange=false;
    
    
    public void openFile(File file){
        String text = "";
        try{
            
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            while ((s=br.readLine())!=null){
                text +=s+"\n";
            }
            br.close();
            
            
            textPane.setText(text);
            setTitle("file "+file.getName() );
        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
    
    class TextFrameCommand extends MDICommand{

        @Override
        public String[] getCommandList() {
            return new String[]{"open;Открыть","save;Сохранить","saveas","copy","past","delete"};
        }

        @Override
        public void doCommand(String command) {
            System.out.println(command);
            switch(command){
                case "open":
                   JFileChooser fc = new JFileChooser(file);
                   Integer result = fc.showOpenDialog(TextFrame.this);
                    if (result==JFileChooser.APPROVE_OPTION){
                        openFile(fc.getSelectedFile());
                    }  
                    hasChange=false;
                    commands.updateActionList();
                    break;
                case "save":
                    hasChange=false;
                    commands.updateActionList();
                    break;
                    
                case "saveass": break;
                case "copy": break;
                case "past": break;
                case "delete": break;
            }
        }

        @Override
        public void updateAction(Action action) {
            String command = (String)action.getValue(AbstractAction.ACTION_COMMAND_KEY);
            switch (command){
                    case "save":
                        action.setEnabled(hasChange);
                        break;
                    case "open": break;    
            }
        }
    }

    public TextFrame(FrameInfo frameInfo) {
        super(frameInfo);
        
        
    }
    
    private void documentChanged(){
        System.out.println("Change");
        hasChange=true;
        commands.updateActionList();
    }

    @Override
    public boolean canClose() {
        return !hasChange;
    }

    
    @Override
    public void initComponents() {
        file = new File("../");
        
        commands = new TextFrameCommand();
        textPane = new JTextPane();
        add(new JScrollPane(textPane));
        textPane.setText("Hello Text");
        commands.setComponent(textPane);
        
        for (Action action:commands.getActionList()){
            toolBar.add(action);
        }
        showToolBar(true);
        textPane.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
               documentChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               documentChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               documentChanged();
            }
        });
    }
    
    
    
}
