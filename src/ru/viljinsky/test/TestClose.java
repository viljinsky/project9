/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import ru.viljinsky.main.FrameInfo;
import ru.viljinsky.main.MDIFrame;
import javax.swing.JDialog;

/**
 *
 * @author вадик
 */
public class TestClose extends MDIFrame {

    public TestClose(FrameInfo frameInfo) {
        super(frameInfo);
    }

    
    private void showDialog(){
        JDialog dlg = new JDialog();
        dlg.setSize(new Dimension(100,100));
        dlg.setModal(true);
        
        Point p1 = desktop.getLocationOnScreen();
        Point p2 = getLocation();
        Dimension d = getSize();
        dlg.setLocation(p1.x+p2.x+ (d.width-dlg.getWidth())/2 , p1.y+p2.y+(d.height-dlg.getHeight())/2);
        
        System.out.println(getLocation());
        dlg.setVisible(true);
    }
    
    @Override
    public void initComponents() {
        Container content = getContentPane();
        content.setLayout(new BorderLayout());
        JButton btn = new JButton("size");
        content.add(btn,BorderLayout.PAGE_START);
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showDialog();
                System.out.println(getLocation());
            }
        });
        
    }


    
    
    @Override
    public boolean canClose() {
        boolean n=(JOptionPane.showConfirmDialog(rootPane, "Закрыть", "Подтвердите", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) ;
        return n;
    }
    
}
