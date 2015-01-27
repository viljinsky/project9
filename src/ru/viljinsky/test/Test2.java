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

public class Test2 extends MDIApplication{

    @Override
    public void initComponets(Container content) {
//        super.initComponets(content);
        JPanel panel = new JPanel();
        Container c = new JPanel(new BorderLayout());
        super.initComponets(c);
        
//        panel.setPreferredSize(new Dimension(200,100));
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(panel);
        splitPane.setRightComponent(c);
        splitPane.setResizeWeight(0.5);
        content.add(splitPane);
    }

    
    
    @Override
    public void open() {
        super.open(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected FrameInfo[] getFrameInfoList() {
        return new FrameInfo[]{
            new FrameInfo("frame1",TabbedFrame.class.getName()),
            new FrameInfo("frame2"),
            new FrameInfo("frame3"),
            new FrameInfo("frame4")
        };
    }
    
    
    
    public static void main(String[] args){
        Test2 frame = new Test2();
        frame.pack();
        frame.setVisible(true);
    }
    
}
