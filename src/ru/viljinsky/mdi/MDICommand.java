/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.mdi;

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

/**
 *
 * @author vadik
 */
public abstract class MDICommand {
    private Action[] actionList;
    JComponent component;

    public Action[] getActionList() {
        return actionList;
    }

    class CommandAction extends AbstractAction {

        public CommandAction(String command) {
            String[] p = command.split(";");
            String commandKey = p[0];
            String commandName = (p.length>1?p[1]:p[0]);
            
            putValue(ACTION_COMMAND_KEY, commandKey);
            putValue(NAME, commandName);
            if (p.length>2){
                putValue(ACCELERATOR_KEY,KeyStroke.getKeyStroke(p[2]));
            }
            if (p.length>3){
                putValue(MNEMONIC_KEY, p[3]);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            doCommand(e.getActionCommand());
            updateActionList();
        }
    }

    public void setComponent(JComponent component) {
        this.component = component;
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                maybeShow(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                maybeShow(e);
            }

            public void maybeShow(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e.getX(), e.getY());
                }
            }
        });
        
        component.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_CONTEXT_MENU){
                    showPopup(0, 0);
                }
            }
            
        });
    }

    public MDICommand() {
        String[] commandList = getCommandList();
        int count = commandList.length;
        actionList = new Action[count];
        for (int i = 0; i < count; i++) {
            actionList[i] = new CommandAction(commandList[i]);
        }
    }

    protected void updateActionList() {
        for (Action action : actionList) {
            updateAction(action);
        }
    }

    public void showPopup(int x, int y) {
        getPopupMenu().show(component, x, y);
    }

    public JPopupMenu getPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        for (Action action : actionList) {
            popupMenu.add(action);
        }
        return popupMenu;
    }

    public abstract String[] getCommandList();

    public abstract void doCommand(String command);

    public abstract void updateAction(Action action);
    
}
