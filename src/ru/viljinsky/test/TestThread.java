/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.viljinsky.test;

/**
 *
 * @author vadik
 */
public class TestThread {
//    public static void main(String[] args){
 public static void main(String args[]){
      new Runnable() {

          @Override
          public void run() {
              
              for (Integer i=0;i<Integer.MAX_VALUE;i++){
              }
          }
      }.run();
      System.out.println("OK");
 }
};
