/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.mdidb;

/**
 *
 * @author vadik
 */
import javax.swing.*;
import ru.viljinsky.main.*;
import ru.viljinsky.db.*;


public class AppDB extends MDIApplication{
    DataModule dataModule;

    @Override
    protected FrameInfo[] getFrameInfoList() {
        return new FrameInfo[]{
            new FrameInfo("Frame1",TreeFrame.class.getName())
        };
    }

    @Override
    public void open() {
        dataModule=DataModule.getInstance();
        try{
            dataModule.open();
        } catch (Exception e){
            JOptionPane.showMessageDialog(rootPane, e.getMessage());
        }
    }
    
    public static void main(String[] args){
        AppDB app = new AppDB();
        app.pack();
        app.setVisible(true);
        app.open();
    }
    
}
