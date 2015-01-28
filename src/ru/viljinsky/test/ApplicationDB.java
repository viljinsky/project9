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
import java.awt.*;
import javax.swing.*;
import ru.viljinsky.main.*;
import ru.viljinsky.db.*;
import ru.viljinsky.mdidb.*;

public class ApplicationDB  extends MDIApplication{
    DataModule dataModule= DataModule.getInstance();
    FrameInfo browserInfo;

    @Override
    public void initComponets(Container content) {
        super.initComponets(content); 
    }

    @Override
    protected FrameInfo[] getFrameInfoList() {
        browserInfo = new FrameInfo("Browser",TreeFrame.class.getName());
        return new FrameInfo[]{browserInfo};
    }

    @Override
    public void open() {
        try{
            dataModule.open();
            System.out.println("Open");
        } catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
        
    }
    
    
    
    
    public static void main(String[] args){
        ApplicationDB application = new ApplicationDB();
//        application.pack();
        application.setVisible(true);
        application.open();
    }
    
}
