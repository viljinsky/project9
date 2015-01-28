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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class BaseDialog  extends JDialog implements ActionListener{
    
    public static final int RESULT_NONE = 0;
    public static final int RESULT_OK = 1;
    public static final int RESULT_CANCEL = 2;
    public static final int RESULT_YES = 3;
    public static final int RESULT_NO = 4;
    public static final int RESULT_RETRY = 5;
    
    ButtonsPane buttonsPane;
    protected int result = RESULT_NONE;

    public Integer getResult(){
        return result;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "OK":
                try{
                    doEnterClick();
                    result=RESULT_OK;
                    setVisible(false);
                } catch (Exception ex){
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(rootPane, ex.getMessage());
                }
                break;
                
            case "CANCEL":
                result=RESULT_CANCEL;
                setVisible(false);
                break;
        }
    }
    
    class ButtonsPane extends JPanel{
        String[] commands={"YES","NO","ABORT","OK","RETRY"};
        public ButtonsPane(){
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            JButton button;
            for (String cmd:new String[]{"OK","CANCEL"}){
                button=new JButton(cmd);
                button.addActionListener(BaseDialog.this);
                add(button);
            };
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
    
    public void initComponents(Container content){
        buttonsPane = new ButtonsPane();
        content.setLayout(new BorderLayout());
        content.add(buttonsPane,BorderLayout.PAGE_END);
        content.setPreferredSize(new Dimension(300, 400));
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
