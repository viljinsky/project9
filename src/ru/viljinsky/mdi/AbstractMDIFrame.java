/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.mdi;

import java.awt.BorderLayout;
import javax.swing.Action;
import javax.swing.JInternalFrame;
import javax.swing.JToolBar;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

/**
 * Абстрактный класс реализующий поведение фрейма
 * @author vadik
 */
public abstract class AbstractMDIFrame extends JInternalFrame {
    protected FrameInfo frameInfo;
    protected MDIDesktop desktop;
    protected MDICommand commands = null;
    protected JToolBar toolBar;


    
    public FrameInfo getFrameInfo(){
        return frameInfo;
    }
    
    public void showToolBar(Boolean visible){
        if (visible){
            add(toolBar,BorderLayout.PAGE_START);
        }
    }
    /**
     * На входе информации о создаваемом фрейме
     * @param frameInfo 
     */
    public AbstractMDIFrame(FrameInfo frameInfo) {
        super(frameInfo.frameTitile, true, true, true, true);
        this.frameInfo = frameInfo;
        toolBar = new JToolBar(frameInfo.frameTitile);
        toolBar.setFloatable(false);
        commands = new MDICommand() {
            @Override
            public String[] getCommandList() {
                return new String[]{};
            }

            @Override
            public void doCommand(String command) {
            }

            @Override
            public void updateAction(Action action) {
            }
        };
        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
                deactivate();
            }

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
                activate();
                desktop.windowCommand.updateActionList();
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                    closed();
                    desktop.windowCommand.updateActionList();
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                if (canClose())
                    setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
            }
            
            

            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
                opened();
            }
        });
    }
    /**
     * Установка родительского элемнта MDIDesktop
     * @param desktop 
     */
    public void setDesktop(MDIDesktop desktop) {
        this.desktop = desktop;
        int n = desktop.getAllFrames().length;
        setLocation(n * 20, n * 20);
        int w = desktop.getWidth();
        int h = desktop.getHeight();
        setSize(w * 7 / 10, h * 7 / 10);
        desktop.add(this);
    }

    /**
     * Инициализация элементов UI 
     */
    public abstract void initComponents();
    /**
     * Событие возникает при активации фрейма
     */
    public abstract void activate();
    /**
     * Событие возникае при деактивации фрейма
     */
    public abstract void deactivate();

    /**
     * Событие возникает при открытии окна
     */
    public abstract void opened();
    /**
     * Событие возникает при закрытии окна
     */
    public abstract void closed();
    
    public boolean queryClose(){
        return false;
    }
    
    public boolean canClose(){
        return true;
    }
    
}
