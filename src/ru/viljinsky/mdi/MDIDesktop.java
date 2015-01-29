/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.mdi;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import java.util.HashMap;

/**
 *     D E S K T O P
 * @author vadik
 */
class FrameProperties{
    Point location;
    Dimension dimension;
    public FrameProperties(Point location,Dimension size){
        this.location=location;
        this.dimension=size;
    }
}

public class MDIDesktop extends JDesktopPane {
    Action[] taskActions;
    JMenu windowMenu;
    JMenu frameMenu;
    WindowCommand windowCommand;
    String defaultFrameClassName = TextFrame.class.getName();
    public HashMap<String, FrameProperties> framePropertyes = new HashMap<String, FrameProperties>();

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
                if (frame.frameInfo.frameClassName.equals(className)) {
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
                        if (((MDIFrame)f).canClose()){
                            f.hide();
                            f.dispose();
                        } else {
                            throw new Exception("Окно не закрыто");
                        }
                    } catch (Exception e) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    protected void cascade(){
        int x,y,w,h;
        x=0;y=0;
        w = getWidth()*70/100;
        h= getHeight()*70/100;
        
        JInternalFrame[] frames = getAllFrames();
        for (int i=frames.length-1;i>=0;i--){
            frames[i].setLocation(x, y);
            frames[i].setSize(w,h);
            x+=20;y+=20;
        }
    }
    
    protected void arrangeVert(){
        int x,y,w,h;
        x=0;y=0;w=getWidth();h=getHeight()/getAllFrames().length;
        for (JInternalFrame frame :getAllFrames()){
            frame.setSize(w,h);
            frame.setLocation(x, y);
            y+=h;
        }
    }
    
    protected void arrangeHor(){
        int x,y,w,h;
        x=0;y=0;w=getWidth()/getAllFrames().length;h=getHeight();
        for (JInternalFrame frame :getAllFrames()){
            frame.setSize(w,h);
            frame.setLocation(x, y);
            x+=w;
        }
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
                    cascade();
                    break;
                case "arrange_ver":
                    arrangeVert();
                    break;
                case "arrange_hor":
                   arrangeHor() ;
                    break;
                case "close_all":
                    ;
                    break;
                case "close":
                    frame = getSelectedFrame();
                    if (frame != null) {
                        if ((frame instanceof MDIFrame) && (!((MDIFrame)frame).canClose()))
                                return;
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
        setBackground(Color.gray);
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
        if (frameInfo.frameClassName==null){
            frameInfo.frameClassName=defaultFrameClassName;
        }
        Class<?> cls = Class.forName(frameInfo.frameClassName);
        Constructor cnstr = cls.getConstructors()[0];
        MDIFrame frame = (MDIFrame) cnstr.newInstance(frameInfo);
        frame.setDesktop(this);
        
        FrameProperties fp = framePropertyes.get(frame.getTitle());
        if (fp!=null){
            frame.setLocation(fp.location);
            frame.setSize(fp.dimension);
        }
        
        
        
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
                e.printStackTrace();
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
