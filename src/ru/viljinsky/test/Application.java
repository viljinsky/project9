/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import ru.viljinsky.main.*;
import java.awt.*;
import javax.swing.*;


public class Application  extends MDIApplication{

    @Override
    protected FrameInfo[] getFrameInfoList() {
        
        FrameInfo[] list = super.getFrameInfoList(); //To change body of generated methods, choose Tools | Templates.

        list[0]= new FrameInfo("test_close",TestClose.class.getName());
        
        return list;
    }
    
    
    public static void main(String[] args){
        Application application = new Application();
        application.pack();
        application.setVisible(true);
    }
    
}
