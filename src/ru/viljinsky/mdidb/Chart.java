/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.mdidb;

/**
 *
 * @author vadik
 */
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import java.awt.*;
import javax.swing.*;

class ChartBar{
    int x,y;
    Rectangle r;
    public void draw(Graphics g){
    }
}
class ChartSeries{
    Color color;
    Color outLine ;
    Integer X0,Y0,WIDTH,HEIGHT;
    Integer maxX,maxY;
    Double kX,kY;
    ChartBar[] bars;
    
    HashMap<Integer, Object> data;
    public ChartSeries(){
        data = new HashMap<>();
    }
    
    public void creatSeries(){
        color = new Color(255,125, 255);
        outLine = Color.gray;
        for (int i=0;i<10;i++){
            data.put(i, Math.round(Math.random()*100));
        }
        bars = new ChartBar[data.size()];
    }
    
    public void setData(HashMap<Integer,Object> data){
        this.data = data;
    }
    
    public void draw(Graphics g,Rectangle area){
        X0=area.x;
        Y0=area.y;
        WIDTH = area.width;
        HEIGHT = area.height;
        Integer w = WIDTH / data.size();
        
        Rectangle r ;
        Integer value;
        int x=X0;
        for (Integer k : data.keySet()){
            value = ((Long)data.get(k)).intValue();
            r=new Rectangle(x,Y0+HEIGHT-value,w,value);
            
            g.setColor(color);
            g.drawRect(r.x, r.y, r.width, r.height);
            
            x+=w;
            
        }
        
    }
}

public class Chart extends JPanel{
    Integer X0=10,Y0=10;
    Integer chartWidth = 500;
    Integer chartHeight = 300;
    ChartSeries series;
    
    public Chart(){
        setPreferredSize(new Dimension(800,600));
        series = new ChartSeries();
        series.creatSeries();
    }

    
//    public void drawBar(Graphics g,Rectangle r){
//        g.setColor(Color.red);
//        g.fillRect(r.x, r.y,r.width,r.height);
//        g.setColor(Color.blue);
//        g.drawRect(r.x, r.y,r.width,r.height);
//    }
    
    public void drawAxis(Graphics g){
        // gor
        g.drawLine(X0,Y0+chartHeight,X0+chartWidth,Y0+chartHeight);
        //vert
        g.drawLine(X0,Y0+chartHeight,X0,Y0);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Rectangle r = new Rectangle(X0,Y0,chartWidth,chartHeight);
        g.setColor(Color.white);
        g.fillRect(r.x, r.y, r.width,r.height);
        
        g.setColor(Color.red);
        drawAxis(g);
        series.draw(g,r);
        
    }
    
    public static void createAndShow(){
        JFrame frame  = new JFrame("Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new Chart());
        frame.pack();
                
        frame.setVisible(true);
        
    }
    
    public static void main(String[] args){
        createAndShow();
    }
    
}
