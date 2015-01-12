/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 *     MDI APPLICATION
 * 
 * Заготовка приложения вида МультиДокусент
 * @author vadik
 */
public class MDIApplication extends JFrame {
    /** Десктоп для размещения фреймов приложения*/
    MDIDesktop desktop;
    JMenuBar menuBar;
    /** Меню управления фреймами */
    JMenu taskMenu; 
    //    JMenu windowMenu; //
    JToolBar toolBar;
    StatusBar statusBar;
    
    
    //** Упраление задачами приложения*/
    TaskCommand taskCommand;

    class StatusBar extends JPanel {

        JLabel label;

        public StatusBar() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            label = new JLabel("StatusBar");
            add(label);
        }
    }

    class TaskCommand extends MDICommand {
        String defaultFrameClass = GridFrame.class.getName() ;

        @Override
        public String[] getCommandList() {
            return new String[]{"new_frame;Новый", "exit;Выход"};
        }

        @Override
        public void doCommand(String command) {
            switch (command) {
                case "exit":
                    close();
                    break;
                case "new_frame":
                    int n = desktop.getFrameCount(defaultFrameClass)+1;
                    FrameInfo frameInfo = new FrameInfo("Новый frame "+n,defaultFrameClass);
                    desktop.showFrame(frameInfo);
                    break;
            }
        }

        @Override
        public void updateAction(Action action) {
        }
    }

    public MDIApplication() {
        super("MDI Application (Project9)");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        initComponets(getContentPane());
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
    }
    
    protected FrameInfo[] getFrameInfoList(){
        return new FrameInfo[]{
        new FrameInfo("MDI Frame1"),
        new FrameInfo("MDI Frame2"),
        new FrameInfo("MDI Frame tabbed",TabbedFrame.class.getName()),
        new FrameInfo("MDI Frame split",SplitFrame.class.getName()),
        new FrameInfo("MDI Frame text",TextFrame.class.getName()),
        new FrameInfo("MDI Frame grid",GridFrame.class.getName())
        };
    }
    
    /**
     * Инициализация окна приложения
     * @param content подложка для размещения копонентов главного окна приложения
     */
    public void initComponets(Container content) {
        
        desktop = new MDIDesktop();
        desktop.setTaskList(getFrameInfoList());
        
        taskCommand = new TaskCommand();
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        taskMenu = new JMenu("Задачи");
        for (Action action : taskCommand.getActionList()) {
            taskMenu.add(action);
        }
        taskMenu.addSeparator();
        desktop.createFrameMenu(taskMenu);
        menuBar.add(taskMenu);
        menuBar.add(desktop.windowMenu);
        menuBar.add(desktop.frameMenu);
        content.add(desktop);
        toolBar = new JToolBar();
        for (Action action : desktop.taskActions) {
            toolBar.add(action);
        }
        content.add(toolBar, BorderLayout.PAGE_START);
        statusBar = new StatusBar();
        content.add(statusBar, BorderLayout.PAGE_END);
    }
    
    
    
    /**
     *  Открытие приложения
     */
    public void open() {
    }
    
    public void close(){
        System.out.println("application closing");
        if (desktop.closeAll()) {
            setVisible(false);
            dispose();
            System.exit(0);
        }
    }
    
}
