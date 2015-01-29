package ru.viljinsky.mdi;

/**
 * Модуль используется для создании приложения с интерфейсом 
 *   Мультидокумент интерфейс
 * 
 * @author vadik
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.lang.reflect.*;




/**
 *
 * @author vadik
 */
public class Main{
    
    /**
     *
     */
    public static void createAndShow(){
        MDIApplication app = new MDIApplication();
        app.pack();
        app.setVisible(true);
        app.open();
    }
    
    /**
     *
     * @param args
     */
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                createAndShow();
            }
        });
    }
    
}
