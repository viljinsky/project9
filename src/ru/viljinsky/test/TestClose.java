/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

import javax.swing.JOptionPane;
import ru.viljinsky.main.FrameInfo;
import ru.viljinsky.main.MDIFrame;

/**
 *
 * @author вадик
 */
public class TestClose extends MDIFrame {

    public TestClose(FrameInfo frameInfo) {
        super(frameInfo);
    }

    @Override
    public boolean canClose() {
        boolean n=(JOptionPane.showConfirmDialog(rootPane, "Закрыть", "Подтвердите", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) ;
        return n;
    }
    
}
