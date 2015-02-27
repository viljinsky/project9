/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.dialogs;

/**
 *
 * @author вадик
 */
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class BaseDialog  extends JDialog {
    
    public static final int RESULT_NONE = 0;
    public static final int RESULT_OK = 1;
    public static final int RESULT_CANCEL = 2;
    public static final int RESULT_YES = 3;
    public static final int RESULT_NO = 4;
    public static final int RESULT_RETRY = 5;
    
    ButtonsPane buttonsPane;
    Action[] actions;
    
    protected int result = RESULT_NONE;

    public Integer getResult(){
        return result;
    }

    class ButtonsPane extends JPanel{
        String[] commands={"YES","NO","ABORT","OK","RETRY"};
        public ButtonsPane(){
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            JButton button;
            add(new JButton(actions[0]));
            add(new JButton(actions[1]));
            
        }
    }
    

    public BaseDialog(JComponent parent){
        
        
        setModal(true);
        setTitle("BaseDialog");
        initComponents(getContentPane());
        pack();
        
        setParent(parent);
    }
    
    public BaseDialog(){
        setModal(true);
        setTitle("BaseDialog");
        initComponents(getContentPane());
        pack();
    }
    
    public void setParent(JComponent parent){
        if (parent!=null){
            Point p = parent.getLocationOnScreen();
            int w,h ;
            w=getWidth();h=getHeight();
            setLocation(p.x + (parent.getWidth()-w)/2,p.y+(parent.getHeight()-h)/2);
        }
    }
    
    protected void enter(){
                try{
                    doEnterClick();
                    result=RESULT_OK;
                    setVisible(false);
                } catch (Exception ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(rootPane, ex.getMessage());
                }
    }
    
    protected void cancel(){
                result=RESULT_CANCEL;
                setVisible(false);                        
    }
    
    public void initComponents(Container content){
        
        actions = new Action[2];
        actions[0] = new AbstractAction("OK"){

            @Override
            public void actionPerformed(ActionEvent e) {
                enter();
            }
        };
        
        actions[1] = new AbstractAction("Cancel") {

            @Override
            public void actionPerformed(ActionEvent e) {
                cancel();
            }
        };
        
        buttonsPane = new ButtonsPane();
        content.setLayout(new BorderLayout());
        content.add(buttonsPane,BorderLayout.PAGE_END);
        content.setPreferredSize(new Dimension(300, 400));
        
//        JRootPane rootPane = new JRootPane();
        
        KeyStroke strok;
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        strok = KeyStroke.getKeyStroke("ENTER");        
        inputMap.put(strok, "ENTER");        
        rootPane.getActionMap().put("ENTER", actions[0]);

        strok = KeyStroke.getKeyStroke("ESCAPE");
        inputMap.put(strok, "ESCAPE");
        rootPane.getActionMap().put("ESCAPE", actions[1]);
        
        
        
    }
    
    
    public static void main(String[] args){
        
        BaseDialog dialog = new BaseDialog();
        dialog.setVisible(true);
        dialog.dispose();
        
        System.out.println("OK");
        
        
    }
    
    protected void doEnterClick() throws Exception{
    }
}
