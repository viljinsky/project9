/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.main;

import java.util.HashMap;

/**
 *   FRAME INFO
 * @author vadik
 */
public class FrameInfo {
    String frameTitile = "No frame title";
    String frameCalssName = MDIFrame.class.getName();
    HashMap<String,Object> values = new HashMap<>();

    public FrameInfo(String frameTitle) {
        this.frameTitile = frameTitle;
    }

    public FrameInfo(String frameTitle, String frameClassName) {
        this.frameTitile = frameTitle;
        this.frameCalssName = frameClassName;
    }
    
    @Override
    public String toString(){
        return frameTitile;
    }
    
    public void putValue(String valueName,Object value){
        values.put(valueName, value);
    }
    
    public Object getValue(String valueName){
        return values.get(valueName);
    }
}
