/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.main;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *   DESKTOP FRAME
 * @author vadik
 */
public class MDIFrame extends AbstractMDIFrame {
    JMenuItem frameMenuItem;

    public MDIFrame(FrameInfo frameInfo) {
        super(frameInfo);
        frameMenuItem = new JMenuItem(new AbstractAction(frameInfo.frameTitile) {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFrame();
            }
        });
        initComponents();
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
    }

    protected void showFrame() {
        desktop.showFrame(frameInfo);
    }

    @Override
    public void initComponents() {
    }

    @Override
    public void activate() {
        desktop.frameMenu.removeAll();
        updateFrameMenu(desktop.frameMenu);
    }

    protected void updateFrameMenu(JMenu menu) {
        for (Action action : commands.getActionList()) {
            desktop.frameMenu.add(action);
        }
        desktop.frameMenu.setEnabled(true);
        frameMenuItem.setEnabled(false);
    }

    @Override
    public void deactivate() {
        desktop.frameMenu.removeAll();
        desktop.frameMenu.setEnabled(false);
        frameMenuItem.setEnabled(true);
    }

    @Override
    public void opened() {
        desktop.windowMenu.add(frameMenuItem);
    }

    

    @Override
    public void closed() {
        desktop.windowMenu.remove(frameMenuItem);
        System.out.println("FRAME CLOSED");
    }
    
}
