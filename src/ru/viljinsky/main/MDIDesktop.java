/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.main;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;

/**
 *     D E S K T O P
 * @author vadik
 */
public class MDIDesktop extends JDesktopPane {
    Action[] taskActions;
    JMenu windowMenu;
    JMenu frameMenu;
    WindowCommand windowCommand;

    /**
     * Подсчёт количества запущенных фреймов класса className
     * @param className имя класса которое надо подсчитать
     * @return  Число фреймов класса className
     */
    public Integer getFrameCount(String className) {
        MDIFrame frame;
        Integer result = 0;
        for (JInternalFrame f : getAllFrames()) {
            if (f instanceof MDIFrame) {
                frame = (MDIFrame) f;
                if (frame.frameInfo.frameCalssName.equals(className)) {
                    result += 1;
                }
            }
        }
        return result;
    }

    public boolean closeAll() {
        if (getAllFrames().length > 0) {
            if (JOptionPane.showConfirmDialog(MDIDesktop.this, "Закончить работу?", "Выход", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                return false;
            }
            for (JInternalFrame f : getAllFrames()) {
                if (f instanceof MDIFrame) {
                    try {
                        f.hide();
                        f.dispose();
                    } catch (Exception e) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    class WindowCommand extends MDICommand {

        @Override
        public String[] getCommandList() {
            return new String[]{"arrange;Упорядочить", "arrange_hor;По горизонтали", "arrange_ver;По вертикали", "close;Закрыть", "close_all;Закрыть всё"};
        }

        @Override
        public void doCommand(String command) {
            JInternalFrame frame;
            switch (command) {
                case "arrange":
                    ;
                    break;
                case "arrange_ver":
                    ;
                    break;
                case "arrange_hor":
                    ;
                    break;
                case "close_all":
                    ;
                    break;
                case "close":
                    frame = getSelectedFrame();
                    if (frame != null) {
                        frame.hide();
                        frame.dispose();
                    }
                    ;
                    break;
            }
        }

        @Override
        public void updateAction(Action action) {
            int count = getAllFrames().length;
            String command = (String) action.getValue(AbstractAction.ACTION_COMMAND_KEY);
            switch (command) {
                case "arrange":
                    action.setEnabled(count > 1);
                    break;
                case "arrange_ver":
                    action.setEnabled(count > 1);
                    break;
                case "arrange_hor":
                    action.setEnabled(count > 1);
                    break;
                case "close_all":
                    action.setEnabled(count > 1);
                    break;
                case "close":
                    action.setEnabled(count > 0);
                    break;
            }
        }
    }

    public MDIDesktop() {
        setPreferredSize(new Dimension(800, 600));
        initComponents();
    }

    public void setTaskList(FrameInfo[] taskList) {
        int count = taskList.length;
        taskActions = new Action[count];
        for (int i = 0; i < count; i++) {
            taskActions[i] = new TaskAction(taskList[i]);
        }
    }

    public void initComponents() {
        //        FrameInfo[] frames = getFrames();
        //        int count = frames.length;
        //        taskActions = new Action[count];
        //        for (int i=0;i<count;i++){
        //            taskActions[i]=new TaskAction(frames[i]);
        //        }
        windowMenu = new JMenu("Окна");
        windowCommand = new WindowCommand();
        for (Action action : windowCommand.getActionList()) {
            windowMenu.add(action);
        }
        windowMenu.addSeparator();
        windowCommand.updateActionList();
        frameMenu = new JMenu("Правка");
        frameMenu.setEnabled(false);
    }

    public void createFrameMenu(JMenu menu) {
        for (Action action : taskActions) {
            menu.add(action);
        }
    }

    class TaskAction extends AbstractAction {

        FrameInfo frameInfo;

        public TaskAction(FrameInfo frameInfo) {
            super(frameInfo.frameTitile);
            this.frameInfo = frameInfo;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            showFrame(frameInfo);
        }
    }

    public MDIFrame findFrame(FrameInfo frameInfo) {
        MDIFrame frame;
        for (JInternalFrame f : getAllFrames()) {
            if (f instanceof MDIFrame) {
                frame = (MDIFrame) f;
                if (frame.frameInfo.equals(frameInfo)) {
                    return frame;
                }
            }
        }
        return null;
    }

    protected MDIFrame createMDIFrame(FrameInfo frameInfo) throws Exception {
        Class<?> cls = Class.forName(frameInfo.frameCalssName);
        Constructor cnstr = cls.getConstructors()[0];
        MDIFrame frame = (MDIFrame) cnstr.newInstance(frameInfo);
        frame.setDesktop(this);
        frame.setVisible(true);
        return frame;
    }

    public MDIFrame showFrame(FrameInfo frameInfo) {
        MDIFrame frame;
        frame = findFrame(frameInfo);
        if (frame == null) {
            try {
                frame = createMDIFrame(frameInfo);
            } catch (Exception e) {
            }
        }
        frame.moveToFront();
        try {
            if (frame.isIcon()) {
                frame.setIcon(false);
            }
            frame.setSelected(true);
        } catch (Exception e) {
        }
        return frame;
    }
    
}
